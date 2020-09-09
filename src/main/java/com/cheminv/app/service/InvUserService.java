package com.cheminv.app.service;

import com.cheminv.app.domain.InvUser;
import com.cheminv.app.repository.InvUserRepository;
import com.cheminv.app.repository.search.InvUserSearchRepository;
import com.cheminv.app.service.dto.InvUserDTO;
import com.cheminv.app.service.mapper.InvUserMapper;
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
 * Service Implementation for managing {@link InvUser}.
 */
@Service
@Transactional
public class InvUserService {

    private final Logger log = LoggerFactory.getLogger(InvUserService.class);

    private final InvUserRepository invUserRepository;

    private final InvUserMapper invUserMapper;

    private final InvUserSearchRepository invUserSearchRepository;

    public InvUserService(InvUserRepository invUserRepository, InvUserMapper invUserMapper, InvUserSearchRepository invUserSearchRepository) {
        this.invUserRepository = invUserRepository;
        this.invUserMapper = invUserMapper;
        this.invUserSearchRepository = invUserSearchRepository;
    }

    /**
     * Save a invUser.
     *
     * @param invUserDTO the entity to save.
     * @return the persisted entity.
     */
    public InvUserDTO save(InvUserDTO invUserDTO) {
        log.debug("Request to save InvUser : {}", invUserDTO);
        InvUser invUser = invUserMapper.toEntity(invUserDTO);
        invUser = invUserRepository.save(invUser);
        InvUserDTO result = invUserMapper.toDto(invUser);
        invUserSearchRepository.save(invUser);
        return result;
    }

    /**
     * Get all the invUsers.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<InvUserDTO> findAll() {
        log.debug("Request to get all InvUsers");
        return invUserRepository.findAll().stream()
            .map(invUserMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one invUser by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<InvUserDTO> findOne(Long id) {
        log.debug("Request to get InvUser : {}", id);
        return invUserRepository.findById(id)
            .map(invUserMapper::toDto);
    }

    /**
     * Delete the invUser by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete InvUser : {}", id);
        invUserRepository.deleteById(id);
        invUserSearchRepository.deleteById(id);
    }

    /**
     * Search for the invUser corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<InvUserDTO> search(String query) {
        log.debug("Request to search InvUsers for query {}", query);
        return StreamSupport
            .stream(invUserSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(invUserMapper::toDto)
        .collect(Collectors.toList());
    }
}
