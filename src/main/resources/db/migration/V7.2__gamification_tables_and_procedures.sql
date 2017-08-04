DROP TABLE IF EXISTS `gamification_kpi_percentile_firm`;
CREATE TABLE `gamification_kpi_percentile_firm` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `advisor_id` bigint(20) NOT NULL,
  `aum` decimal(19,4) DEFAULT 0,
  `net_worth` decimal(19,4) DEFAULT 0,
  `hni` decimal(19, 4) DEFAULT 0,
  `conversion_rate` decimal(19,4) DEFAULT 0,
  `avg_conversion_time` decimal(19,4) DEFAULT 0,
  `retention_rate` decimal(19,4) DEFAULT 0,
  `weekly_logins` decimal(19, 4) DEFAULT 0,
  `aum_growth` decimal(19,4) DEFAULT 0,
  `net_worth_growth` decimal(19,4) DEFAULT 0,
  `clientele_growth` decimal(19,4) DEFAULT 0,
  `update_date` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`advisor_id`) REFERENCES `advisors` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `gamification_kpi_percentile_state`;
CREATE TABLE `gamification_kpi_percentile_state` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `advisor_id` bigint(20) NOT NULL,
  `aum` decimal(19,4) DEFAULT 0,
  `net_worth` decimal(19,4) DEFAULT 0,
  `hni` decimal(19, 4) DEFAULT 0,
  `conversion_rate` decimal(19,4) DEFAULT 0,
  `avg_conversion_time` decimal(19,4) DEFAULT 0,
  `retention_rate` decimal(19,4) DEFAULT 0,
  `weekly_logins` decimal(19, 4) DEFAULT 0,
  `aum_growth` decimal(19,4) DEFAULT 0,
  `net_worth_growth` decimal(19,4) DEFAULT 0,
  `clientele_growth` decimal(19,4) DEFAULT 0,
  `update_date` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`advisor_id`) REFERENCES `advisors` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `gamification_kpi_percentile_overall`;
CREATE TABLE `gamification_kpi_percentile_overall` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `advisor_id` bigint(20) NOT NULL,
  `aum` decimal(19,4) DEFAULT 0,
  `net_worth` decimal(19,4) DEFAULT 0,
  `hni` decimal(19, 4) DEFAULT 0,
  `conversion_rate` decimal(19,4) DEFAULT 0,
  `avg_conversion_time` decimal(19,4) DEFAULT 0,
  `retention_rate` decimal(19,4) DEFAULT 0,
  `weekly_logins` decimal(19, 4) DEFAULT 0,
  `aum_growth` decimal(19,4) DEFAULT 0,
  `net_worth_growth` decimal(19,4) DEFAULT 0,
  `clientele_growth` decimal(19,4) DEFAULT 0,
  `update_date` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`advisor_id`) REFERENCES `advisors` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


