package com.cheminv.app.web.rest;

import com.cheminv.app.service.InvDepartmentService;
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
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link com.cheminv.app.domain.InvDepartment}.
 */
@RestController
@RequestMapping("/api")
public class InvDepartmentResource {

    private final Logger log = LoggerFactory.getLogger(InvDepartmentResource.class);

    private static final String ENTITY_NAME = "invDepartment";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InvDepartmentService invDepartmentService;

    public InvDepartmentResource(InvDepartmentService invDepartmentService) {
        this.invDepartmentService = invDepartmentService;
    }

    /**
     * {@code POST  /inv-departments} : Create a new invDepartment.
     *
     * @param invDepartmentDTO the invDepartmentDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new invDepartmentDTO, or with status {@code 400 (Bad Request)} if the invDepartment has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/inv-departments")
    public ResponseEntity<InvDepartmentDTO> createInvDepartment(@RequestBody InvDepartmentDTO invDepartmentDTO) throws URISyntaxException {
        log.debug("REST request to save InvDepartment : {}", invDepartmentDTO);
        if (invDepartmentDTO.getId() != null) {
            throw new BadRequestAlertException("A new invDepartment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        InvDepartmentDTO result = invDepartmentService.save(invDepartmentDTO);
        return ResponseEntity.created(new URI("/api/inv-departments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /inv-departments} : Updates an existing invDepartment.
     *
     * @param invDepartmentDTO the invDepartmentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated invDepartmentDTO,
     * or with status {@code 400 (Bad Request)} if the invDepartmentDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the invDepartmentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/inv-departments")
    public ResponseEntity<InvDepartmentDTO> updateInvDepartment(@RequestBody InvDepartmentDTO invDepartmentDTO) throws URISyntaxException {
        log.debug("REST request to update InvDepartment : {}", invDepartmentDTO);
        if (invDepartmentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        InvDepartmentDTO result = invDepartmentService.save(invDepartmentDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, invDepartmentDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /inv-departments} : get all the invDepartments.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of invDepartments in body.
     */
    @GetMapping("/inv-departments")
    public List<InvDepartmentDTO> getAllInvDepartments(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all InvDepartments");
        return invDepartmentService.findAll();
    }

    /**
     * {@code GET  /inv-departments/:id} : get the "id" invDepartment.
     *
     * @param id the id of the invDepartmentDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the invDepartmentDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/inv-departments/{id}")
    public ResponseEntity<InvDepartmentDTO> getInvDepartment(@PathVariable Long id) {
        log.debug("REST request to get InvDepartment : {}", id);
        Optional<InvDepartmentDTO> invDepartmentDTO = invDepartmentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(invDepartmentDTO);
    }

    /**
     * {@code DELETE  /inv-departments/:id} : delete the "id" invDepartment.
     *
     * @param id the id of the invDepartmentDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/inv-departments/{id}")
    public ResponseEntity<Void> deleteInvDepartment(@PathVariable Long id) {
        log.debug("REST request to delete InvDepartment : {}", id);
        invDepartmentService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/inv-departments?query=:query} : search for the invDepartment corresponding
     * to the query.
     *
     * @param query the query of the invDepartment search.
     * @return the result of the search.
     */
    @GetMapping("/_search/inv-departments")
    public List<InvDepartmentDTO> searchInvDepartments(@RequestParam String query) {
        log.debug("REST request to search InvDepartments for query {}", query);
        return invDepartmentService.search(query);
    }
}
