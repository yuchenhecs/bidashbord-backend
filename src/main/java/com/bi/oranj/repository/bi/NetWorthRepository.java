package com.bi.oranj.repository.bi;

import com.bi.oranj.model.bi.NetWorth;
import com.bi.oranj.model.bi.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.annotation.security.PermitAll;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by robertyuan on 6/22/17.
 */
public interface NetWorthRepository extends JpaRepository<NetWorth, Long> {

    @Query(value = "select f.id, f.firm_name, sum(t.value), (sum(t.value) / count(c.id))\n" +
            "from firms f\n" +
            "left join clients c\n" +
            "on f.id = c.firm_id\n" +
            "left join\n" +
            "(SELECT net.client_id, net.value from networth net\n" +
            "where date(net.date) in (:date)) t\n" +
            "on t.client_id = c.id\n" +
            "where c.active = 1\n" +
            "group by f.id\n" +
            "order by f.firm_name\n" +
            "limit :offset, :pageSize", nativeQuery = true)
    public List<Object[]> findNetWorthForAdmin(@Param("offset") Integer offset,
                                               @Param("date") String date,
                                               @Param("pageSize") Integer pageSize);

    @Query(value = "select a.id, a.advisor_first_name, a.advisor_last_name, sum(t.value), sum(t.value) / count(c.id) " +
            "from advisors a " +
            "left join clients c " +
            "on a.id = c.advisor_id " +
            "left join " +
            "(SELECT net.client_id, net.value from networth net " +
            "where date(net.date) in (:date)) t " +
            "on t.client_id = c.id " +
            "where c.active = 1 and a.firm_id = :id " +
            "group by a.id " +
            "order by a.advisor_first_name " +
            "limit :offset, :pageSize", nativeQuery = true)
    public List<Object[]> findNetWorthForFirm(@Param("id") Long id,
                                              @Param("offset") Integer offset,
                                              @Param("date") String date,
                                              @Param("pageSize") Integer pageSize);

    @Query(value = "select c.id, c.client_first_name, c.client_last_name, sum(t.value), sum(t.value)/count(c.id) " +
            "from clients c " +
            "left join  " +
            "(SELECT net.client_id, net.value from networth net " +
            "where date(net.date) in (:date)) t " +
            "on t.client_id = c.id " +
            "where c.active = 1 and c.advisor_id = :id " +
            "group by c.id " +
            "order by c.client_first_name " +
            "limit :offset, :pageSize", nativeQuery = true)
    public List<Object[]> findNetWorthForAdvisor(@Param("id") Long id,
                                                 @Param("offset") Integer offset,
                                                 @Param("date") String date,
                                                 @Param("pageSize") Integer pageSize);

    @Query(value = "select sum(value) " +
            "from clients c " +
            "left join networth n " +
            "on c.id = n.client_id " +
            "where c.advisor_id = :id and c.active = 1 and date(n.date) in (:date)", nativeQuery = true)
    public BigDecimal findAdvisorAverage(@Param("id") Long id,
                                         @Param("date") String date);

    @Query(value = "select sum(t.value)/count(c.id) " +
            "from firms f " +
            "left join clients c " +
            "on f.id = c.firm_id " +
            "left join " +
            "(SELECT net.client_id, net.value from networth net " +
            "where date(net.date) in (:date)) t " +
            "on t.client_id = c.id " +
            "where c.active = 1 and f.id = (select firm_id from advisors where id = :id) " +
            "group by f.id " +
            "order by f.firm_name", nativeQuery = true)
    public BigDecimal findFirmAverage(@Param("id") Long id,
                                      @Param("date") String date);

    @Query(value = "select count(*), sum(n.value) " +
            "from networth n " +
            "where date(n.date) in (:date)",nativeQuery = true)
    public List<Object[]> findNetWorthForSummary(@Param("date") String date);

    @Query(value = "select count(*), sum(n.value) " +
            "from networth n join clients c on n.client_id = c.id " +
            "where c.firm_id = :firmId and date(n.date) in (:date)",nativeQuery = true)
    public List<Object[]> findNetWorthForFirmSummary(@Param("firmId") Long firmId,
                                                     @Param("date") String date);

    @Query(value = "select count(*), sum(n.value) " +
            "from networth n join clients c on n.client_id = c.id " +
            "where c.advisor_id = :advisorId and date(n.date) in (:date)",nativeQuery = true)
    public List<Object[]> findNetWorthForAdvisorSummary(@Param("advisorId") Long advisorId,
                                                        @Param("date") String date);
}
