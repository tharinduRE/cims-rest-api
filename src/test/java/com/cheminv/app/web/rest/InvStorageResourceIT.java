package com.cheminv.app.web.rest;

import com.cheminv.app.CimsApp;
import com.cheminv.app.domain.InvStorage;
import com.cheminv.app.repository.InvStorageRepository;
import com.cheminv.app.service.InvStorageService;
import com.cheminv.app.service.dto.InvStorageDTO;
import com.cheminv.app.service.mapper.InvStorageMapper;

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

import com.cheminv.app.domain.enumeration.StorageLocation;
/**
 * Integration tests for the {@link InvStorageResource} REST controller.
 */
@SpringBootTest(classes = CimsApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class InvStorageResourceIT {

    private static final String DEFAULT_STORAGE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_STORAGE_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_STORAGE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_STORAGE_NAME = "BBBBBBBBBB";

    private static final StorageLocation DEFAULT_STORAGE_LOCATION = StorageLocation.GLI;
    private static final StorageLocation UPDATED_STORAGE_LOCATION = StorageLocation.GLII;

    @Autowired
    private InvStorageRepository invStorageRepository;

    @Autowired
    private InvStorageMapper invStorageMapper;

    @Autowired
    private InvStorageService invStorageService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInvStorageMockMvc;

    private InvStorage invStorage;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InvStorage createEntity(EntityManager em) {
        InvStorage invStorage = new InvStorage()
            .storageCode(DEFAULT_STORAGE_CODE)
            .storageName(DEFAULT_STORAGE_NAME)
            .storageLocation(DEFAULT_STORAGE_LOCATION);
        return invStorage;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InvStorage createUpdatedEntity(EntityManager em) {
        InvStorage invStorage = new InvStorage()
            .storageCode(UPDATED_STORAGE_CODE)
            .storageName(UPDATED_STORAGE_NAME)
            .storageLocation(UPDATED_STORAGE_LOCATION);
        return invStorage;
    }

    @BeforeEach
    public void initTest() {
        invStorage = createEntity(em);
    }

    @Test
    @Transactional
    public void createInvStorage() throws Exception {
        int databaseSizeBeforeCreate = invStorageRepository.findAll().size();
        // Create the InvStorage
        InvStorageDTO invStorageDTO = invStorageMapper.toDto(invStorage);
        restInvStorageMockMvc.perform(post("/api/inv-storages")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(invStorageDTO)))
            .andExpect(status().isCreated());

        // Validate the InvStorage in the database
        List<InvStorage> invStorageList = invStorageRepository.findAll();
        assertThat(invStorageList).hasSize(databaseSizeBeforeCreate + 1);
        InvStorage testInvStorage = invStorageList.get(invStorageList.size() - 1);
        assertThat(testInvStorage.getStorageCode()).isEqualTo(DEFAULT_STORAGE_CODE);
        assertThat(testInvStorage.getStorageName()).isEqualTo(DEFAULT_STORAGE_NAME);
        assertThat(testInvStorage.getStorageLocation()).isEqualTo(DEFAULT_STORAGE_LOCATION);
    }

    @Test
    @Transactional
    public void createInvStorageWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = invStorageRepository.findAll().size();

        // Create the InvStorage with an existing ID
        invStorage.setId(1L);
        InvStorageDTO invStorageDTO = invStorageMapper.toDto(invStorage);

        // An entity with an existing ID cannot be created, so this API call must fail
        restInvStorageMockMvc.perform(post("/api/inv-storages")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(invStorageDTO)))
            .andExpect(status().isBadRequest());

        // Validate the InvStorage in the database
        List<InvStorage> invStorageList = invStorageRepository.findAll();
        assertThat(invStorageList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllInvStorages() throws Exception {
        // Initialize the database
        invStorageRepository.saveAndFlush(invStorage);

        // Get all the invStorageList
        restInvStorageMockMvc.perform(get("/api/inv-storages?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(invStorage.getId().intValue())))
            .andExpect(jsonPath("$.[*].storageCode").value(hasItem(DEFAULT_STORAGE_CODE)))
            .andExpect(jsonPath("$.[*].storageName").value(hasItem(DEFAULT_STORAGE_NAME)))
            .andExpect(jsonPath("$.[*].storageLocation").value(hasItem(DEFAULT_STORAGE_LOCATION.toString())));
    }
    
    @Test
    @Transactional
    public void getInvStorage() throws Exception {
        // Initialize the database
        invStorageRepository.saveAndFlush(invStorage);

        // Get the invStorage
        restInvStorageMockMvc.perform(get("/api/inv-storages/{id}", invStorage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(invStorage.getId().intValue()))
            .andExpect(jsonPath("$.storageCode").value(DEFAULT_STORAGE_CODE))
            .andExpect(jsonPath("$.storageName").value(DEFAULT_STORAGE_NAME))
            .andExpect(jsonPath("$.storageLocation").value(DEFAULT_STORAGE_LOCATION.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingInvStorage() throws Exception {
        // Get the invStorage
        restInvStorageMockMvc.perform(get("/api/inv-storages/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInvStorage() throws Exception {
        // Initialize the database
        invStorageRepository.saveAndFlush(invStorage);

        int databaseSizeBeforeUpdate = invStorageRepository.findAll().size();

        // Update the invStorage
        InvStorage updatedInvStorage = invStorageRepository.findById(invStorage.getId()).get();
        // Disconnect from session so that the updates on updatedInvStorage are not directly saved in db
        em.detach(updatedInvStorage);
        updatedInvStorage
            .storageCode(UPDATED_STORAGE_CODE)
            .storageName(UPDATED_STORAGE_NAME)
            .storageLocation(UPDATED_STORAGE_LOCATION);
        InvStorageDTO invStorageDTO = invStorageMapper.toDto(updatedInvStorage);

        restInvStorageMockMvc.perform(put("/api/inv-storages")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(invStorageDTO)))
            .andExpect(status().isOk());

        // Validate the InvStorage in the database
        List<InvStorage> invStorageList = invStorageRepository.findAll();
        assertThat(invStorageList).hasSize(databaseSizeBeforeUpdate);
        InvStorage testInvStorage = invStorageList.get(invStorageList.size() - 1);
        assertThat(testInvStorage.getStorageCode()).isEqualTo(UPDATED_STORAGE_CODE);
        assertThat(testInvStorage.getStorageName()).isEqualTo(UPDATED_STORAGE_NAME);
        assertThat(testInvStorage.getStorageLocation()).isEqualTo(UPDATED_STORAGE_LOCATION);
    }

    @Test
    @Transactional
    public void updateNonExistingInvStorage() throws Exception {
        int databaseSizeBeforeUpdate = invStorageRepository.findAll().size();

        // Create the InvStorage
        InvStorageDTO invStorageDTO = invStorageMapper.toDto(invStorage);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInvStorageMockMvc.perform(put("/api/inv-storages")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(invStorageDTO)))
            .andExpect(status().isBadRequest());

        // Validate the InvStorage in the database
        List<InvStorage> invStorageList = invStorageRepository.findAll();
        assertThat(invStorageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteInvStorage() throws Exception {
        // Initialize the database
        invStorageRepository.saveAndFlush(invStorage);

        int databaseSizeBeforeDelete = invStorageRepository.findAll().size();

        // Delete the invStorage
        restInvStorageMockMvc.perform(delete("/api/inv-storages/{id}", invStorage.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<InvStorage> invStorageList = invStorageRepository.findAll();
        assertThat(invStorageList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
