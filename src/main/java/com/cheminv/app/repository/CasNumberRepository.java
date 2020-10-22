package com.cheminv.app.repository;

import com.cheminv.app.domain.CasNumber;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the CasNumber entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CasNumberRepository extends JpaRepository<CasNumber, Long> {
}
