package com.bi.oranj.repository.bi;

import com.bi.oranj.model.bi.GamificationCategories;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GamificationRepository extends JpaRepository<GamificationCategories, Long> {
}
