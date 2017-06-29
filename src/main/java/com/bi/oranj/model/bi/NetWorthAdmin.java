package com.bi.oranj.model.bi;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by robertyuan on 6/23/17.
 */
@Data
public class NetWorthAdmin extends BaseNetWorth {

    private List<NetWorthForAdmin> firms = new ArrayList<>();

}
