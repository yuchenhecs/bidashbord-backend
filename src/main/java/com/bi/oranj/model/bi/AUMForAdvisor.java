package com.bi.oranj.model.bi;

import com.bi.oranj.model.bi.AdvisorAUM;
import com.bi.oranj.model.bi.BaseAum;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by harshavardhanpatil on 6/13/17.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AUMForAdvisor extends BaseAum {

    private int totalClients;
    private List<ClientAUM> clients = new ArrayList<>();
}
