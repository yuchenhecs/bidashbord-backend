DROP TABLE IF EXISTS `positions`;
CREATE TABLE `positions` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `position_id` bigint(20) NOT NULL,
  `portfolio_id` bigint(20) DEFAULT NULL,
  `client_id` bigint(20) DEFAULT NULL,
  `ticker_name` varchar(255) DEFAULT NULL,
  `asset_class` varchar(255) DEFAULT NULL,
  `price` decimal(19,2) DEFAULT NULL,
  `quantity` double DEFAULT NULL,
  `amount` decimal(19,2) DEFAULT NULL,
  `currency_code` varchar(10) DEFAULT NULL,
  `position_creation_date` datetime DEFAULT NULL,
  `position_updated_on` datetime NOT NULL,
  `modified_on` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
