package com.cheminv.app.repository;

import com.cheminv.app.domain.ItemManufacturer;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the ItemManufacturer entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ItemManufacturerRepository extends JpaRepository<ItemManufacturer, Long> {
}
