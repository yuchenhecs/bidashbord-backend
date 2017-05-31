DROP TABLE IF EXISTS `bi_goal`;
CREATE TABLE `bi_goal` (
  `id` bigint(20) NOT NULL,
  `advisor_first_name` varchar(255) DEFAULT NULL,
  `advisor_id` bigint(20) DEFAULT NULL,
  `advisor_last_name` varchar(255) DEFAULT NULL,
  `creation_date` varchar(255) DEFAULT NULL,
  `deleted` bit(1) NOT NULL,
  `firm_id` bigint(20) DEFAULT NULL,
  `firm_name` varchar(255) DEFAULT NULL,
  `goal_id` bigint(20) DEFAULT NULL,
  `goal_name` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `user_first_name` varchar(255) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  `user_last_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `hibernate_sequence`;
CREATE TABLE `hibernate_sequence` (
  `next_val` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `hibernate_sequence` (`next_val`)
VALUES
	(1);
