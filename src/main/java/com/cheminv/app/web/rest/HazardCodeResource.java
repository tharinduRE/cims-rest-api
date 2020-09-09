package com.cheminv.app.web.rest;

import com.cheminv.app.service.HazardCodeService;
import com.cheminv.app.web.rest.errors.BadRequestAlertException;
import com.cheminv.app.service.dto.HazardCodeDTO;

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
 * REST controller for managing {@link com.cheminv.app.domain.HazardCode}.
 */
@RestController
@RequestMapping("/api")
public class HazardCodeResource {

    private final Logger log = LoggerFactory.getLogger(HazardCodeResource.class);

    private static final String ENTITY_NAME = "hazardCode";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HazardCodeService hazardCodeService;

    public HazardCodeResource(HazardCodeService hazardCodeService) {
        this.hazardCodeService = hazardCodeService;
    }

    /**
     * {@code POST  /hazard-codes} : Create a new hazardCode.
     *
     * @param hazardCodeDTO the hazardCodeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new hazardCodeDTO, or with status {@code 400 (Bad Request)} if the hazardCode has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/hazard-codes")
    public ResponseEntity<HazardCodeDTO> createHazardCode(@RequestBody HazardCodeDTO hazardCodeDTO) throws URISyntaxException {
        log.debug("REST request to save HazardCode : {}", hazardCodeDTO);
        if (hazardCodeDTO.getId() != null) {
            throw new BadRequestAlertException("A new hazardCode cannot already have an ID", ENTITY_NAME, "idexists");
        }
        HazardCodeDTO result = hazardCodeService.save(hazardCodeDTO);
        return ResponseEntity.created(new URI("/api/hazard-codes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /hazard-codes} : Updates an existing hazardCode.
     *
     * @param hazardCodeDTO the hazardCodeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated hazardCodeDTO,
     * or with status {@code 400 (Bad Request)} if the hazardCodeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the hazardCodeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/hazard-codes")
    public ResponseEntity<HazardCodeDTO> updateHazardCode(@RequestBody HazardCodeDTO hazardCodeDTO) throws URISyntaxException {
        log.debug("REST request to update HazardCode : {}", hazardCodeDTO);
        if (hazardCodeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        HazardCodeDTO result = hazardCodeService.save(hazardCodeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, hazardCodeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /hazard-codes} : get all the hazardCodes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of hazardCodes in body.
     */
    @GetMapping("/hazard-codes")
    public List<HazardCodeDTO> getAllHazardCodes() {
        log.debug("REST request to get all HazardCodes");
        return hazardCodeService.findAll();
    }

    /**
     * {@code GET  /hazard-codes/:id} : get the "id" hazardCode.
     *
     * @param id the id of the hazardCodeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the hazardCodeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/hazard-codes/{id}")
    public ResponseEntity<HazardCodeDTO> getHazardCode(@PathVariable Long id) {
        log.debug("REST request to get HazardCode : {}", id);
        Optional<HazardCodeDTO> hazardCodeDTO = hazardCodeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(hazardCodeDTO);
    }

    /**
     * {@code DELETE  /hazard-codes/:id} : delete the "id" hazardCode.
     *
     * @param id the id of the hazardCodeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/hazard-codes/{id}")
    public ResponseEntity<Void> deleteHazardCode(@PathVariable Long id) {
        log.debug("REST request to delete HazardCode : {}", id);
        hazardCodeService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/hazard-codes?query=:query} : search for the hazardCode corresponding
     * to the query.
     *
     * @param query the query of the hazardCode search.
     * @return the result of the search.
     */
    @GetMapping("/_search/hazard-codes")
    public List<HazardCodeDTO> searchHazardCodes(@RequestParam String query) {
        log.debug("REST request to search HazardCodes for query {}", query);
        return hazardCodeService.search(query);
    }
}
