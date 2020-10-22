package com.cheminv.app.service;

import com.cheminv.app.domain.WasteItem;
import com.cheminv.app.repository.WasteItemRepository;
import com.cheminv.app.service.dto.WasteItemDTO;
import com.cheminv.app.service.mapper.WasteItemMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link WasteItem}.
 */
@Service
@Transactional
public class WasteItemService {

    private final Logger log = LoggerFactory.getLogger(WasteItemService.class);

    private final WasteItemRepository wasteItemRepository;

    private final WasteItemMapper wasteItemMapper;

    public WasteItemService(WasteItemRepository wasteItemRepository, WasteItemMapper wasteItemMapper) {
        this.wasteItemRepository = wasteItemRepository;
        this.wasteItemMapper = wasteItemMapper;
    }

    /**
     * Save a wasteItem.
     *
     * @param wasteItemDTO the entity to save.
     * @return the persisted entity.
     */
    public WasteItemDTO save(WasteItemDTO wasteItemDTO) {
        log.debug("Request to save WasteItem : {}", wasteItemDTO);
        WasteItem wasteItem = wasteItemMapper.toEntity(wasteItemDTO);
        wasteItem = wasteItemRepository.save(wasteItem);
        return wasteItemMapper.toDto(wasteItem);
    }

    /**
     * Get all the wasteItems.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<WasteItemDTO> findAll() {
        log.debug("Request to get all WasteItems");
        return wasteItemRepository.findAll().stream()
            .map(wasteItemMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one wasteItem by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<WasteItemDTO> findOne(Long id) {
        log.debug("Request to get WasteItem : {}", id);
        return wasteItemRepository.findById(id)
            .map(wasteItemMapper::toDto);
    }

    /**
     * Delete the wasteItem by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete WasteItem : {}", id);
        wasteItemRepository.deleteById(id);
    }
}
