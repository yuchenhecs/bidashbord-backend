package com.bi.oranj.repository.oranj;

import com.bi.oranj.model.bi.AumHistory;
import com.bi.oranj.model.oranj.OranjAumHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by jaloliddinbakirov on 6/12/17.
 */
public interface OranjAumHistoryRepository extends JpaRepository<OranjAumHistory, Integer> {


    @Query(value = "select qport.account accountId, qport.id portfolioId, qport.oranj_user_id clientId, qhis.value amount, " +
            "qhis.is_inactive isInactive, " +
            "qhis.insert_date updatedOn from quovo_portfolios qport\n" +
            "join quovo_portfolios_history qhis \n" +
            "on qhis.account = qport.account where qport.oranj_user_id is not null \n" +
            "limit 0, 1000", nativeQuery = true)
    List<Object[]> fetchAumHistory();
}
