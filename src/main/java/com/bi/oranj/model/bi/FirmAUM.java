package com.bi.oranj.model.bi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.math.BigInteger;

/**
 * Created by harshavardhanpatil on 6/12/17.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FirmAUM extends AUMSummary {

    private Long firmId;
}
