package com.bi.oranj.constant;

/**
 * Created by harshavardhanpatil on 5/26/17.
 */
public class ConstantQueries {

    public static final String FIND_FIRM = "SELECT name FROM Firm where id = ?1";

    public static final String GET_GOALS_QUERY = "select g.*," +
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


    public static final String GET_CLIENT_AND_GOAL_INFO = "select client.id, client.first_name, client.last_name, " +
            "client.advisor_id, client.firm_id, a.first_name advisorFirstName, a.last_name advisorLastName,  f.name firmName from auth_user client\n" +
            " join advisor a on a.id = client.advisor_id\n" +
            " join firm f on f.id = client.firm_id\n" +
            " where client.id = :id";

}
