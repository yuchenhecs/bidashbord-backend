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

    public static final String GET_ALL_CLIENTS_WHO_ARE_ADVISORS_QUERY = "select a.id, a.first_name as userFirstName, a.last_name as userLastName, ad.id as advisorId, a.firm_id as firmId, a.created, a.active\n" +
                                                                        "from auth_user a \n" +
                                                                            "\tjoin advisor ad\n" +
                                                                                "\tON ad.advisor_user_id = a.id\n" +
                                                                        "where a.advisor_id is null";

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

    public static final String GET_AUM_FOR_FIRM_QUERY = "select p.client_id as clientId, c.client_first_name, c.client_last_name, p.asset_class, sum(p.amount) as positionAmount, p.position_updated_on, f.id\n" +
            "from positions p\n" +
            "join clients c\n" +
            "ON c.id = p.client_id\n" +
            "join firms f\n" +
            "ON f.id = c.firm_id\n" +
            "where f.id = :firm and date(p.position_updated_on) IN (:date) and c.active = 1\n" +
            "group by p.asset_class, p.position_updated_on, p.client_id\n" +
            "order by p.asset_class";

    public static final String GET_AUM_FOR_ADVISOR_QUERY = "select p.client_id as clientId, c.client_first_name, c.client_last_name, p.asset_class, sum(p.amount) as positionAmount, p.position_updated_on, ad.id\n" +
            "from positions p\n" +
            "join clients c\n" +
            "\tON c.id = p.client_id\n" +
            "join advisors ad\n" +
            "\tON ad.id = c.advisor_id\n" +
            "where ad.id = :advisor and date(p.position_updated_on) IN (:date)\n" +
            "group by p.asset_class, p.position_updated_on, p.client_id\n" +
            "order by p.asset_class;\n";

    public static final String GET_AUM_FOR_CLIENT_QUERY = "select p.client_id as clientId, c.client_first_name, c.client_last_name, p.asset_class, sum(p.amount) as positionAmount\n" +
            "from positions p\n" +
            "join clients c\n" +
            "\tON c.id = p.client_id\n" +
            "join advisors ad\n" +
            "\tON ad.id = c.advisor_id\n" +
            "where p.client_id = :client and date(p.position_updated_on) IN (:date)\n" +
            "group by p.asset_class\n" +
            "order by p.asset_class\n";

    public static final String GET_AUM_SUMMARY_QUERY = "select p.asset_class, sum(p.amount) as sum\n" +
            "from positions p\n" +
            "where date(p.position_updated_on) IN (:date)\n" +
            "group by p.asset_class";

    public static final String GET_LOGIN_METRICS_FOR_ADMIN_QUERY = "select count(*) as totalLogins, client_id, sum(session_duration)\n" +
            "from analytics a\n" +
            "join clients c\n" +
            "ON a.client_id = c.id\n" +
            "join roles r\n" +
            "ON r.id = a.role_id\n" +
            "where c.firm_id = :firm and r.id = :role and date(a.session_start_date) between date(:start) and date(:end)\n" +
            "group by a.client_id";

    public static final String GET_LOGIN_METRICS_FOR_FIRM_QUERY = "select count(*) as totalLogins, client_id, sum(session_duration)\n" +
            "from analytics a\n" +
            "join clients c\n" +
            "ON a.client_id = c.id\n" +
            "join roles r\n" +
            "ON r.id = a.role_id\n" +
            "where c.advisor_id = :advisor and r.id = :role and date(a.session_start_date) between date(:start) and date(:end)\n" +
            "group by a.client_id";

    public static final String GET_LOGIN_METRICS_FOR_ADVISOR_QUERY = "select count(*) as totalLogins, client_id, sum(session_duration)\n" +
            "from analytics a\n" +
            "join clients c\n" +
            "ON a.client_id = c.id\n" +
            "join roles r\n" +
            "ON r.id = a.role_id\n" +
            "where c.id = :client and r.id = :role and date(a.session_start_date) between date(:start) and date(:end)\n" +
            "group by a.client_id";
}

