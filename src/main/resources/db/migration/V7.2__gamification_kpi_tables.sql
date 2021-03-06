DROP TABLE IF EXISTS `gamification_kpi_percentile_firm`;
CREATE TABLE `gamification_kpi_percentile_firm` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `advisor_id` bigint(20) NOT NULL,
  `aum` decimal(19,4) DEFAULT 0,
  `net_worth` decimal(19,4) DEFAULT 0,
  `hni` decimal(19, 4) DEFAULT 0,
  `conversion_rate` decimal(19,4) DEFAULT 0,
  `avg_conversion_time` decimal(19,4) DEFAULT 0,
  `retention_rate` decimal(19,4) DEFAULT 0,
  `weekly_logins` decimal(19, 4) DEFAULT 0,
  `aum_growth` decimal(19,4) DEFAULT 0,
  `net_worth_growth` decimal(19,4) DEFAULT 0,
  `clientele_growth` decimal(19,4) DEFAULT 0,
  `update_date` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`advisor_id`) REFERENCES `advisors` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `gamification_kpi_percentile_state`;
CREATE TABLE `gamification_kpi_percentile_state` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `advisor_id` bigint(20) NOT NULL,
  `aum` decimal(19,4) DEFAULT 0,
  `net_worth` decimal(19,4) DEFAULT 0,
  `hni` decimal(19, 4) DEFAULT 0,
  `conversion_rate` decimal(19,4) DEFAULT 0,
  `avg_conversion_time` decimal(19,4) DEFAULT 0,
  `retention_rate` decimal(19,4) DEFAULT 0,
  `weekly_logins` decimal(19, 4) DEFAULT 0,
  `aum_growth` decimal(19,4) DEFAULT 0,
  `net_worth_growth` decimal(19,4) DEFAULT 0,
  `clientele_growth` decimal(19,4) DEFAULT 0,
  `update_date` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`advisor_id`) REFERENCES `advisors` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `gamification_kpi_percentile_overall`;
CREATE TABLE `gamification_kpi_percentile_overall` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `advisor_id` bigint(20) NOT NULL,
  `aum` decimal(19,4) DEFAULT 0,
  `net_worth` decimal(19,4) DEFAULT 0,
  `hni` decimal(19, 4) DEFAULT 0,
  `conversion_rate` decimal(19,4) DEFAULT 0,
  `avg_conversion_time` decimal(19,4) DEFAULT 0,
  `retention_rate` decimal(19,4) DEFAULT 0,
  `weekly_logins` decimal(19, 4) DEFAULT 0,
  `aum_growth` decimal(19,4) DEFAULT 0,
  `net_worth_growth` decimal(19,4) DEFAULT 0,
  `clientele_growth` decimal(19,4) DEFAULT 0,
  `update_date` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`advisor_id`) REFERENCES `advisors` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `gamification_categories`;
CREATE TABLE IF NOT EXISTS gamification_categories (
    id bigint (20) NOT NULL AUTO_INCREMENT,
    advisor_id bigint(20) NOT NULL,
    aum decimal(19,2) DEFAULT NULL,
    net_worth decimal(19,2) DEFAULT NULL,
    hni int DEFAULT NULL,
    conversion_rate decimal(10,4) DEFAULT NULL,
    avg_conversion_time decimal (10, 2) DEFAULT NULL,
    retention_rate decimal (10,4) DEFAULT NULL,
    weekly_logins int DEFAULT NULL,
    aum_growth decimal (10,4) DEFAULT NULL,
    net_worth_growth decimal (19,4) DEFAULT NULL,
    clientele_growth decimal (19,4) DEFAULT NULL,
    update_date datetime DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    FOREIGN KEY (advisor_id) REFERENCES advisors(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `gamification_advisor`;
CREATE TABLE IF NOT EXISTS gamification_advisor (
    id bigint(20) not null auto_increment,
    advisor_id bigint(20) not null,
    points bigint(20) default 0,
    percentile_state decimal(19,4) default 0,
    percentile_overall decimal(19,4) default 0,
    percentile_firm decimal(19,4) default 0,
    updated_on datetime default CURRENT_TIMESTAMP,
    primary key (id),
    foreign key (advisor_id) references advisors (id)
);

ALTER TABLE firms
ADD `state` varchar(255) DEFAULT NULL;

create table IF NOT EXISTS gamification_config(
id bigint(20) not null auto_increment,
config_name varchar(100) default null,
value decimal (19, 4) default null,
primary key (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


