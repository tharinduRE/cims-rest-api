package com.cheminv.app.service;

import com.cheminv.app.domain.InvDepartment;
import com.cheminv.app.repository.InvDepartmentRepository;
import com.cheminv.app.repository.search.InvDepartmentSearchRepository;
import com.cheminv.app.service.dto.InvDepartmentDTO;
import com.cheminv.app.service.mapper.InvDepartmentMapper;
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
 * Service Implementation for managing {@link InvDepartment}.
 */
@Service
@Transactional
public class InvDepartmentService {

    private final Logger log = LoggerFactory.getLogger(InvDepartmentService.class);

    private final InvDepartmentRepository invDepartmentRepository;

    private final InvDepartmentMapper invDepartmentMapper;

    private final InvDepartmentSearchRepository invDepartmentSearchRepository;

    public InvDepartmentService(InvDepartmentRepository invDepartmentRepository, InvDepartmentMapper invDepartmentMapper, InvDepartmentSearchRepository invDepartmentSearchRepository) {
        this.invDepartmentRepository = invDepartmentRepository;
        this.invDepartmentMapper = invDepartmentMapper;
        this.invDepartmentSearchRepository = invDepartmentSearchRepository;
    }

    /**
     * Save a invDepartment.
     *
     * @param invDepartmentDTO the entity to save.
     * @return the persisted entity.
     */
    public InvDepartmentDTO save(InvDepartmentDTO invDepartmentDTO) {
        log.debug("Request to save InvDepartment : {}", invDepartmentDTO);
        InvDepartment invDepartment = invDepartmentMapper.toEntity(invDepartmentDTO);
        invDepartment = invDepartmentRepository.save(invDepartment);
        InvDepartmentDTO result = invDepartmentMapper.toDto(invDepartment);
        invDepartmentSearchRepository.save(invDepartment);
        return result;
    }

    /**
     * Get all the invDepartments.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<InvDepartmentDTO> findAll() {
        log.debug("Request to get all InvDepartments");
        return invDepartmentRepository.findAllWithEagerRelationships().stream()
            .map(invDepartmentMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get all the invDepartments with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<InvDepartmentDTO> findAllWithEagerRelationships(Pageable pageable) {
        return invDepartmentRepository.findAllWithEagerRelationships(pageable).map(invDepartmentMapper::toDto);
    }

    /**
     * Get one invDepartment by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<InvDepartmentDTO> findOne(Long id) {
        log.debug("Request to get InvDepartment : {}", id);
        return invDepartmentRepository.findOneWithEagerRelationships(id)
            .map(invDepartmentMapper::toDto);
    }

    /**
     * Delete the invDepartment by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete InvDepartment : {}", id);
        invDepartmentRepository.deleteById(id);
        invDepartmentSearchRepository.deleteById(id);
    }

    /**
     * Search for the invDepartment corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<InvDepartmentDTO> search(String query) {
        log.debug("Request to search InvDepartments for query {}", query);
        return StreamSupport
            .stream(invDepartmentSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(invDepartmentMapper::toDto)
        .collect(Collectors.toList());
    }
}
