CREATE TABLE `gamification_categories` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `advisor_id` bigint(20) NOT NULL,
  `aum` decimal(19,2) DEFAULT NULL,
  `net_worth` decimal(19,2) DEFAULT NULL,
  `hni` int(11) DEFAULT NULL,
  `conversion_rate` decimal(6,4) DEFAULT NULL,
  `avg_conversion_time` decimal(6,2) DEFAULT NULL,
  `retention_rate` decimal(6,4) DEFAULT NULL,
  `weekly_client_login` int(11) DEFAULT NULL,
  `qoq_aum_growth` decimal(19,4) DEFAULT NULL,
  `qoq_net_worth` decimal(19,4) DEFAULT NULL,
  `clientele_growth` decimal(19,4) DEFAULT NULL,
  `update_date` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `advisor_id` (`advisor_id`),
  CONSTRAINT `gamification_categories_ibfk_1` FOREIGN KEY (`advisor_id`) REFERENCES `advisors` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=45 DEFAULT CHARSET=utf8;