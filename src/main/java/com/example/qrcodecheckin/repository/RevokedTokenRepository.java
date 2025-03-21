package com.example.qrcodecheckin.repository;

import com.example.qrcodecheckin.entity.RevokedToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RevokedTokenRepository extends JpaRepository<RevokedToken, String> {
}
