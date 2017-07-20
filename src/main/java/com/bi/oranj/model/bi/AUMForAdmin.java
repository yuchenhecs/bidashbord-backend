package com.bi.oranj.model.bi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
public class AUMForAdmin extends BaseData {

    private long totalFirms;
    private List<FirmAUM> firms = new ArrayList<>();
    
    public AUMForAdmin(long totalFirms, List<FirmAUM> firms){
        this.totalFirms = totalFirms;
        this.firms = firms;
    }
}
