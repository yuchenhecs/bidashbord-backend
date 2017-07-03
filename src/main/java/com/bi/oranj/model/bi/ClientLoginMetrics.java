package com.bi.oranj.model.bi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * Created by harshavardhanpatil on 7/3/17.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ClientLoginMetrics extends BaseLoginMetrics {

    private Long clientId;
}
