package com.bi.oranj.repository.bi;

import com.bi.oranj.model.bi.BiGoal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Created by jaloliddinbakirov on 5/30/17.
 * returns next given num of records from db
 */
public interface GoalRepository extends JpaRepository<BiGoal, Integer> {

    @Query(value = "SELECT count(id) FROM BiGoal")
    public Integer totalGoals();

    @Query(value = "SELECT count(id) FROM BiGoal WHERE firmId = ?1")
    public Integer totalAdvisorGoals (long firmId);

    @Query(value = "SELECT count(id) FROM BiGoal WHERE advisorId = ?1")
    public Integer totalClientGoals (long advisorId);

    @Query(value = "SELECT count(id) FROM goals WHERE creation_date >= :startDate' 00:00:00' ", nativeQuery = true)
    public Integer totalGoalsWithStartDate (@Param(value = "startDate") String startDate);

    @Query(value = "SELECT count(id) FROM goals WHERE creation_date <= :endDate' 23:59:59'", nativeQuery = true)
    public Integer totalGoalsWithEndDate (@Param(value = "endDate") String endDate);

    @Query (value = "SELECT count(id) FROM goals WHERE creation_date BETWEEN :startDate' 00:00:00' AND :endDate' 23:59:59'", nativeQuery = true)
    public Integer totalGoalsByDateBetween (@Param(value = "startDate") String startDate,
                                            @Param(value = "endDate") String endDate);

    @Query(value = "SELECT count(id) FROM goals WHERE firm_id = :firmId AND creation_date >= :startDate' 00:00:00' ", nativeQuery = true)
    public Integer totalAdvisorGoalsWithStartDate (@Param(value = "startDate") String startDate,
                                                   @Param(value = "firmId") long firmId);

    @Query(value = "SELECT count(id) FROM goals WHERE firm_id = :firmId AND creation_date <= :endDate' 23:59:59'", nativeQuery = true)
    public Integer totalAdvisorGoalsWithEndDate (@Param(value = "endDate") String endDate,
                                                 @Param(value = "firmId") long firmId);

    @Query (value = "SELECT count(id) FROM goals WHERE firm_id = :firmId AND creation_date BETWEEN :startDate' 00:00:00' AND :endDate' 23:59:59'", nativeQuery = true)
    public Integer totalAdvisorGoalsByDateBetween (@Param(value = "startDate") String startDate,
                                                   @Param(value = "endDate") String endDate,
                                                   @Param(value = "firmId") long firmId);

    @Query(value = "SELECT count(id) FROM goals WHERE advisor_id = :advisorId AND creation_date >= :startDate' 00:00:00' ", nativeQuery = true)
    public Integer totalClientGoalsWithStartDate (@Param(value = "startDate") String startDate,
                                                  @Param(value = "advisorId") long advisorId);

    @Query(value = "SELECT count(id) FROM goals WHERE advisor_id = :advisorId AND creation_date <= :endDate' 23:59:59'", nativeQuery = true)
    public Integer totalClientGoalsWithEndDate (@Param(value = "endDate") String endDate,
                                                @Param(value = "advisorId") long advisorId);

    @Query (value = "SELECT count(id) FROM goals WHERE advisor_id = :advisorId AND creation_date " +
                    "BETWEEN :startDate' 00:00:00' AND :endDate' 23:59:59'", nativeQuery = true)
    public Integer totalClientGoalsByDateBetween (@Param(value = "startDate") String startDate,
                                                  @Param(value = "endDate") String endDate,
                                                  @Param(value = "advisorId") long advisorId);
}