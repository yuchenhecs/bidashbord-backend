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


}
