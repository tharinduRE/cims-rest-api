package com.cheminv.app.repository;

import com.cheminv.app.domain.InvStore;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the InvStore entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InvStoreRepository extends JpaRepository<InvStore, Long> {
}
