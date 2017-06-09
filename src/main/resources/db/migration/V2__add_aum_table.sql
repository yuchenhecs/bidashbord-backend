DROP TABLE IF EXISTS `positions`;
CREATE TABLE `positions` (
  `id` bigint(20) AUTO_INCREMENT,
  `position_id` bigint(20) NOT NULL,
  `portfolio_id` bigint(20) DEFAULT NULL,
  `ticker_name` varchar(255) DEFAULT NULL,
  `asset_class` varchar(255) DEFAULT NULL,
  `price` decimal(19,2) DEFAULT NULL,
  `quantity` double DEFAULT NULL,
  `amount` decimal(19,2) DEFAULT NULL,
  `currency_code` varchar(10) DEFAULT NULL,
  `creation_date` date DEFAULT NULL,
  `updated_on` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `positions_history`;
CREATE TABLE `positions_history` (
  `id` bigint(20) AUTO_INCREMENT,
  `position_id` bigint(20) DEFAULT NULL,
  `portfolio_id` bigint(20) DEFAULT NULL,
  `ticker_name` varchar(255) DEFAULT NULL,
  `asset_class` varchar(255) DEFAULT NULL,
  `price` decimal(19,2) DEFAULT NULL,
  `quantity` double DEFAULT NULL,
  `amount` decimal(19,2) DEFAULT NULL,
  `currency_code` varchar(10) DEFAULT NULL,
  `creation_date` date DEFAULT NULL,
  `updated_on` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `aum`;
CREATE TABLE `aum` (
  `id` bigint (20) AUTO_INCREMENT,
  `account_id` bigint(20) NOT NULL,
  `portfolio_id` bigint(20) DEFAULT NULL,
  `client_id` bigint(20)DEFAULT NULL,
  `is_inactive` bit(1) DEFAULT 0,
  `creation_date` date DEFAULT NULL,
  `updated_on` datetime NOT NULL,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`client_id`) REFERENCES clients(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `aum_history`;
CREATE TABLE `aum_history` (
  `id` bigint (20) AUTO_INCREMENT,
  `account_id` bigint(20) NOT NULL,
  `portfolio_id` bigint(20) DEFAULT NULL,
  `client_id` bigint(20)DEFAULT NULL,
  `is_inactive` bit(1) DEFAULT 0,
  `creation_date` date DEFAULT NULL,
  `updated_on` datetime NOT NULL,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`client_id`) REFERENCES clients(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
