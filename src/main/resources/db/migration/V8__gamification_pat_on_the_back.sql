DROP TABLE IF EXISTS `gamification_pat_on_the_back`;
CREATE TABLE `gamification_pat_on_the_back` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `advisor_id` bigint(20) NOT NULL,
  `region` enum('STATE','OVERALL','FIRM') NOT NULL,
  `aum` enum('RANK_ONE','RANK_TWO','RANK_THREE','TOP_X','ABOVE_AVG','NO_RANK') DEFAULT 'NO_RANK',
  `net_worth` enum('RANK_ONE','RANK_TWO','RANK_THREE','TOP_X','ABOVE_AVG','NO_RANK') DEFAULT 'NO_RANK',
  `hni` enum('RANK_ONE','RANK_TWO','RANK_THREE','TOP_X','ABOVE_AVG','NO_RANK') DEFAULT 'NO_RANK',
  `conversion_rate` enum('RANK_ONE','RANK_TWO','RANK_THREE','TOP_X','ABOVE_AVG','NO_RANK') DEFAULT 'NO_RANK',
  `avg_conversion_time` enum('RANK_ONE','RANK_TWO','RANK_THREE','TOP_X','ABOVE_AVG','NO_RANK') DEFAULT 'NO_RANK',
  `retention_rate` enum('RANK_ONE','RANK_TWO','RANK_THREE','TOP_X','ABOVE_AVG','NO_RANK') DEFAULT 'NO_RANK',
  `weekly_logins` enum('RANK_ONE','RANK_TWO','RANK_THREE','TOP_X','ABOVE_AVG','NO_RANK') DEFAULT 'NO_RANK',
  `aum_growth` enum('RANK_ONE','RANK_TWO','RANK_THREE','TOP_X','ABOVE_AVG','NO_RANK') DEFAULT 'NO_RANK',
  `net_worth_growth` enum('RANK_ONE','RANK_TWO','RANK_THREE','TOP_X','ABOVE_AVG','NO_RANK') DEFAULT 'NO_RANK',
  `clientele_growth` enum('RANK_ONE','RANK_TWO','RANK_THREE','TOP_X','ABOVE_AVG','NO_RANK') DEFAULT 'NO_RANK',
  `created_on` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `advisor_id` (`advisor_id`),
  CONSTRAINT `gamification_pat_on_the_back_ibfk_1` FOREIGN KEY (`advisor_id`) REFERENCES `advisors` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;


alter table positions drop index idx_position_updated_on;
create index idx_position_updated_on on positions (position_updated_on);

alter table networth drop index idx_networth_date;
create index idx_networth_date on networth (date);

alter table networth drop index idx_networth_value;
create index idx_networth_value on networth (value);

alter table advisors drop index idx_advisor_active;
create index idx_advisor_active on advisors (active);

alter table clients drop index idx_client_active;
create index idx_client_active on clients (active);

alter table clients drop index idx_client_converted;
create index idx_client_converted on clients (converted);

alter table clients drop index idx_client_created_on;
create index idx_client_created_on on clients (client_created_on);

alter table clients drop index idx_client_converted_date;
create index idx_client_converted_date on clients (converted_date);

alter table clients drop index idx_client_role_id;
create index idx_client_role_id on clients (role_id);

alter table analytics drop index idx_analytics_session_start_date;
create index idx_analytics_session_start_date on analytics (session_start_date);