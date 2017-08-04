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
					(select client_id, count(distinct(client_id)) count from analytics
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