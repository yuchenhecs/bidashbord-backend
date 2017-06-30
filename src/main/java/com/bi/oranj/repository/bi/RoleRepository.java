package com.bi.oranj.repository.bi;

import com.bi.oranj.model.bi.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Created by jaloliddinbakirov on 6/29/17.
 */
public interface RoleRepository extends JpaRepository<Role, Long> {
    @Query("SELECT id FROM Role WHERE name = :roleName")
    public Role getRoleId (@Param(value = "roleName") String roleName);
}
