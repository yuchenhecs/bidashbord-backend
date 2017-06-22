package com.bi.oranj.repository.bi;

import com.bi.oranj.constant.ConstantQueries;
import com.bi.oranj.model.bi.NetWorth;
import com.bi.oranj.model.bi.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

import static com.bi.oranj.constant.ConstantQueries.GET_AUM_FOR_CLIENT_QUERY;
import static com.bi.oranj.constant.ConstantQueries.GET_AUM_SUMMARY_QUERY;

public interface NetWorthRepository extends JpaRepository<NetWorth, Long> {
}