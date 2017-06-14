package com.bi.oranj.repository.bi;

import com.bi.oranj.constant.ConstantQueries;
import com.bi.oranj.model.bi.Aum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

import static com.bi.oranj.constant.ConstantQueries.GET_AUM_FOR_CLIENT_QUERY;

/**
 * Created by jaloliddinbakirov on 6/8/17.
 */
public interface AumRepository extends JpaRepository<Aum, Long> {

    @Query(value = GET_AUM_FOR_CLIENT_QUERY, nativeQuery = true)
    public List<Object[]> findAUMsForClient(@Param("client") Long client, @Param("start") String start, @Param("end") String end);

    @Query(value = ConstantQueries.GET_AUM_FOR_ADVISOR_QUERY, nativeQuery = true)
    public List<Object[]> findAUMsForAdvisor(@Param("advisor") Long advisor, @Param("start") String start, @Param("end") String end);

    @Query(value = ConstantQueries.GET_AUM_FOR_FIRM_QUERY, nativeQuery = true)
    public List<Object[]> findAUMsForFirm(@Param("firm") Long firm, @Param("start") String start, @Param("end") String end);

}
