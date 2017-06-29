DROP TABLE IF EXISTS `roles`;
CREATE TABLE `roles` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `name` varchar(255) NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `roles` (`name`)
VALUES
	('ROLE_USER'),
	('ROLE_ADMIN'),
	('ROLE_LITE'),
	('ROLE_ADVISOR'),
	('ROLE_PROSPECT'),
	('ROLE_CLIENT'),
	('ROLE_SUPER_ADMIN');

DROP TABLE IF EXISTS `analytics`;
CREATE TABLE `analytics` (
  `id` bigint(20) NOT NULL,
  `client_id` bigint(20) NOT NULL,
  `session_duration` int(10) NOT NULL,
  `session_start_date` datetime DEFAULT NULL,
  `role_id` bigint(3) NOT NULL,
  `inserted_on` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`client_id`) REFERENCES clients(`id`),
  FOREIGN KEY (`role_id`) REFERENCES roles(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;