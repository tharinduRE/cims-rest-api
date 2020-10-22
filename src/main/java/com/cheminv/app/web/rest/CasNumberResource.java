package com.cheminv.app.web.rest;

import com.cheminv.app.domain.CasNumber;
import com.cheminv.app.repository.CasNumberRepository;
import com.cheminv.app.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.cheminv.app.domain.CasNumber}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class CasNumberResource {

    private final Logger log = LoggerFactory.getLogger(CasNumberResource.class);

    private final CasNumberRepository casNumberRepository;

    public CasNumberResource(CasNumberRepository casNumberRepository) {
        this.casNumberRepository = casNumberRepository;
    }

    /**
     * {@code GET  /cas-numbers} : get all the casNumbers.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of casNumbers in body.
     */
    @GetMapping("/cas-numbers")
    public List<CasNumber> getAllCasNumbers() {
        log.debug("REST request to get all CasNumbers");
        return casNumberRepository.findAll();
    }

    /**
     * {@code GET  /cas-numbers/:id} : get the "id" casNumber.
     *
     * @param id the id of the casNumber to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the casNumber, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/cas-numbers/{id}")
    public ResponseEntity<CasNumber> getCasNumber(@PathVariable Long id) {
        log.debug("REST request to get CasNumber : {}", id);
        Optional<CasNumber> casNumber = casNumberRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(casNumber);
    }
}
