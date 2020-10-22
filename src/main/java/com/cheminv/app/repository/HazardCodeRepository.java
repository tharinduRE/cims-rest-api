package com.cheminv.app.repository;

import com.cheminv.app.domain.HazardCode;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the HazardCode entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HazardCodeRepository extends JpaRepository<HazardCode, Long> {
}
