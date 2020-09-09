package com.cheminv.app.repository;

import com.cheminv.app.domain.InvStorage;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the InvStorage entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InvStorageRepository extends JpaRepository<InvStorage, Long> {
}
