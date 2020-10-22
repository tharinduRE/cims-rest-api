package com.cheminv.app.web.rest;

import com.cheminv.app.CimsApp;
import com.cheminv.app.domain.CasNumber;
import com.cheminv.app.repository.CasNumberRepository;

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
 * Integration tests for the {@link CasNumberResource} REST controller.
 */
@SpringBootTest(classes = CimsApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class CasNumberResourceIT {

    private static final String DEFAULT_CAS_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_CAS_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_ITEM_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ITEM_NAME = "BBBBBBBBBB";

    @Autowired
    private CasNumberRepository casNumberRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCasNumberMockMvc;

    private CasNumber casNumber;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CasNumber createEntity(EntityManager em) {
        CasNumber casNumber = new CasNumber()
            .casNumber(DEFAULT_CAS_NUMBER)
            .itemName(DEFAULT_ITEM_NAME);
        return casNumber;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CasNumber createUpdatedEntity(EntityManager em) {
        CasNumber casNumber = new CasNumber()
            .casNumber(UPDATED_CAS_NUMBER)
            .itemName(UPDATED_ITEM_NAME);
        return casNumber;
    }

    @BeforeEach
    public void initTest() {
        casNumber = createEntity(em);
    }

    @Test
    @Transactional
    public void getAllCasNumbers() throws Exception {
        // Initialize the database
        casNumberRepository.saveAndFlush(casNumber);

        // Get all the casNumberList
        restCasNumberMockMvc.perform(get("/api/cas-numbers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(casNumber.getId().intValue())))
            .andExpect(jsonPath("$.[*].casNumber").value(hasItem(DEFAULT_CAS_NUMBER)))
            .andExpect(jsonPath("$.[*].itemName").value(hasItem(DEFAULT_ITEM_NAME)));
    }
    
    @Test
    @Transactional
    public void getCasNumber() throws Exception {
        // Initialize the database
        casNumberRepository.saveAndFlush(casNumber);

        // Get the casNumber
        restCasNumberMockMvc.perform(get("/api/cas-numbers/{id}", casNumber.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(casNumber.getId().intValue()))
            .andExpect(jsonPath("$.casNumber").value(DEFAULT_CAS_NUMBER))
            .andExpect(jsonPath("$.itemName").value(DEFAULT_ITEM_NAME));
    }
    @Test
    @Transactional
    public void getNonExistingCasNumber() throws Exception {
        // Get the casNumber
        restCasNumberMockMvc.perform(get("/api/cas-numbers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }
}
