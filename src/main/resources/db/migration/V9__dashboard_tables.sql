DROP TABLE IF EXISTS `grid_config`;
CREATE TABLE `grid_config` (
  `user_id` bigint(20) NOT NULL,
  `goals` varchar(50) DEFAULT NULL,
  `aum` varchar(50) DEFAULT NULL,
  `net_worth` varchar(50) DEFAULT NULL,
  `logins` varchar(50) DEFAULT NULL,
  `update_date` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;