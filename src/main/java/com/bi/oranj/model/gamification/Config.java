package com.bi.oranj.model.gamification;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.context.annotation.Configuration;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Created by jaloliddinbakirov on 7/20/17.
 */
@Data
@Entity
@Table(name = "gamification_config")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Config {

    @JsonIgnore
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name = "config_name")
    private String configName;

    private BigDecimal value;
}
