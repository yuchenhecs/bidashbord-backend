package com.bi.oranj.repository.bi;

import com.bi.oranj.model.bi.NetWorth;
import com.bi.oranj.model.bi.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by robertyuan on 6/22/17.
 */
public interface NetWorthRepository extends JpaRepository<NetWorth, Long> {

    @Query(value = "select count(*), sum(p.amount)\n" +
            "from positions p\n" +
            "join clients c\n" +
            "    ON c.id = p.client_id\n" +
            "where firm_id=:id", nativeQuery = true)
    public List<Object[]> findNetWorthForAdmin(@Param("id") Long id);

    @Query(value = "select count(*), sum(p.amount)\n" +
            "from positions p\n" +
            "join clients c\n" +
            "    ON c.id = p.client_id\n" +
            "where advisor_id=:id", nativeQuery = true)
    public List<Object[]> findNetWorthForFirm(@Param("id") Long id);
}
