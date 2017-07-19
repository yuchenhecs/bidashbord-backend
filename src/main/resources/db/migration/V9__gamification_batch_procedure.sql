create table gamification_config(
id bigint(20) not null auto_increment,
config_name varchar(100) default null,
value decimal (19, 4) default null,
primary key (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DELIMITER //
CREATE PROCEDURE compute_category_values (date1 varchar(15), date2 varchar(15), date3 varchar(15),
                                          date4 varchar(15), today varchar(15), week_ago varchar(15), benchmark bigint(10))
    BEGIN
    insert into gamification_categories (advisor_id, aum, net_worth, hni, conversion_rate, avg_conversion_time, retention_rate, weekly_logins, aum_growth, net_worth_growth,
             clientele_growth)
	select a.id advisor_id,
			@aum := IFNULL(sum(aum.amount), 0) aum,
			@networth := IFNULL(sum(t.value), 0) net_worth,
			@hni := IFNULL(hni.hni, 0) hni,

			@conv_rate := IFNULL(((select count(id) from clients where active = 1 and advisor_id = a.id and converted = 1 and role_id = 6) /
				NULLIF((select count(id) from clients where active = 1 and advisor_id = a.id and (role_id = 6 or role_id = 5)), 0) * 100), 0) as conversion_rate,


			@avg_conv_rate := IFNULL(sum(avg_conv_time.conv_time) / NULLIF(count(avg_conv_time.id), 0), 0)  as avg_conversion_time,
			@retention_rate := IFNULL(( 100 - (select count(id) from clients where ((role_id = 5 and converted = 1) or (active = 0 and role_id = 6)) and advisor_id = a.id and active = 1) /
				NULLIF((select count(id) from clients where (role_id = 5 or role_id = 6) and advisor_id = a.id and active = 1),0) * 100), 0) as retention_rate,
			@weekly_logins := IFNULL(sum(l.count), 0) weekly_logins,
			TRUNCATE(qoq_aum.qoq_aum, 3) aum_growth,
			TRUNCATE(qoq_networth.qoq_networth, 3) net_worth_growth,
			TRUNCATE(clientele.clientele_growth, 3) clientele_growth

            from advisors a
		    left join clients c
		    on a.id = c.advisor_id
		    left join
		    (
		    	SELECT p.client_id, p.amount
		    	from positions p
		        where date(p.position_updated_on) IN (today)) aum
		   	on aum.client_id = c.id
		    left join
		    (
		    	SELECT net.client_id, sum(net.value) value
		    	from networth net
		        where date(net.date) in (today) group by net.client_id) t
		    on t.client_id = c.id
		    left join
		    (
		    	select advisor_id, count(id) hni
		    	from clients c
        		where c.id in (SELECT client_id from networth where value > benchmark and date in (today))
		            			group by advisor_id) hni
			on a.id = hni.advisor_id
			left join
			(
				select TIMESTAMPDIFF(HOUR, c.client_created_on, c.converted_date) conv_time, c.id from clients c
					where role_id = 6 and c.active = 1) avg_conv_time
			on c.id = avg_conv_time.id
			left join
			(
				select c.advisor_id, a.count from clients c left join (select client_id, count(client_id) count from analytics where role_id = 6
		            and session_start_date between week_ago and today group by client_id) a
					on c.id = a.client_id) l
            on a.id = l.advisor_id
			left join
			(
				select t.id, ((1 + IFNULL((t.aum2 - t.aum1)/NULLIF(t.aum1, 0), 0)) * (1 + IFNULL((t.aum3 - t.aum2)/NULLIF(t.aum2, 0), 0)) *
 					(1 + IFNULL((t.aum4 - t.aum3)/NULLIF(t.aum3, 0), 0)) * (1 + IFNULL((t.aum5 - t.aum4)/NULLIF(t.aum4, 0), 0)) - 1) * 100 as qoq_aum
 				from (
	 					select a.id,
						@aum1 := sum(aum1.amount) aum1,
						@aum2 := sum(aum2.amount) aum2,
						@aum3 := sum(aum3.amount) aum3,
						@aum4 := sum(aum4.amount) aum4,
						@aum5 := sum(aum5.amount) aum5
			            from advisors a
 			            left join clients c
        			    on a.id = c.advisor_id
		        	    left join
        		    	(SELECT p.client_id, p.amount from positions p
	        		    where date(p.position_updated_on) IN (date1)) aum1
			           	on aum1.client_id = c.id
        			   	left join
		    	       	(SELECT p.client_id, p.amount from positions p
						where date(p.position_updated_on) IN (date2)) aum2
						on aum2.client_id = c.id
			            left join
			            (SELECT p.client_id, p.amount from positions p
		    	        where date(p.position_updated_on) IN (date3)) aum3
		        	    on aum3.client_id = c.id
			            left join
			            (SELECT p.client_id, p.amount from positions p
			            where date(p.position_updated_on) IN (date4)) aum4
		    	        on aum4.client_id = c.id
			            left join
			            (SELECT p.client_id, p.amount from positions p
			            where date(p.position_updated_on) IN (today)) aum5
		    	        on aum5.client_id = c.id
		        	   	where a.active = 1
		            	group by a.id
			            order by a.id) t
					) qoq_aum
			on a.id = qoq_aum.id
			left join
			(
				select t.id, ((1 + IFNULL((net2 - net1)/NULLIF(net1, 0), 0)) * (1 + IFNULL((net3 - net2)/NULLIF(net2, 0), 0)) *
				(1 + IFNULL((net4 - net3)/NULLIF(net3, 0), 0)) * (1 + IFNULL((net5 - net4)/NULLIF(net4, 0), 0)) - 1) * 100 as qoq_networth
				from (
						select a.id,
						@net1 := sum(nw1.val) net1,
						@net2 := sum(nw2.val) net2,
						@net3 := sum(nw3.val) net3,
						@net4 := sum(nw4.val) net4,
						@net5 := sum(nw5.val) net5
            			from advisors a
				        left join clients c
				        on a.id = c.advisor_id
				       	left join
				        (SELECT client_id, value val from networth
				        where date in (date1)) nw1
				        on nw1.client_id = c.id
				        left join
				        (SELECT client_id, value val from networth
			           	where date(date) in (date2)) nw2
            			on nw2.client_id = c.id
			            left join
            			(SELECT client_id, value val from networth net
			           	where date(date) in (date3)) nw3
            			on nw3.client_id = c.id
			            left join
            			(SELECT client_id, value val from networth net
			           	where date(date) in (date4)) nw4
            			on nw4.client_id = c.id
			            left join
            			(SELECT client_id, value val from networth net
			           	where date(date) in (today)) nw5
            			on nw5.client_id = c.id
           				where a.active = 1
          				group by a.id
			            order by a.id) t
			    	) qoq_networth
			on a.id = qoq_networth.id
			left join
			(
				select t.id, ((1 + IFNULL((t.count2 - t.count1)/NULLIF(t.count1, 0), 0)) * (1 + IFNULL((t.count3 - t.count2)/NULLIF(t.count2,0), 0)) *
				 (1 + IFNULL((t.count4 - t.count3)/NULLIF(t.count3, 0), 0)) * (1 + IFNULL((t.count5 - t.count4)/NULLIF(t.count4, 0), 0)) - 1) * 100 as clientele_growth from (
				select a.id, c1.count1, c2.count2, c3.count3, c4.count4, c5.count5 from advisors a
				left join
				(select count(id) count1, advisor_id from clients where client_created_on < date1 and active = 1 and role_id = 6 group by advisor_id) c1
				on a.id = c1.advisor_id
				left join
				(select count(id) count2, advisor_id from clients where client_created_on < date2 and active = 1 and role_id = 6 group by advisor_id) c2
				on a.id = c2.advisor_id
				left join
				(select count(id) count3, advisor_id from clients where client_created_on < date3 and active = 1 and role_id = 6 group by advisor_id) c3
				on a.id = c3.advisor_id
				left join
				(select count(id) count4, advisor_id from clients where client_created_on < date4 and active = 1 and role_id = 6 group by advisor_id) c4
				on a.id = c4.advisor_id
				left join
				(select count(id) count5, advisor_id from clients where client_created_on < today and active = 1 and role_id = 6 group by advisor_id) c5
				on a.id = c5.advisor_id
				group by a.id, c1.count1, c2.count2, c3.count3, c4.count4, c5.count5) t
			) clientele
			on a.id = clientele.id
            where a.active = 1
            group by a.id, hni.hni, clientele.clientele_growth
            order by a.id;
END //
DELIMITER ;

-- delimiter to compute points

DELIMITER //
			CREATE PROCEDURE compute_points(aum_v decimal(5,4), net_worth_v decimal(5,4), hni_v decimal(5,4), financial_v decimal(5,4), conversion_rate_v decimal(5,4), 																avg_conversion_time_v decimal(5,4), retention_rate_v decimal(5,4),
											weekly_logins_v decimal(5,4), client_management_v decimal(5,4), aum_growth_v decimal(5,4), net_worth_growth_v decimal(5,4),
											clientele_growth_v decimal(5,4), growth_v decimal(5,4))
			BEGIN
				insert into gamification_advisor (advisor_id, points)
				select advisor_id, ceil((aum * aum_v + net_worth * net_worth_v + hni * hni_v) * financial_v +
   (conversion_rate * conversion_rate_v + avg_conversion_time * avg_conversion_time_v + retention_rate * retention_rate_v + weekly_logins * weekly_logins_v) * client_management_v +
	   			   (aum_growth * aum_growth_v + net_worth_growth * net_worth_growth_v + clientele_growth * clientele_growth_v) * growth_v) score
	   			   from gamification_categories;
			END//
DELIMITER ;

---------------------------------
-- delimiter to update overall_percentile

DELIMITER //
CREATE PROCEDURE update_overall_percentile()
BEGIN
	update gamification_advisor ga
	left join
	(SELECT ordered.advisor_id,
    (@rnk := @rnk+1),
    (@rank:= IF(@curscore=ordered.points, @rank, @rnk)) rank,
    (@curscore:=ordered.points) newscore,
    ((@total-@rank+1)/@total) * 100 as percentrank
    FROM
    (
        SELECT advisor_id, points FROM gamification_advisor, (SELECT @tmp:=0, @rnk := 0, @rank:=0, @curscore:=0, @total:=count(*) from gamification_advisor) t ORDER BY points DESC
	) ordered) t
	on t.advisor_id = ga.advisor_id
	set ga.percentile_overall = t.percentrank;
END//
DELIMITER ;


-----------------------------------
-- delimiter to update firm_percentile

DELIMITER //
CREATE PROCEDURE update_percentile_firm()
BEGIN
update gamification_advisor ga
	left join
	(SELECT advisor_id, firm_id, points, t.firm_count,
	   (@rn := @rn+1),
       (@rank := if(@firm = firm_id, if(@curscore = points, @rank, @rn + 1), (@rn := 0) + 1)) as rank,
       (@curscore := points),
       (@firm := firm_id),
       TRUNCATE(((t.firm_count - @rank + 1)/t.firm_count) * 100, 2) as percent_firm
	FROM
		(SELECT @firm := -1, @rn := 0, @curscore := 0, @rank := 0, ga.advisor_id, a.firm_id, SUM(ga.points) as points, f.firm_count
      	FROM gamification_advisor ga
		join advisors a on ga.advisor_id = a.id
		left join
		(
			select count(id) firm_count, firm_id from advisors group by firm_id
		) f on f.firm_id = a.firm_id
		GROUP BY ga.advisor_id, a.firm_id, f.firm_count) t
	ORDER BY firm_id, points desc) tt
	on tt.advisor_id = ga.advisor_id
	set ga.percentile_firm = tt.percent_firm;
END//
DELIMITER ;


------------------------------------------
----- delimiter to update state_percentile

    DELIMITER //
    CREATE PROCEDURE update_state_percentile()
    BEGIN
    	UPDATE gamification_advisor ga
    	left join
       (SELECT advisor_id, points, t.state, cc.state_count,
	   (@rn := @rn+1),
       (@rank := if(@state = t.state, if(@curscore = points, @rank, @rn + 1), (@rn := 0) + 1)) as rank,
       (@curscore := points),
       (@state := t.state),
       TRUNCATE(((cc.state_count - @rank+1)/cc.state_count) * 100, 2) as percent_state
	FROM
		(SELECT @state := null, @rn := 0, @curscore := 0, @rank := 0, ga.advisor_id, a.firm_id, f.state, ga.points as points
      	FROM gamification_advisor ga
      	left join advisors a on ga.advisor_id = a.id
		left join
		(select id, state from firms where active = 1) f
		on f.id = a.firm_id
		WHERE a.active = 1) t
		left join
		(select count(id) state_count, state from firms group by state) cc
		on t.state = cc.state
	ORDER BY state, points desc) tt
	on tt.advisor_id = ga.advisor_id
	set ga.percentile_state = tt.percent_state;
    END//
    DELIMITER ;