DROP TABLE IF EXISTS `gamification_pat_on_the_back`;
CREATE TABLE `gamification_pat_on_the_back` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `advisor_id` bigint(20) NOT NULL,
  `region` varchar(255) NOT NULL,
  `aum` varchar(255) DEFAULT '0',
  `net_worth` varchar(255) DEFAULT '0',
  `hni` varchar(255) DEFAULT '0',
  `conversion_rate` varchar(255) DEFAULT '0',
  `avg_conversion_time` varchar(255) DEFAULT '0',
  `retention_rate` varchar(255) DEFAULT '0',
  `weekly_logins` varchar(255) DEFAULT '0',
  `aum_growth` varchar(255) DEFAULT '0',
  `net_worth_growth` varchar(255) DEFAULT '0',
  `clientele_growth` varchar(255) DEFAULT '0',
  `created_on` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `advisor_id` (`advisor_id`),
  CONSTRAINT `gamification_pat_on_the_back_ibfk_1` FOREIGN KEY (`advisor_id`) REFERENCES `advisors` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

create index idx_position_updated_on on positions (position_updated_on);
create index idx_networth_date on networth (date);
create index idx_networth_value on networth (value);
create index idx_advisor_active on advisors (active);
create index idx_client_active on clients (active);
create index idx_client_converted on clients (converted);
create index idx_client_created_on on clients (client_created_on);
create index idx_client_converted_date on clients (converted_date);
create index idx_client_role_id on clients (role_id);
create index idx_analytics_role_id on analytics (role_id);
create index idx_analytics_session_start_date on analytics (session_start_date);