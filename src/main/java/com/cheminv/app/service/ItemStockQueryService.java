package com.cheminv.app.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.cheminv.app.domain.ItemStock;
import com.cheminv.app.domain.*; // for static metamodels
import com.cheminv.app.repository.ItemStockRepository;
import com.cheminv.app.repository.search.ItemStockSearchRepository;
import com.cheminv.app.service.dto.ItemStockCriteria;
import com.cheminv.app.service.dto.ItemStockDTO;
import com.cheminv.app.service.mapper.ItemStockMapper;

/**
 * Service for executing complex queries for {@link ItemStock} entities in the database.
 * The main input is a {@link ItemStockCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ItemStockDTO} or a {@link Page} of {@link ItemStockDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ItemStockQueryService extends QueryService<ItemStock> {

    private final Logger log = LoggerFactory.getLogger(ItemStockQueryService.class);

    private final ItemStockRepository itemStockRepository;

    private final ItemStockMapper itemStockMapper;

    private final ItemStockSearchRepository itemStockSearchRepository;

    public ItemStockQueryService(ItemStockRepository itemStockRepository, ItemStockMapper itemStockMapper, ItemStockSearchRepository itemStockSearchRepository) {
        this.itemStockRepository = itemStockRepository;
        this.itemStockMapper = itemStockMapper;
        this.itemStockSearchRepository = itemStockSearchRepository;
    }

    /**
     * Return a {@link List} of {@link ItemStockDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ItemStockDTO> findByCriteria(ItemStockCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ItemStock> specification = createSpecification(criteria);
        return itemStockMapper.toDto(itemStockRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ItemStockDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ItemStockDTO> findByCriteria(ItemStockCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ItemStock> specification = createSpecification(criteria);
        return itemStockRepository.findAll(specification, page)
            .map(itemStockMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ItemStockCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ItemStock> specification = createSpecification(criteria);
        return itemStockRepository.count(specification);
    }

    /**
     * Function to convert {@link ItemStockCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ItemStock> createSpecification(ItemStockCriteria criteria) {
        Specification<ItemStock> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ItemStock_.id));
            }
            if (criteria.getTotalQuantity() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTotalQuantity(), ItemStock_.totalQuantity));
            }
            if (criteria.getMinimumQuantity() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMinimumQuantity(), ItemStock_.minimumQuantity));
            }
            if (criteria.getItemStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getItemStatus(), ItemStock_.itemStatus));
            }
            if (criteria.getStockStore() != null) {
                specification = specification.and(buildSpecification(criteria.getStockStore(), ItemStock_.stockStore));
            }
            if (criteria.getEntryDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEntryDate(), ItemStock_.entryDate));
            }
            if (criteria.getExpiryDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getExpiryDate(), ItemStock_.expiryDate));
            }
            if (criteria.getCreatorId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatorId(), ItemStock_.creatorId));
            }
            if (criteria.getCreatedOn() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedOn(), ItemStock_.createdOn));
            }
            if (criteria.getSdsfile() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSdsfile(), ItemStock_.sdsfile));
            }
            if (criteria.getItemTransactionId() != null) {
                specification = specification.and(buildSpecification(criteria.getItemTransactionId(),
                    root -> root.join(ItemStock_.itemTransactions, JoinType.LEFT).get(ItemTransaction_.id)));
            }
            if (criteria.getInvStorageId() != null) {
                specification = specification.and(buildSpecification(criteria.getInvStorageId(),
                    root -> root.join(ItemStock_.invStorage, JoinType.LEFT).get(InvStorage_.id)));
            }
            if (criteria.getStorageUnitId() != null) {
                specification = specification.and(buildSpecification(criteria.getStorageUnitId(),
                    root -> root.join(ItemStock_.storageUnit, JoinType.LEFT).get(MeasUnit_.id)));
            }
            if (criteria.getItemId() != null) {
                specification = specification.and(buildSpecification(criteria.getItemId(),
                    root -> root.join(ItemStock_.item, JoinType.LEFT).get(Item_.id)));
            }
        }
        return specification;
    }
}
