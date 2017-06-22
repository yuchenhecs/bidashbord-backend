DROP TABLE IF EXISTS `networth`;
CREATE TABLE `networth` (
  `id` bigint(20) NOT NULL,
  `date` datetime DEFAULT NULL,
  `value` double DEFAULT NULL,
  `client_id` bigint(20) DEFAULT NULL,
  `asset_value` double DEFAULT NULL,
  `liability_value` double DEFAULT NULL,
  `inserted_on` datetime DEFAULT NULL,
  `last_updated_on` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;