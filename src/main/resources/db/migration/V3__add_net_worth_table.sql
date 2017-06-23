DROP TABLE IF EXISTS `net_worth`;
CREATE TABLE `net_worth` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) DEFAULT NULL,
  `price` double DEFAULT NULL,
  `asset_value` double DEFAULT NULL,
  `value` double DEFAULT NULL,
  `liability_value` double DEFAULT NULL,
  `date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;