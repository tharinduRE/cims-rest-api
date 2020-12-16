package com.cheminv.app.web.rest;

import com.cheminv.app.service.JasperReportService;
import com.cheminv.app.service.dto.InvReportDTO;
import com.cheminv.app.service.dto.ReportDTO;
import com.cheminv.app.web.rest.errors.BadRequestAlertException;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * ReportsResource controller
 */
@RestController
@RequestMapping("/api")
public class ReportsResource {

    private final Logger log = LoggerFactory.getLogger(ReportsResource.class);

    private JasperReportService jasperReportService;

    public ReportsResource(JasperReportService jasperReportService) {
        this.jasperReportService = jasperReportService;
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
