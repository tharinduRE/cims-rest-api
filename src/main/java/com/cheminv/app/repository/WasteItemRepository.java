package com.cheminv.app.repository;

import com.cheminv.app.domain.WasteItem;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the WasteItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WasteItemRepository extends JpaRepository<WasteItem, Long> {
}
