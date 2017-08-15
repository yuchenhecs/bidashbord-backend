package com.bi.oranj.model.bi;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by robertyuan on 6/23/17.
 */
@Data
public class NetWorthFirm extends BaseNetWorth {

    private List<NetWorthForFirm> advisors = new ArrayList<>();
}

