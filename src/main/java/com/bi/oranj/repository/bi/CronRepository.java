package com.bi.oranj.repository.bi;

import com.bi.oranj.model.bi.Cron;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by jaloliddinbakirov on 7/10/17.
 */
@Repository
public interface CronRepository extends JpaRepository<Cron, Long>{

}
