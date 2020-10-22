package com.cheminv.app.repository;

import com.cheminv.app.domain.Item;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Item entity.
 */
@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query(value = "select distinct item from Item item left join fetch item.hazardCodes",
        countQuery = "select count(distinct item) from Item item")
    Page<Item> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct item from Item item left join fetch item.hazardCodes")
    List<Item> findAllWithEagerRelationships();

    @Query("select item from Item item left join fetch item.hazardCodes where item.id =:id")
    Optional<Item> findOneWithEagerRelationships(@Param("id") Long id);
}
