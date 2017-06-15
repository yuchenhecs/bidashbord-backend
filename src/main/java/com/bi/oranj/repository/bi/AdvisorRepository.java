package com.bi.oranj.repository.bi;

import com.bi.oranj.model.bi.Advisor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.bi.oranj.model.bi.BiGoal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

import static com.bi.oranj.constant.ConstantQueries.GET_ALL_ADVISORS_AND_THEIR_CLIENTS;


/**
 * Created by harshavardhanpatil on 6/2/17.
 */
public interface AdvisorRepository extends JpaRepository<Advisor, Long> {

    @Query(value = "SELECT DISTINCT(advisorId) FROM BiGoal WHERE firmId = ?1")
    public List<Advisor> findDistinct(long firmId);

    @Query (value = "SELECT a.id advisorId, a.advisor_first_name firstName, a.advisor_last_name lastName, g.type type, " +
            "COUNT(g.id) count FROM goals g " +
            "JOIN advisors a ON g.advisor_id = a.id WHERE g.firm_id = ?1 GROUP BY g.type, a.id, a.advisor_first_name " +
            "ORDER BY firstName LIMIT ?2, ?3", nativeQuery = true)
    public List<Object[]> findGoalsOrdered (long firmId, int start, int next);

    public Page<Advisor> findByFirmId(Long firmId, Pageable pageable);

}
