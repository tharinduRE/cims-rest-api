package com.cheminv.app.service;

import com.cheminv.app.domain.MeasUnit;
import com.cheminv.app.repository.MeasUnitRepository;
import com.cheminv.app.service.dto.MeasUnitDTO;
import com.cheminv.app.service.mapper.MeasUnitMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link MeasUnit}.
 */
@Service
@Transactional
public class MeasUnitService {

    private final Logger log = LoggerFactory.getLogger(MeasUnitService.class);

    private final MeasUnitRepository measUnitRepository;

    private final MeasUnitMapper measUnitMapper;

    public MeasUnitService(MeasUnitRepository measUnitRepository, MeasUnitMapper measUnitMapper) {
        this.measUnitRepository = measUnitRepository;
        this.measUnitMapper = measUnitMapper;
    }

    /**
     * Save a measUnit.
     *
     * @param measUnitDTO the entity to save.
     * @return the persisted entity.
     */
    public MeasUnitDTO save(MeasUnitDTO measUnitDTO) {
        log.debug("Request to save MeasUnit : {}", measUnitDTO);
        MeasUnit measUnit = measUnitMapper.toEntity(measUnitDTO);
        measUnit = measUnitRepository.save(measUnit);
        return measUnitMapper.toDto(measUnit);
    }

    /**
     * Get all the measUnits.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<MeasUnitDTO> findAll() {
        log.debug("Request to get all MeasUnits");
        return measUnitRepository.findAll().stream()
            .map(measUnitMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one measUnit by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<MeasUnitDTO> findOne(Long id) {
        log.debug("Request to get MeasUnit : {}", id);
        return measUnitRepository.findById(id)
            .map(measUnitMapper::toDto);
    }

    /**
     * Delete the measUnit by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete MeasUnit : {}", id);
        measUnitRepository.deleteById(id);
    }
}
