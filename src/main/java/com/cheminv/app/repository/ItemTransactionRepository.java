package com.cheminv.app.repository;

import com.cheminv.app.domain.ItemTransaction;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the ItemTransaction entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ItemTransactionRepository extends JpaRepository<ItemTransaction, Long>, JpaSpecificationExecutor<ItemTransaction> {
}
