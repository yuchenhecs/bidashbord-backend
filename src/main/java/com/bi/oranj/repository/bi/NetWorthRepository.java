package com.bi.oranj.repository.bi;

import com.bi.oranj.model.bi.NetWorth;
import com.bi.oranj.model.bi.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by robertyuan on 6/22/17.
 */
public interface NetWorthRepository extends JpaRepository<NetWorth, Long> {

    @Query(value = "select count(*), sum(n.value)\n" +
            "            from networth n\n" +
            "            join clients c\n" +
            "                ON c.id = n.client_id\n" +
            "            where firm_id=:id\n", nativeQuery = true)
    public List<Object[]> findNetWorthForAdmin(@Param("id") Long id);

    @Query(value = "select count(*), sum(n.value)\n" +
            "            from networth n\n" +
            "            join clients c\n" +
            "                ON c.id = n.client_id\n" +
            "            where advisor_id=:id\n", nativeQuery = true)
    public List<Object[]> findNetWorthForFirm(@Param("id") Long id);

    @Query(value = "select sum(value)\n" +
            "from networth n\n" +
            "where n.client_id=:id and date(n.date) IN (:date)", nativeQuery = true)
    public BigDecimal findNetWorthForAdvisor(@Param("id") Long id, @Param("date") String date);

    @Query(value = "select count(*), sum(n.value)\n" +
            "from networth n\n" +
            "where date(n.date) IN (:date)",nativeQuery = true)
    public List<Object[]> findNetWorthForSummary(@Param("date") String date);
}
