package com.bi.oranj.repository.bi;

import com.bi.oranj.model.bi.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PositionRepository extends JpaRepository <Position, Integer>{

    @Query(value = "SELECT EXISTS (SELECT 1 FROM positions WHERE position_updated_on > :date)", nativeQuery = true)
    public Integer isExist (@Param(value = "date") String date);

    @Query(value = "DELETE FROM positions WHERE position_updated_on >= :date", nativeQuery = true)
    public void deleteAllAfterDate (@Param("date") String date);
}
