package com.bi.oranj.repository.bi;

import com.bi.oranj.model.bi.GamificationAdvisor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GamificationRankRepository extends JpaRepository<GamificationAdvisor, Long>{
}
