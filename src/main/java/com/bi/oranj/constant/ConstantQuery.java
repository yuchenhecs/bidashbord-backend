package com.bi.oranj.constant;

/**
 * Created by harshavardhanpatil on 5/26/17.
 */
public class ConstantQuery {

    public static final String FIND_GOALS = "SELECT id, name, type, creation_date, user_id FROM user_goal";
    public static final String FIND_FIRM = "SELECT name FROM Firm where id = ?1";
}
