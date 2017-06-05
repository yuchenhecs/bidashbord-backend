package com.bi.oranj.model.bi;

import com.bi.oranj.controller.bi.resp.BIResponse;
import com.bi.oranj.serializer.GoalResponseSerializer;
import com.bi.oranj.model.bi.wrapper.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.Collection;

/**
 * Created by jaloliddinbakirov on 5/30/17.
 *
 * User is an abstract class extended by Client, Advisor, Firm classes
 *
 */

@JsonSerialize(using = GoalResponseSerializer.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GoalResponse implements BIResponse{

    private int totalGoals;     // _
//    private int totalFirms;     //  |
//    private int totalAdvisors;  //  | one of them
//    private int totalClients;   // _|

    private int totalUsers;

    private int page;   //  page number
    private int count;  //  page count
    private boolean last;
    private Collection<? extends User> users;

    public int getTotalGoals() {
        return totalGoals;
    }

    public void setTotalGoals(int totalGoals) {
        this.totalGoals = totalGoals;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Collection<? extends User> getUsers() {
        return users;
    }

    public void setUsers(Collection<? extends User> users) {
        this.users = users;
    }

    public int getTotalUsers() {
        return totalUsers;
    }

    public void setTotalUsers(int totalUsers) {
        this.totalUsers = totalUsers;
    }

    public boolean isLast() {
        return last;
    }

    public void setLast(boolean last) {
        this.last = last;
    }

}
