package com.cheminv.app.service;

import com.cheminv.app.domain.HazardCode;
import com.cheminv.app.repository.HazardCodeRepository;
import com.cheminv.app.repository.search.HazardCodeSearchRepository;
import com.cheminv.app.service.dto.HazardCodeDTO;
import com.cheminv.app.service.mapper.HazardCodeMapper;
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
 * Service Implementation for managing {@link HazardCode}.
 */
@Service
@Transactional
public class HazardCodeService {

    private final Logger log = LoggerFactory.getLogger(HazardCodeService.class);

    private final HazardCodeRepository hazardCodeRepository;

    private final HazardCodeMapper hazardCodeMapper;

    private final HazardCodeSearchRepository hazardCodeSearchRepository;

    public HazardCodeService(HazardCodeRepository hazardCodeRepository, HazardCodeMapper hazardCodeMapper, HazardCodeSearchRepository hazardCodeSearchRepository) {
        this.hazardCodeRepository = hazardCodeRepository;
        this.hazardCodeMapper = hazardCodeMapper;
        this.hazardCodeSearchRepository = hazardCodeSearchRepository;
    }

    /**
     * Save a hazardCode.
     *
     * @param hazardCodeDTO the entity to save.
     * @return the persisted entity.
     */
    public HazardCodeDTO save(HazardCodeDTO hazardCodeDTO) {
        log.debug("Request to save HazardCode : {}", hazardCodeDTO);
        HazardCode hazardCode = hazardCodeMapper.toEntity(hazardCodeDTO);
        hazardCode = hazardCodeRepository.save(hazardCode);
        HazardCodeDTO result = hazardCodeMapper.toDto(hazardCode);
        hazardCodeSearchRepository.save(hazardCode);
        return result;
    }

    /**
     * Get all the hazardCodes.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<HazardCodeDTO> findAll() {
        log.debug("Request to get all HazardCodes");
        return hazardCodeRepository.findAll().stream()
            .map(hazardCodeMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one hazardCode by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<HazardCodeDTO> findOne(Long id) {
        log.debug("Request to get HazardCode : {}", id);
        return hazardCodeRepository.findById(id)
            .map(hazardCodeMapper::toDto);
    }

    /**
     * Delete the hazardCode by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete HazardCode : {}", id);
        hazardCodeRepository.deleteById(id);
        hazardCodeSearchRepository.deleteById(id);
    }

    /**
     * Search for the hazardCode corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<HazardCodeDTO> search(String query) {
        log.debug("Request to search HazardCodes for query {}", query);
        return StreamSupport
            .stream(hazardCodeSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(hazardCodeMapper::toDto)
        .collect(Collectors.toList());
    }
}
