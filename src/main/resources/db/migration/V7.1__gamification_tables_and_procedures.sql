------------- to calculate values
DROP PROCEDURE IF EXISTS compute_category_values;

DELIMITER //
CREATE PROCEDURE compute_category_values (date1 varchar(15), date2 varchar(15), date3 varchar(15),
                                          date4 varchar(15), today varchar(15), week_ago varchar(15), benchmark bigint(10))
    BEGIN
    insert into gamification_categories (advisor_id, aum, net_worth, hni, conversion_rate, avg_conversion_time, retention_rate, weekly_logins, aum_growth, net_worth_growth,
             clientele_growth)
	SELECT id advisor_id, aum.amount aum, net_worth.amount net_worth, hni.amount hni, cr.conversion_rate,
		avg_conv_time.avg_conversion_time, rr.retention_rate, weekly_logins.weekly_logins, TRUNCATE(qoq_aum,3) qoq_aum,
		TRUNCATE(qoq_networth, 3) qoq_networth, TRUNCATE(clientele_growth, 3) clientele_growth from advisors a left join
	(
		select l.advisor_id, l.logins weekly_logins from (
					select a.id advisor_id, @logins := IFNULL(sum(a.count), 0) logins from advisors a left join
					(select id, advisor_id from clients where active = 1) c on a.id = c.advisor_id left join
					(select client_id, count(client_id) count from analytics
											where role_id = 6
													and session_start_date between week_ago and today group by client_id) a
					on c.id = a.client_id group by a.id
					) l
	) weekly_logins on a.id = weekly_logins.advisor_id left join
	(
		select aum.advisor_id, aum.amount from (
				SELECT a.id advisor_id, @aum_cal := IFNULL(sum(aum.amount), 0) amount from advisors a left join
				(SELECT id, advisor_id from clients where active = 1) c on c.advisor_id = a.id
				left join
		    	(SELECT p.client_id, p.amount
		    	from positions p
		        where date(p.position_updated_on) IN (today) ) aum
		        on aum.client_id = c.id
		        group by a.id
		        ) aum
	) aum on a.id = aum.advisor_id left join
	(
		select net.advisor_id, net.amount from (
				SELECT a.id advisor_id, @net_cal := IFNULL(sum(net.amount), 0) amount from advisors a left join
				(SELECT id, advisor_id from clients where active = 1) c on c.advisor_id = a.id left join
				(SELECT net.client_id, net.value amount from networth net where date(net.date) in (today)) net
				on net.client_id = c.id
				group by a.id
				) net
	) net_worth on a.id = net_worth.advisor_id left join
	(
		SELECT hni.advisor_id, hni.amount from (
				SELECT a.id advisor_id, @hni_cal := IFNULL(count(hni.client_id), 0) amount from advisors a left join
        		(SELECT id, advisor_id from clients where active = 1) c on c.advisor_id = a.id left join
        		(SELECT client_id from networth where value > benchmark and date in (today)) hni
        		on c.id = hni.client_id
        		group by a.id) hni
	) hni on a.id = hni.advisor_id left join
	(
		SELECT a.id advisor_id, IFNULL(
				((select count(id) from clients where active = 1 and advisor_id = a.id and converted = 1 and role_id = 6) /
				NULLIF((select count(id) from clients where active = 1 and advisor_id = a.id and (role_id = 6 or role_id = 5)), 0) * 100), 0) as conversion_rate
				from advisors a
	) cr on a.id = cr.advisor_id left join
	(
		SELECT avg_conv_time.id advisor_id, avg_conv_time.avg_conversion_time from (
				SELECT a.id, IFNULL(sum(c.conv_time) / NULLIF(count(c.id), 0), 0)  as avg_conversion_time from advisors a left join
				(SELECT TIMESTAMPDIFF(HOUR, c.client_created_on, c.converted_date) conv_time, c.id, c.advisor_id from clients c
					where role_id = 6 and c.active = 1) c on a.id = c.advisor_id group by a.id
				) avg_conv_time
	) avg_conv_time on a.id = avg_conv_time.advisor_id left join
	(
		SELECT a.id advisor_id, IFNULL(( 100 - (select count(id) from clients where ((role_id = 5 and converted = 1) or (active = 0 and role_id = 6)) and advisor_id = a.id and active = 1) /
		NULLIF((select count(id) from clients where (role_id = 5 or role_id = 6) and advisor_id = a.id and active = 1),0) * 100), 0) as retention_rate from advisors a
	) rr on a.id = rr.advisor_id left join
	(
		select t.id advisor_id, ((1 + IFNULL((t.aum2 - t.aum1)/NULLIF(t.aum1, 0), 0)) * (1 + IFNULL((t.aum3 - t.aum2)/NULLIF(t.aum2, 0), 0)) *
 					(1 + IFNULL((t.aum4 - t.aum3)/NULLIF(t.aum3, 0), 0)) * (1 + IFNULL((t.aum5 - t.aum4)/NULLIF(t.aum4, 0), 0)) - 1) * 100 as qoq_aum
 				from (
 						SELECT aum1.advisor_id id, aum1.amount aum1, aum2.amount aum2, aum3.amount aum3, aum4.amount aum4, aum5.amount aum5 FROM (
							SELECT a.id advisor_id, @aum_cal1 := IFNULL(sum(aum1.amount), 0) amount from advisors a left join
			            	(SELECT id, advisor_id from clients where active = 1) c on c.advisor_id = a.id
							left join
			    			(SELECT p.client_id, p.amount from positions p where date(p.position_updated_on) IN (date1) ) aum1
					    	on aum1.client_id = c.id
					    	group by a.id
						) aum1 left join
						(
					        SELECT a.id advisor_id, @aum_cal2 := IFNULL(sum(aum2.amount), 0) amount from advisors a left join
			        	    (SELECT id, advisor_id from clients where active = 1) c on c.advisor_id = a.id
							left join
			    			(SELECT p.client_id, p.amount from positions p where date(p.position_updated_on) IN (date2) ) aum2
					    	on aum2.client_id = c.id
					    	group by a.id
						) aum2 on aum1.advisor_id = aum2.advisor_id left join
						(
					        SELECT a.id advisor_id, @aum_cal3 := IFNULL(sum(aum3.amount), 0) amount from advisors a left join
			        	    (SELECT id, advisor_id from clients where active = 1) c on c.advisor_id = a.id
							left join
			    			(SELECT p.client_id, p.amount from positions p where date(p.position_updated_on) IN (date3) ) aum3
					    	on aum3.client_id = c.id
					    	group by a.id
						) aum3 on aum1.advisor_id = aum3.advisor_id left join
						(
					        SELECT a.id advisor_id, @aum_cal4 := IFNULL(sum(aum4.amount), 0) amount from advisors a left join
			        	    (SELECT id, advisor_id from clients where active = 1) c on c.advisor_id = a.id
							left join
			    			(SELECT p.client_id, p.amount from positions p where date(p.position_updated_on) IN (date4) ) aum4
					    	on aum4.client_id = c.id
					    	group by a.id
						) aum4 on aum1.advisor_id = aum4.advisor_id left join
						(
					        SELECT a.id advisor_id, @aum_cal5 := IFNULL(sum(aum5.amount), 0) amount from advisors a left join
			        	    (SELECT id, advisor_id from clients where active = 1) c on c.advisor_id = a.id
							left join
			    			(SELECT p.client_id, p.amount from positions p where date(p.position_updated_on) IN (today) ) aum5
					    	on aum5.client_id = c.id
					    	group by a.id
						) aum5 on aum1.advisor_id = aum5.advisor_id
	 				 ) t
	)qoq_aum on a.id = qoq_aum.advisor_id left join
	(
		SELECT t.advisor_id, ((1 + IFNULL((t.net2 - t.net1)/NULLIF(t.net1, 0), 0)) * (1 + IFNULL((t.net3 - t.net2)/NULLIF(t.net2, 0), 0)) *
				(1 + IFNULL((t.net4 - t.net3)/NULLIF(t.net3, 0), 0)) * (1 + IFNULL((t.net5 - t.net4)/NULLIF(t.net4, 0), 0)) - 1) * 100 as qoq_networth
				from (
						SELECT net1.advisor_id, net1.amount net1, net2.amount net2, net3.amount net3, net4.amount net4, net5.amount net5 FROM
							(
			            		SELECT a.id advisor_id, @net_cal1 := IFNULL(sum(net1.val), 0) amount from advisors a left join
				            	(SELECT id, advisor_id from clients where active = 1) c on c.advisor_id = a.id left join
					            (SELECT client_id, value val from networth net where date(date) in (date1)) net1
					            on c.id = net1.client_id
					            group by a.id
				            ) net1 left join
				            (
				            	SELECT a.id advisor_id, @net_cal2 := IFNULL(sum(net2.val), 0) amount from advisors a left join
				            	(SELECT id, advisor_id from clients where active = 1) c on c.advisor_id = a.id left join
					            (SELECT client_id, value val from networth net where date(date) in (date2)) net2
					            on c.id = net2.client_id
					            group by a.id
				            ) net2 on net1.advisor_id = net2.advisor_id
				            left join
				            (
				            	SELECT a.id advisor_id, @net_cal3 := IFNULL(sum(net3.val), 0) amount from advisors a left join
				            	(SELECT id, advisor_id from clients where active = 1) c on c.advisor_id = a.id left join
					            (SELECT client_id, value val from networth net where date(date) in (date3)) net3
					            on c.id = net3.client_id
					            group by a.id
				            ) net3 on net1.advisor_id = net3.advisor_id
				            left join
				            (
				            	SELECT a.id advisor_id, @net_cal4 := IFNULL(sum(net4.val), 0) amount from advisors a left join
				            	(SELECT id, advisor_id from clients where active = 1) c on c.advisor_id = a.id left join
					            (SELECT client_id, value val from networth net where date(date) in (date4)) net4
					            on c.id = net4.client_id
					            group by a.id
				            ) net4 on net1.advisor_id = net4.advisor_id
				            left join
				            (
				            	SELECT a.id advisor_id, @net_cal5 := IFNULL(sum(net5.val), 0) amount from advisors a left join
				            	(SELECT id, advisor_id from clients where active = 1) c on c.advisor_id = a.id left join
					            (SELECT client_id, value val from networth net where date(date) in (today)) net5
					            on c.id = net5.client_id
					            group by a.id
				            ) net5 on net1.advisor_id = net5.advisor_id
			          ) t
	)qoq_networth on a.id = qoq_networth.advisor_id
	left join
	(
		select t.id advisor_id, ((1 + IFNULL((t.count2 - t.count1)/NULLIF(t.count1, 0), 0)) * (1 + IFNULL((t.count3 - t.count2)/NULLIF(t.count2,0), 0)) *
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
	) clientele_growth on a.id = clientele_growth.advisor_id;
END//
DELIMITER ;

--------- to calculate kpi percentile in state
DROP procedure IF EXISTS kpi_percentile_state;
DELIMITER //
CREATE PROCEDURE kpi_percentile_state (today varchar(15), clm varchar(25))
	BEGIN
		SET @query = CONCAT('UPDATE gamification_kpi_percentile_state gkp
		left join
(
	SELECT advisor_id, points, t.state, t.state_count,
      (@rn := @rn+1),
       (@rank := if(@state = t.state, if(@curscore = points, @rank, @rn), @rn := 1)) as rank,
       (@curscore := points),
       (@state := t.state),
       TRUNCATE(((t.state_count - t.point_count - @rank + 1)/t.state_count) * 100, 2) as percentrank
   FROM
      (
       	 SELECT @state := null, @rn := 0, @curscore := 0, @rank := 0, ga.advisor_id, a.firm_id, f.state, ga.',clm,' as points, st.point_count, cc.state_count FROM gamification_categories ga
      left join
      	 advisors a on ga.advisor_id = a.id
      left join
 	     (select id, state from firms where active = 1) f on f.id = a.firm_id
      left join
      	 (select count(f.id) state_count, state from firms f left join advisors a on f.id = a.firm_id group by state) cc on f.state = cc.state
      left join
      	 (select count(',clm,') point_count, ',clm,', f.state from gamification_categories ga left join advisors a on a.id = ga.advisor_id
      	 left join firms f on a.firm_id = f.id WHERE f.active = 1 and date(ga.update_date) = (?) group by f.state, ',clm,') st on st.',clm,' = ga.',clm,'
      	 and st.state = f.state
	  WHERE date(ga.update_date) = (?)
	  ORDER BY state, points desc
	  ) t
) tt on tt.advisor_id = gkp.advisor_id
		set gkp.',clm,' = tt.percentrank WHERE DATE(gkp.update_date) = (?);');

		PREPARE stmt FROM @query;
		SET @a = today;
		SET @b = today;
		SET @c = today;
		EXECUTE stmt USING @a, @b, @c;
		DEALLOCATE PREPARE stmt;
	END//
DELIMITER ;

------------------- calculate kpi percentile overall
DROP procedure IF EXISTS kpi_percentile_overall;
DELIMITER //
CREATE PROCEDURE kpi_percentile_overall (today varchar(15), clm varchar(25))
	BEGIN
		SET @query = CONCAT('UPDATE gamification_kpi_percentile_overall gkp
		left join
(
	SELECT ordered.*,
	    (@rnk := @rnk+1),
    	(@rank:= IF(@curscore=ordered.',clm,', @rank, @rnk)) rank,
    	s.count as rank_counter,
    	(@curscore:=ordered.',clm,') newscore,
	    ((@total-@rank-s.count + 1)/@total) * 100 as percentrank
    	FROM
	    (
    	    SELECT advisor_id, ',clm,' FROM gamification_categories gc
    	    left join advisors a on a.id = gc.advisor_id
    	    ,(SELECT @tmp:=0, @rnk := 0, @rank:=0, @curscore:=0, @total:=count(*) FROM gamification_categories WHERE date(update_date)  = (?)) t  WHERE date(gc.update_date)  = (?)
    	    ORDER BY ',clm,' DESC

		) ordered left join
		(SELECT ',clm,', COUNT(',clm,') count from gamification_categories gc WHERE date(gc.update_date) = (?) group by ',clm,') s on ordered.',clm,' = s.',clm,'
) tt on tt.advisor_id = gkp.advisor_id
		set gkp.',clm,' = tt.percentrank WHERE DATE(gkp.update_date) = (?);');

		PREPARE stmt FROM @query;
		SET @a = today;
		SET @b = today;
		SET @c = today;
		SET @d = today;
		EXECUTE stmt USING @a, @b, @c, @d;
		DEALLOCATE PREPARE stmt;
	END//
DELIMITER ;

--------------------------- to calculate kpi percentile within firm
DROP procedure IF EXISTS kpi_percentile_firm;
DELIMITER //
CREATE PROCEDURE kpi_percentile_firm (today varchar(15), clm varchar(25))
	BEGIN
		SET @query = CONCAT('UPDATE gamification_kpi_percentile_firm gkp
		left join (
	SELECT advisor_id, firm_id, points, t.firm_count,
      (@rn := @rn+1),
       (@rank := if(@firm = firm_id, if(@curscore = points, @rank, @rn), @rn := 1)) as rank,
       (@curscore := points),
       (@firm := firm_id),
       TRUNCATE(((t.firm_count - t.count - @rank + 1)/t.firm_count) * 100, 2) as percentrank, t.count
   FROM
      (SELECT @firm := -1, @rn := 0, @curscore := 0, @rank := 0, ga.advisor_id, a.firm_id, ga.', clm,' as points, f.firm_count, p.count
       FROM gamification_categories ga
      join advisors a on ga.advisor_id = a.id
      left join
      (
         select count(id) firm_count, firm_id from advisors group by firm_id
      ) f on f.firm_id = a.firm_id
      left join
      (
      	SELECT f.id firm_id, count(',clm,') count, ',clm,' from gamification_categories ga
      	left join advisors a on ga.advisor_id = a.id left join firms f on a.firm_id = f.id WHERE f.active = 1 and date(ga.update_date) = (?) group by f.id, ',clm,'
      ) p on p.firm_id = f.firm_id and p.',clm,' = ga.',clm,'
      WHERE date(ga.update_date) = (?)
      GROUP BY ga.advisor_id, a.firm_id, f.firm_count, p.count, ga.',clm,') t
   ORDER BY firm_id, points desc
) tt on tt.advisor_id = gkp.advisor_id
		set gkp.',clm,' = tt.percentrank WHERE DATE(gkp.update_date) = (?);');

		PREPARE stmt FROM @query;
		SET @a = today;
		SET @b = today;
		SET @c = today;
		EXECUTE stmt USING @a, @b, @c;
		DEALLOCATE PREPARE stmt;
	END//
DELIMITER ;


----------- to calculate points
DROP PROCEDURE IF EXISTS compute_points;
DELIMITER //
     CREATE PROCEDURE compute_points(aum_v decimal(19,4), net_worth_v decimal(19,4), hni_v decimal(19,4), financial_v decimal(19,4), conversion_rate_v decimal(19,4),                                                 			avg_conversion_time_v decimal(5,4), retention_rate_v decimal(5,4),
                             weekly_logins_v decimal(19,4), client_management_v decimal(19,4), aum_growth_v decimal(19,4), net_worth_growth_v decimal(19,4),
                             clientele_growth_v decimal(19,4), growth_v decimal(19,4), today varchar(15))
     BEGIN
        update gamification_advisor ga left join
        (select advisor_id, ceil((aum * aum_v + net_worth * net_worth_v + hni * hni_v) * financial_v +
               (conversion_rate * conversion_rate_v + avg_conversion_time * avg_conversion_time_v + retention_rate * retention_rate_v + weekly_logins * weekly_logins_v) * client_management_v +
               (aum_growth * aum_growth_v + net_worth_growth * net_worth_growth_v + clientele_growth * clientele_growth_v) * growth_v) score
               from gamification_kpi_percentile_overall WHERE DATE(update_date) = today) t on t.advisor_id = ga.advisor_id
        SET ga.points = t.score WHERE DATE(ga.updated_on) = today;
     END//
DELIMITER ;


-------------- to calculate overall percentile

DROP PROCEDURE IF EXISTS update_overall_percentile;
DELIMITER //
CREATE PROCEDURE update_overall_percentile(today varchar(15))
BEGIN
   update gamification_advisor ga
   left join
   (
   	SELECT ordered.*,
	    (@rnk := @rnk+1),
    	(@rank:= IF(@curscore=ordered.points, @rank, @rnk)) rank,
    	s.count as rank_counter,
    	(@curscore:=ordered.points) newscore,
	    ((@total-@rank-s.count + 1)/@total) * 100 as percentrank
    	FROM
	    (
    	    SELECT advisor_id, points FROM gamification_advisor ga,
    	    (SELECT @tmp:=0, @rnk := 0, @rank:=0, @curscore:=0, @total:=count(*) FROM gamification_advisor WHERE date(update_date) = (today)) t
    	    WHERE date(ga.update_date)  = (today)
    	    ORDER BY points DESC
		) ordered left join
		(SELECT points, COUNT(points) count from gamification_advisor WHERE date(updated_on) = (today) group by points) s on ordered.points = s.points
   ) t
   on t.advisor_id = ga.advisor_id
   set ga.percentile_overall = t.percentrank WHERE date(ga.updated_on) = (today);
END//
DELIMITER ;


------------- to calcualte firm percentile

DROP PROCEDURE IF EXISTS update_firm_percentile;
DELIMITER //
CREATE PROCEDURE update_firm_percentile(today varchar(15))
BEGIN
   update gamification_advisor ga
   left join
   (
SELECT advisor_id, firm_id, points, t.firm_count,
      (@rn := @rn+1),
       (@rank := if(@firm = firm_id, if(@curscore = points, @rank, @rn + 1), (@rn := 0) + 1)) as rank,
       (@curscore := points),
       (@firm := firm_id),
       TRUNCATE(((t.firm_count - t.count - @rank + 1)/t.firm_count) * 100, 2) as percent_firm, t.count
   FROM
      (SELECT @firm := -1, @rn := 0, @curscore := 0, @rank := 0, ga.advisor_id, a.firm_id, ga.points as points, f.firm_count, p.count
       FROM gamification_advisor ga
      join advisors a on ga.advisor_id = a.id
      left join
      (
         select count(id) firm_count, firm_id from advisors group by firm_id
      ) f on f.firm_id = a.firm_id
      left join
      (
      	SELECT f.id firm_id, count(points) count, points from gamification_advisor ga
      	left join advisors a on ga.advisor_id = a.id left join firms f on a.firm_id = f.id WHERE f.active = 1 and date(ga.updated_on) = (today) group by f.id, points
      ) p on p.firm_id = f.firm_id and p.points = ga.points
      WHERE date(ga.updated_on) = (today)
      GROUP BY ga.advisor_id, a.firm_id, f.firm_count, p.count, ga.points) t
   ORDER BY firm_id, points desc
   ) t
   on t.advisor_id = ga.advisor_id
   set ga.percentile_firm = t.percentrank WHERE date(ga.updated_on) = (today);
END//
DELIMITER ;



------------- to calcualte state percentile

DROP PROCEDURE IF EXISTS update_state_percentile;
DELIMITER //
CREATE PROCEDURE update_state_percentile(today varchar(15))
    BEGIN
       UPDATE gamification_advisor ga
       left join
   (
       SELECT advisor_id, points, t.state, t.state_count,
      (@rn := @rn+1),
       (@rank := if(@state = t.state, if(@curscore = points, @rank, @rn + 1), (@rn := 0) + 1)) as rank,
       (@curscore := points),
       (@state := t.state),
       TRUNCATE(((t.state_count - t.point_count - @rank + 1)/t.state_count) * 100, 2) as percent_state
   FROM
      (
       	 SELECT @state := null, @rn := 0, @curscore := 0, @rank := 0, ga.advisor_id, a.firm_id, f.state, ga.points as points, st.point_count, cc.state_count FROM gamification_advisor ga
      left join
      	 advisors a on ga.advisor_id = a.id
      left join
 	     (select id, state from firms where active = 1) f on f.id = a.firm_id
      left join
      	 (select count(f.id) state_count, state from firms f left join advisors a on f.id = a.firm_id group by state) cc on f.state = cc.state
      left join
      	 (select count(points) point_count, points, f.state from gamification_advisor ga left join advisors a on a.id = ga.advisor_id
      	 left join firms f on a.firm_id = f.id WHERE date(ga.updated_on) = (today) group by f.state, points) st on st.points = ga.points and st.state = f.state
	  WHERE date(ga.updated_on) = (today)
	  ORDER BY state, points desc
	  ) t
   ) tt
   on tt.advisor_id = ga.advisor_id
   set ga.percentile_state = tt.percent_state;
END//
DELIMITER ;


--------- to get max and min kpi
drop procedure if exists max_min_kpi;
DELIMITER //
create procedure max_min_kpi (clm varchar(25), scope varchar(10), advisor_id bigint(20), today varchar(10))
begin
	SET @query = CONCAT ('select max(', clm, '), min(', clm,')');
	SET @query = CONCAT (@query, ' from gamification_categories gc ');
	if (scope = "state") then
		SET @query = CONCAT (@query, 'left join advisors a on a.id = gc.advisor_id left join firms f on f.id = a.firm_id where f.state =
		(select f.state from advisors a join firms f on a.firm_id = f.id where a.id = ?) AND ');
	elseif (scope = "overall") then
		SET @query = CONCAT (@query, ' WHERE');
	elseif (scope = "firm") then
		SET @query = CONCAT (@query, ' left join advisors a on a.id = gc.advisor_id left join firms f on f.id = a.firm_id where f.id =
		(select firm_id from advisors where id = ?) AND ');
	end if;
	SET @query = CONCAT (@query, ' date(update_date) = ?');

	PREPARE stmt from @query;
	SET @advisor_id = advisor_id;
	SET @today = today;

	if (scope = "overall") then
		EXECUTE stmt USING @today;
	else
		EXECUTE stmt USING @advisor_id, @today;
	end if;

	DEALLOCATE PREPARE stmt;
END//
DELIMITER ;


----------- to get kpi of advisor
DROP PROCEDURE IF EXISTS advisor_kpi;
DELIMITER //
CREATE PROCEDURE advisor_kpi (advisor_id bigint(20), clm varchar(25), today varchar(20))
	BEGIN
		SET @query = CONCAT('SELECT ', clm,' from gamification_categories WHERE DATE(update_date) = ? and advisor_id = ?;');

		PREPARE stmt FROM @query;
		SET @a = today;
		SET @b = advisor_id;
		EXECUTE stmt USING @a, @b;

		DEALLOCATE PREPARE stmt;
	END//
DELIMITER ;


------------ to get advisor percentile based on scope
DROP PROCEDURE IF EXISTS advisor_kpi_percentile;
DELIMITER //
	CREATE PROCEDURE advisor_kpi_percentile (advisor_id bigint(20), clm varchar(25), today varchar(25), scope varchar(15))
		BEGIN
			SET @query = CONCAT('SELECT ', clm,' FROM gamification_kpi_percentile_', scope,' WHERE DATE(update_date) = ? AND advisor_id = ?;');
			PREPARE stmt FROM @query;
			SET @a = today;
			SET @b = advisor_id;
			EXECUTE stmt USING @a, @b;

			DEALLOCATE PREPARE stmt;
		END//
DELIMITER ;
