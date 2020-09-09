package com.cheminv.app.repository;

import com.cheminv.app.domain.MeasUnit;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the MeasUnit entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MeasUnitRepository extends JpaRepository<MeasUnit, Long> {
}
