DROP TABLE IF EXISTS `cron_monitoring`;
CREATE TABLE `cron_monitoring` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `task_name` varchar(250) DEFAULT NULL,
    `error_message` varchar(250) DEFAULT NULL,
    `start_time` datetime DEFAULT NULL,
    `end_time` datetime DEFAULT NULL,
    `inserted_on` datetime DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;