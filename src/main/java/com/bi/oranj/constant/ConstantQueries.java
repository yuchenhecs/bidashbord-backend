package com.bi.oranj.constant;

public class ConstantQueries {

    private ConstantQueries(){
        throw new IllegalStateException("ConstantQueries class");
    }

    public static final String GET_ALL_FIRMS_QUERY = "select id, name, created_on, active from firm";

    public static final String GET_ALL_ADVISORS_QUERY = "select ad.id, ad.first_name as advisorFirstName, ad.last_name as advisorLastName, ad.firm_id, a.created, ad.active\n" +
                                                        "from advisor ad\n" +
                                                        "join auth_user a\n" +
                                                        "ON ad.advisor_user_id = a.id";

    public static final String GET_ALL_CLIENTS_QUERY = "select a.id, a.first_name as userFirstName, a.last_name as userLastName, a.advisor_id as advisorId, a.firm_id as firmId, a.created, a.active from auth_user a where advisor_id is not null";

    public static final String GET_ALL_CLIENTS_WHO_ARE_ADVISORS_QUERY  = "select a.id, a.first_name as userFirstName, a.last_name as userLastName, ad.id as advisorId, a.firm_id as firmId, a.created, a.active\n" +
                                                                        "from auth_user a \n" +
                                                                            "\tjoin advisor ad\n" +
                                                                                "\tON ad.advisor_user_id = a.id\n" +
                                                                        "where a.advisor_id is null";

    public static final String GET_ALL_CLIENTS = "select a.id, a.first_name as userFirstName, a.last_name as userLastName, IFNULL(a.advisor_id, ad.id)  as advisorId, a.firm_id as firmId, a.created, a.active, au.role_id, IFNULL(f.coverted, FALSE) as coverted, max(f.created_on) as converted_date\n" +
            "from auth_user a\n" +
            "\tleft join advisor ad\n" +
            "\t\ton  a.id = ad.advisor_user_id\t\n" +
            "\tleft join auth_user_role au\n" +
            "\t\ton au.user_id = a.id\n" +
            "\tleft join (\n" +
            "\t\tselect fa.user_id, fa.created_on, TRUE as coverted from feed_activity_detail fad \n" +
            "\t\t\tjoin feed_activity fa \n" +
            "\t\t\t\ton fa.id = fad.feed_id\n" +
            "\t) f\n" +
            "\t\ton f.user_id = a.id\n" +
            "where IFNULL(a.advisor_id, ad.id) is not NULL\n" +
            "group by a.id;";

    public static final String GET_ALL_ADVISORS_AND_THEIR_CLIENTS = "select a.id, a.advisor_first_name, a.advisor_last_name, a.firm_id, c.id as client_id\n" +
            "from advisors a\n" +
            "join clients c\n" +
            "\tON c.advisor_id = a.id\n" +
            "where a.firm_id = :firm\n" +
            "order by advisor_first_name\n";

    public static final String GET_GOALS_FOR_GIVEN_DAY_QUERY = "select g.*," +
                                                                "a.first_name as userFirstName, a.last_name as userLastName, a.firm_id as firmId, a.advisor_id as advisorId," +
                                                                "ad.first_name as advisorFirstName, ad.last_name as advisorLastName," +
                                                                "f.name as firmName\n" +
                                                                "from user_goal g " +
                                                                    "inner join auth_user a " +
                                                                        "ON g.user_id = a.id \n" +
                                                                    "inner join advisor ad " +
                                                                        "ON a.advisor_id = ad.id \n" +
                                                                    "inner join firm f " +
                                                                        "ON f.id = a.firm_id \n" +
                                                                "where date(creation_date) IN (:date)";

    public static final String GET_GOALS_GROUPED_BY_TYPE = "select type as type, count(*) as count from BiGoal group by type";


    public static final String GET_GOALS_TILL_DATE_QUERY = "select g.*," +
                                                            "a.first_name as userFirstName, a.last_name as userLastName, a.firm_id as firmId, a.advisor_id as advisorId," +
                                                            "ad.first_name as advisorFirstName, ad.last_name as advisorLastName," +
                                                            "f.name as firmName\n" +
                                                            "from user_goal g " +
                                                                "inner join auth_user a " +
                                                                    "ON g.user_id = a.id \n" +
                                                                "inner join advisor ad " +
                                                                    "ON a.advisor_id = ad.id \n" +
                                                                "inner join firm f " +
                                                                    "ON f.id = a.firm_id \n" +
                                                            "where date(creation_date) <= (:date)";

    public static final String GET_AUM_FOR_ADMIN_QUERY = "select f.id, innerTable.asset_class, sum(innerTable.amount)\n" +
            "from firms f\n" +
            "left join clients c\n" +
            "ON f.id = c.firm_id\n" +
            "left join \n" +
            "(SELECT p.client_id, p.asset_class, p.amount from positions p\n" +
            "where date(p.position_updated_on) IN (:date)\n" +
            ") as innerTable\n" +
            "ON innerTable.client_id = c.id\n" +
            "where f.active=1 and c.active=1 and innerTable.asset_class is not null\n" +
            "group by f.id, innerTable.asset_class \n" +
            "order by f.id";

