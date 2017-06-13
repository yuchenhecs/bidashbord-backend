package com.bi.oranj.model.bi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by harshavardhanpatil on 6/12/17.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AUMForAdmin extends BaseAum {

    private int totalFirms;
    private List<FirmAUM> firms = new ArrayList<>();
}
