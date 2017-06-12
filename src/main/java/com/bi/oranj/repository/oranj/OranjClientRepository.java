package com.bi.oranj.repository.oranj;

import com.bi.oranj.model.oranj.OranjClient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigInteger;
import java.util.List;

import static com.bi.oranj.constant.ConstantQueries.GET_CLIENT_AND_GOAL_INFO;

/**
 * Created by jaloliddinbakirov on 6/12/17.
 */
public interface OranjClientRepository extends JpaRepository<OranjClient, Integer> {


    @Query (value = GET_CLIENT_AND_GOAL_INFO, nativeQuery = true)
    List<OranjClient> findByClientId (@Param("id") BigInteger id);

}
