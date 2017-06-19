package com.bi.oranj.constant;

public class ConstantQueries {

    public static final String GET_ALL_FIRMS_QUERY = "select id, name from firm";

    public static final String GET_ALL_ADVISORS_QUERY = "select ad.id, ad.first_name as advisorFirstName, ad.last_name as advisorLastName, ad.firm_id from advisor ad";

    public static final String GET_ALL_CLIENTS_QUERY = "select a.id, a.first_name as userFirstName, a.last_name as userLastName, a.advisor_id as advisorId, a.firm_id as firmId from auth_user a where advisor_id is not null";

    public static final String GET_ALL_CLIENTS_WHO_ARE_ADVISORS_QUERY = "select a.id, a.first_name as userFirstName, a.last_name as userLastName, ad.id as advisorId, a.firm_id as firmId \n" +
                                                                        "from auth_user a \n" +
                                                                            "\tjoin advisor ad\n" +
                                                                                "\tON ad.advisor_user_id = a.id\n" +
                                                                        "where a.advisor_id is null;";

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
                                                                "where creation_date >= :start and creation_date <= :end";

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
                                                            "where creation_date <= :end";



    public static final String GET_CLIENT_AND_GOAL_INFO = "select client.id, client.first_name , client.last_name , " +
            "client.advisor_id, client.firm_id , a.first_name  advisorFirstName, a.last_name  advisorLastName,f.name  firmNamefrom  auth_user client \n" +
            " join advisor  aon a.id = client.advisor_id\n" +
            " join firm f on f.id = client.firm_id \n" +
            "where client.id = :id";

    public static final String GET_AUM_FOR_FIRM_QUERY = "select p.client_id as clientId, c.client_first_name, c.client_last_name, p.asset_class, sum(p.amount) as positionAmount, p.position_updated_on, f.id\n" +
            "from positions p\n" +
            "join clients c\n" +
            "\tON c.id = p.client_id\n" +
            "join firms f\n" +
            "\tON f.id = c.firm_id\n" +
            "where f.id = :firm and p.position_updated_on >= :start and p.position_updated_on <= :end\n" +
            "group by p.asset_class, p.position_updated_on, p.client_id\n" +
            "order by p.asset_class;\n";

    public static final String GET_AUM_FOR_ADVISOR_QUERY = "select p.client_id as clientId, c.client_first_name, c.client_last_name, p.asset_class, sum(p.amount) as positionAmount, p.position_updated_on, ad.id\n" +
            "from positions p\n" +
            "join clients c\n" +
            "\tON c.id = p.client_id\n" +
            "join advisors ad\n" +
            "\tON ad.id = c.advisor_id\n" +
            "where ad.id = :advisor and p.position_updated_on >= :start and p.position_updated_on <= :end\n" +
            "group by p.asset_class, p.position_updated_on, p.client_id\n" +
            "order by p.asset_class;\n";

    public static final String GET_AUM_FOR_CLIENT_QUERY = "select p.client_id as clientId, c.client_first_name, c.client_last_name, p.asset_class, sum(p.amount) as positionAmount\n" +
            "from positions p\n" +
            "join clients c\n" +
            "\tON c.id = p.client_id\n" +
            "join advisors ad\n" +
            "\tON ad.id = c.advisor_id\n" +
            "where p.client_id = :client and p.position_updated_on >= :start and p.position_updated_on <= :end\n" +
            "group by p.asset_class\n" +
            "order by p.asset_class\n";

    public static final String GET_AUM_SUMMARY_QUERY = "select p.asset_class, sum(p.amount) as sum\n" +
            "from positions p\n" +
            "where p.position_updated_on >= :start and p.position_updated_on <= :end\n" +
            "group by p.asset_class";

}
