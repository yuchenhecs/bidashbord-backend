package com.bi.oranj.repository.bi;

import com.bi.oranj.model.bi.BiGoal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

import static com.bi.oranj.constant.ConstantQueries.GET_GOALS_GROUPED_BY_TYPE;
import static com.bi.oranj.constant.ConstantQueries.GET_GOALS_GROUPED_BY_TYPE_FOR_ADVISOR;
import static com.bi.oranj.constant.ConstantQueries.GET_GOALS_GROUPED_BY_TYPE_FOR_FIRM;

public interface GoalRepository extends JpaRepository<BiGoal, Integer> {

    @Query(value = "select count(g.id) from goals g join firms f on g.firm_id = f.id where f.active = 1;", nativeQuery = true)
    public Integer totalGoals();

    @Query(value = "select count(g.id) from goals g join advisors a on g.advisor_id = a.id where g.firm_id = ?1 and a.active = 1;", nativeQuery = true)
    public Integer totalAdvisorGoals (long firmId);

    @Query(value = "SELECT count(g.id) FROM goals g join clients c on c.id = g.client_id WHERE g.advisor_id = ?1 and c.active = 1;", nativeQuery = true)
    public Integer totalClientGoals (long advisorId);

    @Query(value = GET_GOALS_GROUPED_BY_TYPE, nativeQuery = true)
    public List<Object[]> findGoalsGroupedByType();

    @Query(value = GET_GOALS_GROUPED_BY_TYPE_FOR_FIRM, nativeQuery = true)
    public List<Object[]> findGoalsGroupedByTypeForFirm(@Param("firmId") Long firmId);

    @Query(value = GET_GOALS_GROUPED_BY_TYPE_FOR_ADVISOR, nativeQuery = true)
    public List<Object[]> findGoalsGroupedByTypeForAdvisor(@Param("advisorId") Long advisorId);

    @Query(value = "SELECT count(id) FROM goals WHERE goal_creation_date >= :startDate' 00:00:00' ", nativeQuery = true)
    public Integer totalGoalsWithStartDate (@Param(value = "startDate") String startDate);

    @Query(value = "SELECT count(id) FROM goals WHERE goal_creation_date <= :endDate' 23:59:59'", nativeQuery = true)
    public Integer totalGoalsWithEndDate (@Param(value = "endDate") String endDate);

    @Query (value = "SELECT count(id) FROM goals WHERE goal_creation_date BETWEEN :startDate' 00:00:00' AND :endDate' 23:59:59'", nativeQuery = true)
    public Integer totalGoalsByDateBetween (@Param(value = "startDate") String startDate,
                                            @Param(value = "endDate") String endDate);

    @Query(value = "SELECT count(id) FROM goals WHERE firm_id = :firmId AND goal_creation_date >= :startDate' 00:00:00' ", nativeQuery = true)
    public Integer totalAdvisorGoalsWithStartDate (@Param(value = "startDate") String startDate,
                                                   @Param(value = "firmId") long firmId);

    @Query(value = "SELECT count(id) FROM goals WHERE firm_id = :firmId AND goal_creation_date <= :endDate' 23:59:59'", nativeQuery = true)
    public Integer totalAdvisorGoalsWithEndDate (@Param(value = "endDate") String endDate,
                                                 @Param(value = "firmId") long firmId);

    @Query (value = "SELECT count(id) FROM goals WHERE firm_id = :firmId AND goal_creation_date BETWEEN :startDate' 00:00:00' AND :endDate' 23:59:59'", nativeQuery = true)
    public Integer totalAdvisorGoalsByDateBetween (@Param(value = "startDate") String startDate,
                                                   @Param(value = "endDate") String endDate,
                                                   @Param(value = "firmId") long firmId);

    @Query(value = "SELECT count(id) FROM goals WHERE advisor_id = :advisorId AND goal_creation_date >= :startDate' 00:00:00' ", nativeQuery = true)
    public Integer totalClientGoalsWithStartDate (@Param(value = "startDate") String startDate,
                                                  @Param(value = "advisorId") long advisorId);

    @Query(value = "SELECT count(id) FROM goals WHERE advisor_id = :advisorId AND goal_creation_date <= :endDate' 23:59:59'", nativeQuery = true)
    public Integer totalClientGoalsWithEndDate (@Param(value = "endDate") String endDate,
                                                @Param(value = "advisorId") long advisorId);

    @Query (value = "SELECT count(id) FROM goals WHERE advisor_id = :advisorId AND goal_creation_date " +
                    "BETWEEN :startDate' 00:00:00' AND :endDate' 23:59:59'", nativeQuery = true)
    public Integer totalClientGoalsByDateBetween (@Param(value = "startDate") String startDate,
                                                  @Param(value = "endDate") String endDate,
                                                  @Param(value = "advisorId") long advisorId);
}