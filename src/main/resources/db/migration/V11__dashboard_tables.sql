DROP TABLE IF EXISTS `grid_config`;
CREATE TABLE `grid_config` (
  `user_id` bigint(20) NOT NULL,
  `tile_type` varchar(100) NOT NULL,
  `settings` varchar(100) NOT NULL,
  `update_date` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`user_id`,`tile_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
