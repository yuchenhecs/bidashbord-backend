package com.bi.oranj.repository.bi;

import com.bi.oranj.model.bi.GridEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by jaloliddinbakirov on 8/3/17.
 */
public interface GridRepository extends JpaRepository<GridEntity, Long>{

    @Query (value = "SELECT * FROM grid_config WHERE user_id = :userId", nativeQuery = true)
    public GridEntity getGridConfig (@Param("userId") Long userId);

    @Transactional
    @Modifying
    @Query (value = "INSERT INTO grid_config (user_id, goals, aum, net_worth, logins) \n" +
            "values (:userId, :goals, :aum, :netWorth, :logins) \n" +
            "ON DUPLICATE KEY UPDATE goals = VALUES(goals), aum = VALUES(aum), \n" +
            "net_worth = VALUES (net_worth), logins = VALUES (logins);", nativeQuery = true)
    public void insertOrUpdateIfExists (@Param("userId") Long userId,
                                        @Param("goals") String goals,
                                        @Param("aum") String aum,
                                        @Param("netWorth") String netWorth,
                                        @Param("logins") String logins);
}
