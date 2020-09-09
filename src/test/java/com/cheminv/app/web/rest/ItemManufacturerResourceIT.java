package com.cheminv.app.web.rest;

import com.cheminv.app.CimsApp;
import com.cheminv.app.domain.ItemManufacturer;
import com.cheminv.app.domain.Item;
import com.cheminv.app.repository.ItemManufacturerRepository;
import com.cheminv.app.repository.search.ItemManufacturerSearchRepository;
import com.cheminv.app.service.ItemManufacturerService;
import com.cheminv.app.service.dto.ItemManufacturerDTO;
import com.cheminv.app.service.mapper.ItemManufacturerMapper;

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
 * Integration tests for the {@link ItemManufacturerResource} REST controller.
 */
@SpringBootTest(classes = CimsApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class ItemManufacturerResourceIT {

    private static final String DEFAULT_MANUFACTURER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_MANUFACTURER_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_MANUFACTURER_DESC = "AAAAAAAAAA";
    private static final String UPDATED_MANUFACTURER_DESC = "BBBBBBBBBB";

    @Autowired
    private ItemManufacturerRepository itemManufacturerRepository;

    @Autowired
    private ItemManufacturerMapper itemManufacturerMapper;

    @Autowired
    private ItemManufacturerService itemManufacturerService;

    /**
     * This repository is mocked in the com.cheminv.app.repository.search test package.
     *
     * @see com.cheminv.app.repository.search.ItemManufacturerSearchRepositoryMockConfiguration
     */
    @Autowired
    private ItemManufacturerSearchRepository mockItemManufacturerSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restItemManufacturerMockMvc;

    private ItemManufacturer itemManufacturer;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ItemManufacturer createEntity(EntityManager em) {
        ItemManufacturer itemManufacturer = new ItemManufacturer()
            .manufacturerName(DEFAULT_MANUFACTURER_NAME)
            .manufacturerDesc(DEFAULT_MANUFACTURER_DESC);
        // Add required entity
        Item item;
        if (TestUtil.findAll(em, Item.class).isEmpty()) {
            item = ItemResourceIT.createEntity(em);
            em.persist(item);
            em.flush();
        } else {
            item = TestUtil.findAll(em, Item.class).get(0);
        }
        itemManufacturer.setItem(item);
        return itemManufacturer;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ItemManufacturer createUpdatedEntity(EntityManager em) {
        ItemManufacturer itemManufacturer = new ItemManufacturer()
            .manufacturerName(UPDATED_MANUFACTURER_NAME)
            .manufacturerDesc(UPDATED_MANUFACTURER_DESC);
        // Add required entity
        Item item;
        if (TestUtil.findAll(em, Item.class).isEmpty()) {
            item = ItemResourceIT.createUpdatedEntity(em);
            em.persist(item);
            em.flush();
        } else {
            item = TestUtil.findAll(em, Item.class).get(0);
        }
        itemManufacturer.setItem(item);
        return itemManufacturer;
    }

    @BeforeEach
    public void initTest() {
        itemManufacturer = createEntity(em);
    }

    @Test
    @Transactional
    public void createItemManufacturer() throws Exception {
        int databaseSizeBeforeCreate = itemManufacturerRepository.findAll().size();
        // Create the ItemManufacturer
        ItemManufacturerDTO itemManufacturerDTO = itemManufacturerMapper.toDto(itemManufacturer);
        restItemManufacturerMockMvc.perform(post("/api/item-manufacturers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(itemManufacturerDTO)))
            .andExpect(status().isCreated());

        // Validate the ItemManufacturer in the database
        List<ItemManufacturer> itemManufacturerList = itemManufacturerRepository.findAll();
        assertThat(itemManufacturerList).hasSize(databaseSizeBeforeCreate + 1);
        ItemManufacturer testItemManufacturer = itemManufacturerList.get(itemManufacturerList.size() - 1);
        assertThat(testItemManufacturer.getManufacturerName()).isEqualTo(DEFAULT_MANUFACTURER_NAME);
        assertThat(testItemManufacturer.getManufacturerDesc()).isEqualTo(DEFAULT_MANUFACTURER_DESC);

        // Validate the ItemManufacturer in Elasticsearch
        verify(mockItemManufacturerSearchRepository, times(1)).save(testItemManufacturer);
    }

    @Test
    @Transactional
    public void createItemManufacturerWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = itemManufacturerRepository.findAll().size();

        // Create the ItemManufacturer with an existing ID
        itemManufacturer.setId(1L);
        ItemManufacturerDTO itemManufacturerDTO = itemManufacturerMapper.toDto(itemManufacturer);

        // An entity with an existing ID cannot be created, so this API call must fail
        restItemManufacturerMockMvc.perform(post("/api/item-manufacturers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(itemManufacturerDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ItemManufacturer in the database
        List<ItemManufacturer> itemManufacturerList = itemManufacturerRepository.findAll();
        assertThat(itemManufacturerList).hasSize(databaseSizeBeforeCreate);

        // Validate the ItemManufacturer in Elasticsearch
        verify(mockItemManufacturerSearchRepository, times(0)).save(itemManufacturer);
    }


    @Test
    @Transactional
    public void getAllItemManufacturers() throws Exception {
        // Initialize the database
        itemManufacturerRepository.saveAndFlush(itemManufacturer);

        // Get all the itemManufacturerList
        restItemManufacturerMockMvc.perform(get("/api/item-manufacturers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(itemManufacturer.getId().intValue())))
            .andExpect(jsonPath("$.[*].manufacturerName").value(hasItem(DEFAULT_MANUFACTURER_NAME)))
            .andExpect(jsonPath("$.[*].manufacturerDesc").value(hasItem(DEFAULT_MANUFACTURER_DESC)));
    }
    
    @Test
    @Transactional
    public void getItemManufacturer() throws Exception {
        // Initialize the database
        itemManufacturerRepository.saveAndFlush(itemManufacturer);

        // Get the itemManufacturer
        restItemManufacturerMockMvc.perform(get("/api/item-manufacturers/{id}", itemManufacturer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(itemManufacturer.getId().intValue()))
            .andExpect(jsonPath("$.manufacturerName").value(DEFAULT_MANUFACTURER_NAME))
            .andExpect(jsonPath("$.manufacturerDesc").value(DEFAULT_MANUFACTURER_DESC));
    }
    @Test
    @Transactional
    public void getNonExistingItemManufacturer() throws Exception {
        // Get the itemManufacturer
        restItemManufacturerMockMvc.perform(get("/api/item-manufacturers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateItemManufacturer() throws Exception {
        // Initialize the database
        itemManufacturerRepository.saveAndFlush(itemManufacturer);

        int databaseSizeBeforeUpdate = itemManufacturerRepository.findAll().size();

        // Update the itemManufacturer
        ItemManufacturer updatedItemManufacturer = itemManufacturerRepository.findById(itemManufacturer.getId()).get();
        // Disconnect from session so that the updates on updatedItemManufacturer are not directly saved in db
        em.detach(updatedItemManufacturer);
        updatedItemManufacturer
            .manufacturerName(UPDATED_MANUFACTURER_NAME)
            .manufacturerDesc(UPDATED_MANUFACTURER_DESC);
        ItemManufacturerDTO itemManufacturerDTO = itemManufacturerMapper.toDto(updatedItemManufacturer);

        restItemManufacturerMockMvc.perform(put("/api/item-manufacturers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(itemManufacturerDTO)))
            .andExpect(status().isOk());

        // Validate the ItemManufacturer in the database
        List<ItemManufacturer> itemManufacturerList = itemManufacturerRepository.findAll();
        assertThat(itemManufacturerList).hasSize(databaseSizeBeforeUpdate);
        ItemManufacturer testItemManufacturer = itemManufacturerList.get(itemManufacturerList.size() - 1);
        assertThat(testItemManufacturer.getManufacturerName()).isEqualTo(UPDATED_MANUFACTURER_NAME);
        assertThat(testItemManufacturer.getManufacturerDesc()).isEqualTo(UPDATED_MANUFACTURER_DESC);

        // Validate the ItemManufacturer in Elasticsearch
        verify(mockItemManufacturerSearchRepository, times(1)).save(testItemManufacturer);
    }

    @Test
    @Transactional
    public void updateNonExistingItemManufacturer() throws Exception {
        int databaseSizeBeforeUpdate = itemManufacturerRepository.findAll().size();

        // Create the ItemManufacturer
        ItemManufacturerDTO itemManufacturerDTO = itemManufacturerMapper.toDto(itemManufacturer);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restItemManufacturerMockMvc.perform(put("/api/item-manufacturers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(itemManufacturerDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ItemManufacturer in the database
        List<ItemManufacturer> itemManufacturerList = itemManufacturerRepository.findAll();
        assertThat(itemManufacturerList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ItemManufacturer in Elasticsearch
        verify(mockItemManufacturerSearchRepository, times(0)).save(itemManufacturer);
    }

    @Test
    @Transactional
    public void deleteItemManufacturer() throws Exception {
        // Initialize the database
        itemManufacturerRepository.saveAndFlush(itemManufacturer);

        int databaseSizeBeforeDelete = itemManufacturerRepository.findAll().size();

        // Delete the itemManufacturer
        restItemManufacturerMockMvc.perform(delete("/api/item-manufacturers/{id}", itemManufacturer.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ItemManufacturer> itemManufacturerList = itemManufacturerRepository.findAll();
        assertThat(itemManufacturerList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the ItemManufacturer in Elasticsearch
        verify(mockItemManufacturerSearchRepository, times(1)).deleteById(itemManufacturer.getId());
    }

    @Test
    @Transactional
    public void searchItemManufacturer() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        itemManufacturerRepository.saveAndFlush(itemManufacturer);
        when(mockItemManufacturerSearchRepository.search(queryStringQuery("id:" + itemManufacturer.getId())))
            .thenReturn(Collections.singletonList(itemManufacturer));

        // Search the itemManufacturer
        restItemManufacturerMockMvc.perform(get("/api/_search/item-manufacturers?query=id:" + itemManufacturer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(itemManufacturer.getId().intValue())))
            .andExpect(jsonPath("$.[*].manufacturerName").value(hasItem(DEFAULT_MANUFACTURER_NAME)))
            .andExpect(jsonPath("$.[*].manufacturerDesc").value(hasItem(DEFAULT_MANUFACTURER_DESC)));
    }
}