    public static final String GET_AUM_FOR_FIRM_QUERY = "select a.id, innerTable.asset_class, sum(innerTable.amount)\n" +
            "from advisors a\n" +
            "left join clients c\n" +
            "ON a.id = c.advisor_id\n" +
            "left join \n" +
            "(SELECT p.client_id, p.asset_class, p.amount from positions p\n" +
            "where date(p.position_updated_on) IN (:date)\n" +
            ") as innerTable\n" +
            "ON innerTable.client_id = c.id\n" +
            "where c.firm_id=:firm and a.active=1 and c.active=1 and innerTable.asset_class is not null\n" +
            "group by a.id, innerTable.asset_class \n" +
            "order by a.id";

    public static final String GET_AUM_FOR_ADVISOR_QUERY = "select c.id, innerTable.asset_class, sum(innerTable.amount)\n" +
            "from clients c\n" +
            "left join \n" +
            "(SELECT p.client_id, p.asset_class, p.amount from positions p\n" +
            "where date(p.position_updated_on) IN (:date)\n" +
            ") as innerTable\n" +
            "ON innerTable.client_id = c.id\n" +
            "where c.advisor_id=:advisor and c.active=1 and innerTable.asset_class is not null\n" +
            "group by c.id, innerTable.asset_class \n" +
            "order by c.id";

    public static final String GET_AUM_SUMMARY_QUERY = "select p.asset_class, sum(p.amount) as sum\n" +
            "from positions p\n" +
            "where date(p.position_updated_on) IN (:date)\n" +
            "group by p.asset_class";

    public static final String GET_LOGIN_METRICS_FOR_ADMIN_QUERY = "select f.id, f.firm_name as firmName, sum(innerTable.sum) as sessionSum, count(innerTable.client_id) as totalLogins, count(distinct innerTable.client_id) as uniqueLogins\n" +
            "from firms f \n" +
            "left join clients c\n" +
            "ON c.firm_id = f.id\n" +
            "left join\n" +
            "(select a.client_id, session_duration as sum\n" +
            "from analytics a\n" +
            "where a.role_id = :role and date(a.session_start_date) between date(:start) and date(:end)" +
            ") as innerTable \n" +
            "ON innerTable.client_id = c.id\n" +
            "where f.active=1\n" +
            "group by f.id\n" +
            "order by f.firm_name";

    public static final String GET_LOGIN_METRICS_FOR_FIRM_QUERY = "select ad.id, ad.advisor_first_name as firstName, ad.advisor_last_name as lastName, sum(innerTable.sum) as sessionSum, count(innerTable.client_id) as totalLogins, count(distinct innerTable.client_id) as uniqueLogins\n" +
            "from advisors ad \n" +
            "left join clients c\n" +
            "ON c.advisor_id = ad.id\n" +
            "left join\n" +
            "(select a.client_id, session_duration as sum\n" +
            "from analytics a\n" +
            "where a.role_id = :role and date(a.session_start_date) between date(:start) and date(:end)\n" +
            ") as innerTable \n" +
            "ON innerTable.client_id = c.id\n" +
            "where ad.active=1 and ad.firm_id=:firm\n" +
            "group by ad.id\n" +
            "order by ad.advisor_first_name";

    public static final String GET_LOGIN_METRICS_FOR_ADVISOR_QUERY = "select c.id, c.client_first_name as firstName, c.client_last_name as lastName, sum(innerTable.sum) as sessionSum, count(innerTable.client_id) as totalLogins, count(distinct innerTable.client_id) as uniqueLogins\n" +
            "from clients c\n" +
            "left join\n" +
            "(select a.client_id, session_duration as sum\n" +
            "from analytics a\n" +
            "where a.role_id = :role and date(a.session_start_date) between date(:start) and date(:end)\n" +
            ") as innerTable \n" +
            "ON innerTable.client_id = c.id\n" +
            "where c.active=1 and c.advisor_id=:advisor\n" +
            "group by c.id\n" +
            "order by c.client_first_name";

    public static final String GET_LOGIN_METRICS_FOR_SUMMARY_QUERY = "select sum(totalLogins) as totalLogins, count(client_id) as uniqueLogins, sum(totalSessionDuration) as totalSessionTime from (\n" +
            "select count(*) as totalLogins, client_id, sum(session_duration) as totalSessionDuration\n" +
            "from analytics \n" +
            "where role_id = :role and date(session_start_date) between date(:start) and date(:end)\n" +
            "group by client_id) as o";

    public static final String GET_GAMIFICATION_QUERY = "select g.advisor_id, a.advisor_first_name, a.advisor_last_name, g.points, g.percentile_overall, g.percentile_state, g.percentile_firm,\n" +
            "    innerTable.aum, innerTable.net_worth, innerTable.hni, innerTable.conversion_rate, innerTable.avg_conversion_time, innerTable.retention_rate, innerTable.weekly_logins, innerTable.aum_growth, innerTable.net_worth_growth, innerTable.clientele_growth\n" +
            "    from gamification_advisor g\n" +
            "    left join advisors a\n" +
            "    ON g.advisor_id = a.id\n" +
            "    left join (select * from gamification_categories where advisor_id=:advisor and date(update_date) IN (:date)) as innerTable\n" +
            "    ON g.advisor_id = innerTable.advisor_id\n" +
            "    where g.advisor_id=:advisor and date(updated_on) IN (:date);";
}

