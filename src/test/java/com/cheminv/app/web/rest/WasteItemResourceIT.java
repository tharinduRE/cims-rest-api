package com.cheminv.app.web.rest;

import com.cheminv.app.CimsApp;
import com.cheminv.app.domain.WasteItem;
import com.cheminv.app.domain.ItemStock;
import com.cheminv.app.repository.WasteItemRepository;
import com.cheminv.app.service.WasteItemService;
import com.cheminv.app.service.dto.WasteItemDTO;
import com.cheminv.app.service.mapper.WasteItemMapper;

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
 * Integration tests for the {@link WasteItemResource} REST controller.
 */
@SpringBootTest(classes = CimsApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class WasteItemResourceIT {

    private static final Float DEFAULT_ITEM_QUANTITY = 1F;
    private static final Float UPDATED_ITEM_QUANTITY = 2F;

    private static final Float DEFAULT_MIN_QUANTITY = 1F;
    private static final Float UPDATED_MIN_QUANTITY = 2F;

    @Autowired
    private WasteItemRepository wasteItemRepository;

    @Autowired
    private WasteItemMapper wasteItemMapper;

    @Autowired
    private WasteItemService wasteItemService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restWasteItemMockMvc;

    private WasteItem wasteItem;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WasteItem createEntity(EntityManager em) {
        WasteItem wasteItem = new WasteItem()
            .itemQuantity(DEFAULT_ITEM_QUANTITY)
            .minQuantity(DEFAULT_MIN_QUANTITY);
        // Add required entity
        ItemStock itemStock;
        if (TestUtil.findAll(em, ItemStock.class).isEmpty()) {
            itemStock = ItemStockResourceIT.createEntity(em);
            em.persist(itemStock);
            em.flush();
        } else {
            itemStock = TestUtil.findAll(em, ItemStock.class).get(0);
        }
        wasteItem.setItemStock(itemStock);
        return wasteItem;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WasteItem createUpdatedEntity(EntityManager em) {
        WasteItem wasteItem = new WasteItem()
            .itemQuantity(UPDATED_ITEM_QUANTITY)
            .minQuantity(UPDATED_MIN_QUANTITY);
        // Add required entity
        ItemStock itemStock;
        if (TestUtil.findAll(em, ItemStock.class).isEmpty()) {
            itemStock = ItemStockResourceIT.createUpdatedEntity(em);
            em.persist(itemStock);
            em.flush();
        } else {
            itemStock = TestUtil.findAll(em, ItemStock.class).get(0);
        }
        wasteItem.setItemStock(itemStock);
        return wasteItem;
    }

    @BeforeEach
    public void initTest() {
        wasteItem = createEntity(em);
    }

    @Test
    @Transactional
    public void createWasteItem() throws Exception {
        int databaseSizeBeforeCreate = wasteItemRepository.findAll().size();
        // Create the WasteItem
        WasteItemDTO wasteItemDTO = wasteItemMapper.toDto(wasteItem);
        restWasteItemMockMvc.perform(post("/api/waste-items")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(wasteItemDTO)))
            .andExpect(status().isCreated());

        // Validate the WasteItem in the database
        List<WasteItem> wasteItemList = wasteItemRepository.findAll();
        assertThat(wasteItemList).hasSize(databaseSizeBeforeCreate + 1);
        WasteItem testWasteItem = wasteItemList.get(wasteItemList.size() - 1);
        assertThat(testWasteItem.getItemQuantity()).isEqualTo(DEFAULT_ITEM_QUANTITY);
        assertThat(testWasteItem.getMinQuantity()).isEqualTo(DEFAULT_MIN_QUANTITY);
    }

    @Test
    @Transactional
    public void createWasteItemWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = wasteItemRepository.findAll().size();

        // Create the WasteItem with an existing ID
        wasteItem.setId(1L);
        WasteItemDTO wasteItemDTO = wasteItemMapper.toDto(wasteItem);

        // An entity with an existing ID cannot be created, so this API call must fail
        restWasteItemMockMvc.perform(post("/api/waste-items")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(wasteItemDTO)))
            .andExpect(status().isBadRequest());

        // Validate the WasteItem in the database
        List<WasteItem> wasteItemList = wasteItemRepository.findAll();
        assertThat(wasteItemList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllWasteItems() throws Exception {
        // Initialize the database
        wasteItemRepository.saveAndFlush(wasteItem);

        // Get all the wasteItemList
        restWasteItemMockMvc.perform(get("/api/waste-items?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(wasteItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].itemQuantity").value(hasItem(DEFAULT_ITEM_QUANTITY.doubleValue())))
            .andExpect(jsonPath("$.[*].minQuantity").value(hasItem(DEFAULT_MIN_QUANTITY.doubleValue())));
    }
    
    @Test
    @Transactional
    public void getWasteItem() throws Exception {
        // Initialize the database
        wasteItemRepository.saveAndFlush(wasteItem);

        // Get the wasteItem
        restWasteItemMockMvc.perform(get("/api/waste-items/{id}", wasteItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(wasteItem.getId().intValue()))
            .andExpect(jsonPath("$.itemQuantity").value(DEFAULT_ITEM_QUANTITY.doubleValue()))
            .andExpect(jsonPath("$.minQuantity").value(DEFAULT_MIN_QUANTITY.doubleValue()));
    }
    @Test
    @Transactional
    public void getNonExistingWasteItem() throws Exception {
        // Get the wasteItem
        restWasteItemMockMvc.perform(get("/api/waste-items/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateWasteItem() throws Exception {
        // Initialize the database
        wasteItemRepository.saveAndFlush(wasteItem);

        int databaseSizeBeforeUpdate = wasteItemRepository.findAll().size();

        // Update the wasteItem
        WasteItem updatedWasteItem = wasteItemRepository.findById(wasteItem.getId()).get();
        // Disconnect from session so that the updates on updatedWasteItem are not directly saved in db
        em.detach(updatedWasteItem);
        updatedWasteItem
            .itemQuantity(UPDATED_ITEM_QUANTITY)
            .minQuantity(UPDATED_MIN_QUANTITY);
        WasteItemDTO wasteItemDTO = wasteItemMapper.toDto(updatedWasteItem);

        restWasteItemMockMvc.perform(put("/api/waste-items")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(wasteItemDTO)))
            .andExpect(status().isOk());

        // Validate the WasteItem in the database
        List<WasteItem> wasteItemList = wasteItemRepository.findAll();
        assertThat(wasteItemList).hasSize(databaseSizeBeforeUpdate);
        WasteItem testWasteItem = wasteItemList.get(wasteItemList.size() - 1);
        assertThat(testWasteItem.getItemQuantity()).isEqualTo(UPDATED_ITEM_QUANTITY);
        assertThat(testWasteItem.getMinQuantity()).isEqualTo(UPDATED_MIN_QUANTITY);
    }

    @Test
    @Transactional
    public void updateNonExistingWasteItem() throws Exception {
        int databaseSizeBeforeUpdate = wasteItemRepository.findAll().size();

        // Create the WasteItem
        WasteItemDTO wasteItemDTO = wasteItemMapper.toDto(wasteItem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWasteItemMockMvc.perform(put("/api/waste-items")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(wasteItemDTO)))
            .andExpect(status().isBadRequest());

        // Validate the WasteItem in the database
        List<WasteItem> wasteItemList = wasteItemRepository.findAll();
        assertThat(wasteItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteWasteItem() throws Exception {
        // Initialize the database
        wasteItemRepository.saveAndFlush(wasteItem);

        int databaseSizeBeforeDelete = wasteItemRepository.findAll().size();

        // Delete the wasteItem
        restWasteItemMockMvc.perform(delete("/api/waste-items/{id}", wasteItem.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<WasteItem> wasteItemList = wasteItemRepository.findAll();
        assertThat(wasteItemList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
