package com.example.qrcodecheckin.repository;

import com.example.qrcodecheckin.entity.Role;
import com.example.qrcodecheckin.enums.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleEnum name);
}
