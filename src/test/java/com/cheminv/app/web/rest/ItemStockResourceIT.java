package com.cheminv.app.web.rest;

import com.cheminv.app.CimsApp;
import com.cheminv.app.domain.ItemStock;
import com.cheminv.app.domain.ItemTransaction;
import com.cheminv.app.domain.WasteItem;
import com.cheminv.app.domain.HazardCode;
import com.cheminv.app.domain.InvStorage;
import com.cheminv.app.domain.MeasUnit;
import com.cheminv.app.domain.Order;
import com.cheminv.app.repository.ItemStockRepository;
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
import org.springframework.util.Base64Utils;
import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.cheminv.app.domain.enumeration.ItemStatus;
/**
 * Integration tests for the {@link ItemStockResource} REST controller.
 */
@SpringBootTest(classes = CimsApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class ItemStockResourceIT {

    private static final String DEFAULT_ITEM_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ITEM_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CAS_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_CAS_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_STOCK_BOOK_FOLIO = "AAAAAAAAAA";
    private static final String UPDATED_STOCK_BOOK_FOLIO = "BBBBBBBBBB";

    private static final String DEFAULT_ITEM_MANUFACTURER = "AAAAAAAAAA";
    private static final String UPDATED_ITEM_MANUFACTURER = "BBBBBBBBBB";

    private static final Float DEFAULT_ITEM_CAPACITY = 1F;
    private static final Float UPDATED_ITEM_CAPACITY = 2F;
    private static final Float SMALLER_ITEM_CAPACITY = 1F - 1F;

    private static final Float DEFAULT_UNIT_PRICE = 1F;
    private static final Float UPDATED_UNIT_PRICE = 2F;
    private static final Float SMALLER_UNIT_PRICE = 1F - 1F;

    private static final Float DEFAULT_TOTAL_QUANTITY = 1F;
    private static final Float UPDATED_TOTAL_QUANTITY = 2F;
    private static final Float SMALLER_TOTAL_QUANTITY = 1F - 1F;

    private static final Float DEFAULT_MINIMUM_QUANTITY = 1F;
    private static final Float UPDATED_MINIMUM_QUANTITY = 2F;
    private static final Float SMALLER_MINIMUM_QUANTITY = 1F - 1F;

    private static final ItemStatus DEFAULT_ITEM_STATUS = ItemStatus.NEW;
    private static final ItemStatus UPDATED_ITEM_STATUS = ItemStatus.USED;

    private static final Instant DEFAULT_CREATED_ON = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_ON = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_LAST_UPDATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_UPDATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final byte[] DEFAULT_SDSFILE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_SDSFILE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_SDSFILE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_SDSFILE_CONTENT_TYPE = "image/png";

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
            .itemName(DEFAULT_ITEM_NAME)
            .casNumber(DEFAULT_CAS_NUMBER)
            .stockBookFolio(DEFAULT_STOCK_BOOK_FOLIO)
            .itemManufacturer(DEFAULT_ITEM_MANUFACTURER)
            .itemCapacity(DEFAULT_ITEM_CAPACITY)
            .unitPrice(DEFAULT_UNIT_PRICE)
            .totalQuantity(DEFAULT_TOTAL_QUANTITY)
            .minimumQuantity(DEFAULT_MINIMUM_QUANTITY)
            .itemStatus(DEFAULT_ITEM_STATUS)
            .createdOn(DEFAULT_CREATED_ON)
            .lastUpdated(DEFAULT_LAST_UPDATED)
            .sdsfile(DEFAULT_SDSFILE)
            .sdsfileContentType(DEFAULT_SDSFILE_CONTENT_TYPE);
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
            .itemName(UPDATED_ITEM_NAME)
            .casNumber(UPDATED_CAS_NUMBER)
            .stockBookFolio(UPDATED_STOCK_BOOK_FOLIO)
            .itemManufacturer(UPDATED_ITEM_MANUFACTURER)
            .itemCapacity(UPDATED_ITEM_CAPACITY)
            .unitPrice(UPDATED_UNIT_PRICE)
            .totalQuantity(UPDATED_TOTAL_QUANTITY)
            .minimumQuantity(UPDATED_MINIMUM_QUANTITY)
            .itemStatus(UPDATED_ITEM_STATUS)
            .createdOn(UPDATED_CREATED_ON)
            .lastUpdated(UPDATED_LAST_UPDATED)
            .sdsfile(UPDATED_SDSFILE)
            .sdsfileContentType(UPDATED_SDSFILE_CONTENT_TYPE);
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
        assertThat(testItemStock.getItemName()).isEqualTo(DEFAULT_ITEM_NAME);
        assertThat(testItemStock.getCasNumber()).isEqualTo(DEFAULT_CAS_NUMBER);
        assertThat(testItemStock.getStockBookFolio()).isEqualTo(DEFAULT_STOCK_BOOK_FOLIO);
        assertThat(testItemStock.getItemManufacturer()).isEqualTo(DEFAULT_ITEM_MANUFACTURER);
        assertThat(testItemStock.getItemCapacity()).isEqualTo(DEFAULT_ITEM_CAPACITY);
        assertThat(testItemStock.getUnitPrice()).isEqualTo(DEFAULT_UNIT_PRICE);
        assertThat(testItemStock.getTotalQuantity()).isEqualTo(DEFAULT_TOTAL_QUANTITY);
        assertThat(testItemStock.getMinimumQuantity()).isEqualTo(DEFAULT_MINIMUM_QUANTITY);
        assertThat(testItemStock.getItemStatus()).isEqualTo(DEFAULT_ITEM_STATUS);
        assertThat(testItemStock.getCreatedOn()).isEqualTo(DEFAULT_CREATED_ON);
        assertThat(testItemStock.getLastUpdated()).isEqualTo(DEFAULT_LAST_UPDATED);
        assertThat(testItemStock.getSdsfile()).isEqualTo(DEFAULT_SDSFILE);
        assertThat(testItemStock.getSdsfileContentType()).isEqualTo(DEFAULT_SDSFILE_CONTENT_TYPE);
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
    }


    @Test
    @Transactional
    public void checkItemNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = itemStockRepository.findAll().size();
        // set the field null
        itemStock.setItemName(null);

        // Create the ItemStock, which fails.
        ItemStockDTO itemStockDTO = itemStockMapper.toDto(itemStock);


        restItemStockMockMvc.perform(post("/api/item-stocks")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(itemStockDTO)))
            .andExpect(status().isBadRequest());

        List<ItemStock> itemStockList = itemStockRepository.findAll();
        assertThat(itemStockList).hasSize(databaseSizeBeforeTest);
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
            .andExpect(jsonPath("$.[*].itemName").value(hasItem(DEFAULT_ITEM_NAME)))
            .andExpect(jsonPath("$.[*].casNumber").value(hasItem(DEFAULT_CAS_NUMBER)))
            .andExpect(jsonPath("$.[*].stockBookFolio").value(hasItem(DEFAULT_STOCK_BOOK_FOLIO)))
            .andExpect(jsonPath("$.[*].itemManufacturer").value(hasItem(DEFAULT_ITEM_MANUFACTURER)))
            .andExpect(jsonPath("$.[*].itemCapacity").value(hasItem(DEFAULT_ITEM_CAPACITY.doubleValue())))
            .andExpect(jsonPath("$.[*].unitPrice").value(hasItem(DEFAULT_UNIT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].totalQuantity").value(hasItem(DEFAULT_TOTAL_QUANTITY.doubleValue())))
            .andExpect(jsonPath("$.[*].minimumQuantity").value(hasItem(DEFAULT_MINIMUM_QUANTITY.doubleValue())))
            .andExpect(jsonPath("$.[*].itemStatus").value(hasItem(DEFAULT_ITEM_STATUS.toString())))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())))
            .andExpect(jsonPath("$.[*].lastUpdated").value(hasItem(DEFAULT_LAST_UPDATED.toString())))
            .andExpect(jsonPath("$.[*].sdsfileContentType").value(hasItem(DEFAULT_SDSFILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].sdsfile").value(hasItem(Base64Utils.encodeToString(DEFAULT_SDSFILE))));
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
            .andExpect(jsonPath("$.itemName").value(DEFAULT_ITEM_NAME))
            .andExpect(jsonPath("$.casNumber").value(DEFAULT_CAS_NUMBER))
            .andExpect(jsonPath("$.stockBookFolio").value(DEFAULT_STOCK_BOOK_FOLIO))
            .andExpect(jsonPath("$.itemManufacturer").value(DEFAULT_ITEM_MANUFACTURER))
            .andExpect(jsonPath("$.itemCapacity").value(DEFAULT_ITEM_CAPACITY.doubleValue()))
            .andExpect(jsonPath("$.unitPrice").value(DEFAULT_UNIT_PRICE.doubleValue()))
            .andExpect(jsonPath("$.totalQuantity").value(DEFAULT_TOTAL_QUANTITY.doubleValue()))
            .andExpect(jsonPath("$.minimumQuantity").value(DEFAULT_MINIMUM_QUANTITY.doubleValue()))
            .andExpect(jsonPath("$.itemStatus").value(DEFAULT_ITEM_STATUS.toString()))
            .andExpect(jsonPath("$.createdOn").value(DEFAULT_CREATED_ON.toString()))
            .andExpect(jsonPath("$.lastUpdated").value(DEFAULT_LAST_UPDATED.toString()))
            .andExpect(jsonPath("$.sdsfileContentType").value(DEFAULT_SDSFILE_CONTENT_TYPE))
            .andExpect(jsonPath("$.sdsfile").value(Base64Utils.encodeToString(DEFAULT_SDSFILE)));
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
    public void getAllItemStocksByItemNameIsEqualToSomething() throws Exception {
        // Initialize the database
        itemStockRepository.saveAndFlush(itemStock);

        // Get all the itemStockList where itemName equals to DEFAULT_ITEM_NAME
        defaultItemStockShouldBeFound("itemName.equals=" + DEFAULT_ITEM_NAME);

        // Get all the itemStockList where itemName equals to UPDATED_ITEM_NAME
        defaultItemStockShouldNotBeFound("itemName.equals=" + UPDATED_ITEM_NAME);
    }

    @Test
    @Transactional
    public void getAllItemStocksByItemNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        itemStockRepository.saveAndFlush(itemStock);

        // Get all the itemStockList where itemName not equals to DEFAULT_ITEM_NAME
        defaultItemStockShouldNotBeFound("itemName.notEquals=" + DEFAULT_ITEM_NAME);

        // Get all the itemStockList where itemName not equals to UPDATED_ITEM_NAME
        defaultItemStockShouldBeFound("itemName.notEquals=" + UPDATED_ITEM_NAME);
    }

    @Test
    @Transactional
    public void getAllItemStocksByItemNameIsInShouldWork() throws Exception {
        // Initialize the database
        itemStockRepository.saveAndFlush(itemStock);

        // Get all the itemStockList where itemName in DEFAULT_ITEM_NAME or UPDATED_ITEM_NAME
        defaultItemStockShouldBeFound("itemName.in=" + DEFAULT_ITEM_NAME + "," + UPDATED_ITEM_NAME);

        // Get all the itemStockList where itemName equals to UPDATED_ITEM_NAME
        defaultItemStockShouldNotBeFound("itemName.in=" + UPDATED_ITEM_NAME);
    }

    @Test
    @Transactional
    public void getAllItemStocksByItemNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        itemStockRepository.saveAndFlush(itemStock);

        // Get all the itemStockList where itemName is not null
        defaultItemStockShouldBeFound("itemName.specified=true");

        // Get all the itemStockList where itemName is null
        defaultItemStockShouldNotBeFound("itemName.specified=false");
    }
                @Test
    @Transactional
    public void getAllItemStocksByItemNameContainsSomething() throws Exception {
        // Initialize the database
        itemStockRepository.saveAndFlush(itemStock);

        // Get all the itemStockList where itemName contains DEFAULT_ITEM_NAME
        defaultItemStockShouldBeFound("itemName.contains=" + DEFAULT_ITEM_NAME);

        // Get all the itemStockList where itemName contains UPDATED_ITEM_NAME
        defaultItemStockShouldNotBeFound("itemName.contains=" + UPDATED_ITEM_NAME);
    }

    @Test
    @Transactional
    public void getAllItemStocksByItemNameNotContainsSomething() throws Exception {
        // Initialize the database
        itemStockRepository.saveAndFlush(itemStock);

        // Get all the itemStockList where itemName does not contain DEFAULT_ITEM_NAME
        defaultItemStockShouldNotBeFound("itemName.doesNotContain=" + DEFAULT_ITEM_NAME);

        // Get all the itemStockList where itemName does not contain UPDATED_ITEM_NAME
        defaultItemStockShouldBeFound("itemName.doesNotContain=" + UPDATED_ITEM_NAME);
    }


    @Test
    @Transactional
    public void getAllItemStocksByCasNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        itemStockRepository.saveAndFlush(itemStock);

        // Get all the itemStockList where casNumber equals to DEFAULT_CAS_NUMBER
        defaultItemStockShouldBeFound("casNumber.equals=" + DEFAULT_CAS_NUMBER);

        // Get all the itemStockList where casNumber equals to UPDATED_CAS_NUMBER
        defaultItemStockShouldNotBeFound("casNumber.equals=" + UPDATED_CAS_NUMBER);
    }

    @Test
    @Transactional
    public void getAllItemStocksByCasNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        itemStockRepository.saveAndFlush(itemStock);

        // Get all the itemStockList where casNumber not equals to DEFAULT_CAS_NUMBER
        defaultItemStockShouldNotBeFound("casNumber.notEquals=" + DEFAULT_CAS_NUMBER);

        // Get all the itemStockList where casNumber not equals to UPDATED_CAS_NUMBER
        defaultItemStockShouldBeFound("casNumber.notEquals=" + UPDATED_CAS_NUMBER);
    }

    @Test
    @Transactional
    public void getAllItemStocksByCasNumberIsInShouldWork() throws Exception {
        // Initialize the database
        itemStockRepository.saveAndFlush(itemStock);

        // Get all the itemStockList where casNumber in DEFAULT_CAS_NUMBER or UPDATED_CAS_NUMBER
        defaultItemStockShouldBeFound("casNumber.in=" + DEFAULT_CAS_NUMBER + "," + UPDATED_CAS_NUMBER);

        // Get all the itemStockList where casNumber equals to UPDATED_CAS_NUMBER
        defaultItemStockShouldNotBeFound("casNumber.in=" + UPDATED_CAS_NUMBER);
    }

    @Test
    @Transactional
    public void getAllItemStocksByCasNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        itemStockRepository.saveAndFlush(itemStock);

        // Get all the itemStockList where casNumber is not null
        defaultItemStockShouldBeFound("casNumber.specified=true");

        // Get all the itemStockList where casNumber is null
        defaultItemStockShouldNotBeFound("casNumber.specified=false");
    }
                @Test
    @Transactional
    public void getAllItemStocksByCasNumberContainsSomething() throws Exception {
        // Initialize the database
        itemStockRepository.saveAndFlush(itemStock);

        // Get all the itemStockList where casNumber contains DEFAULT_CAS_NUMBER
        defaultItemStockShouldBeFound("casNumber.contains=" + DEFAULT_CAS_NUMBER);

        // Get all the itemStockList where casNumber contains UPDATED_CAS_NUMBER
        defaultItemStockShouldNotBeFound("casNumber.contains=" + UPDATED_CAS_NUMBER);
    }

    @Test
    @Transactional
    public void getAllItemStocksByCasNumberNotContainsSomething() throws Exception {
        // Initialize the database
        itemStockRepository.saveAndFlush(itemStock);

        // Get all the itemStockList where casNumber does not contain DEFAULT_CAS_NUMBER
        defaultItemStockShouldNotBeFound("casNumber.doesNotContain=" + DEFAULT_CAS_NUMBER);

        // Get all the itemStockList where casNumber does not contain UPDATED_CAS_NUMBER
        defaultItemStockShouldBeFound("casNumber.doesNotContain=" + UPDATED_CAS_NUMBER);
    }


    @Test
    @Transactional
    public void getAllItemStocksByStockBookFolioIsEqualToSomething() throws Exception {
        // Initialize the database
        itemStockRepository.saveAndFlush(itemStock);

        // Get all the itemStockList where stockBookFolio equals to DEFAULT_STOCK_BOOK_FOLIO
        defaultItemStockShouldBeFound("stockBookFolio.equals=" + DEFAULT_STOCK_BOOK_FOLIO);

        // Get all the itemStockList where stockBookFolio equals to UPDATED_STOCK_BOOK_FOLIO
        defaultItemStockShouldNotBeFound("stockBookFolio.equals=" + UPDATED_STOCK_BOOK_FOLIO);
    }

    @Test
    @Transactional
    public void getAllItemStocksByStockBookFolioIsNotEqualToSomething() throws Exception {
        // Initialize the database
        itemStockRepository.saveAndFlush(itemStock);

        // Get all the itemStockList where stockBookFolio not equals to DEFAULT_STOCK_BOOK_FOLIO
        defaultItemStockShouldNotBeFound("stockBookFolio.notEquals=" + DEFAULT_STOCK_BOOK_FOLIO);

        // Get all the itemStockList where stockBookFolio not equals to UPDATED_STOCK_BOOK_FOLIO
        defaultItemStockShouldBeFound("stockBookFolio.notEquals=" + UPDATED_STOCK_BOOK_FOLIO);
    }

    @Test
    @Transactional
    public void getAllItemStocksByStockBookFolioIsInShouldWork() throws Exception {
        // Initialize the database
        itemStockRepository.saveAndFlush(itemStock);

        // Get all the itemStockList where stockBookFolio in DEFAULT_STOCK_BOOK_FOLIO or UPDATED_STOCK_BOOK_FOLIO
        defaultItemStockShouldBeFound("stockBookFolio.in=" + DEFAULT_STOCK_BOOK_FOLIO + "," + UPDATED_STOCK_BOOK_FOLIO);

        // Get all the itemStockList where stockBookFolio equals to UPDATED_STOCK_BOOK_FOLIO
        defaultItemStockShouldNotBeFound("stockBookFolio.in=" + UPDATED_STOCK_BOOK_FOLIO);
    }

    @Test
    @Transactional
    public void getAllItemStocksByStockBookFolioIsNullOrNotNull() throws Exception {
        // Initialize the database
        itemStockRepository.saveAndFlush(itemStock);

        // Get all the itemStockList where stockBookFolio is not null
        defaultItemStockShouldBeFound("stockBookFolio.specified=true");

        // Get all the itemStockList where stockBookFolio is null
        defaultItemStockShouldNotBeFound("stockBookFolio.specified=false");
    }
                @Test
    @Transactional
    public void getAllItemStocksByStockBookFolioContainsSomething() throws Exception {
        // Initialize the database
        itemStockRepository.saveAndFlush(itemStock);

        // Get all the itemStockList where stockBookFolio contains DEFAULT_STOCK_BOOK_FOLIO
        defaultItemStockShouldBeFound("stockBookFolio.contains=" + DEFAULT_STOCK_BOOK_FOLIO);

        // Get all the itemStockList where stockBookFolio contains UPDATED_STOCK_BOOK_FOLIO
        defaultItemStockShouldNotBeFound("stockBookFolio.contains=" + UPDATED_STOCK_BOOK_FOLIO);
    }

    @Test
    @Transactional
    public void getAllItemStocksByStockBookFolioNotContainsSomething() throws Exception {
        // Initialize the database
        itemStockRepository.saveAndFlush(itemStock);

        // Get all the itemStockList where stockBookFolio does not contain DEFAULT_STOCK_BOOK_FOLIO
        defaultItemStockShouldNotBeFound("stockBookFolio.doesNotContain=" + DEFAULT_STOCK_BOOK_FOLIO);

        // Get all the itemStockList where stockBookFolio does not contain UPDATED_STOCK_BOOK_FOLIO
        defaultItemStockShouldBeFound("stockBookFolio.doesNotContain=" + UPDATED_STOCK_BOOK_FOLIO);
    }


    @Test
    @Transactional
    public void getAllItemStocksByItemManufacturerIsEqualToSomething() throws Exception {
        // Initialize the database
        itemStockRepository.saveAndFlush(itemStock);

        // Get all the itemStockList where itemManufacturer equals to DEFAULT_ITEM_MANUFACTURER
        defaultItemStockShouldBeFound("itemManufacturer.equals=" + DEFAULT_ITEM_MANUFACTURER);

        // Get all the itemStockList where itemManufacturer equals to UPDATED_ITEM_MANUFACTURER
        defaultItemStockShouldNotBeFound("itemManufacturer.equals=" + UPDATED_ITEM_MANUFACTURER);
    }

    @Test
    @Transactional
    public void getAllItemStocksByItemManufacturerIsNotEqualToSomething() throws Exception {
        // Initialize the database
        itemStockRepository.saveAndFlush(itemStock);

        // Get all the itemStockList where itemManufacturer not equals to DEFAULT_ITEM_MANUFACTURER
        defaultItemStockShouldNotBeFound("itemManufacturer.notEquals=" + DEFAULT_ITEM_MANUFACTURER);

        // Get all the itemStockList where itemManufacturer not equals to UPDATED_ITEM_MANUFACTURER
        defaultItemStockShouldBeFound("itemManufacturer.notEquals=" + UPDATED_ITEM_MANUFACTURER);
    }

    @Test
    @Transactional
    public void getAllItemStocksByItemManufacturerIsInShouldWork() throws Exception {
        // Initialize the database
        itemStockRepository.saveAndFlush(itemStock);

        // Get all the itemStockList where itemManufacturer in DEFAULT_ITEM_MANUFACTURER or UPDATED_ITEM_MANUFACTURER
        defaultItemStockShouldBeFound("itemManufacturer.in=" + DEFAULT_ITEM_MANUFACTURER + "," + UPDATED_ITEM_MANUFACTURER);

        // Get all the itemStockList where itemManufacturer equals to UPDATED_ITEM_MANUFACTURER
        defaultItemStockShouldNotBeFound("itemManufacturer.in=" + UPDATED_ITEM_MANUFACTURER);
    }

    @Test
    @Transactional
    public void getAllItemStocksByItemManufacturerIsNullOrNotNull() throws Exception {
        // Initialize the database
        itemStockRepository.saveAndFlush(itemStock);

        // Get all the itemStockList where itemManufacturer is not null
        defaultItemStockShouldBeFound("itemManufacturer.specified=true");

        // Get all the itemStockList where itemManufacturer is null
        defaultItemStockShouldNotBeFound("itemManufacturer.specified=false");
    }
                @Test
    @Transactional
    public void getAllItemStocksByItemManufacturerContainsSomething() throws Exception {
        // Initialize the database
        itemStockRepository.saveAndFlush(itemStock);

        // Get all the itemStockList where itemManufacturer contains DEFAULT_ITEM_MANUFACTURER
        defaultItemStockShouldBeFound("itemManufacturer.contains=" + DEFAULT_ITEM_MANUFACTURER);

        // Get all the itemStockList where itemManufacturer contains UPDATED_ITEM_MANUFACTURER
        defaultItemStockShouldNotBeFound("itemManufacturer.contains=" + UPDATED_ITEM_MANUFACTURER);
    }

    @Test
    @Transactional
    public void getAllItemStocksByItemManufacturerNotContainsSomething() throws Exception {
        // Initialize the database
        itemStockRepository.saveAndFlush(itemStock);

        // Get all the itemStockList where itemManufacturer does not contain DEFAULT_ITEM_MANUFACTURER
        defaultItemStockShouldNotBeFound("itemManufacturer.doesNotContain=" + DEFAULT_ITEM_MANUFACTURER);

        // Get all the itemStockList where itemManufacturer does not contain UPDATED_ITEM_MANUFACTURER
        defaultItemStockShouldBeFound("itemManufacturer.doesNotContain=" + UPDATED_ITEM_MANUFACTURER);
    }


    @Test
    @Transactional
    public void getAllItemStocksByItemCapacityIsEqualToSomething() throws Exception {
        // Initialize the database
        itemStockRepository.saveAndFlush(itemStock);

        // Get all the itemStockList where itemCapacity equals to DEFAULT_ITEM_CAPACITY
        defaultItemStockShouldBeFound("itemCapacity.equals=" + DEFAULT_ITEM_CAPACITY);

        // Get all the itemStockList where itemCapacity equals to UPDATED_ITEM_CAPACITY
        defaultItemStockShouldNotBeFound("itemCapacity.equals=" + UPDATED_ITEM_CAPACITY);
    }

    @Test
    @Transactional
    public void getAllItemStocksByItemCapacityIsNotEqualToSomething() throws Exception {
        // Initialize the database
        itemStockRepository.saveAndFlush(itemStock);

        // Get all the itemStockList where itemCapacity not equals to DEFAULT_ITEM_CAPACITY
        defaultItemStockShouldNotBeFound("itemCapacity.notEquals=" + DEFAULT_ITEM_CAPACITY);

        // Get all the itemStockList where itemCapacity not equals to UPDATED_ITEM_CAPACITY
        defaultItemStockShouldBeFound("itemCapacity.notEquals=" + UPDATED_ITEM_CAPACITY);
    }

    @Test
    @Transactional
    public void getAllItemStocksByItemCapacityIsInShouldWork() throws Exception {
        // Initialize the database
        itemStockRepository.saveAndFlush(itemStock);

        // Get all the itemStockList where itemCapacity in DEFAULT_ITEM_CAPACITY or UPDATED_ITEM_CAPACITY
        defaultItemStockShouldBeFound("itemCapacity.in=" + DEFAULT_ITEM_CAPACITY + "," + UPDATED_ITEM_CAPACITY);

        // Get all the itemStockList where itemCapacity equals to UPDATED_ITEM_CAPACITY
        defaultItemStockShouldNotBeFound("itemCapacity.in=" + UPDATED_ITEM_CAPACITY);
    }

    @Test
    @Transactional
    public void getAllItemStocksByItemCapacityIsNullOrNotNull() throws Exception {
        // Initialize the database
        itemStockRepository.saveAndFlush(itemStock);

        // Get all the itemStockList where itemCapacity is not null
        defaultItemStockShouldBeFound("itemCapacity.specified=true");

        // Get all the itemStockList where itemCapacity is null
        defaultItemStockShouldNotBeFound("itemCapacity.specified=false");
    }

    @Test
    @Transactional
    public void getAllItemStocksByItemCapacityIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        itemStockRepository.saveAndFlush(itemStock);

        // Get all the itemStockList where itemCapacity is greater than or equal to DEFAULT_ITEM_CAPACITY
        defaultItemStockShouldBeFound("itemCapacity.greaterThanOrEqual=" + DEFAULT_ITEM_CAPACITY);

        // Get all the itemStockList where itemCapacity is greater than or equal to UPDATED_ITEM_CAPACITY
        defaultItemStockShouldNotBeFound("itemCapacity.greaterThanOrEqual=" + UPDATED_ITEM_CAPACITY);
    }

    @Test
    @Transactional
    public void getAllItemStocksByItemCapacityIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        itemStockRepository.saveAndFlush(itemStock);

        // Get all the itemStockList where itemCapacity is less than or equal to DEFAULT_ITEM_CAPACITY
        defaultItemStockShouldBeFound("itemCapacity.lessThanOrEqual=" + DEFAULT_ITEM_CAPACITY);

        // Get all the itemStockList where itemCapacity is less than or equal to SMALLER_ITEM_CAPACITY
        defaultItemStockShouldNotBeFound("itemCapacity.lessThanOrEqual=" + SMALLER_ITEM_CAPACITY);
    }

    @Test
    @Transactional
    public void getAllItemStocksByItemCapacityIsLessThanSomething() throws Exception {
        // Initialize the database
        itemStockRepository.saveAndFlush(itemStock);

        // Get all the itemStockList where itemCapacity is less than DEFAULT_ITEM_CAPACITY
        defaultItemStockShouldNotBeFound("itemCapacity.lessThan=" + DEFAULT_ITEM_CAPACITY);

        // Get all the itemStockList where itemCapacity is less than UPDATED_ITEM_CAPACITY
        defaultItemStockShouldBeFound("itemCapacity.lessThan=" + UPDATED_ITEM_CAPACITY);
    }

    @Test
    @Transactional
    public void getAllItemStocksByItemCapacityIsGreaterThanSomething() throws Exception {
        // Initialize the database
        itemStockRepository.saveAndFlush(itemStock);

        // Get all the itemStockList where itemCapacity is greater than DEFAULT_ITEM_CAPACITY
        defaultItemStockShouldNotBeFound("itemCapacity.greaterThan=" + DEFAULT_ITEM_CAPACITY);

        // Get all the itemStockList where itemCapacity is greater than SMALLER_ITEM_CAPACITY
        defaultItemStockShouldBeFound("itemCapacity.greaterThan=" + SMALLER_ITEM_CAPACITY);
    }


    @Test
    @Transactional
    public void getAllItemStocksByUnitPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        itemStockRepository.saveAndFlush(itemStock);

        // Get all the itemStockList where unitPrice equals to DEFAULT_UNIT_PRICE
        defaultItemStockShouldBeFound("unitPrice.equals=" + DEFAULT_UNIT_PRICE);

        // Get all the itemStockList where unitPrice equals to UPDATED_UNIT_PRICE
        defaultItemStockShouldNotBeFound("unitPrice.equals=" + UPDATED_UNIT_PRICE);
    }

    @Test
    @Transactional
    public void getAllItemStocksByUnitPriceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        itemStockRepository.saveAndFlush(itemStock);

        // Get all the itemStockList where unitPrice not equals to DEFAULT_UNIT_PRICE
        defaultItemStockShouldNotBeFound("unitPrice.notEquals=" + DEFAULT_UNIT_PRICE);

        // Get all the itemStockList where unitPrice not equals to UPDATED_UNIT_PRICE
        defaultItemStockShouldBeFound("unitPrice.notEquals=" + UPDATED_UNIT_PRICE);
    }

    @Test
    @Transactional
    public void getAllItemStocksByUnitPriceIsInShouldWork() throws Exception {
        // Initialize the database
        itemStockRepository.saveAndFlush(itemStock);

        // Get all the itemStockList where unitPrice in DEFAULT_UNIT_PRICE or UPDATED_UNIT_PRICE
        defaultItemStockShouldBeFound("unitPrice.in=" + DEFAULT_UNIT_PRICE + "," + UPDATED_UNIT_PRICE);

        // Get all the itemStockList where unitPrice equals to UPDATED_UNIT_PRICE
        defaultItemStockShouldNotBeFound("unitPrice.in=" + UPDATED_UNIT_PRICE);
    }

    @Test
    @Transactional
    public void getAllItemStocksByUnitPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        itemStockRepository.saveAndFlush(itemStock);

        // Get all the itemStockList where unitPrice is not null
        defaultItemStockShouldBeFound("unitPrice.specified=true");

        // Get all the itemStockList where unitPrice is null
        defaultItemStockShouldNotBeFound("unitPrice.specified=false");
    }

    @Test
    @Transactional
    public void getAllItemStocksByUnitPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        itemStockRepository.saveAndFlush(itemStock);

        // Get all the itemStockList where unitPrice is greater than or equal to DEFAULT_UNIT_PRICE
        defaultItemStockShouldBeFound("unitPrice.greaterThanOrEqual=" + DEFAULT_UNIT_PRICE);

        // Get all the itemStockList where unitPrice is greater than or equal to UPDATED_UNIT_PRICE
        defaultItemStockShouldNotBeFound("unitPrice.greaterThanOrEqual=" + UPDATED_UNIT_PRICE);
    }

    @Test
    @Transactional
    public void getAllItemStocksByUnitPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        itemStockRepository.saveAndFlush(itemStock);

        // Get all the itemStockList where unitPrice is less than or equal to DEFAULT_UNIT_PRICE
        defaultItemStockShouldBeFound("unitPrice.lessThanOrEqual=" + DEFAULT_UNIT_PRICE);

        // Get all the itemStockList where unitPrice is less than or equal to SMALLER_UNIT_PRICE
        defaultItemStockShouldNotBeFound("unitPrice.lessThanOrEqual=" + SMALLER_UNIT_PRICE);
    }

    @Test
    @Transactional
    public void getAllItemStocksByUnitPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        itemStockRepository.saveAndFlush(itemStock);

        // Get all the itemStockList where unitPrice is less than DEFAULT_UNIT_PRICE
        defaultItemStockShouldNotBeFound("unitPrice.lessThan=" + DEFAULT_UNIT_PRICE);

        // Get all the itemStockList where unitPrice is less than UPDATED_UNIT_PRICE
        defaultItemStockShouldBeFound("unitPrice.lessThan=" + UPDATED_UNIT_PRICE);
    }

    @Test
    @Transactional
    public void getAllItemStocksByUnitPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        itemStockRepository.saveAndFlush(itemStock);

        // Get all the itemStockList where unitPrice is greater than DEFAULT_UNIT_PRICE
        defaultItemStockShouldNotBeFound("unitPrice.greaterThan=" + DEFAULT_UNIT_PRICE);

        // Get all the itemStockList where unitPrice is greater than SMALLER_UNIT_PRICE
        defaultItemStockShouldBeFound("unitPrice.greaterThan=" + SMALLER_UNIT_PRICE);
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
    public void getAllItemStocksByLastUpdatedIsEqualToSomething() throws Exception {
        // Initialize the database
        itemStockRepository.saveAndFlush(itemStock);

        // Get all the itemStockList where lastUpdated equals to DEFAULT_LAST_UPDATED
        defaultItemStockShouldBeFound("lastUpdated.equals=" + DEFAULT_LAST_UPDATED);

        // Get all the itemStockList where lastUpdated equals to UPDATED_LAST_UPDATED
        defaultItemStockShouldNotBeFound("lastUpdated.equals=" + UPDATED_LAST_UPDATED);
    }

    @Test
    @Transactional
    public void getAllItemStocksByLastUpdatedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        itemStockRepository.saveAndFlush(itemStock);

        // Get all the itemStockList where lastUpdated not equals to DEFAULT_LAST_UPDATED
        defaultItemStockShouldNotBeFound("lastUpdated.notEquals=" + DEFAULT_LAST_UPDATED);

        // Get all the itemStockList where lastUpdated not equals to UPDATED_LAST_UPDATED
        defaultItemStockShouldBeFound("lastUpdated.notEquals=" + UPDATED_LAST_UPDATED);
    }

    @Test
    @Transactional
    public void getAllItemStocksByLastUpdatedIsInShouldWork() throws Exception {
        // Initialize the database
        itemStockRepository.saveAndFlush(itemStock);

        // Get all the itemStockList where lastUpdated in DEFAULT_LAST_UPDATED or UPDATED_LAST_UPDATED
        defaultItemStockShouldBeFound("lastUpdated.in=" + DEFAULT_LAST_UPDATED + "," + UPDATED_LAST_UPDATED);

        // Get all the itemStockList where lastUpdated equals to UPDATED_LAST_UPDATED
        defaultItemStockShouldNotBeFound("lastUpdated.in=" + UPDATED_LAST_UPDATED);
    }

    @Test
    @Transactional
    public void getAllItemStocksByLastUpdatedIsNullOrNotNull() throws Exception {
        // Initialize the database
        itemStockRepository.saveAndFlush(itemStock);

        // Get all the itemStockList where lastUpdated is not null
        defaultItemStockShouldBeFound("lastUpdated.specified=true");

        // Get all the itemStockList where lastUpdated is null
        defaultItemStockShouldNotBeFound("lastUpdated.specified=false");
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
    public void getAllItemStocksByWasteItemIsEqualToSomething() throws Exception {
        // Initialize the database
        itemStockRepository.saveAndFlush(itemStock);
        WasteItem wasteItem = WasteItemResourceIT.createEntity(em);
        em.persist(wasteItem);
        em.flush();
        itemStock.addWasteItem(wasteItem);
        itemStockRepository.saveAndFlush(itemStock);
        Long wasteItemId = wasteItem.getId();

        // Get all the itemStockList where wasteItem equals to wasteItemId
        defaultItemStockShouldBeFound("wasteItemId.equals=" + wasteItemId);

        // Get all the itemStockList where wasteItem equals to wasteItemId + 1
        defaultItemStockShouldNotBeFound("wasteItemId.equals=" + (wasteItemId + 1));
    }


    @Test
    @Transactional
    public void getAllItemStocksByHazardCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        itemStockRepository.saveAndFlush(itemStock);
        HazardCode hazardCode = HazardCodeResourceIT.createEntity(em);
        em.persist(hazardCode);
        em.flush();
        itemStock.addHazardCode(hazardCode);
        itemStockRepository.saveAndFlush(itemStock);
        Long hazardCodeId = hazardCode.getId();

        // Get all the itemStockList where hazardCode equals to hazardCodeId
        defaultItemStockShouldBeFound("hazardCodeId.equals=" + hazardCodeId);

        // Get all the itemStockList where hazardCode equals to hazardCodeId + 1
        defaultItemStockShouldNotBeFound("hazardCodeId.equals=" + (hazardCodeId + 1));
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
    public void getAllItemStocksByItemOrdersIsEqualToSomething() throws Exception {
        // Initialize the database
        itemStockRepository.saveAndFlush(itemStock);
        Order itemOrders = OrderResourceIT.createEntity(em);
        em.persist(itemOrders);
        em.flush();
        itemStock.addItemOrders(itemOrders);
        itemStockRepository.saveAndFlush(itemStock);
        Long itemOrdersId = itemOrders.getId();

        // Get all the itemStockList where itemOrders equals to itemOrdersId
        defaultItemStockShouldBeFound("itemOrdersId.equals=" + itemOrdersId);

        // Get all the itemStockList where itemOrders equals to itemOrdersId + 1
        defaultItemStockShouldNotBeFound("itemOrdersId.equals=" + (itemOrdersId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultItemStockShouldBeFound(String filter) throws Exception {
        restItemStockMockMvc.perform(get("/api/item-stocks?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(itemStock.getId().intValue())))
            .andExpect(jsonPath("$.[*].itemName").value(hasItem(DEFAULT_ITEM_NAME)))
            .andExpect(jsonPath("$.[*].casNumber").value(hasItem(DEFAULT_CAS_NUMBER)))
            .andExpect(jsonPath("$.[*].stockBookFolio").value(hasItem(DEFAULT_STOCK_BOOK_FOLIO)))
            .andExpect(jsonPath("$.[*].itemManufacturer").value(hasItem(DEFAULT_ITEM_MANUFACTURER)))
            .andExpect(jsonPath("$.[*].itemCapacity").value(hasItem(DEFAULT_ITEM_CAPACITY.doubleValue())))
            .andExpect(jsonPath("$.[*].unitPrice").value(hasItem(DEFAULT_UNIT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].totalQuantity").value(hasItem(DEFAULT_TOTAL_QUANTITY.doubleValue())))
            .andExpect(jsonPath("$.[*].minimumQuantity").value(hasItem(DEFAULT_MINIMUM_QUANTITY.doubleValue())))
            .andExpect(jsonPath("$.[*].itemStatus").value(hasItem(DEFAULT_ITEM_STATUS.toString())))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())))
            .andExpect(jsonPath("$.[*].lastUpdated").value(hasItem(DEFAULT_LAST_UPDATED.toString())))
            .andExpect(jsonPath("$.[*].sdsfileContentType").value(hasItem(DEFAULT_SDSFILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].sdsfile").value(hasItem(Base64Utils.encodeToString(DEFAULT_SDSFILE))));

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
            .itemName(UPDATED_ITEM_NAME)
            .casNumber(UPDATED_CAS_NUMBER)
            .stockBookFolio(UPDATED_STOCK_BOOK_FOLIO)
            .itemManufacturer(UPDATED_ITEM_MANUFACTURER)
            .itemCapacity(UPDATED_ITEM_CAPACITY)
            .unitPrice(UPDATED_UNIT_PRICE)
            .totalQuantity(UPDATED_TOTAL_QUANTITY)
            .minimumQuantity(UPDATED_MINIMUM_QUANTITY)
            .itemStatus(UPDATED_ITEM_STATUS)
            .createdOn(UPDATED_CREATED_ON)
            .lastUpdated(UPDATED_LAST_UPDATED)
            .sdsfile(UPDATED_SDSFILE)
            .sdsfileContentType(UPDATED_SDSFILE_CONTENT_TYPE);
        ItemStockDTO itemStockDTO = itemStockMapper.toDto(updatedItemStock);

        restItemStockMockMvc.perform(put("/api/item-stocks")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(itemStockDTO)))
            .andExpect(status().isOk());

        // Validate the ItemStock in the database
        List<ItemStock> itemStockList = itemStockRepository.findAll();
        assertThat(itemStockList).hasSize(databaseSizeBeforeUpdate);
        ItemStock testItemStock = itemStockList.get(itemStockList.size() - 1);
        assertThat(testItemStock.getItemName()).isEqualTo(UPDATED_ITEM_NAME);
        assertThat(testItemStock.getCasNumber()).isEqualTo(UPDATED_CAS_NUMBER);
        assertThat(testItemStock.getStockBookFolio()).isEqualTo(UPDATED_STOCK_BOOK_FOLIO);
        assertThat(testItemStock.getItemManufacturer()).isEqualTo(UPDATED_ITEM_MANUFACTURER);
        assertThat(testItemStock.getItemCapacity()).isEqualTo(UPDATED_ITEM_CAPACITY);
        assertThat(testItemStock.getUnitPrice()).isEqualTo(UPDATED_UNIT_PRICE);
        assertThat(testItemStock.getTotalQuantity()).isEqualTo(UPDATED_TOTAL_QUANTITY);
        assertThat(testItemStock.getMinimumQuantity()).isEqualTo(UPDATED_MINIMUM_QUANTITY);
        assertThat(testItemStock.getItemStatus()).isEqualTo(UPDATED_ITEM_STATUS);
        assertThat(testItemStock.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
        assertThat(testItemStock.getLastUpdated()).isEqualTo(UPDATED_LAST_UPDATED);
        assertThat(testItemStock.getSdsfile()).isEqualTo(UPDATED_SDSFILE);
        assertThat(testItemStock.getSdsfileContentType()).isEqualTo(UPDATED_SDSFILE_CONTENT_TYPE);
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
    }
}
