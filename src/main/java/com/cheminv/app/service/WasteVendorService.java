package com.cheminv.app.service;

import com.cheminv.app.domain.WasteVendor;
import com.cheminv.app.repository.WasteVendorRepository;
import com.cheminv.app.service.dto.WasteVendorDTO;
import com.cheminv.app.service.mapper.WasteVendorMapper;
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

/**
 * Service Implementation for managing {@link WasteVendor}.
 */
@Service
@Transactional
public class WasteVendorService {

    private final Logger log = LoggerFactory.getLogger(WasteVendorService.class);

    private final WasteVendorRepository wasteVendorRepository;

    private final WasteVendorMapper wasteVendorMapper;

    public WasteVendorService(WasteVendorRepository wasteVendorRepository, WasteVendorMapper wasteVendorMapper) {
        this.wasteVendorRepository = wasteVendorRepository;
        this.wasteVendorMapper = wasteVendorMapper;
    }

    /**
     * Save a wasteVendor.
     *
     * @param wasteVendorDTO the entity to save.
     * @return the persisted entity.
     */
    public WasteVendorDTO save(WasteVendorDTO wasteVendorDTO) {
        log.debug("Request to save WasteVendor : {}", wasteVendorDTO);
        WasteVendor wasteVendor = wasteVendorMapper.toEntity(wasteVendorDTO);
        wasteVendor = wasteVendorRepository.save(wasteVendor);
        return wasteVendorMapper.toDto(wasteVendor);
    }

    /**
     * Get all the wasteVendors.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<WasteVendorDTO> findAll() {
        log.debug("Request to get all WasteVendors");
        return wasteVendorRepository.findAllWithEagerRelationships().stream()
            .map(wasteVendorMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get all the wasteVendors with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<WasteVendorDTO> findAllWithEagerRelationships(Pageable pageable) {
        return wasteVendorRepository.findAllWithEagerRelationships(pageable).map(wasteVendorMapper::toDto);
    }

    /**
     * Get one wasteVendor by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<WasteVendorDTO> findOne(Long id) {
        log.debug("Request to get WasteVendor : {}", id);
        return wasteVendorRepository.findOneWithEagerRelationships(id)
            .map(wasteVendorMapper::toDto);
    }

    /**
     * Delete the wasteVendor by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete WasteVendor : {}", id);
        wasteVendorRepository.deleteById(id);
    }
}
