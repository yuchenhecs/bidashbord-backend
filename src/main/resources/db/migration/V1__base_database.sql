DROP TABLE IF EXISTS `firms`;
CREATE TABLE `firms` (
  `id` bigint(20) NOT NULL,
  `firm_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `advisors`;
CREATE TABLE `advisors` (
  `id` bigint(20) NOT NULL,
  `advisor_first_name` varchar(255) DEFAULT NULL,
  `advisor_last_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `clients`;
CREATE TABLE `clients` (
  `id` bigint(20) NOT NULL,
  `client_first_name` varchar(255) DEFAULT NULL,
  `client_last_name` varchar(255) DEFAULT NULL,
  `firm_id` bigint(20) DEFAULT NULL,
  `advisor_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`advisor_id`) REFERENCES advisors(`id`),
  FOREIGN KEY (`firm_id`) REFERENCES firms(`id`)
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `goals`;
CREATE TABLE `goals` (
  `id` bigint(20) NOT NULL,
  `advisor_id` bigint(20) DEFAULT NULL,
  `creation_date` varchar(255) DEFAULT NULL,
  `deleted` bit(1) NOT NULL,
  `firm_id` bigint(20) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `goal_name` varchar(255) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`advisor_id`) REFERENCES advisors(`id`),
  FOREIGN KEY (`firm_id`) REFERENCES firms(`id`),
  FOREIGN KEY (`user_id`) REFERENCES clients(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `hibernate_sequence`;
CREATE TABLE `hibernate_sequence` (
  `next_val` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `hibernate_sequence` (`next_val`)
VALUES
	(1);
