package com.cheminv.app.web.rest;

import com.cheminv.app.CimsApp;
import com.cheminv.app.domain.InvReport;
import com.cheminv.app.repository.InvReportRepository;
import com.cheminv.app.service.InvReportService;
import com.cheminv.app.service.dto.InvReportDTO;
import com.cheminv.app.service.mapper.InvReportMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;
import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link InvReportResource} REST controller.
 */
@SpringBootTest(classes = CimsApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class InvReportResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_ON = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_ON = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final byte[] DEFAULT_REPORT = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_REPORT = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_REPORT_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_REPORT_CONTENT_TYPE = "image/png";

    @Autowired
    private InvReportRepository invReportRepository;

    @Autowired
    private InvReportMapper invReportMapper;

    @Autowired
    private InvReportService invReportService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInvReportMockMvc;

    private InvReport invReport;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InvReport createEntity(EntityManager em) {
        InvReport invReport = new InvReport()
            .name(DEFAULT_NAME)
            .createdOn(DEFAULT_CREATED_ON)
            .report(DEFAULT_REPORT)
            .reportContentType(DEFAULT_REPORT_CONTENT_TYPE);
        return invReport;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InvReport createUpdatedEntity(EntityManager em) {
        InvReport invReport = new InvReport()
            .name(UPDATED_NAME)
            .createdOn(UPDATED_CREATED_ON)
            .report(UPDATED_REPORT)
            .reportContentType(UPDATED_REPORT_CONTENT_TYPE);
        return invReport;
    }

    @BeforeEach
    public void initTest() {
        invReport = createEntity(em);
    }

    @Test
    @Transactional
    public void getAllInvReports() throws Exception {
        // Initialize the database
        invReportRepository.saveAndFlush(invReport);

        // Get all the invReportList
        restInvReportMockMvc.perform(get("/api/inv-reports?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(invReport.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())))
            .andExpect(jsonPath("$.[*].reportContentType").value(hasItem(DEFAULT_REPORT_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].report").value(hasItem(Base64Utils.encodeToString(DEFAULT_REPORT))));
    }
    
    @Test
    @Transactional
    public void getInvReport() throws Exception {
        // Initialize the database
        invReportRepository.saveAndFlush(invReport);

        // Get the invReport
        restInvReportMockMvc.perform(get("/api/inv-reports/{id}", invReport.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(invReport.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.createdOn").value(DEFAULT_CREATED_ON.toString()))
            .andExpect(jsonPath("$.reportContentType").value(DEFAULT_REPORT_CONTENT_TYPE))
            .andExpect(jsonPath("$.report").value(Base64Utils.encodeToString(DEFAULT_REPORT)));
    }
    @Test
    @Transactional
    public void getNonExistingInvReport() throws Exception {
        // Get the invReport
        restInvReportMockMvc.perform(get("/api/inv-reports/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }
}
