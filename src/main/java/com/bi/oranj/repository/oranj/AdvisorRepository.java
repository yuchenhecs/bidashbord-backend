package com.bi.oranj.repository.oranj;

import com.bi.oranj.model.oranj.Advisor;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by harshavardhanpatil on 5/30/17.
 */
public interface AdvisorRepository extends JpaRepository<Advisor, Long> {

    Advisor findById(Long id);
}
