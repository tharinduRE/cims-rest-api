package com.cheminv.app.service;

import com.cheminv.app.domain.InvStorage;
import com.cheminv.app.repository.InvStorageRepository;
import com.cheminv.app.service.dto.InvStorageDTO;
import com.cheminv.app.service.mapper.InvStorageMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link InvStorage}.
 */
@Service
@Transactional
public class InvStorageService {

    private final Logger log = LoggerFactory.getLogger(InvStorageService.class);

    private final InvStorageRepository invStorageRepository;

    private final InvStorageMapper invStorageMapper;

    public InvStorageService(InvStorageRepository invStorageRepository, InvStorageMapper invStorageMapper) {
        this.invStorageRepository = invStorageRepository;
        this.invStorageMapper = invStorageMapper;
    }

    /**
     * Save a invStorage.
     *
     * @param invStorageDTO the entity to save.
     * @return the persisted entity.
     */
    public InvStorageDTO save(InvStorageDTO invStorageDTO) {
        log.debug("Request to save InvStorage : {}", invStorageDTO);
        InvStorage invStorage = invStorageMapper.toEntity(invStorageDTO);
        invStorage = invStorageRepository.save(invStorage);
        return invStorageMapper.toDto(invStorage);
    }

    /**
     * Get all the invStorages.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<InvStorageDTO> findAll() {
        log.debug("Request to get all InvStorages");
        return invStorageRepository.findAll().stream()
            .map(invStorageMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one invStorage by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<InvStorageDTO> findOne(Long id) {
        log.debug("Request to get InvStorage : {}", id);
        return invStorageRepository.findById(id)
            .map(invStorageMapper::toDto);
    }

    /**
     * Delete the invStorage by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete InvStorage : {}", id);
        invStorageRepository.deleteById(id);
    }
}
