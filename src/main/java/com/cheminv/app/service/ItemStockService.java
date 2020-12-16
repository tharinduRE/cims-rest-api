package com.cheminv.app.service;

import com.cheminv.app.domain.ItemStock;
import com.cheminv.app.repository.ItemStockRepository;
import com.cheminv.app.service.dto.ItemStockDTO;
import com.cheminv.app.service.mapper.ItemStockMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link ItemStock}.
 */
@Service
@Transactional
public class ItemStockService {

    private final Logger log = LoggerFactory.getLogger(ItemStockService.class);

    private final ItemStockRepository itemStockRepository;

    private final ItemStockMapper itemStockMapper;

    public ItemStockService(ItemStockRepository itemStockRepository, ItemStockMapper itemStockMapper) {
        this.itemStockRepository = itemStockRepository;
        this.itemStockMapper = itemStockMapper;
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
        return itemStockMapper.toDto(itemStock);
    }

    /**
     * Get all the itemStocks.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ItemStockDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ItemStocks");
        return itemStockRepository.findAll(pageable)
            .map(itemStockMapper::toDto);
    }

    /*@Transactional(readOnly = true)
    public Page<ItemStockDTO> findAllLow(Pageable pageable) {
        log.debug("Request to get all ItemStocks");
        return itemStockRepository.findAllByLessThanOrEqualToMinimum(stores,pageable)
            .map(itemStockMapper::toDto);
    }*/


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
    }
}
