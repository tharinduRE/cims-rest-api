package com.cheminv.app.web.rest;

import com.cheminv.app.CimsApp;
import com.cheminv.app.domain.ItemTransaction;
import com.cheminv.app.repository.ItemTransactionRepository;
import com.cheminv.app.repository.search.ItemTransactionSearchRepository;
import com.cheminv.app.service.ItemTransactionService;
import com.cheminv.app.service.dto.ItemTransactionDTO;
import com.cheminv.app.service.mapper.ItemTransactionMapper;

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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link ItemTransactionResource} REST controller.
 */
@SpringBootTest(classes = CimsApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class ItemTransactionResourceIT {

    private static final Integer DEFAULT_QUANTITY = 1;
    private static final Integer UPDATED_QUANTITY = 2;

    private static final String DEFAULT_REMARKS = "AAAAAAAAAA";
    private static final String UPDATED_REMARKS = "BBBBBBBBBB";

    private static final UUID DEFAULT_TRANSACTION_UUID = UUID.randomUUID();
    private static final UUID UPDATED_TRANSACTION_UUID = UUID.randomUUID();

    private static final LocalDate DEFAULT_TRANSACTION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_TRANSACTION_DATE = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private ItemTransactionRepository itemTransactionRepository;

    @Autowired
    private ItemTransactionMapper itemTransactionMapper;

    @Autowired
    private ItemTransactionService itemTransactionService;

    /**
     * This repository is mocked in the com.cheminv.app.repository.search test package.
     *
     * @see com.cheminv.app.repository.search.ItemTransactionSearchRepositoryMockConfiguration
     */
    @Autowired
    private ItemTransactionSearchRepository mockItemTransactionSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restItemTransactionMockMvc;

    private ItemTransaction itemTransaction;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ItemTransaction createEntity(EntityManager em) {
        ItemTransaction itemTransaction = new ItemTransaction()
            .quantity(DEFAULT_QUANTITY)
            .remarks(DEFAULT_REMARKS)
            .transactionUUID(DEFAULT_TRANSACTION_UUID)
            .transactionDate(DEFAULT_TRANSACTION_DATE);
        return itemTransaction;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ItemTransaction createUpdatedEntity(EntityManager em) {
        ItemTransaction itemTransaction = new ItemTransaction()
            .quantity(UPDATED_QUANTITY)
            .remarks(UPDATED_REMARKS)
            .transactionUUID(UPDATED_TRANSACTION_UUID)
            .transactionDate(UPDATED_TRANSACTION_DATE);
        return itemTransaction;
    }

    @BeforeEach
    public void initTest() {
        itemTransaction = createEntity(em);
    }

    @Test
    @Transactional
    public void createItemTransaction() throws Exception {
        int databaseSizeBeforeCreate = itemTransactionRepository.findAll().size();
        // Create the ItemTransaction
        ItemTransactionDTO itemTransactionDTO = itemTransactionMapper.toDto(itemTransaction);
        restItemTransactionMockMvc.perform(post("/api/item-transactions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(itemTransactionDTO)))
            .andExpect(status().isCreated());

        // Validate the ItemTransaction in the database
        List<ItemTransaction> itemTransactionList = itemTransactionRepository.findAll();
        assertThat(itemTransactionList).hasSize(databaseSizeBeforeCreate + 1);
        ItemTransaction testItemTransaction = itemTransactionList.get(itemTransactionList.size() - 1);
        assertThat(testItemTransaction.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
        assertThat(testItemTransaction.getRemarks()).isEqualTo(DEFAULT_REMARKS);
        assertThat(testItemTransaction.getTransactionUUID()).isEqualTo(DEFAULT_TRANSACTION_UUID);
        assertThat(testItemTransaction.getTransactionDate()).isEqualTo(DEFAULT_TRANSACTION_DATE);

        // Validate the ItemTransaction in Elasticsearch
        verify(mockItemTransactionSearchRepository, times(1)).save(testItemTransaction);
    }

    @Test
    @Transactional
    public void createItemTransactionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = itemTransactionRepository.findAll().size();

        // Create the ItemTransaction with an existing ID
        itemTransaction.setId(1L);
        ItemTransactionDTO itemTransactionDTO = itemTransactionMapper.toDto(itemTransaction);

        // An entity with an existing ID cannot be created, so this API call must fail
        restItemTransactionMockMvc.perform(post("/api/item-transactions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(itemTransactionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ItemTransaction in the database
        List<ItemTransaction> itemTransactionList = itemTransactionRepository.findAll();
        assertThat(itemTransactionList).hasSize(databaseSizeBeforeCreate);

        // Validate the ItemTransaction in Elasticsearch
        verify(mockItemTransactionSearchRepository, times(0)).save(itemTransaction);
    }


    @Test
    @Transactional
    public void checkQuantityIsRequired() throws Exception {
        int databaseSizeBeforeTest = itemTransactionRepository.findAll().size();
        // set the field null
        itemTransaction.setQuantity(null);

        // Create the ItemTransaction, which fails.
        ItemTransactionDTO itemTransactionDTO = itemTransactionMapper.toDto(itemTransaction);


        restItemTransactionMockMvc.perform(post("/api/item-transactions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(itemTransactionDTO)))
            .andExpect(status().isBadRequest());

        List<ItemTransaction> itemTransactionList = itemTransactionRepository.findAll();
        assertThat(itemTransactionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllItemTransactions() throws Exception {
        // Initialize the database
        itemTransactionRepository.saveAndFlush(itemTransaction);

        // Get all the itemTransactionList
        restItemTransactionMockMvc.perform(get("/api/item-transactions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(itemTransaction.getId().intValue())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].remarks").value(hasItem(DEFAULT_REMARKS)))
            .andExpect(jsonPath("$.[*].transactionUUID").value(hasItem(DEFAULT_TRANSACTION_UUID.toString())))
            .andExpect(jsonPath("$.[*].transactionDate").value(hasItem(DEFAULT_TRANSACTION_DATE.toString())));
    }
    
    @Test
    @Transactional
    public void getItemTransaction() throws Exception {
        // Initialize the database
        itemTransactionRepository.saveAndFlush(itemTransaction);

        // Get the itemTransaction
        restItemTransactionMockMvc.perform(get("/api/item-transactions/{id}", itemTransaction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(itemTransaction.getId().intValue()))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY))
            .andExpect(jsonPath("$.remarks").value(DEFAULT_REMARKS))
            .andExpect(jsonPath("$.transactionUUID").value(DEFAULT_TRANSACTION_UUID.toString()))
            .andExpect(jsonPath("$.transactionDate").value(DEFAULT_TRANSACTION_DATE.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingItemTransaction() throws Exception {
        // Get the itemTransaction
        restItemTransactionMockMvc.perform(get("/api/item-transactions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateItemTransaction() throws Exception {
        // Initialize the database
        itemTransactionRepository.saveAndFlush(itemTransaction);

        int databaseSizeBeforeUpdate = itemTransactionRepository.findAll().size();

        // Update the itemTransaction
        ItemTransaction updatedItemTransaction = itemTransactionRepository.findById(itemTransaction.getId()).get();
        // Disconnect from session so that the updates on updatedItemTransaction are not directly saved in db
        em.detach(updatedItemTransaction);
        updatedItemTransaction
            .quantity(UPDATED_QUANTITY)
            .remarks(UPDATED_REMARKS)
            .transactionUUID(UPDATED_TRANSACTION_UUID)
            .transactionDate(UPDATED_TRANSACTION_DATE);
        ItemTransactionDTO itemTransactionDTO = itemTransactionMapper.toDto(updatedItemTransaction);

        restItemTransactionMockMvc.perform(put("/api/item-transactions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(itemTransactionDTO)))
            .andExpect(status().isOk());

        // Validate the ItemTransaction in the database
        List<ItemTransaction> itemTransactionList = itemTransactionRepository.findAll();
        assertThat(itemTransactionList).hasSize(databaseSizeBeforeUpdate);
        ItemTransaction testItemTransaction = itemTransactionList.get(itemTransactionList.size() - 1);
        assertThat(testItemTransaction.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testItemTransaction.getRemarks()).isEqualTo(UPDATED_REMARKS);
        assertThat(testItemTransaction.getTransactionUUID()).isEqualTo(UPDATED_TRANSACTION_UUID);
        assertThat(testItemTransaction.getTransactionDate()).isEqualTo(UPDATED_TRANSACTION_DATE);

        // Validate the ItemTransaction in Elasticsearch
        verify(mockItemTransactionSearchRepository, times(1)).save(testItemTransaction);
    }

    @Test
    @Transactional
    public void updateNonExistingItemTransaction() throws Exception {
        int databaseSizeBeforeUpdate = itemTransactionRepository.findAll().size();

        // Create the ItemTransaction
        ItemTransactionDTO itemTransactionDTO = itemTransactionMapper.toDto(itemTransaction);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restItemTransactionMockMvc.perform(put("/api/item-transactions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(itemTransactionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ItemTransaction in the database
        List<ItemTransaction> itemTransactionList = itemTransactionRepository.findAll();
        assertThat(itemTransactionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ItemTransaction in Elasticsearch
        verify(mockItemTransactionSearchRepository, times(0)).save(itemTransaction);
    }

    @Test
    @Transactional
    public void deleteItemTransaction() throws Exception {
        // Initialize the database
        itemTransactionRepository.saveAndFlush(itemTransaction);

        int databaseSizeBeforeDelete = itemTransactionRepository.findAll().size();

        // Delete the itemTransaction
        restItemTransactionMockMvc.perform(delete("/api/item-transactions/{id}", itemTransaction.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ItemTransaction> itemTransactionList = itemTransactionRepository.findAll();
        assertThat(itemTransactionList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the ItemTransaction in Elasticsearch
        verify(mockItemTransactionSearchRepository, times(1)).deleteById(itemTransaction.getId());
    }

    @Test
    @Transactional
    public void searchItemTransaction() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        itemTransactionRepository.saveAndFlush(itemTransaction);
        when(mockItemTransactionSearchRepository.search(queryStringQuery("id:" + itemTransaction.getId())))
            .thenReturn(Collections.singletonList(itemTransaction));

        // Search the itemTransaction
        restItemTransactionMockMvc.perform(get("/api/_search/item-transactions?query=id:" + itemTransaction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(itemTransaction.getId().intValue())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].remarks").value(hasItem(DEFAULT_REMARKS)))
            .andExpect(jsonPath("$.[*].transactionUUID").value(hasItem(DEFAULT_TRANSACTION_UUID.toString())))
            .andExpect(jsonPath("$.[*].transactionDate").value(hasItem(DEFAULT_TRANSACTION_DATE.toString())));
    }
}
