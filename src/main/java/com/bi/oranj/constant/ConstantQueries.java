package com.bi.oranj.constant;

/**
 * Created by harshavardhanpatil on 5/26/17.
 */
public class ConstantQueries {

    public static final String FIND_FIRM = "SELECT name FROM Firm where id = ?1";

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



    public static final String GET_CLIENT_AND_GOAL_INFO = "select g.*," +
                                                            "a.first_name as userFirstName, a.last_name as userLastName, a.firm_id as firmId, a.advisor_id as advisorId," +
                                                            "ad.first_name as advisorFirstName, ad.last_name as advisorLastName," +
                                                            "f.name as firmName\n" +
                                                            "from user_goal g " +
                                                                "inner join auth_user a " +
                                                                    "ON g.user_id = a.id \n" +
                                                                "inner join advisor ad " +
                                                                    "ON a.advisor_id = ad.id \n" +
                                                                "inner join firm f " +
                                                                    "ON f.id = a.firm_id " +
                                                            "where a.id = :id";

    public static final String GET_AUM_FOR_ADMIN_QUERY = "select c.firm_id, f.firm_name, p.asset_class, sum(p.amount) as positionAmount, c.id " +
                                                            "from aum a\n" +
                                                                "join positions p\n" +
                                                                    "ON p.portfolio_id = a.portfolio_id\n" +
                                                                "join clients c\n" +
                                                                    "ON c.id = a.client_id\n" +
                                                                "join firms f\n" +
                                                                    "ON c.firm_id = f.id\n" +
                                                            "where a.updated_on >= :start and a.updated_on <= :end\n"+
                                                            "group by p.asset_class, c.firm_id, a.client_id\n" +
                                                            "order by firm_id";


    public static final String GET_AUM_FOR_FIRM_QUERY = "select ad.id as advisorId, ad.advisor_first_name, p.asset_class, sum(p.amount) as positionAmount, c.firm_id\n" +
                                                        "from aum a\n" +
                                                            "join positions p\n" +
                                                                "ON p.portfolio_id = a.portfolio_id\n" +
                                                            "join clients c\n" +
                                                                "ON c.id = a.client_id\n" +
                                                            "join advisors ad\n" +
                                                                "ON ad.id = c.advisor_id\n" +
                                                        "where c.firm_id = :firm\n" +
                                                        "group by p.asset_class, ad.id\n" +
                                                        "order by ad.id";

    public static final String GET_AUM_FOR_ADVISOR_QUERY = "select c.id as clientId, c.client_first_name, p.asset_class, sum(p.amount) as positionAmount, c.advisor_id\n" +
            "from aum a\n" +
            "join positions p\n" +
            "\tON p.portfolio_id = a.portfolio_id\n" +
            "join clients c\n" +
            "\tON c.id = a.client_id\n" +
            "join advisors ad\n" +
            "\tON ad.id = c.advisor_id\n" +
            "where ad.id = :advisor\n" +
            "group by p.asset_class, a.client_id\n" +
            "order by c.id";

}
