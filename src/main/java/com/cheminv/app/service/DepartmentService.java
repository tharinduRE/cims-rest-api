package com.cheminv.app.service;

import com.cheminv.app.domain.Department;
import com.cheminv.app.repository.DepartmentRepository;
import com.cheminv.app.service.dto.InvDepartmentDTO;
import com.cheminv.app.service.mapper.DepartmentMapper;
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
 * Service Implementation for managing {@link Department}.
 */
@Service
@Transactional
public class DepartmentService {

    private final Logger log = LoggerFactory.getLogger(DepartmentService.class);

    private final DepartmentRepository departmentRepository;

    private final DepartmentMapper departmentMapper;

    public DepartmentService(DepartmentRepository departmentRepository, DepartmentMapper departmentMapper) {
        this.departmentRepository = departmentRepository;
        this.departmentMapper = departmentMapper;
    }

    /**
     * Save a invDepartment.
     *
     * @param invDepartmentDTO the entity to save.
     * @return the persisted entity.
     */
    public InvDepartmentDTO save(InvDepartmentDTO invDepartmentDTO) {
        log.debug("Request to save Department : {}", invDepartmentDTO);
        Department department = departmentMapper.toEntity(invDepartmentDTO);
        department = departmentRepository.save(department);
        return departmentMapper.toDto(department);
    }

    /**
     * Get all the invDepartments.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<InvDepartmentDTO> findAll() {
        log.debug("Request to get all InvDepartments");
        return departmentRepository.findAllWithEagerRelationships().stream()
            .map(departmentMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get all the invDepartments with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<InvDepartmentDTO> findAllWithEagerRelationships(Pageable pageable) {
        return departmentRepository.findAllWithEagerRelationships(pageable).map(departmentMapper::toDto);
    }

    /**
     * Get one invDepartment by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<InvDepartmentDTO> findOne(Long id) {
        log.debug("Request to get Department : {}", id);
        return departmentRepository.findOneWithEagerRelationships(id)
            .map(departmentMapper::toDto);
    }

    /**
     * Delete the invDepartment by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Department : {}", id);
        departmentRepository.deleteById(id);
    }
}
