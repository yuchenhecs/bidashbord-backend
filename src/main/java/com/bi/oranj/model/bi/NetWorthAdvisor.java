package com.bi.oranj.model.bi;

import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by robertyuan on 6/23/17.
 */
@Data
public class NetWorthAdvisor extends BaseNetWorth {

    private BigDecimal avgAdvisor;
    private BigDecimal avgFirm;
    private List<NetWorthForAdvisor> clients = new ArrayList<>();
}
