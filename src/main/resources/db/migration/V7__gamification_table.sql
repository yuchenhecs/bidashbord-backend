CREATE TABLE gamification_categories (
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