package com.bi.oranj.repository.bi;

import com.bi.oranj.model.bi.Aum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

import static com.bi.oranj.constant.ConstantQueries.GET_AUM_FOR_ADMIN_QUERY;

/**
 * Created by jaloliddinbakirov on 6/8/17.
 */
public interface AumRepository extends JpaRepository<Aum, Long> {

    @Query(value = GET_AUM_FOR_ADMIN_QUERY, nativeQuery = true)
    public List<Object[]> findAUMsForAdmin();

}
