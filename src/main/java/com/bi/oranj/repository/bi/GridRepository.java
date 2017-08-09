package com.bi.oranj.repository.bi;

import com.bi.oranj.model.bi.GridEntity;
import org.hibernate.annotations.SQLUpdate;
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
    public List<GridEntity> getGridConfig (@Param("userId") Long userId);

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO grid_config (user_id, tile_type, settings) " +
            "values (:userId, :tileType, :settings) " +
            "ON DUPLICATE KEY UPDATE settings = VALUES(settings);", nativeQuery = true)
    public List<GridEntity> insertOrUpdateIfExists(@Param("userId") Long userId,
                                                   @Param("tileType") String tileType,
                                                   @Param("settings") String settings);
}
