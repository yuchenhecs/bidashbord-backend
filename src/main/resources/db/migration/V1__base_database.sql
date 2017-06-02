DROP TABLE IF EXISTS `firm`;
CREATE TABLE `firm` (
  `id` bigint(20) NOT NULL,
  `firm_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `advisor_table`;
CREATE TABLE `advisor_table` (
  `id` bigint(20) NOT NULL,
  `advisor_first_name` varchar(255) DEFAULT NULL,
  `advisor_last_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `user_table`;
CREATE TABLE `user_table` (
  `id` bigint(20) NOT NULL,
  `client_first_name` varchar(255) DEFAULT NULL,
  `client_last_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `goal`;
CREATE TABLE `goal` (
  `goal_id` bigint(20) NOT NULL,
  `advisor_id` bigint(20) DEFAULT NULL,
  `creation_date` varchar(255) DEFAULT NULL,
  `deleted` bit(1) NOT NULL,
  `firm_id` bigint(20) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `goal_name` varchar(255) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`goal_id`),
  FOREIGN KEY (`advisor_id`) REFERENCES advisor_table(`id`),
  FOREIGN KEY (`firm_id`) REFERENCES firm(`id`),
  FOREIGN KEY (`user_id`) REFERENCES user_table(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `hibernate_sequence`;
CREATE TABLE `hibernate_sequence` (
  `next_val` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `hibernate_sequence` (`next_val`)
VALUES
	(1);
