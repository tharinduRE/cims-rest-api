package com.cheminv.app.web.rest;

import com.cheminv.app.domain.Department;
import com.cheminv.app.service.DepartmentService;
import com.cheminv.app.web.rest.errors.BadRequestAlertException;
import com.cheminv.app.service.dto.InvDepartmentDTO;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link Department}.
 */
@RestController
@RequestMapping("/api")
public class DepartmentResource {

    private final Logger log = LoggerFactory.getLogger(DepartmentResource.class);

    private static final String ENTITY_NAME = "invDepartment";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DepartmentService departmentService;

    public DepartmentResource(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    /**
     * {@code POST  /departments} : Create a new invDepartment.
     *
     * @param invDepartmentDTO the invDepartmentDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new invDepartmentDTO, or with status {@code 400 (Bad Request)} if the invDepartment has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/departments")
    public ResponseEntity<InvDepartmentDTO> createInvDepartment(@RequestBody InvDepartmentDTO invDepartmentDTO) throws URISyntaxException {
        log.debug("REST request to save Department : {}", invDepartmentDTO);
        if (invDepartmentDTO.getId() != null) {
            throw new BadRequestAlertException("A new invDepartment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        InvDepartmentDTO result = departmentService.save(invDepartmentDTO);
        return ResponseEntity.created(new URI("/api/departments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /departments} : Updates an existing invDepartment.
     *
     * @param invDepartmentDTO the invDepartmentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated invDepartmentDTO,
     * or with status {@code 400 (Bad Request)} if the invDepartmentDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the invDepartmentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/departments")
    public ResponseEntity<InvDepartmentDTO> updateInvDepartment(@RequestBody InvDepartmentDTO invDepartmentDTO) throws URISyntaxException {
        log.debug("REST request to update Department : {}", invDepartmentDTO);
        if (invDepartmentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        InvDepartmentDTO result = departmentService.save(invDepartmentDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, invDepartmentDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /departments} : get all the invDepartments.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of invDepartments in body.
     */
    @GetMapping("/departments")
    public List<InvDepartmentDTO> getAllInvDepartments(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all InvDepartments");
        return departmentService.findAll();
    }

    /**
     * {@code GET  /departments/:id} : get the "id" invDepartment.
     *
     * @param id the id of the invDepartmentDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the invDepartmentDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/departments/{id}")
    public ResponseEntity<InvDepartmentDTO> getInvDepartment(@PathVariable Long id) {
        log.debug("REST request to get Department : {}", id);
        Optional<InvDepartmentDTO> invDepartmentDTO = departmentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(invDepartmentDTO);
    }

    /**
     * {@code DELETE  /departments/:id} : delete the "id" invDepartment.
     *
     * @param id the id of the invDepartmentDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/departments/{id}")
    public ResponseEntity<Void> deleteInvDepartment(@PathVariable Long id) {
        log.debug("REST request to delete Department : {}", id);
        departmentService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
