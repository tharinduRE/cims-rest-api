package com.cheminv.app.web.rest;

import com.cheminv.app.CimsApp;
import com.cheminv.app.domain.HazardCode;
import com.cheminv.app.repository.HazardCodeRepository;
import com.cheminv.app.service.HazardCodeService;
import com.cheminv.app.service.dto.HazardCodeDTO;
import com.cheminv.app.service.mapper.HazardCodeMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link HazardCodeResource} REST controller.
 */
@SpringBootTest(classes = CimsApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class HazardCodeResourceIT {

    private static final Integer DEFAULT_HAZARD_CODE = 1;
    private static final Integer UPDATED_HAZARD_CODE = 2;

    private static final String DEFAULT_HAZARD_CODE_DESC = "AAAAAAAAAA";
    private static final String UPDATED_HAZARD_CODE_DESC = "BBBBBBBBBB";

    @Autowired
    private HazardCodeRepository hazardCodeRepository;

    @Autowired
    private HazardCodeMapper hazardCodeMapper;

    @Autowired
    private HazardCodeService hazardCodeService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHazardCodeMockMvc;

    private HazardCode hazardCode;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HazardCode createEntity(EntityManager em) {
        HazardCode hazardCode = new HazardCode()
            .hazardCode(DEFAULT_HAZARD_CODE)
            .hazardCodeDesc(DEFAULT_HAZARD_CODE_DESC);
        return hazardCode;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HazardCode createUpdatedEntity(EntityManager em) {
        HazardCode hazardCode = new HazardCode()
            .hazardCode(UPDATED_HAZARD_CODE)
            .hazardCodeDesc(UPDATED_HAZARD_CODE_DESC);
        return hazardCode;
    }

    @BeforeEach
    public void initTest() {
        hazardCode = createEntity(em);
    }

    @Test
    @Transactional
    public void getAllHazardCodes() throws Exception {
        // Initialize the database
        hazardCodeRepository.saveAndFlush(hazardCode);

        // Get all the hazardCodeList
        restHazardCodeMockMvc.perform(get("/api/hazard-codes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(hazardCode.getId().intValue())))
            .andExpect(jsonPath("$.[*].hazardCode").value(hasItem(DEFAULT_HAZARD_CODE)))
            .andExpect(jsonPath("$.[*].hazardCodeDesc").value(hasItem(DEFAULT_HAZARD_CODE_DESC)));
    }
    
    @Test
    @Transactional
    public void getHazardCode() throws Exception {
        // Initialize the database
        hazardCodeRepository.saveAndFlush(hazardCode);

        // Get the hazardCode
        restHazardCodeMockMvc.perform(get("/api/hazard-codes/{id}", hazardCode.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(hazardCode.getId().intValue()))
            .andExpect(jsonPath("$.hazardCode").value(DEFAULT_HAZARD_CODE))
            .andExpect(jsonPath("$.hazardCodeDesc").value(DEFAULT_HAZARD_CODE_DESC));
    }
    @Test
    @Transactional
    public void getNonExistingHazardCode() throws Exception {
        // Get the hazardCode
        restHazardCodeMockMvc.perform(get("/api/hazard-codes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }
}
