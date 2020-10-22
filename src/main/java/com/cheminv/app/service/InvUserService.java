package com.cheminv.app.service;

import com.cheminv.app.domain.InvUser;
import com.cheminv.app.repository.InvUserRepository;
import com.cheminv.app.service.dto.InvUserDTO;
import com.cheminv.app.service.mapper.InvUserMapper;
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
 * Service Implementation for managing {@link InvUser}.
 */
@Service
@Transactional
public class InvUserService {

    private final Logger log = LoggerFactory.getLogger(InvUserService.class);

    private final InvUserRepository invUserRepository;

    private final InvUserMapper invUserMapper;

    public InvUserService(InvUserRepository invUserRepository, InvUserMapper invUserMapper) {
        this.invUserRepository = invUserRepository;
        this.invUserMapper = invUserMapper;
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
        return invUserMapper.toDto(invUser);
    }

    /**
     * Get all the invUsers.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<InvUserDTO> findAll() {
        log.debug("Request to get all InvUsers");
        return invUserRepository.findAllWithEagerRelationships().stream()
            .map(invUserMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get all the invUsers with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<InvUserDTO> findAllWithEagerRelationships(Pageable pageable) {
        return invUserRepository.findAllWithEagerRelationships(pageable).map(invUserMapper::toDto);
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
        return invUserRepository.findOneWithEagerRelationships(id)
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
    }
}
