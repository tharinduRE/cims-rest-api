package com.cheminv.app.web.rest;

import com.cheminv.app.service.MeasUnitService;
import com.cheminv.app.web.rest.errors.BadRequestAlertException;
import com.cheminv.app.service.dto.MeasUnitDTO;

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
 * REST controller for managing {@link com.cheminv.app.domain.MeasUnit}.
 */
@RestController
@RequestMapping("/api")
public class MeasUnitResource {

    private final Logger log = LoggerFactory.getLogger(MeasUnitResource.class);

    private static final String ENTITY_NAME = "measUnit";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MeasUnitService measUnitService;

    public MeasUnitResource(MeasUnitService measUnitService) {
        this.measUnitService = measUnitService;
    }

    /**
     * {@code POST  /meas-units} : Create a new measUnit.
     *
     * @param measUnitDTO the measUnitDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new measUnitDTO, or with status {@code 400 (Bad Request)} if the measUnit has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/meas-units")
    public ResponseEntity<MeasUnitDTO> createMeasUnit(@RequestBody MeasUnitDTO measUnitDTO) throws URISyntaxException {
        log.debug("REST request to save MeasUnit : {}", measUnitDTO);
        if (measUnitDTO.getId() != null) {
            throw new BadRequestAlertException("A new measUnit cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MeasUnitDTO result = measUnitService.save(measUnitDTO);
        return ResponseEntity.created(new URI("/api/meas-units/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /meas-units} : Updates an existing measUnit.
     *
     * @param measUnitDTO the measUnitDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated measUnitDTO,
     * or with status {@code 400 (Bad Request)} if the measUnitDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the measUnitDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/meas-units")
    public ResponseEntity<MeasUnitDTO> updateMeasUnit(@RequestBody MeasUnitDTO measUnitDTO) throws URISyntaxException {
        log.debug("REST request to update MeasUnit : {}", measUnitDTO);
        if (measUnitDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        MeasUnitDTO result = measUnitService.save(measUnitDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, measUnitDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /meas-units} : get all the measUnits.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of measUnits in body.
     */
    @GetMapping("/meas-units")
    public List<MeasUnitDTO> getAllMeasUnits() {
        log.debug("REST request to get all MeasUnits");
        return measUnitService.findAll();
    }

    /**
     * {@code GET  /meas-units/:id} : get the "id" measUnit.
     *
     * @param id the id of the measUnitDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the measUnitDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/meas-units/{id}")
    public ResponseEntity<MeasUnitDTO> getMeasUnit(@PathVariable Long id) {
        log.debug("REST request to get MeasUnit : {}", id);
        Optional<MeasUnitDTO> measUnitDTO = measUnitService.findOne(id);
        return ResponseUtil.wrapOrNotFound(measUnitDTO);
    }

    /**
     * {@code DELETE  /meas-units/:id} : delete the "id" measUnit.
     *
     * @param id the id of the measUnitDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/meas-units/{id}")
    public ResponseEntity<Void> deleteMeasUnit(@PathVariable Long id) {
        log.debug("REST request to delete MeasUnit : {}", id);
        measUnitService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
