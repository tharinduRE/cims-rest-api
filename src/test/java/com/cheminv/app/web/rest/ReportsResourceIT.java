package com.cheminv.app.web.rest;

import com.cheminv.app.CimsApp;
import com.cheminv.app.service.JasperReportService;
import com.cheminv.app.service.ReportService;
import com.cheminv.app.service.dto.ReportDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
/**
 * Test class for the ReportsResource REST controller.
 *
 * @see ReportsResource
 */
@SpringBootTest(classes = CimsApp.class)
public class ReportsResourceIT {

    private MockMvc restMockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        JasperReportService jasperReportService = null;
        ReportService reportService = null;
        ReportResource reportsResource = new ReportResource(reportService,jasperReportService);
        restMockMvc = MockMvcBuilders
            .standaloneSetup(reportsResource)
            .build();
    }

    /**
     * Test reports
     */
    @Test
    public void testReports() throws Exception {
        ReportDTO reportDTO = new ReportDTO();
        reportDTO.setUserId(Long.valueOf("1"));
        restMockMvc.perform(post("/api/reports/")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(reportDTO)))
            .andExpect(status().isOk());
    }
}
