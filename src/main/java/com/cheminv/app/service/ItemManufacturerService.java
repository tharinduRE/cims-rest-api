package com.cheminv.app.service;

import com.cheminv.app.domain.ItemManufacturer;
import com.cheminv.app.repository.ItemManufacturerRepository;
import com.cheminv.app.repository.search.ItemManufacturerSearchRepository;
import com.cheminv.app.service.dto.ItemManufacturerDTO;
import com.cheminv.app.service.mapper.ItemManufacturerMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link ItemManufacturer}.
 */
@Service
@Transactional
public class ItemManufacturerService {

    private final Logger log = LoggerFactory.getLogger(ItemManufacturerService.class);

    private final ItemManufacturerRepository itemManufacturerRepository;

    private final ItemManufacturerMapper itemManufacturerMapper;

    private final ItemManufacturerSearchRepository itemManufacturerSearchRepository;

    public ItemManufacturerService(ItemManufacturerRepository itemManufacturerRepository, ItemManufacturerMapper itemManufacturerMapper, ItemManufacturerSearchRepository itemManufacturerSearchRepository) {
        this.itemManufacturerRepository = itemManufacturerRepository;
        this.itemManufacturerMapper = itemManufacturerMapper;
        this.itemManufacturerSearchRepository = itemManufacturerSearchRepository;
    }

    /**
     * Save a itemManufacturer.
     *
     * @param itemManufacturerDTO the entity to save.
     * @return the persisted entity.
     */
    public ItemManufacturerDTO save(ItemManufacturerDTO itemManufacturerDTO) {
        log.debug("Request to save ItemManufacturer : {}", itemManufacturerDTO);
        ItemManufacturer itemManufacturer = itemManufacturerMapper.toEntity(itemManufacturerDTO);
        itemManufacturer = itemManufacturerRepository.save(itemManufacturer);
        ItemManufacturerDTO result = itemManufacturerMapper.toDto(itemManufacturer);
        itemManufacturerSearchRepository.save(itemManufacturer);
        return result;
    }

    /**
     * Get all the itemManufacturers.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ItemManufacturerDTO> findAll() {
        log.debug("Request to get all ItemManufacturers");
        return itemManufacturerRepository.findAll().stream()
            .map(itemManufacturerMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one itemManufacturer by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ItemManufacturerDTO> findOne(Long id) {
        log.debug("Request to get ItemManufacturer : {}", id);
        return itemManufacturerRepository.findById(id)
            .map(itemManufacturerMapper::toDto);
    }

    /**
     * Delete the itemManufacturer by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ItemManufacturer : {}", id);
        itemManufacturerRepository.deleteById(id);
        itemManufacturerSearchRepository.deleteById(id);
    }

    /**
     * Search for the itemManufacturer corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ItemManufacturerDTO> search(String query) {
        log.debug("Request to search ItemManufacturers for query {}", query);
        return StreamSupport
            .stream(itemManufacturerSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(itemManufacturerMapper::toDto)
        .collect(Collectors.toList());
    }
}
