DROP TABLE IF EXISTS `firms`;
CREATE TABLE `firms` (
  `id` bigint(20) NOT NULL,
  `firm_name` varchar(255) DEFAULT NULL,
  `firm_created_on` datetime DEFAULT NULL,
  `active` bit(1) NOT NULL DEFAULT b'1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `advisors`;
CREATE TABLE `advisors` (
  `id` bigint(20) NOT NULL,
  `advisor_first_name` varchar(255) DEFAULT NULL,
  `advisor_last_name` varchar(255) DEFAULT NULL,
  `firm_id` bigint(20) DEFAULT NULL,
  `advisor_created_on` datetime DEFAULT '2010-01-01 00:00:00',
  `active` bit(1) NOT NULL DEFAULT b'1',
  PRIMARY KEY (`id`),
  FOREIGN KEY (`firm_id`) REFERENCES firms(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `clients`;
CREATE TABLE `clients` (
  `id` bigint(20) NOT NULL,
  `client_first_name` varchar(255) DEFAULT NULL,
  `client_last_name` varchar(255) DEFAULT NULL,
  `firm_id` bigint(20) DEFAULT NULL,
  `advisor_id` bigint(20) DEFAULT NULL,
  `client_created_on` datetime DEFAULT NULL,
  `active` bit(1) NOT NULL DEFAULT b'1',
  PRIMARY KEY (`id`),
  FOREIGN KEY (`advisor_id`) REFERENCES advisors(`id`),
  FOREIGN KEY (`firm_id`) REFERENCES firms(`id`)
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `goals`;
CREATE TABLE `goals` (
  `id` bigint(20) NOT NULL,
  `advisor_id` bigint(20) DEFAULT NULL,
  `goal_creation_date` datetime DEFAULT NULL,
  `deleted` bit(1) NOT NULL,
  `firm_id` bigint(20) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `goal_name` varchar(255) DEFAULT NULL,
  `client_id` bigint(20) DEFAULT NULL,
  `inserted_on` datetime DEFAULT NULL,
  `last_updated_on` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`advisor_id`) REFERENCES advisors(`id`),
  FOREIGN KEY (`firm_id`) REFERENCES firms(`id`),
  FOREIGN KEY (`client_id`) REFERENCES clients(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `hibernate_sequence`;
CREATE TABLE `hibernate_sequence` (
  `next_val` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `hibernate_sequence` (`next_val`)
VALUES
	(1);