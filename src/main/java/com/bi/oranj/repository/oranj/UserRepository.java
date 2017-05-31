package com.bi.oranj.repository.oranj;

import com.bi.oranj.model.oranj.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by harshavardhanpatil on 5/30/17.
 */
public interface UserRepository extends JpaRepository<User, Integer> {

    User findById(Long id);
}
