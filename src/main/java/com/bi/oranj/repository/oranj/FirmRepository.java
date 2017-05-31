package com.bi.oranj.repository.oranj;

import com.bi.oranj.model.oranj.Firm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import static com.bi.oranj.constant.ConstantQuery.FIND_FIRM;

/**
 * Created by harshavardhanpatil on 5/31/17.
 */
public interface FirmRepository extends JpaRepository<Firm, Long> {

    @Query(FIND_FIRM)
    String findByFirm(Long id);
}
