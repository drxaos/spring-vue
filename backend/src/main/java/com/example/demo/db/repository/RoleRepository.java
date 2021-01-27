package com.example.demo.db.repository;

import com.example.demo.db.model.auth.Role;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends PersistableRepository<Role> {
    Optional<Role> findByRole(String role);
}
