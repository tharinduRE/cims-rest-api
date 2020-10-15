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

/**
 * REST controller for managing {@link com.cheminv.app.domain.HazardCode}.
 */
@RestController
@RequestMapping("/api")
public class HazardCodeResource {

    private final Logger log = LoggerFactory.getLogger(HazardCodeResource.class);

    private final HazardCodeService hazardCodeService;

    public HazardCodeResource(HazardCodeService hazardCodeService) {
        this.hazardCodeService = hazardCodeService;
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
}
