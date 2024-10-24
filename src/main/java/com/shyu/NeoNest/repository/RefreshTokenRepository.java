package com.shyu.NeoNest.repository;

import com.shyu.NeoNest.domain.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    boolean existsByRefresh(String refresh);

    @Transactional
    void deleteByRefresh(String refresh);

}
