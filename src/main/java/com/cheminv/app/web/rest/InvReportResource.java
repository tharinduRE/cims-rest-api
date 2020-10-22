package com.cheminv.app.web.rest;

import com.cheminv.app.service.InvReportService;
import com.cheminv.app.web.rest.errors.BadRequestAlertException;
import com.cheminv.app.service.dto.InvReportDTO;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.cheminv.app.domain.InvReport}.
 */
@RestController
@RequestMapping("/api")
public class InvReportResource {

    private final Logger log = LoggerFactory.getLogger(InvReportResource.class);

    private final InvReportService invReportService;

    public InvReportResource(InvReportService invReportService) {
        this.invReportService = invReportService;
    }

    /**
     * {@code GET  /inv-reports} : get all the invReports.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of invReports in body.
     */
    @GetMapping("/inv-reports")
    public ResponseEntity<List<InvReportDTO>> getAllInvReports(Pageable pageable) {
        log.debug("REST request to get a page of InvReports");
        Page<InvReportDTO> page = invReportService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /inv-reports/:id} : get the "id" invReport.
     *
     * @param id the id of the invReportDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the invReportDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/inv-reports/{id}")
    public ResponseEntity<InvReportDTO> getInvReport(@PathVariable Long id) {
        log.debug("REST request to get InvReport : {}", id);
        Optional<InvReportDTO> invReportDTO = invReportService.findOne(id);
        return ResponseUtil.wrapOrNotFound(invReportDTO);
    }
}
