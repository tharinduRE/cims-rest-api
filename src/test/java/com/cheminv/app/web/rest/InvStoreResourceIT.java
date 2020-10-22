package com.cheminv.app.web.rest;

import com.cheminv.app.CimsApp;
import com.cheminv.app.domain.InvStore;
import com.cheminv.app.repository.InvStoreRepository;

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

import com.cheminv.app.domain.enumeration.StockStore;
/**
 * Integration tests for the {@link InvStoreResource} REST controller.
 */
@SpringBootTest(classes = CimsApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class InvStoreResourceIT {

    private static final StockStore DEFAULT_CODE = StockStore.ORG;
    private static final StockStore UPDATED_CODE = StockStore.ORG;

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private InvStoreRepository invStoreRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInvStoreMockMvc;

    private InvStore invStore;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InvStore createEntity(EntityManager em) {
        InvStore invStore = new InvStore()
            .code(DEFAULT_CODE)
            .name(DEFAULT_NAME);
        return invStore;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InvStore createUpdatedEntity(EntityManager em) {
        InvStore invStore = new InvStore()
            .code(UPDATED_CODE)
            .name(UPDATED_NAME);
        return invStore;
    }

    @BeforeEach
    public void initTest() {
        invStore = createEntity(em);
    }

    @Test
    @Transactional
    public void createInvStore() throws Exception {
        int databaseSizeBeforeCreate = invStoreRepository.findAll().size();
        // Create the InvStore
        restInvStoreMockMvc.perform(post("/api/inv-stores")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(invStore)))
            .andExpect(status().isCreated());

        // Validate the InvStore in the database
        List<InvStore> invStoreList = invStoreRepository.findAll();
        assertThat(invStoreList).hasSize(databaseSizeBeforeCreate + 1);
        InvStore testInvStore = invStoreList.get(invStoreList.size() - 1);
        assertThat(testInvStore.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testInvStore.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createInvStoreWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = invStoreRepository.findAll().size();

        // Create the InvStore with an existing ID
        invStore.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restInvStoreMockMvc.perform(post("/api/inv-stores")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(invStore)))
            .andExpect(status().isBadRequest());

        // Validate the InvStore in the database
        List<InvStore> invStoreList = invStoreRepository.findAll();
        assertThat(invStoreList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllInvStores() throws Exception {
        // Initialize the database
        invStoreRepository.saveAndFlush(invStore);

        // Get all the invStoreList
        restInvStoreMockMvc.perform(get("/api/inv-stores?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(invStore.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }
    
    @Test
    @Transactional
    public void getInvStore() throws Exception {
        // Initialize the database
        invStoreRepository.saveAndFlush(invStore);

        // Get the invStore
        restInvStoreMockMvc.perform(get("/api/inv-stores/{id}", invStore.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(invStore.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }
    @Test
    @Transactional
    public void getNonExistingInvStore() throws Exception {
        // Get the invStore
        restInvStoreMockMvc.perform(get("/api/inv-stores/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInvStore() throws Exception {
        // Initialize the database
        invStoreRepository.saveAndFlush(invStore);

        int databaseSizeBeforeUpdate = invStoreRepository.findAll().size();

        // Update the invStore
        InvStore updatedInvStore = invStoreRepository.findById(invStore.getId()).get();
        // Disconnect from session so that the updates on updatedInvStore are not directly saved in db
        em.detach(updatedInvStore);
        updatedInvStore
            .code(UPDATED_CODE)
            .name(UPDATED_NAME);

        restInvStoreMockMvc.perform(put("/api/inv-stores")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedInvStore)))
            .andExpect(status().isOk());

        // Validate the InvStore in the database
        List<InvStore> invStoreList = invStoreRepository.findAll();
        assertThat(invStoreList).hasSize(databaseSizeBeforeUpdate);
        InvStore testInvStore = invStoreList.get(invStoreList.size() - 1);
        assertThat(testInvStore.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testInvStore.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingInvStore() throws Exception {
        int databaseSizeBeforeUpdate = invStoreRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInvStoreMockMvc.perform(put("/api/inv-stores")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(invStore)))
            .andExpect(status().isBadRequest());

        // Validate the InvStore in the database
        List<InvStore> invStoreList = invStoreRepository.findAll();
        assertThat(invStoreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteInvStore() throws Exception {
        // Initialize the database
        invStoreRepository.saveAndFlush(invStore);

        int databaseSizeBeforeDelete = invStoreRepository.findAll().size();

        // Delete the invStore
        restInvStoreMockMvc.perform(delete("/api/inv-stores/{id}", invStore.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<InvStore> invStoreList = invStoreRepository.findAll();
        assertThat(invStoreList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
