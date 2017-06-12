package com.bi.oranj.repository.bi;

import com.bi.oranj.model.bi.AUM;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by harshavardhanpatil on 6/9/17.
 */
public interface AUMRespository extends JpaRepository<AUM, Long> {

    @Query(value = "select p.asset_class, a.`client_id`, SUM(a.value) from aum a join\n" +
            "positions p on a.`position_id` = p.`position_id`\n" +
            "group by p.asset_class, a.client_id", nativeQuery = true)
    public List<Object[]> findAUMsForAdmin();
}
