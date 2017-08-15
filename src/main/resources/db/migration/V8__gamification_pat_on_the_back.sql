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