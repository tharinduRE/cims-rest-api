package com.cheminv.app.service;

import com.cheminv.app.domain.ItemStock;
import com.cheminv.app.repository.ItemStockRepository;
import com.cheminv.app.repository.search.ItemStockSearchRepository;
import com.cheminv.app.service.dto.ItemStockDTO;
import com.cheminv.app.service.mapper.ItemStockMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link ItemStock}.
 */
@Service
@Transactional
public class ItemStockService {

    private final Logger log = LoggerFactory.getLogger(ItemStockService.class);

    private final ItemStockRepository itemStockRepository;

    private final ItemStockMapper itemStockMapper;

    private final ItemStockSearchRepository itemStockSearchRepository;

    public ItemStockService(ItemStockRepository itemStockRepository, ItemStockMapper itemStockMapper, ItemStockSearchRepository itemStockSearchRepository) {
        this.itemStockRepository = itemStockRepository;
        this.itemStockMapper = itemStockMapper;
        this.itemStockSearchRepository = itemStockSearchRepository;
    }

    /**
     * Save a itemStock.
     *
     * @param itemStockDTO the entity to save.
     * @return the persisted entity.
     */
    public ItemStockDTO save(ItemStockDTO itemStockDTO) {
        log.debug("Request to save ItemStock : {}", itemStockDTO);
        ItemStock itemStock = itemStockMapper.toEntity(itemStockDTO);
        itemStock = itemStockRepository.save(itemStock);
        ItemStockDTO result = itemStockMapper.toDto(itemStock);
        itemStockSearchRepository.save(itemStock);
        return result;
    }

    /**
     * Get all the itemStocks.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ItemStockDTO> findAll() {
        log.debug("Request to get all ItemStocks");
        return itemStockRepository.findAllWithEagerRelationships().stream()
            .map(itemStockMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get all the itemStocks with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ItemStockDTO> findAllWithEagerRelationships(Pageable pageable) {
        return itemStockRepository.findAllWithEagerRelationships(pageable).map(itemStockMapper::toDto);
    }

    /**
     * Get one itemStock by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ItemStockDTO> findOne(Long id) {
        log.debug("Request to get ItemStock : {}", id);
        return itemStockRepository.findOneWithEagerRelationships(id)
            .map(itemStockMapper::toDto);
    }

    /**
     * Delete the itemStock by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ItemStock : {}", id);
        itemStockRepository.deleteById(id);
        itemStockSearchRepository.deleteById(id);
    }

    /**
     * Search for the itemStock corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ItemStockDTO> search(String query) {
        log.debug("Request to search ItemStocks for query {}", query);
        return StreamSupport
            .stream(itemStockSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(itemStockMapper::toDto)
        .collect(Collectors.toList());
    }
}
