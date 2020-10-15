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

import com.cheminv.app.domain.ItemTransaction;
import com.cheminv.app.domain.*; // for static metamodels
import com.cheminv.app.repository.ItemTransactionRepository;
import com.cheminv.app.service.dto.ItemTransactionCriteria;
import com.cheminv.app.service.dto.ItemTransactionDTO;
import com.cheminv.app.service.mapper.ItemTransactionMapper;

/**
 * Service for executing complex queries for {@link ItemTransaction} entities in the database.
 * The main input is a {@link ItemTransactionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ItemTransactionDTO} or a {@link Page} of {@link ItemTransactionDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ItemTransactionQueryService extends QueryService<ItemTransaction> {

    private final Logger log = LoggerFactory.getLogger(ItemTransactionQueryService.class);

    private final ItemTransactionRepository itemTransactionRepository;

    private final ItemTransactionMapper itemTransactionMapper;

    public ItemTransactionQueryService(ItemTransactionRepository itemTransactionRepository, ItemTransactionMapper itemTransactionMapper) {
        this.itemTransactionRepository = itemTransactionRepository;
        this.itemTransactionMapper = itemTransactionMapper;
    }

    /**
     * Return a {@link List} of {@link ItemTransactionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ItemTransactionDTO> findByCriteria(ItemTransactionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ItemTransaction> specification = createSpecification(criteria);
        return itemTransactionMapper.toDto(itemTransactionRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ItemTransactionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ItemTransactionDTO> findByCriteria(ItemTransactionCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ItemTransaction> specification = createSpecification(criteria);
        return itemTransactionRepository.findAll(specification, page)
            .map(itemTransactionMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ItemTransactionCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ItemTransaction> specification = createSpecification(criteria);
        return itemTransactionRepository.count(specification);
    }

    /**
     * Function to convert {@link ItemTransactionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ItemTransaction> createSpecification(ItemTransactionCriteria criteria) {
        Specification<ItemTransaction> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ItemTransaction_.id));
            }
            if (criteria.getQuantity() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getQuantity(), ItemTransaction_.quantity));
            }
            if (criteria.getRemarks() != null) {
                specification = specification.and(buildStringSpecification(criteria.getRemarks(), ItemTransaction_.remarks));
            }
            if (criteria.getTransactionType() != null) {
                specification = specification.and(buildSpecification(criteria.getTransactionType(), ItemTransaction_.transactionType));
            }
            if (criteria.getTransactionDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTransactionDate(), ItemTransaction_.transactionDate));
            }
            if (criteria.getItemStockId() != null) {
                specification = specification.and(buildSpecification(criteria.getItemStockId(),
                    root -> root.join(ItemTransaction_.itemStock, JoinType.LEFT).get(ItemStock_.id)));
            }
            if (criteria.getCreatedById() != null) {
                specification = specification.and(buildSpecification(criteria.getCreatedById(),
                    root -> root.join(ItemTransaction_.createdBy, JoinType.LEFT).get(InvUser_.id)));
            }
        }
        return specification;
    }
}
