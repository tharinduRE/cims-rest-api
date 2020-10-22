package com.cheminv.app.repository;

import com.cheminv.app.domain.WasteVendor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the WasteVendor entity.
 */
@Repository
public interface WasteVendorRepository extends JpaRepository<WasteVendor, Long> {

    @Query(value = "select distinct wasteVendor from WasteVendor wasteVendor left join fetch wasteVendor.wasteItems",
        countQuery = "select count(distinct wasteVendor) from WasteVendor wasteVendor")
    Page<WasteVendor> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct wasteVendor from WasteVendor wasteVendor left join fetch wasteVendor.wasteItems")
    List<WasteVendor> findAllWithEagerRelationships();

    @Query("select wasteVendor from WasteVendor wasteVendor left join fetch wasteVendor.wasteItems where wasteVendor.id =:id")
    Optional<WasteVendor> findOneWithEagerRelationships(@Param("id") Long id);
}
