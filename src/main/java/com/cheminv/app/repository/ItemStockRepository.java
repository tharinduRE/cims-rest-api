package com.cheminv.app.repository;

import com.cheminv.app.domain.ItemStock;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the ItemStock entity.
 */
@Repository
public interface ItemStockRepository extends JpaRepository<ItemStock, Long>, JpaSpecificationExecutor<ItemStock> {

    List<ItemStock> findAllByStore(Long storeId);
    /*
    @Query("select distinct itemStock from ItemStock itemStock where itemStock.totalQuantity <= itemStock.minimumQuantity and itemStock.stockStore in :stores")
    Page<ItemStock> findAllByLessThanOrEqualToMinimum(@Param("stores") List<StockStore> stores,Pageable pageable);*/

    @Query(value = "select distinct itemStock from ItemStock itemStock left join fetch itemStock.hazardCodes",
        countQuery = "select count(distinct itemStock) from ItemStock itemStock")
    Page<ItemStock> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct itemStock from ItemStock itemStock left join fetch itemStock.hazardCodes")
    List<ItemStock> findAllWithEagerRelationships();

    @Query("select itemStock from ItemStock itemStock left join fetch itemStock.hazardCodes where itemStock.id =:id")
    Optional<ItemStock> findOneWithEagerRelationships(@Param("id") Long id);
}
