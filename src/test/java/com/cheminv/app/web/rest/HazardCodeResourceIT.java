package com.cheminv.app.web.rest;

import com.cheminv.app.CimsApp;
import com.cheminv.app.domain.HazardCode;
import com.cheminv.app.repository.HazardCodeRepository;
import com.cheminv.app.repository.search.HazardCodeSearchRepository;
import com.cheminv.app.service.HazardCodeService;
import com.cheminv.app.service.dto.HazardCodeDTO;
import com.cheminv.app.service.mapper.HazardCodeMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link HazardCodeResource} REST controller.
 */
@SpringBootTest(classes = CimsApp.class)
@ExtendWith(MockitoExtension.class)
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

    /**
     * This repository is mocked in the com.cheminv.app.repository.search test package.
     *
     * @see com.cheminv.app.repository.search.HazardCodeSearchRepositoryMockConfiguration
     */
    @Autowired
    private HazardCodeSearchRepository mockHazardCodeSearchRepository;

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
    public void createHazardCode() throws Exception {
        int databaseSizeBeforeCreate = hazardCodeRepository.findAll().size();
        // Create the HazardCode
        HazardCodeDTO hazardCodeDTO = hazardCodeMapper.toDto(hazardCode);
        restHazardCodeMockMvc.perform(post("/api/hazard-codes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(hazardCodeDTO)))
            .andExpect(status().isCreated());

        // Validate the HazardCode in the database
        List<HazardCode> hazardCodeList = hazardCodeRepository.findAll();
        assertThat(hazardCodeList).hasSize(databaseSizeBeforeCreate + 1);
        HazardCode testHazardCode = hazardCodeList.get(hazardCodeList.size() - 1);
        assertThat(testHazardCode.getHazardCode()).isEqualTo(DEFAULT_HAZARD_CODE);
        assertThat(testHazardCode.getHazardCodeDesc()).isEqualTo(DEFAULT_HAZARD_CODE_DESC);

        // Validate the HazardCode in Elasticsearch
        verify(mockHazardCodeSearchRepository, times(1)).save(testHazardCode);
    }

    @Test
    @Transactional
    public void createHazardCodeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = hazardCodeRepository.findAll().size();

        // Create the HazardCode with an existing ID
        hazardCode.setId(1L);
        HazardCodeDTO hazardCodeDTO = hazardCodeMapper.toDto(hazardCode);

        // An entity with an existing ID cannot be created, so this API call must fail
        restHazardCodeMockMvc.perform(post("/api/hazard-codes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(hazardCodeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the HazardCode in the database
        List<HazardCode> hazardCodeList = hazardCodeRepository.findAll();
        assertThat(hazardCodeList).hasSize(databaseSizeBeforeCreate);

        // Validate the HazardCode in Elasticsearch
        verify(mockHazardCodeSearchRepository, times(0)).save(hazardCode);
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

    @Test
    @Transactional
    public void updateHazardCode() throws Exception {
        // Initialize the database
        hazardCodeRepository.saveAndFlush(hazardCode);

        int databaseSizeBeforeUpdate = hazardCodeRepository.findAll().size();

        // Update the hazardCode
        HazardCode updatedHazardCode = hazardCodeRepository.findById(hazardCode.getId()).get();
        // Disconnect from session so that the updates on updatedHazardCode are not directly saved in db
        em.detach(updatedHazardCode);
        updatedHazardCode
            .hazardCode(UPDATED_HAZARD_CODE)
            .hazardCodeDesc(UPDATED_HAZARD_CODE_DESC);
        HazardCodeDTO hazardCodeDTO = hazardCodeMapper.toDto(updatedHazardCode);

        restHazardCodeMockMvc.perform(put("/api/hazard-codes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(hazardCodeDTO)))
            .andExpect(status().isOk());

        // Validate the HazardCode in the database
        List<HazardCode> hazardCodeList = hazardCodeRepository.findAll();
        assertThat(hazardCodeList).hasSize(databaseSizeBeforeUpdate);
        HazardCode testHazardCode = hazardCodeList.get(hazardCodeList.size() - 1);
        assertThat(testHazardCode.getHazardCode()).isEqualTo(UPDATED_HAZARD_CODE);
        assertThat(testHazardCode.getHazardCodeDesc()).isEqualTo(UPDATED_HAZARD_CODE_DESC);

        // Validate the HazardCode in Elasticsearch
        verify(mockHazardCodeSearchRepository, times(1)).save(testHazardCode);
    }

    @Test
    @Transactional
    public void updateNonExistingHazardCode() throws Exception {
        int databaseSizeBeforeUpdate = hazardCodeRepository.findAll().size();

        // Create the HazardCode
        HazardCodeDTO hazardCodeDTO = hazardCodeMapper.toDto(hazardCode);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHazardCodeMockMvc.perform(put("/api/hazard-codes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(hazardCodeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the HazardCode in the database
        List<HazardCode> hazardCodeList = hazardCodeRepository.findAll();
        assertThat(hazardCodeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the HazardCode in Elasticsearch
        verify(mockHazardCodeSearchRepository, times(0)).save(hazardCode);
    }

    @Test
    @Transactional
    public void deleteHazardCode() throws Exception {
        // Initialize the database
        hazardCodeRepository.saveAndFlush(hazardCode);

        int databaseSizeBeforeDelete = hazardCodeRepository.findAll().size();

        // Delete the hazardCode
        restHazardCodeMockMvc.perform(delete("/api/hazard-codes/{id}", hazardCode.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<HazardCode> hazardCodeList = hazardCodeRepository.findAll();
        assertThat(hazardCodeList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the HazardCode in Elasticsearch
        verify(mockHazardCodeSearchRepository, times(1)).deleteById(hazardCode.getId());
    }

    @Test
    @Transactional
    public void searchHazardCode() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        hazardCodeRepository.saveAndFlush(hazardCode);
        when(mockHazardCodeSearchRepository.search(queryStringQuery("id:" + hazardCode.getId())))
            .thenReturn(Collections.singletonList(hazardCode));

        // Search the hazardCode
        restHazardCodeMockMvc.perform(get("/api/_search/hazard-codes?query=id:" + hazardCode.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(hazardCode.getId().intValue())))
            .andExpect(jsonPath("$.[*].hazardCode").value(hasItem(DEFAULT_HAZARD_CODE)))
            .andExpect(jsonPath("$.[*].hazardCodeDesc").value(hasItem(DEFAULT_HAZARD_CODE_DESC)));
    }
}
