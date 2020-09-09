package com.cheminv.app.web.rest;

import com.cheminv.app.CimsApp;
import com.cheminv.app.domain.Item;
import com.cheminv.app.repository.ItemRepository;
import com.cheminv.app.repository.search.ItemSearchRepository;
import com.cheminv.app.service.ItemService;
import com.cheminv.app.service.dto.ItemDTO;
import com.cheminv.app.service.mapper.ItemMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link ItemResource} REST controller.
 */
@SpringBootTest(classes = CimsApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class ItemResourceIT {

    private static final String DEFAULT_ITEM_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ITEM_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ITEM_DESC = "AAAAAAAAAA";
    private static final String UPDATED_ITEM_DESC = "BBBBBBBBBB";

    private static final String DEFAULT_CAS_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_CAS_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_STOCK_BOOK_FOLIO = "AAAAAAAAAA";
    private static final String UPDATED_STOCK_BOOK_FOLIO = "BBBBBBBBBB";

    private static final Float DEFAULT_ITEM_CAPACITY = 1F;
    private static final Float UPDATED_ITEM_CAPACITY = 2F;

    private static final Float DEFAULT_UNIT_PRICE = 1F;
    private static final Float UPDATED_UNIT_PRICE = 2F;

    @Autowired
    private ItemRepository itemRepository;

    @Mock
    private ItemRepository itemRepositoryMock;

    @Autowired
    private ItemMapper itemMapper;

    @Mock
    private ItemService itemServiceMock;

    @Autowired
    private ItemService itemService;

    /**
     * This repository is mocked in the com.cheminv.app.repository.search test package.
     *
     * @see com.cheminv.app.repository.search.ItemSearchRepositoryMockConfiguration
     */
    @Autowired
    private ItemSearchRepository mockItemSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restItemMockMvc;

    private Item item;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Item createEntity(EntityManager em) {
        Item item = new Item()
            .itemName(DEFAULT_ITEM_NAME)
            .itemDesc(DEFAULT_ITEM_DESC)
            .casNumber(DEFAULT_CAS_NUMBER)
            .stockBookFolio(DEFAULT_STOCK_BOOK_FOLIO)
            .itemCapacity(DEFAULT_ITEM_CAPACITY)
            .unitPrice(DEFAULT_UNIT_PRICE);
        return item;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Item createUpdatedEntity(EntityManager em) {
        Item item = new Item()
            .itemName(UPDATED_ITEM_NAME)
            .itemDesc(UPDATED_ITEM_DESC)
            .casNumber(UPDATED_CAS_NUMBER)
            .stockBookFolio(UPDATED_STOCK_BOOK_FOLIO)
            .itemCapacity(UPDATED_ITEM_CAPACITY)
            .unitPrice(UPDATED_UNIT_PRICE);
        return item;
    }

    @BeforeEach
    public void initTest() {
        item = createEntity(em);
    }

    @Test
    @Transactional
    public void createItem() throws Exception {
        int databaseSizeBeforeCreate = itemRepository.findAll().size();
        // Create the Item
        ItemDTO itemDTO = itemMapper.toDto(item);
        restItemMockMvc.perform(post("/api/items")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(itemDTO)))
            .andExpect(status().isCreated());

        // Validate the Item in the database
        List<Item> itemList = itemRepository.findAll();
        assertThat(itemList).hasSize(databaseSizeBeforeCreate + 1);
        Item testItem = itemList.get(itemList.size() - 1);
        assertThat(testItem.getItemName()).isEqualTo(DEFAULT_ITEM_NAME);
        assertThat(testItem.getItemDesc()).isEqualTo(DEFAULT_ITEM_DESC);
        assertThat(testItem.getCasNumber()).isEqualTo(DEFAULT_CAS_NUMBER);
        assertThat(testItem.getStockBookFolio()).isEqualTo(DEFAULT_STOCK_BOOK_FOLIO);
        assertThat(testItem.getItemCapacity()).isEqualTo(DEFAULT_ITEM_CAPACITY);
        assertThat(testItem.getUnitPrice()).isEqualTo(DEFAULT_UNIT_PRICE);

        // Validate the Item in Elasticsearch
        verify(mockItemSearchRepository, times(1)).save(testItem);
    }

    @Test
    @Transactional
    public void createItemWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = itemRepository.findAll().size();

        // Create the Item with an existing ID
        item.setId(1L);
        ItemDTO itemDTO = itemMapper.toDto(item);

        // An entity with an existing ID cannot be created, so this API call must fail
        restItemMockMvc.perform(post("/api/items")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(itemDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Item in the database
        List<Item> itemList = itemRepository.findAll();
        assertThat(itemList).hasSize(databaseSizeBeforeCreate);

        // Validate the Item in Elasticsearch
        verify(mockItemSearchRepository, times(0)).save(item);
    }


    @Test
    @Transactional
    public void checkItemNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = itemRepository.findAll().size();
        // set the field null
        item.setItemName(null);

        // Create the Item, which fails.
        ItemDTO itemDTO = itemMapper.toDto(item);


        restItemMockMvc.perform(post("/api/items")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(itemDTO)))
            .andExpect(status().isBadRequest());

        List<Item> itemList = itemRepository.findAll();
        assertThat(itemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllItems() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get all the itemList
        restItemMockMvc.perform(get("/api/items?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(item.getId().intValue())))
            .andExpect(jsonPath("$.[*].itemName").value(hasItem(DEFAULT_ITEM_NAME)))
            .andExpect(jsonPath("$.[*].itemDesc").value(hasItem(DEFAULT_ITEM_DESC)))
            .andExpect(jsonPath("$.[*].casNumber").value(hasItem(DEFAULT_CAS_NUMBER)))
            .andExpect(jsonPath("$.[*].stockBookFolio").value(hasItem(DEFAULT_STOCK_BOOK_FOLIO)))
            .andExpect(jsonPath("$.[*].itemCapacity").value(hasItem(DEFAULT_ITEM_CAPACITY.doubleValue())))
            .andExpect(jsonPath("$.[*].unitPrice").value(hasItem(DEFAULT_UNIT_PRICE.doubleValue())));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllItemsWithEagerRelationshipsIsEnabled() throws Exception {
        when(itemServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restItemMockMvc.perform(get("/api/items?eagerload=true"))
            .andExpect(status().isOk());

        verify(itemServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllItemsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(itemServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restItemMockMvc.perform(get("/api/items?eagerload=true"))
            .andExpect(status().isOk());

        verify(itemServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getItem() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get the item
        restItemMockMvc.perform(get("/api/items/{id}", item.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(item.getId().intValue()))
            .andExpect(jsonPath("$.itemName").value(DEFAULT_ITEM_NAME))
            .andExpect(jsonPath("$.itemDesc").value(DEFAULT_ITEM_DESC))
            .andExpect(jsonPath("$.casNumber").value(DEFAULT_CAS_NUMBER))
            .andExpect(jsonPath("$.stockBookFolio").value(DEFAULT_STOCK_BOOK_FOLIO))
            .andExpect(jsonPath("$.itemCapacity").value(DEFAULT_ITEM_CAPACITY.doubleValue()))
            .andExpect(jsonPath("$.unitPrice").value(DEFAULT_UNIT_PRICE.doubleValue()));
    }
    @Test
    @Transactional
    public void getNonExistingItem() throws Exception {
        // Get the item
        restItemMockMvc.perform(get("/api/items/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateItem() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        int databaseSizeBeforeUpdate = itemRepository.findAll().size();

        // Update the item
        Item updatedItem = itemRepository.findById(item.getId()).get();
        // Disconnect from session so that the updates on updatedItem are not directly saved in db
        em.detach(updatedItem);
        updatedItem
            .itemName(UPDATED_ITEM_NAME)
            .itemDesc(UPDATED_ITEM_DESC)
            .casNumber(UPDATED_CAS_NUMBER)
            .stockBookFolio(UPDATED_STOCK_BOOK_FOLIO)
            .itemCapacity(UPDATED_ITEM_CAPACITY)
            .unitPrice(UPDATED_UNIT_PRICE);
        ItemDTO itemDTO = itemMapper.toDto(updatedItem);

        restItemMockMvc.perform(put("/api/items")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(itemDTO)))
            .andExpect(status().isOk());

        // Validate the Item in the database
        List<Item> itemList = itemRepository.findAll();
        assertThat(itemList).hasSize(databaseSizeBeforeUpdate);
        Item testItem = itemList.get(itemList.size() - 1);
        assertThat(testItem.getItemName()).isEqualTo(UPDATED_ITEM_NAME);
        assertThat(testItem.getItemDesc()).isEqualTo(UPDATED_ITEM_DESC);
        assertThat(testItem.getCasNumber()).isEqualTo(UPDATED_CAS_NUMBER);
        assertThat(testItem.getStockBookFolio()).isEqualTo(UPDATED_STOCK_BOOK_FOLIO);
        assertThat(testItem.getItemCapacity()).isEqualTo(UPDATED_ITEM_CAPACITY);
        assertThat(testItem.getUnitPrice()).isEqualTo(UPDATED_UNIT_PRICE);

        // Validate the Item in Elasticsearch
        verify(mockItemSearchRepository, times(1)).save(testItem);
    }

    @Test
    @Transactional
    public void updateNonExistingItem() throws Exception {
        int databaseSizeBeforeUpdate = itemRepository.findAll().size();

        // Create the Item
        ItemDTO itemDTO = itemMapper.toDto(item);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restItemMockMvc.perform(put("/api/items")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(itemDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Item in the database
        List<Item> itemList = itemRepository.findAll();
        assertThat(itemList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Item in Elasticsearch
        verify(mockItemSearchRepository, times(0)).save(item);
    }

    @Test
    @Transactional
    public void deleteItem() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        int databaseSizeBeforeDelete = itemRepository.findAll().size();

        // Delete the item
        restItemMockMvc.perform(delete("/api/items/{id}", item.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Item> itemList = itemRepository.findAll();
        assertThat(itemList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Item in Elasticsearch
        verify(mockItemSearchRepository, times(1)).deleteById(item.getId());
    }

    @Test
    @Transactional
    public void searchItem() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        itemRepository.saveAndFlush(item);
        when(mockItemSearchRepository.search(queryStringQuery("id:" + item.getId())))
            .thenReturn(Collections.singletonList(item));

        // Search the item
        restItemMockMvc.perform(get("/api/_search/items?query=id:" + item.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(item.getId().intValue())))
            .andExpect(jsonPath("$.[*].itemName").value(hasItem(DEFAULT_ITEM_NAME)))
            .andExpect(jsonPath("$.[*].itemDesc").value(hasItem(DEFAULT_ITEM_DESC)))
            .andExpect(jsonPath("$.[*].casNumber").value(hasItem(DEFAULT_CAS_NUMBER)))
            .andExpect(jsonPath("$.[*].stockBookFolio").value(hasItem(DEFAULT_STOCK_BOOK_FOLIO)))
            .andExpect(jsonPath("$.[*].itemCapacity").value(hasItem(DEFAULT_ITEM_CAPACITY.doubleValue())))
            .andExpect(jsonPath("$.[*].unitPrice").value(hasItem(DEFAULT_UNIT_PRICE.doubleValue())));
    }
}
