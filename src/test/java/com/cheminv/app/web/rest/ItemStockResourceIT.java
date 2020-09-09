package com.cheminv.app.web.rest;

import com.cheminv.app.CimsApp;
import com.cheminv.app.domain.ItemStock;
import com.cheminv.app.domain.ItemTransaction;
import com.cheminv.app.domain.InvStorage;
import com.cheminv.app.domain.MeasUnit;
import com.cheminv.app.domain.Item;
import com.cheminv.app.repository.ItemStockRepository;
import com.cheminv.app.repository.search.ItemStockSearchRepository;
import com.cheminv.app.service.ItemStockService;
import com.cheminv.app.service.dto.ItemStockDTO;
import com.cheminv.app.service.mapper.ItemStockMapper;
import com.cheminv.app.service.dto.ItemStockCriteria;
import com.cheminv.app.service.ItemStockQueryService;

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
import java.time.LocalDate;
import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.cheminv.app.domain.enumeration.ItemStatus;
import com.cheminv.app.domain.enumeration.StockStore;
/**
 * Integration tests for the {@link ItemStockResource} REST controller.
 */
@SpringBootTest(classes = CimsApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class ItemStockResourceIT {

    private static final Integer DEFAULT_TOTAL_QUANTITY = 1;
    private static final Integer UPDATED_TOTAL_QUANTITY = 2;
    private static final Integer SMALLER_TOTAL_QUANTITY = 1 - 1;

    private static final Integer DEFAULT_MINIMUM_QUANTITY = 1;
    private static final Integer UPDATED_MINIMUM_QUANTITY = 2;
    private static final Integer SMALLER_MINIMUM_QUANTITY = 1 - 1;

    private static final ItemStatus DEFAULT_ITEM_STATUS = ItemStatus.NEW;
    private static final ItemStatus UPDATED_ITEM_STATUS = ItemStatus.USED;

    private static final StockStore DEFAULT_STOCK_STORE = StockStore.ORG;
    private static final StockStore UPDATED_STOCK_STORE = StockStore.INORG;

    private static final LocalDate DEFAULT_ENTRY_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ENTRY_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_ENTRY_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_EXPIRY_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_EXPIRY_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_EXPIRY_DATE = LocalDate.ofEpochDay(-1L);

    private static final Integer DEFAULT_CREATOR_ID = 1;
    private static final Integer UPDATED_CREATOR_ID = 2;
    private static final Integer SMALLER_CREATOR_ID = 1 - 1;

    private static final Instant DEFAULT_CREATED_ON = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_ON = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_SDSFILE = "AAAAAAAAAA";
    private static final String UPDATED_SDSFILE = "BBBBBBBBBB";

    @Autowired
    private ItemStockRepository itemStockRepository;

    @Mock
    private ItemStockRepository itemStockRepositoryMock;

    @Autowired
    private ItemStockMapper itemStockMapper;

    @Mock
    private ItemStockService itemStockServiceMock;

    @Autowired
    private ItemStockService itemStockService;

    /**
     * This repository is mocked in the com.cheminv.app.repository.search test package.
     *
     * @see com.cheminv.app.repository.search.ItemStockSearchRepositoryMockConfiguration
     */
    @Autowired
    private ItemStockSearchRepository mockItemStockSearchRepository;

    @Autowired
    private ItemStockQueryService itemStockQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restItemStockMockMvc;

    private ItemStock itemStock;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ItemStock createEntity(EntityManager em) {
        ItemStock itemStock = new ItemStock()
            .totalQuantity(DEFAULT_TOTAL_QUANTITY)
            .minimumQuantity(DEFAULT_MINIMUM_QUANTITY)
            .itemStatus(DEFAULT_ITEM_STATUS)
            .stockStore(DEFAULT_STOCK_STORE)
            .entryDate(DEFAULT_ENTRY_DATE)
            .expiryDate(DEFAULT_EXPIRY_DATE)
            .creatorId(DEFAULT_CREATOR_ID)
            .createdOn(DEFAULT_CREATED_ON)
            .sdsfile(DEFAULT_SDSFILE);
        // Add required entity
        InvStorage invStorage;
        if (TestUtil.findAll(em, InvStorage.class).isEmpty()) {
            invStorage = InvStorageResourceIT.createEntity(em);
            em.persist(invStorage);
            em.flush();
        } else {
            invStorage = TestUtil.findAll(em, InvStorage.class).get(0);
        }
        itemStock.setInvStorage(invStorage);
        // Add required entity
        MeasUnit measUnit;
        if (TestUtil.findAll(em, MeasUnit.class).isEmpty()) {
            measUnit = MeasUnitResourceIT.createEntity(em);
            em.persist(measUnit);
            em.flush();
        } else {
            measUnit = TestUtil.findAll(em, MeasUnit.class).get(0);
        }
        itemStock.setStorageUnit(measUnit);
        // Add required entity
        Item item;
        if (TestUtil.findAll(em, Item.class).isEmpty()) {
            item = ItemResourceIT.createEntity(em);
            em.persist(item);
            em.flush();
        } else {
            item = TestUtil.findAll(em, Item.class).get(0);
        }
        itemStock.setItem(item);
        return itemStock;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ItemStock createUpdatedEntity(EntityManager em) {
        ItemStock itemStock = new ItemStock()
            .totalQuantity(UPDATED_TOTAL_QUANTITY)
            .minimumQuantity(UPDATED_MINIMUM_QUANTITY)
            .itemStatus(UPDATED_ITEM_STATUS)
            .stockStore(UPDATED_STOCK_STORE)
            .entryDate(UPDATED_ENTRY_DATE)
            .expiryDate(UPDATED_EXPIRY_DATE)
            .creatorId(UPDATED_CREATOR_ID)
            .createdOn(UPDATED_CREATED_ON)
            .sdsfile(UPDATED_SDSFILE);
        // Add required entity
        InvStorage invStorage;
        if (TestUtil.findAll(em, InvStorage.class).isEmpty()) {
            invStorage = InvStorageResourceIT.createUpdatedEntity(em);
            em.persist(invStorage);
            em.flush();
        } else {
            invStorage = TestUtil.findAll(em, InvStorage.class).get(0);
        }
        itemStock.setInvStorage(invStorage);
        // Add required entity
        MeasUnit measUnit;
        if (TestUtil.findAll(em, MeasUnit.class).isEmpty()) {
            measUnit = MeasUnitResourceIT.createUpdatedEntity(em);
            em.persist(measUnit);
            em.flush();
        } else {
            measUnit = TestUtil.findAll(em, MeasUnit.class).get(0);
        }
        itemStock.setStorageUnit(measUnit);
        // Add required entity
        Item item;
        if (TestUtil.findAll(em, Item.class).isEmpty()) {
            item = ItemResourceIT.createUpdatedEntity(em);
            em.persist(item);
            em.flush();
        } else {
            item = TestUtil.findAll(em, Item.class).get(0);
        }
        itemStock.setItem(item);
        return itemStock;
    }

    @BeforeEach
    public void initTest() {
        itemStock = createEntity(em);
    }

    @Test
    @Transactional
    public void createItemStock() throws Exception {
        int databaseSizeBeforeCreate = itemStockRepository.findAll().size();
        // Create the ItemStock
        ItemStockDTO itemStockDTO = itemStockMapper.toDto(itemStock);
        restItemStockMockMvc.perform(post("/api/item-stocks")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(itemStockDTO)))
            .andExpect(status().isCreated());

        // Validate the ItemStock in the database
        List<ItemStock> itemStockList = itemStockRepository.findAll();
        assertThat(itemStockList).hasSize(databaseSizeBeforeCreate + 1);
        ItemStock testItemStock = itemStockList.get(itemStockList.size() - 1);
        assertThat(testItemStock.getTotalQuantity()).isEqualTo(DEFAULT_TOTAL_QUANTITY);
        assertThat(testItemStock.getMinimumQuantity()).isEqualTo(DEFAULT_MINIMUM_QUANTITY);
        assertThat(testItemStock.getItemStatus()).isEqualTo(DEFAULT_ITEM_STATUS);
        assertThat(testItemStock.getStockStore()).isEqualTo(DEFAULT_STOCK_STORE);
        assertThat(testItemStock.getEntryDate()).isEqualTo(DEFAULT_ENTRY_DATE);
        assertThat(testItemStock.getExpiryDate()).isEqualTo(DEFAULT_EXPIRY_DATE);
        assertThat(testItemStock.getCreatorId()).isEqualTo(DEFAULT_CREATOR_ID);
        assertThat(testItemStock.getCreatedOn()).isEqualTo(DEFAULT_CREATED_ON);
        assertThat(testItemStock.getSdsfile()).isEqualTo(DEFAULT_SDSFILE);

        // Validate the ItemStock in Elasticsearch
        verify(mockItemStockSearchRepository, times(1)).save(testItemStock);
    }

    @Test
    @Transactional
    public void createItemStockWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = itemStockRepository.findAll().size();

        // Create the ItemStock with an existing ID
        itemStock.setId(1L);
        ItemStockDTO itemStockDTO = itemStockMapper.toDto(itemStock);

        // An entity with an existing ID cannot be created, so this API call must fail
        restItemStockMockMvc.perform(post("/api/item-stocks")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(itemStockDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ItemStock in the database
        List<ItemStock> itemStockList = itemStockRepository.findAll();
        assertThat(itemStockList).hasSize(databaseSizeBeforeCreate);

        // Validate the ItemStock in Elasticsearch
        verify(mockItemStockSearchRepository, times(0)).save(itemStock);
    }


    @Test
    @Transactional
    public void getAllItemStocks() throws Exception {
        // Initialize the database
        itemStockRepository.saveAndFlush(itemStock);

        // Get all the itemStockList
        restItemStockMockMvc.perform(get("/api/item-stocks?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(itemStock.getId().intValue())))
            .andExpect(jsonPath("$.[*].totalQuantity").value(hasItem(DEFAULT_TOTAL_QUANTITY)))
            .andExpect(jsonPath("$.[*].minimumQuantity").value(hasItem(DEFAULT_MINIMUM_QUANTITY)))
            .andExpect(jsonPath("$.[*].itemStatus").value(hasItem(DEFAULT_ITEM_STATUS.toString())))
            .andExpect(jsonPath("$.[*].stockStore").value(hasItem(DEFAULT_STOCK_STORE.toString())))
            .andExpect(jsonPath("$.[*].entryDate").value(hasItem(DEFAULT_ENTRY_DATE.toString())))
            .andExpect(jsonPath("$.[*].expiryDate").value(hasItem(DEFAULT_EXPIRY_DATE.toString())))
            .andExpect(jsonPath("$.[*].creatorId").value(hasItem(DEFAULT_CREATOR_ID)))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())))
            .andExpect(jsonPath("$.[*].sdsfile").value(hasItem(DEFAULT_SDSFILE)));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllItemStocksWithEagerRelationshipsIsEnabled() throws Exception {
        when(itemStockServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restItemStockMockMvc.perform(get("/api/item-stocks?eagerload=true"))
            .andExpect(status().isOk());

        verify(itemStockServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllItemStocksWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(itemStockServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restItemStockMockMvc.perform(get("/api/item-stocks?eagerload=true"))
            .andExpect(status().isOk());

        verify(itemStockServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getItemStock() throws Exception {
        // Initialize the database
        itemStockRepository.saveAndFlush(itemStock);

        // Get the itemStock
        restItemStockMockMvc.perform(get("/api/item-stocks/{id}", itemStock.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(itemStock.getId().intValue()))
            .andExpect(jsonPath("$.totalQuantity").value(DEFAULT_TOTAL_QUANTITY))
            .andExpect(jsonPath("$.minimumQuantity").value(DEFAULT_MINIMUM_QUANTITY))
            .andExpect(jsonPath("$.itemStatus").value(DEFAULT_ITEM_STATUS.toString()))
            .andExpect(jsonPath("$.stockStore").value(DEFAULT_STOCK_STORE.toString()))
            .andExpect(jsonPath("$.entryDate").value(DEFAULT_ENTRY_DATE.toString()))
            .andExpect(jsonPath("$.expiryDate").value(DEFAULT_EXPIRY_DATE.toString()))
            .andExpect(jsonPath("$.creatorId").value(DEFAULT_CREATOR_ID))
            .andExpect(jsonPath("$.createdOn").value(DEFAULT_CREATED_ON.toString()))
            .andExpect(jsonPath("$.sdsfile").value(DEFAULT_SDSFILE));
    }


    @Test
    @Transactional
    public void getItemStocksByIdFiltering() throws Exception {
        // Initialize the database
        itemStockRepository.saveAndFlush(itemStock);

        Long id = itemStock.getId();

        defaultItemStockShouldBeFound("id.equals=" + id);
        defaultItemStockShouldNotBeFound("id.notEquals=" + id);

        defaultItemStockShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultItemStockShouldNotBeFound("id.greaterThan=" + id);

        defaultItemStockShouldBeFound("id.lessThanOrEqual=" + id);
        defaultItemStockShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllItemStocksByTotalQuantityIsEqualToSomething() throws Exception {
        // Initialize the database
        itemStockRepository.saveAndFlush(itemStock);

        // Get all the itemStockList where totalQuantity equals to DEFAULT_TOTAL_QUANTITY
        defaultItemStockShouldBeFound("totalQuantity.equals=" + DEFAULT_TOTAL_QUANTITY);

        // Get all the itemStockList where totalQuantity equals to UPDATED_TOTAL_QUANTITY
        defaultItemStockShouldNotBeFound("totalQuantity.equals=" + UPDATED_TOTAL_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllItemStocksByTotalQuantityIsNotEqualToSomething() throws Exception {
        // Initialize the database
        itemStockRepository.saveAndFlush(itemStock);

        // Get all the itemStockList where totalQuantity not equals to DEFAULT_TOTAL_QUANTITY
        defaultItemStockShouldNotBeFound("totalQuantity.notEquals=" + DEFAULT_TOTAL_QUANTITY);

        // Get all the itemStockList where totalQuantity not equals to UPDATED_TOTAL_QUANTITY
        defaultItemStockShouldBeFound("totalQuantity.notEquals=" + UPDATED_TOTAL_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllItemStocksByTotalQuantityIsInShouldWork() throws Exception {
        // Initialize the database
        itemStockRepository.saveAndFlush(itemStock);

        // Get all the itemStockList where totalQuantity in DEFAULT_TOTAL_QUANTITY or UPDATED_TOTAL_QUANTITY
        defaultItemStockShouldBeFound("totalQuantity.in=" + DEFAULT_TOTAL_QUANTITY + "," + UPDATED_TOTAL_QUANTITY);

        // Get all the itemStockList where totalQuantity equals to UPDATED_TOTAL_QUANTITY
        defaultItemStockShouldNotBeFound("totalQuantity.in=" + UPDATED_TOTAL_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllItemStocksByTotalQuantityIsNullOrNotNull() throws Exception {
        // Initialize the database
        itemStockRepository.saveAndFlush(itemStock);

        // Get all the itemStockList where totalQuantity is not null
        defaultItemStockShouldBeFound("totalQuantity.specified=true");

        // Get all the itemStockList where totalQuantity is null
        defaultItemStockShouldNotBeFound("totalQuantity.specified=false");
    }

    @Test
    @Transactional
    public void getAllItemStocksByTotalQuantityIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        itemStockRepository.saveAndFlush(itemStock);

        // Get all the itemStockList where totalQuantity is greater than or equal to DEFAULT_TOTAL_QUANTITY
        defaultItemStockShouldBeFound("totalQuantity.greaterThanOrEqual=" + DEFAULT_TOTAL_QUANTITY);

        // Get all the itemStockList where totalQuantity is greater than or equal to UPDATED_TOTAL_QUANTITY
        defaultItemStockShouldNotBeFound("totalQuantity.greaterThanOrEqual=" + UPDATED_TOTAL_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllItemStocksByTotalQuantityIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        itemStockRepository.saveAndFlush(itemStock);

        // Get all the itemStockList where totalQuantity is less than or equal to DEFAULT_TOTAL_QUANTITY
        defaultItemStockShouldBeFound("totalQuantity.lessThanOrEqual=" + DEFAULT_TOTAL_QUANTITY);

        // Get all the itemStockList where totalQuantity is less than or equal to SMALLER_TOTAL_QUANTITY
        defaultItemStockShouldNotBeFound("totalQuantity.lessThanOrEqual=" + SMALLER_TOTAL_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllItemStocksByTotalQuantityIsLessThanSomething() throws Exception {
        // Initialize the database
        itemStockRepository.saveAndFlush(itemStock);

        // Get all the itemStockList where totalQuantity is less than DEFAULT_TOTAL_QUANTITY
        defaultItemStockShouldNotBeFound("totalQuantity.lessThan=" + DEFAULT_TOTAL_QUANTITY);

        // Get all the itemStockList where totalQuantity is less than UPDATED_TOTAL_QUANTITY
        defaultItemStockShouldBeFound("totalQuantity.lessThan=" + UPDATED_TOTAL_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllItemStocksByTotalQuantityIsGreaterThanSomething() throws Exception {
        // Initialize the database
        itemStockRepository.saveAndFlush(itemStock);

        // Get all the itemStockList where totalQuantity is greater than DEFAULT_TOTAL_QUANTITY
        defaultItemStockShouldNotBeFound("totalQuantity.greaterThan=" + DEFAULT_TOTAL_QUANTITY);

        // Get all the itemStockList where totalQuantity is greater than SMALLER_TOTAL_QUANTITY
        defaultItemStockShouldBeFound("totalQuantity.greaterThan=" + SMALLER_TOTAL_QUANTITY);
    }


    @Test
    @Transactional
    public void getAllItemStocksByMinimumQuantityIsEqualToSomething() throws Exception {
        // Initialize the database
        itemStockRepository.saveAndFlush(itemStock);

        // Get all the itemStockList where minimumQuantity equals to DEFAULT_MINIMUM_QUANTITY
        defaultItemStockShouldBeFound("minimumQuantity.equals=" + DEFAULT_MINIMUM_QUANTITY);

        // Get all the itemStockList where minimumQuantity equals to UPDATED_MINIMUM_QUANTITY
        defaultItemStockShouldNotBeFound("minimumQuantity.equals=" + UPDATED_MINIMUM_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllItemStocksByMinimumQuantityIsNotEqualToSomething() throws Exception {
        // Initialize the database
        itemStockRepository.saveAndFlush(itemStock);

        // Get all the itemStockList where minimumQuantity not equals to DEFAULT_MINIMUM_QUANTITY
        defaultItemStockShouldNotBeFound("minimumQuantity.notEquals=" + DEFAULT_MINIMUM_QUANTITY);

        // Get all the itemStockList where minimumQuantity not equals to UPDATED_MINIMUM_QUANTITY
        defaultItemStockShouldBeFound("minimumQuantity.notEquals=" + UPDATED_MINIMUM_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllItemStocksByMinimumQuantityIsInShouldWork() throws Exception {
        // Initialize the database
        itemStockRepository.saveAndFlush(itemStock);

        // Get all the itemStockList where minimumQuantity in DEFAULT_MINIMUM_QUANTITY or UPDATED_MINIMUM_QUANTITY
        defaultItemStockShouldBeFound("minimumQuantity.in=" + DEFAULT_MINIMUM_QUANTITY + "," + UPDATED_MINIMUM_QUANTITY);

        // Get all the itemStockList where minimumQuantity equals to UPDATED_MINIMUM_QUANTITY
        defaultItemStockShouldNotBeFound("minimumQuantity.in=" + UPDATED_MINIMUM_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllItemStocksByMinimumQuantityIsNullOrNotNull() throws Exception {
        // Initialize the database
        itemStockRepository.saveAndFlush(itemStock);

        // Get all the itemStockList where minimumQuantity is not null
        defaultItemStockShouldBeFound("minimumQuantity.specified=true");

        // Get all the itemStockList where minimumQuantity is null
        defaultItemStockShouldNotBeFound("minimumQuantity.specified=false");
    }

    @Test
    @Transactional
    public void getAllItemStocksByMinimumQuantityIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        itemStockRepository.saveAndFlush(itemStock);

        // Get all the itemStockList where minimumQuantity is greater than or equal to DEFAULT_MINIMUM_QUANTITY
        defaultItemStockShouldBeFound("minimumQuantity.greaterThanOrEqual=" + DEFAULT_MINIMUM_QUANTITY);

        // Get all the itemStockList where minimumQuantity is greater than or equal to UPDATED_MINIMUM_QUANTITY
        defaultItemStockShouldNotBeFound("minimumQuantity.greaterThanOrEqual=" + UPDATED_MINIMUM_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllItemStocksByMinimumQuantityIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        itemStockRepository.saveAndFlush(itemStock);

        // Get all the itemStockList where minimumQuantity is less than or equal to DEFAULT_MINIMUM_QUANTITY
        defaultItemStockShouldBeFound("minimumQuantity.lessThanOrEqual=" + DEFAULT_MINIMUM_QUANTITY);

        // Get all the itemStockList where minimumQuantity is less than or equal to SMALLER_MINIMUM_QUANTITY
        defaultItemStockShouldNotBeFound("minimumQuantity.lessThanOrEqual=" + SMALLER_MINIMUM_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllItemStocksByMinimumQuantityIsLessThanSomething() throws Exception {
        // Initialize the database
        itemStockRepository.saveAndFlush(itemStock);

        // Get all the itemStockList where minimumQuantity is less than DEFAULT_MINIMUM_QUANTITY
        defaultItemStockShouldNotBeFound("minimumQuantity.lessThan=" + DEFAULT_MINIMUM_QUANTITY);

        // Get all the itemStockList where minimumQuantity is less than UPDATED_MINIMUM_QUANTITY
        defaultItemStockShouldBeFound("minimumQuantity.lessThan=" + UPDATED_MINIMUM_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllItemStocksByMinimumQuantityIsGreaterThanSomething() throws Exception {
        // Initialize the database
        itemStockRepository.saveAndFlush(itemStock);

        // Get all the itemStockList where minimumQuantity is greater than DEFAULT_MINIMUM_QUANTITY
        defaultItemStockShouldNotBeFound("minimumQuantity.greaterThan=" + DEFAULT_MINIMUM_QUANTITY);

        // Get all the itemStockList where minimumQuantity is greater than SMALLER_MINIMUM_QUANTITY
        defaultItemStockShouldBeFound("minimumQuantity.greaterThan=" + SMALLER_MINIMUM_QUANTITY);
    }


    @Test
    @Transactional
    public void getAllItemStocksByItemStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        itemStockRepository.saveAndFlush(itemStock);

        // Get all the itemStockList where itemStatus equals to DEFAULT_ITEM_STATUS
        defaultItemStockShouldBeFound("itemStatus.equals=" + DEFAULT_ITEM_STATUS);

        // Get all the itemStockList where itemStatus equals to UPDATED_ITEM_STATUS
        defaultItemStockShouldNotBeFound("itemStatus.equals=" + UPDATED_ITEM_STATUS);
    }

    @Test
    @Transactional
    public void getAllItemStocksByItemStatusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        itemStockRepository.saveAndFlush(itemStock);

        // Get all the itemStockList where itemStatus not equals to DEFAULT_ITEM_STATUS
        defaultItemStockShouldNotBeFound("itemStatus.notEquals=" + DEFAULT_ITEM_STATUS);

        // Get all the itemStockList where itemStatus not equals to UPDATED_ITEM_STATUS
        defaultItemStockShouldBeFound("itemStatus.notEquals=" + UPDATED_ITEM_STATUS);
    }

    @Test
    @Transactional
    public void getAllItemStocksByItemStatusIsInShouldWork() throws Exception {
        // Initialize the database
        itemStockRepository.saveAndFlush(itemStock);

        // Get all the itemStockList where itemStatus in DEFAULT_ITEM_STATUS or UPDATED_ITEM_STATUS
        defaultItemStockShouldBeFound("itemStatus.in=" + DEFAULT_ITEM_STATUS + "," + UPDATED_ITEM_STATUS);

        // Get all the itemStockList where itemStatus equals to UPDATED_ITEM_STATUS
        defaultItemStockShouldNotBeFound("itemStatus.in=" + UPDATED_ITEM_STATUS);
    }

    @Test
    @Transactional
    public void getAllItemStocksByItemStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        itemStockRepository.saveAndFlush(itemStock);

        // Get all the itemStockList where itemStatus is not null
        defaultItemStockShouldBeFound("itemStatus.specified=true");

        // Get all the itemStockList where itemStatus is null
        defaultItemStockShouldNotBeFound("itemStatus.specified=false");
    }

    @Test
    @Transactional
    public void getAllItemStocksByStockStoreIsEqualToSomething() throws Exception {
        // Initialize the database
        itemStockRepository.saveAndFlush(itemStock);

        // Get all the itemStockList where stockStore equals to DEFAULT_STOCK_STORE
        defaultItemStockShouldBeFound("stockStore.equals=" + DEFAULT_STOCK_STORE);

        // Get all the itemStockList where stockStore equals to UPDATED_STOCK_STORE
        defaultItemStockShouldNotBeFound("stockStore.equals=" + UPDATED_STOCK_STORE);
    }

    @Test
    @Transactional
    public void getAllItemStocksByStockStoreIsNotEqualToSomething() throws Exception {
        // Initialize the database
        itemStockRepository.saveAndFlush(itemStock);

        // Get all the itemStockList where stockStore not equals to DEFAULT_STOCK_STORE
        defaultItemStockShouldNotBeFound("stockStore.notEquals=" + DEFAULT_STOCK_STORE);

        // Get all the itemStockList where stockStore not equals to UPDATED_STOCK_STORE
        defaultItemStockShouldBeFound("stockStore.notEquals=" + UPDATED_STOCK_STORE);
    }

    @Test
    @Transactional
    public void getAllItemStocksByStockStoreIsInShouldWork() throws Exception {
        // Initialize the database
        itemStockRepository.saveAndFlush(itemStock);

        // Get all the itemStockList where stockStore in DEFAULT_STOCK_STORE or UPDATED_STOCK_STORE
        defaultItemStockShouldBeFound("stockStore.in=" + DEFAULT_STOCK_STORE + "," + UPDATED_STOCK_STORE);

        // Get all the itemStockList where stockStore equals to UPDATED_STOCK_STORE
        defaultItemStockShouldNotBeFound("stockStore.in=" + UPDATED_STOCK_STORE);
    }

    @Test
    @Transactional
    public void getAllItemStocksByStockStoreIsNullOrNotNull() throws Exception {
        // Initialize the database
        itemStockRepository.saveAndFlush(itemStock);

        // Get all the itemStockList where stockStore is not null
        defaultItemStockShouldBeFound("stockStore.specified=true");

        // Get all the itemStockList where stockStore is null
        defaultItemStockShouldNotBeFound("stockStore.specified=false");
    }

    @Test
    @Transactional
    public void getAllItemStocksByEntryDateIsEqualToSomething() throws Exception {
        // Initialize the database
        itemStockRepository.saveAndFlush(itemStock);

        // Get all the itemStockList where entryDate equals to DEFAULT_ENTRY_DATE
        defaultItemStockShouldBeFound("entryDate.equals=" + DEFAULT_ENTRY_DATE);

        // Get all the itemStockList where entryDate equals to UPDATED_ENTRY_DATE
        defaultItemStockShouldNotBeFound("entryDate.equals=" + UPDATED_ENTRY_DATE);
    }

    @Test
    @Transactional
    public void getAllItemStocksByEntryDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        itemStockRepository.saveAndFlush(itemStock);

        // Get all the itemStockList where entryDate not equals to DEFAULT_ENTRY_DATE
        defaultItemStockShouldNotBeFound("entryDate.notEquals=" + DEFAULT_ENTRY_DATE);

        // Get all the itemStockList where entryDate not equals to UPDATED_ENTRY_DATE
        defaultItemStockShouldBeFound("entryDate.notEquals=" + UPDATED_ENTRY_DATE);
    }

    @Test
    @Transactional
    public void getAllItemStocksByEntryDateIsInShouldWork() throws Exception {
        // Initialize the database
        itemStockRepository.saveAndFlush(itemStock);

        // Get all the itemStockList where entryDate in DEFAULT_ENTRY_DATE or UPDATED_ENTRY_DATE
        defaultItemStockShouldBeFound("entryDate.in=" + DEFAULT_ENTRY_DATE + "," + UPDATED_ENTRY_DATE);

        // Get all the itemStockList where entryDate equals to UPDATED_ENTRY_DATE
        defaultItemStockShouldNotBeFound("entryDate.in=" + UPDATED_ENTRY_DATE);
    }

    @Test
    @Transactional
    public void getAllItemStocksByEntryDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        itemStockRepository.saveAndFlush(itemStock);

        // Get all the itemStockList where entryDate is not null
        defaultItemStockShouldBeFound("entryDate.specified=true");

        // Get all the itemStockList where entryDate is null
        defaultItemStockShouldNotBeFound("entryDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllItemStocksByEntryDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        itemStockRepository.saveAndFlush(itemStock);

        // Get all the itemStockList where entryDate is greater than or equal to DEFAULT_ENTRY_DATE
        defaultItemStockShouldBeFound("entryDate.greaterThanOrEqual=" + DEFAULT_ENTRY_DATE);

        // Get all the itemStockList where entryDate is greater than or equal to UPDATED_ENTRY_DATE
        defaultItemStockShouldNotBeFound("entryDate.greaterThanOrEqual=" + UPDATED_ENTRY_DATE);
    }

    @Test
    @Transactional
    public void getAllItemStocksByEntryDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        itemStockRepository.saveAndFlush(itemStock);

        // Get all the itemStockList where entryDate is less than or equal to DEFAULT_ENTRY_DATE
        defaultItemStockShouldBeFound("entryDate.lessThanOrEqual=" + DEFAULT_ENTRY_DATE);

        // Get all the itemStockList where entryDate is less than or equal to SMALLER_ENTRY_DATE
        defaultItemStockShouldNotBeFound("entryDate.lessThanOrEqual=" + SMALLER_ENTRY_DATE);
    }

    @Test
    @Transactional
    public void getAllItemStocksByEntryDateIsLessThanSomething() throws Exception {
        // Initialize the database
        itemStockRepository.saveAndFlush(itemStock);

        // Get all the itemStockList where entryDate is less than DEFAULT_ENTRY_DATE
        defaultItemStockShouldNotBeFound("entryDate.lessThan=" + DEFAULT_ENTRY_DATE);

        // Get all the itemStockList where entryDate is less than UPDATED_ENTRY_DATE
        defaultItemStockShouldBeFound("entryDate.lessThan=" + UPDATED_ENTRY_DATE);
    }

    @Test
    @Transactional
    public void getAllItemStocksByEntryDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        itemStockRepository.saveAndFlush(itemStock);

        // Get all the itemStockList where entryDate is greater than DEFAULT_ENTRY_DATE
        defaultItemStockShouldNotBeFound("entryDate.greaterThan=" + DEFAULT_ENTRY_DATE);

        // Get all the itemStockList where entryDate is greater than SMALLER_ENTRY_DATE
        defaultItemStockShouldBeFound("entryDate.greaterThan=" + SMALLER_ENTRY_DATE);
    }


    @Test
    @Transactional
    public void getAllItemStocksByExpiryDateIsEqualToSomething() throws Exception {
        // Initialize the database
        itemStockRepository.saveAndFlush(itemStock);

        // Get all the itemStockList where expiryDate equals to DEFAULT_EXPIRY_DATE
        defaultItemStockShouldBeFound("expiryDate.equals=" + DEFAULT_EXPIRY_DATE);

        // Get all the itemStockList where expiryDate equals to UPDATED_EXPIRY_DATE
        defaultItemStockShouldNotBeFound("expiryDate.equals=" + UPDATED_EXPIRY_DATE);
    }

    @Test
    @Transactional
    public void getAllItemStocksByExpiryDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        itemStockRepository.saveAndFlush(itemStock);

        // Get all the itemStockList where expiryDate not equals to DEFAULT_EXPIRY_DATE
        defaultItemStockShouldNotBeFound("expiryDate.notEquals=" + DEFAULT_EXPIRY_DATE);

        // Get all the itemStockList where expiryDate not equals to UPDATED_EXPIRY_DATE
        defaultItemStockShouldBeFound("expiryDate.notEquals=" + UPDATED_EXPIRY_DATE);
    }

    @Test
    @Transactional
    public void getAllItemStocksByExpiryDateIsInShouldWork() throws Exception {
        // Initialize the database
        itemStockRepository.saveAndFlush(itemStock);

        // Get all the itemStockList where expiryDate in DEFAULT_EXPIRY_DATE or UPDATED_EXPIRY_DATE
        defaultItemStockShouldBeFound("expiryDate.in=" + DEFAULT_EXPIRY_DATE + "," + UPDATED_EXPIRY_DATE);

        // Get all the itemStockList where expiryDate equals to UPDATED_EXPIRY_DATE
        defaultItemStockShouldNotBeFound("expiryDate.in=" + UPDATED_EXPIRY_DATE);
    }

    @Test
    @Transactional
    public void getAllItemStocksByExpiryDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        itemStockRepository.saveAndFlush(itemStock);

        // Get all the itemStockList where expiryDate is not null
        defaultItemStockShouldBeFound("expiryDate.specified=true");

        // Get all the itemStockList where expiryDate is null
        defaultItemStockShouldNotBeFound("expiryDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllItemStocksByExpiryDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        itemStockRepository.saveAndFlush(itemStock);

        // Get all the itemStockList where expiryDate is greater than or equal to DEFAULT_EXPIRY_DATE
        defaultItemStockShouldBeFound("expiryDate.greaterThanOrEqual=" + DEFAULT_EXPIRY_DATE);

        // Get all the itemStockList where expiryDate is greater than or equal to UPDATED_EXPIRY_DATE
        defaultItemStockShouldNotBeFound("expiryDate.greaterThanOrEqual=" + UPDATED_EXPIRY_DATE);
    }

    @Test
    @Transactional
    public void getAllItemStocksByExpiryDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        itemStockRepository.saveAndFlush(itemStock);

        // Get all the itemStockList where expiryDate is less than or equal to DEFAULT_EXPIRY_DATE
        defaultItemStockShouldBeFound("expiryDate.lessThanOrEqual=" + DEFAULT_EXPIRY_DATE);

        // Get all the itemStockList where expiryDate is less than or equal to SMALLER_EXPIRY_DATE
        defaultItemStockShouldNotBeFound("expiryDate.lessThanOrEqual=" + SMALLER_EXPIRY_DATE);
    }

    @Test
    @Transactional
    public void getAllItemStocksByExpiryDateIsLessThanSomething() throws Exception {
        // Initialize the database
        itemStockRepository.saveAndFlush(itemStock);

        // Get all the itemStockList where expiryDate is less than DEFAULT_EXPIRY_DATE
        defaultItemStockShouldNotBeFound("expiryDate.lessThan=" + DEFAULT_EXPIRY_DATE);

        // Get all the itemStockList where expiryDate is less than UPDATED_EXPIRY_DATE
        defaultItemStockShouldBeFound("expiryDate.lessThan=" + UPDATED_EXPIRY_DATE);
    }

    @Test
    @Transactional
    public void getAllItemStocksByExpiryDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        itemStockRepository.saveAndFlush(itemStock);

        // Get all the itemStockList where expiryDate is greater than DEFAULT_EXPIRY_DATE
        defaultItemStockShouldNotBeFound("expiryDate.greaterThan=" + DEFAULT_EXPIRY_DATE);

        // Get all the itemStockList where expiryDate is greater than SMALLER_EXPIRY_DATE
        defaultItemStockShouldBeFound("expiryDate.greaterThan=" + SMALLER_EXPIRY_DATE);
    }


    @Test
    @Transactional
    public void getAllItemStocksByCreatorIdIsEqualToSomething() throws Exception {
        // Initialize the database
        itemStockRepository.saveAndFlush(itemStock);

        // Get all the itemStockList where creatorId equals to DEFAULT_CREATOR_ID
        defaultItemStockShouldBeFound("creatorId.equals=" + DEFAULT_CREATOR_ID);

        // Get all the itemStockList where creatorId equals to UPDATED_CREATOR_ID
        defaultItemStockShouldNotBeFound("creatorId.equals=" + UPDATED_CREATOR_ID);
    }

    @Test
    @Transactional
    public void getAllItemStocksByCreatorIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        itemStockRepository.saveAndFlush(itemStock);

        // Get all the itemStockList where creatorId not equals to DEFAULT_CREATOR_ID
        defaultItemStockShouldNotBeFound("creatorId.notEquals=" + DEFAULT_CREATOR_ID);

        // Get all the itemStockList where creatorId not equals to UPDATED_CREATOR_ID
        defaultItemStockShouldBeFound("creatorId.notEquals=" + UPDATED_CREATOR_ID);
    }

    @Test
    @Transactional
    public void getAllItemStocksByCreatorIdIsInShouldWork() throws Exception {
        // Initialize the database
        itemStockRepository.saveAndFlush(itemStock);

        // Get all the itemStockList where creatorId in DEFAULT_CREATOR_ID or UPDATED_CREATOR_ID
        defaultItemStockShouldBeFound("creatorId.in=" + DEFAULT_CREATOR_ID + "," + UPDATED_CREATOR_ID);

        // Get all the itemStockList where creatorId equals to UPDATED_CREATOR_ID
        defaultItemStockShouldNotBeFound("creatorId.in=" + UPDATED_CREATOR_ID);
    }

    @Test
    @Transactional
    public void getAllItemStocksByCreatorIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        itemStockRepository.saveAndFlush(itemStock);

        // Get all the itemStockList where creatorId is not null
        defaultItemStockShouldBeFound("creatorId.specified=true");

        // Get all the itemStockList where creatorId is null
        defaultItemStockShouldNotBeFound("creatorId.specified=false");
    }

    @Test
    @Transactional
    public void getAllItemStocksByCreatorIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        itemStockRepository.saveAndFlush(itemStock);

        // Get all the itemStockList where creatorId is greater than or equal to DEFAULT_CREATOR_ID
        defaultItemStockShouldBeFound("creatorId.greaterThanOrEqual=" + DEFAULT_CREATOR_ID);

        // Get all the itemStockList where creatorId is greater than or equal to UPDATED_CREATOR_ID
        defaultItemStockShouldNotBeFound("creatorId.greaterThanOrEqual=" + UPDATED_CREATOR_ID);
    }

    @Test
    @Transactional
    public void getAllItemStocksByCreatorIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        itemStockRepository.saveAndFlush(itemStock);

        // Get all the itemStockList where creatorId is less than or equal to DEFAULT_CREATOR_ID
        defaultItemStockShouldBeFound("creatorId.lessThanOrEqual=" + DEFAULT_CREATOR_ID);

        // Get all the itemStockList where creatorId is less than or equal to SMALLER_CREATOR_ID
        defaultItemStockShouldNotBeFound("creatorId.lessThanOrEqual=" + SMALLER_CREATOR_ID);
    }

    @Test
    @Transactional
    public void getAllItemStocksByCreatorIdIsLessThanSomething() throws Exception {
        // Initialize the database
        itemStockRepository.saveAndFlush(itemStock);

        // Get all the itemStockList where creatorId is less than DEFAULT_CREATOR_ID
        defaultItemStockShouldNotBeFound("creatorId.lessThan=" + DEFAULT_CREATOR_ID);

        // Get all the itemStockList where creatorId is less than UPDATED_CREATOR_ID
        defaultItemStockShouldBeFound("creatorId.lessThan=" + UPDATED_CREATOR_ID);
    }

    @Test
    @Transactional
    public void getAllItemStocksByCreatorIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        itemStockRepository.saveAndFlush(itemStock);

        // Get all the itemStockList where creatorId is greater than DEFAULT_CREATOR_ID
        defaultItemStockShouldNotBeFound("creatorId.greaterThan=" + DEFAULT_CREATOR_ID);

        // Get all the itemStockList where creatorId is greater than SMALLER_CREATOR_ID
        defaultItemStockShouldBeFound("creatorId.greaterThan=" + SMALLER_CREATOR_ID);
    }


    @Test
    @Transactional
    public void getAllItemStocksByCreatedOnIsEqualToSomething() throws Exception {
        // Initialize the database
        itemStockRepository.saveAndFlush(itemStock);

        // Get all the itemStockList where createdOn equals to DEFAULT_CREATED_ON
        defaultItemStockShouldBeFound("createdOn.equals=" + DEFAULT_CREATED_ON);

        // Get all the itemStockList where createdOn equals to UPDATED_CREATED_ON
        defaultItemStockShouldNotBeFound("createdOn.equals=" + UPDATED_CREATED_ON);
    }

    @Test
    @Transactional
    public void getAllItemStocksByCreatedOnIsNotEqualToSomething() throws Exception {
        // Initialize the database
        itemStockRepository.saveAndFlush(itemStock);

        // Get all the itemStockList where createdOn not equals to DEFAULT_CREATED_ON
        defaultItemStockShouldNotBeFound("createdOn.notEquals=" + DEFAULT_CREATED_ON);

        // Get all the itemStockList where createdOn not equals to UPDATED_CREATED_ON
        defaultItemStockShouldBeFound("createdOn.notEquals=" + UPDATED_CREATED_ON);
    }

    @Test
    @Transactional
    public void getAllItemStocksByCreatedOnIsInShouldWork() throws Exception {
        // Initialize the database
        itemStockRepository.saveAndFlush(itemStock);

        // Get all the itemStockList where createdOn in DEFAULT_CREATED_ON or UPDATED_CREATED_ON
        defaultItemStockShouldBeFound("createdOn.in=" + DEFAULT_CREATED_ON + "," + UPDATED_CREATED_ON);

        // Get all the itemStockList where createdOn equals to UPDATED_CREATED_ON
        defaultItemStockShouldNotBeFound("createdOn.in=" + UPDATED_CREATED_ON);
    }

    @Test
    @Transactional
    public void getAllItemStocksByCreatedOnIsNullOrNotNull() throws Exception {
        // Initialize the database
        itemStockRepository.saveAndFlush(itemStock);

        // Get all the itemStockList where createdOn is not null
        defaultItemStockShouldBeFound("createdOn.specified=true");

        // Get all the itemStockList where createdOn is null
        defaultItemStockShouldNotBeFound("createdOn.specified=false");
    }

    @Test
    @Transactional
    public void getAllItemStocksBySdsfileIsEqualToSomething() throws Exception {
        // Initialize the database
        itemStockRepository.saveAndFlush(itemStock);

        // Get all the itemStockList where sdsfile equals to DEFAULT_SDSFILE
        defaultItemStockShouldBeFound("sdsfile.equals=" + DEFAULT_SDSFILE);

        // Get all the itemStockList where sdsfile equals to UPDATED_SDSFILE
        defaultItemStockShouldNotBeFound("sdsfile.equals=" + UPDATED_SDSFILE);
    }

    @Test
    @Transactional
    public void getAllItemStocksBySdsfileIsNotEqualToSomething() throws Exception {
        // Initialize the database
        itemStockRepository.saveAndFlush(itemStock);

        // Get all the itemStockList where sdsfile not equals to DEFAULT_SDSFILE
        defaultItemStockShouldNotBeFound("sdsfile.notEquals=" + DEFAULT_SDSFILE);

        // Get all the itemStockList where sdsfile not equals to UPDATED_SDSFILE
        defaultItemStockShouldBeFound("sdsfile.notEquals=" + UPDATED_SDSFILE);
    }

    @Test
    @Transactional
    public void getAllItemStocksBySdsfileIsInShouldWork() throws Exception {
        // Initialize the database
        itemStockRepository.saveAndFlush(itemStock);

        // Get all the itemStockList where sdsfile in DEFAULT_SDSFILE or UPDATED_SDSFILE
        defaultItemStockShouldBeFound("sdsfile.in=" + DEFAULT_SDSFILE + "," + UPDATED_SDSFILE);

        // Get all the itemStockList where sdsfile equals to UPDATED_SDSFILE
        defaultItemStockShouldNotBeFound("sdsfile.in=" + UPDATED_SDSFILE);
    }

    @Test
    @Transactional
    public void getAllItemStocksBySdsfileIsNullOrNotNull() throws Exception {
        // Initialize the database
        itemStockRepository.saveAndFlush(itemStock);

        // Get all the itemStockList where sdsfile is not null
        defaultItemStockShouldBeFound("sdsfile.specified=true");

        // Get all the itemStockList where sdsfile is null
        defaultItemStockShouldNotBeFound("sdsfile.specified=false");
    }
                @Test
    @Transactional
    public void getAllItemStocksBySdsfileContainsSomething() throws Exception {
        // Initialize the database
        itemStockRepository.saveAndFlush(itemStock);

        // Get all the itemStockList where sdsfile contains DEFAULT_SDSFILE
        defaultItemStockShouldBeFound("sdsfile.contains=" + DEFAULT_SDSFILE);

        // Get all the itemStockList where sdsfile contains UPDATED_SDSFILE
        defaultItemStockShouldNotBeFound("sdsfile.contains=" + UPDATED_SDSFILE);
    }

    @Test
    @Transactional
    public void getAllItemStocksBySdsfileNotContainsSomething() throws Exception {
        // Initialize the database
        itemStockRepository.saveAndFlush(itemStock);

        // Get all the itemStockList where sdsfile does not contain DEFAULT_SDSFILE
        defaultItemStockShouldNotBeFound("sdsfile.doesNotContain=" + DEFAULT_SDSFILE);

        // Get all the itemStockList where sdsfile does not contain UPDATED_SDSFILE
        defaultItemStockShouldBeFound("sdsfile.doesNotContain=" + UPDATED_SDSFILE);
    }


    @Test
    @Transactional
    public void getAllItemStocksByItemTransactionIsEqualToSomething() throws Exception {
        // Initialize the database
        itemStockRepository.saveAndFlush(itemStock);
        ItemTransaction itemTransaction = ItemTransactionResourceIT.createEntity(em);
        em.persist(itemTransaction);
        em.flush();
        itemStock.addItemTransaction(itemTransaction);
        itemStockRepository.saveAndFlush(itemStock);
        Long itemTransactionId = itemTransaction.getId();

        // Get all the itemStockList where itemTransaction equals to itemTransactionId
        defaultItemStockShouldBeFound("itemTransactionId.equals=" + itemTransactionId);

        // Get all the itemStockList where itemTransaction equals to itemTransactionId + 1
        defaultItemStockShouldNotBeFound("itemTransactionId.equals=" + (itemTransactionId + 1));
    }


    @Test
    @Transactional
    public void getAllItemStocksByInvStorageIsEqualToSomething() throws Exception {
        // Get already existing entity
        InvStorage invStorage = itemStock.getInvStorage();
        itemStockRepository.saveAndFlush(itemStock);
        Long invStorageId = invStorage.getId();

        // Get all the itemStockList where invStorage equals to invStorageId
        defaultItemStockShouldBeFound("invStorageId.equals=" + invStorageId);

        // Get all the itemStockList where invStorage equals to invStorageId + 1
        defaultItemStockShouldNotBeFound("invStorageId.equals=" + (invStorageId + 1));
    }


    @Test
    @Transactional
    public void getAllItemStocksByStorageUnitIsEqualToSomething() throws Exception {
        // Get already existing entity
        MeasUnit storageUnit = itemStock.getStorageUnit();
        itemStockRepository.saveAndFlush(itemStock);
        Long storageUnitId = storageUnit.getId();

        // Get all the itemStockList where storageUnit equals to storageUnitId
        defaultItemStockShouldBeFound("storageUnitId.equals=" + storageUnitId);

        // Get all the itemStockList where storageUnit equals to storageUnitId + 1
        defaultItemStockShouldNotBeFound("storageUnitId.equals=" + (storageUnitId + 1));
    }


    @Test
    @Transactional
    public void getAllItemStocksByItemIsEqualToSomething() throws Exception {
        // Get already existing entity
        Item item = itemStock.getItem();
        itemStockRepository.saveAndFlush(itemStock);
        Long itemId = item.getId();

        // Get all the itemStockList where item equals to itemId
        defaultItemStockShouldBeFound("itemId.equals=" + itemId);

        // Get all the itemStockList where item equals to itemId + 1
        defaultItemStockShouldNotBeFound("itemId.equals=" + (itemId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultItemStockShouldBeFound(String filter) throws Exception {
        restItemStockMockMvc.perform(get("/api/item-stocks?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(itemStock.getId().intValue())))
            .andExpect(jsonPath("$.[*].totalQuantity").value(hasItem(DEFAULT_TOTAL_QUANTITY)))
            .andExpect(jsonPath("$.[*].minimumQuantity").value(hasItem(DEFAULT_MINIMUM_QUANTITY)))
            .andExpect(jsonPath("$.[*].itemStatus").value(hasItem(DEFAULT_ITEM_STATUS.toString())))
            .andExpect(jsonPath("$.[*].stockStore").value(hasItem(DEFAULT_STOCK_STORE.toString())))
            .andExpect(jsonPath("$.[*].entryDate").value(hasItem(DEFAULT_ENTRY_DATE.toString())))
            .andExpect(jsonPath("$.[*].expiryDate").value(hasItem(DEFAULT_EXPIRY_DATE.toString())))
            .andExpect(jsonPath("$.[*].creatorId").value(hasItem(DEFAULT_CREATOR_ID)))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())))
            .andExpect(jsonPath("$.[*].sdsfile").value(hasItem(DEFAULT_SDSFILE)));

        // Check, that the count call also returns 1
        restItemStockMockMvc.perform(get("/api/item-stocks/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultItemStockShouldNotBeFound(String filter) throws Exception {
        restItemStockMockMvc.perform(get("/api/item-stocks?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restItemStockMockMvc.perform(get("/api/item-stocks/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingItemStock() throws Exception {
        // Get the itemStock
        restItemStockMockMvc.perform(get("/api/item-stocks/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateItemStock() throws Exception {
        // Initialize the database
        itemStockRepository.saveAndFlush(itemStock);

        int databaseSizeBeforeUpdate = itemStockRepository.findAll().size();

        // Update the itemStock
        ItemStock updatedItemStock = itemStockRepository.findById(itemStock.getId()).get();
        // Disconnect from session so that the updates on updatedItemStock are not directly saved in db
        em.detach(updatedItemStock);
        updatedItemStock
            .totalQuantity(UPDATED_TOTAL_QUANTITY)
            .minimumQuantity(UPDATED_MINIMUM_QUANTITY)
            .itemStatus(UPDATED_ITEM_STATUS)
            .stockStore(UPDATED_STOCK_STORE)
            .entryDate(UPDATED_ENTRY_DATE)
            .expiryDate(UPDATED_EXPIRY_DATE)
            .creatorId(UPDATED_CREATOR_ID)
            .createdOn(UPDATED_CREATED_ON)
            .sdsfile(UPDATED_SDSFILE);
        ItemStockDTO itemStockDTO = itemStockMapper.toDto(updatedItemStock);

        restItemStockMockMvc.perform(put("/api/item-stocks")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(itemStockDTO)))
            .andExpect(status().isOk());

        // Validate the ItemStock in the database
        List<ItemStock> itemStockList = itemStockRepository.findAll();
        assertThat(itemStockList).hasSize(databaseSizeBeforeUpdate);
        ItemStock testItemStock = itemStockList.get(itemStockList.size() - 1);
        assertThat(testItemStock.getTotalQuantity()).isEqualTo(UPDATED_TOTAL_QUANTITY);
        assertThat(testItemStock.getMinimumQuantity()).isEqualTo(UPDATED_MINIMUM_QUANTITY);
        assertThat(testItemStock.getItemStatus()).isEqualTo(UPDATED_ITEM_STATUS);
        assertThat(testItemStock.getStockStore()).isEqualTo(UPDATED_STOCK_STORE);
        assertThat(testItemStock.getEntryDate()).isEqualTo(UPDATED_ENTRY_DATE);
        assertThat(testItemStock.getExpiryDate()).isEqualTo(UPDATED_EXPIRY_DATE);
        assertThat(testItemStock.getCreatorId()).isEqualTo(UPDATED_CREATOR_ID);
        assertThat(testItemStock.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
        assertThat(testItemStock.getSdsfile()).isEqualTo(UPDATED_SDSFILE);

        // Validate the ItemStock in Elasticsearch
        verify(mockItemStockSearchRepository, times(1)).save(testItemStock);
    }

    @Test
    @Transactional
    public void updateNonExistingItemStock() throws Exception {
        int databaseSizeBeforeUpdate = itemStockRepository.findAll().size();

        // Create the ItemStock
        ItemStockDTO itemStockDTO = itemStockMapper.toDto(itemStock);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restItemStockMockMvc.perform(put("/api/item-stocks")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(itemStockDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ItemStock in the database
        List<ItemStock> itemStockList = itemStockRepository.findAll();
        assertThat(itemStockList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ItemStock in Elasticsearch
        verify(mockItemStockSearchRepository, times(0)).save(itemStock);
    }

    @Test
    @Transactional
    public void deleteItemStock() throws Exception {
        // Initialize the database
        itemStockRepository.saveAndFlush(itemStock);

        int databaseSizeBeforeDelete = itemStockRepository.findAll().size();

        // Delete the itemStock
        restItemStockMockMvc.perform(delete("/api/item-stocks/{id}", itemStock.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ItemStock> itemStockList = itemStockRepository.findAll();
        assertThat(itemStockList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the ItemStock in Elasticsearch
        verify(mockItemStockSearchRepository, times(1)).deleteById(itemStock.getId());
    }

    @Test
    @Transactional
    public void searchItemStock() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        itemStockRepository.saveAndFlush(itemStock);
        when(mockItemStockSearchRepository.search(queryStringQuery("id:" + itemStock.getId())))
            .thenReturn(Collections.singletonList(itemStock));

        // Search the itemStock
        restItemStockMockMvc.perform(get("/api/_search/item-stocks?query=id:" + itemStock.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(itemStock.getId().intValue())))
            .andExpect(jsonPath("$.[*].totalQuantity").value(hasItem(DEFAULT_TOTAL_QUANTITY)))
            .andExpect(jsonPath("$.[*].minimumQuantity").value(hasItem(DEFAULT_MINIMUM_QUANTITY)))
            .andExpect(jsonPath("$.[*].itemStatus").value(hasItem(DEFAULT_ITEM_STATUS.toString())))
            .andExpect(jsonPath("$.[*].stockStore").value(hasItem(DEFAULT_STOCK_STORE.toString())))
            .andExpect(jsonPath("$.[*].entryDate").value(hasItem(DEFAULT_ENTRY_DATE.toString())))
            .andExpect(jsonPath("$.[*].expiryDate").value(hasItem(DEFAULT_EXPIRY_DATE.toString())))
            .andExpect(jsonPath("$.[*].creatorId").value(hasItem(DEFAULT_CREATOR_ID)))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())))
            .andExpect(jsonPath("$.[*].sdsfile").value(hasItem(DEFAULT_SDSFILE)));
    }
}
