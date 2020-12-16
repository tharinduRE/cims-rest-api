package com.cheminv.app.web.rest;

import com.cheminv.app.domain.Report;
import com.cheminv.app.service.ReportService;
import com.cheminv.app.service.JasperReportService;
import com.cheminv.app.service.dto.InvReportDTO;

import com.cheminv.app.service.dto.ReportDTO;
import com.cheminv.app.web.rest.errors.BadRequestAlertException;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link Report}.
 */
@RestController
@RequestMapping("/api")
public class ReportResource {

    private final Logger log = LoggerFactory.getLogger(ReportResource.class);

    private final ReportService reportService;

    private final JasperReportService jasperReportService;

    public ReportResource(ReportService reportService, JasperReportService jasperReportService) {
        this.reportService = reportService;
        this.jasperReportService = jasperReportService;
    }

    /**
     * {@code GET  /reports} : get all the invReports.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of invReports in body.
     */
    @GetMapping("/reports")
    public ResponseEntity<List<InvReportDTO>> getAllInvReports(Pageable pageable) {
        log.debug("REST request to get a page of InvReports");
        Page<InvReportDTO> page = reportService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /reports/:id} : get the "id" invReport.
     *
     * @param id the id of the invReportDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the invReportDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/reports/{id}")
    public ResponseEntity<InvReportDTO> getInvReport(@PathVariable Long id) {
        log.debug("REST request to get Report : {}", id);
        Optional<InvReportDTO> invReportDTO = reportService.findOne(id);
        return ResponseUtil.wrapOrNotFound(invReportDTO);
    }

    /**
     * Generate and get reports
     */
    @PostMapping("/reports")
    public void reports(HttpServletResponse response, @RequestBody ReportDTO reportDTO) throws IOException, JRException {
        if(reportDTO.getUserId() == null ){
            throw new BadRequestAlertException("A user must initiate a report generation","Report","idMustHave");
        }
        String filename = "inventory-report-" + DateTimeFormatter.ofPattern("dd-MM-yyyy-hh-mma").format(LocalDateTime.now());
        response.setContentType("application/x-download");
        response.setHeader("Content-Disposition",String.format("attachment; filename=\"%s.pdf\"",filename));
        OutputStream out = response.getOutputStream();
        JasperExportManager.exportReportToPdfStream(jasperReportService.generateReports(reportDTO.getUserId(),reportDTO.getStoreId()),out);
        out.flush();
        out.close();
    }
}
