package com.cheminv.app.repository;

import com.cheminv.app.domain.InvReport;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the InvReport entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InvReportRepository extends JpaRepository<InvReport, Long> {
}
