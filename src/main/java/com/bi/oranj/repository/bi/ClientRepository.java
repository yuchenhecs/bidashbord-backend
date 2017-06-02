package com.bi.oranj.repository.bi;

import com.bi.oranj.model.bi.Client;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by harshavardhanpatil on 6/2/17.
 */
public interface ClientRepository extends JpaRepository<Client, Long> {
}
