package com.bi.oranj.repository.gamification;

import com.bi.oranj.model.gamification.Config;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;


public interface ConfigRepository extends JpaRepository<Config, Long> {

    @Query (value = "SELECT * FROM gamification_config where config_name = :configName", nativeQuery = true)
    public Config findByConfigName(@Param("configName") String configName);

    @Transactional
    @Modifying
    @Query (value = "UPDATE gamification_config SET value = :newValue WHERE config_name = :configName", nativeQuery = true)
    public void updateByConfigName (@Param("configName") String configName, @Param("newValue")BigDecimal newValue);

}
