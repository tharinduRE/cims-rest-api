package com.cheminv.app.web.rest;

import com.cheminv.app.CimsApp;
import com.cheminv.app.domain.Transaction;
import com.cheminv.app.domain.ItemStock;
import com.cheminv.app.domain.User;
import com.cheminv.app.repository.TransactionRepository;
import com.cheminv.app.service.TransactionService;
import com.cheminv.app.service.dto.ItemTransactionDTO;
import com.cheminv.app.service.mapper.TransactionMapper;
import com.cheminv.app.service.TransactionQueryService;

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
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.cheminv.app.domain.enumeration.TransactionType;
/**
 * Integration tests for the {@link TransactionResource} REST controller.
 */
@SpringBootTest(classes = CimsApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class TransactionResourceIT {

    private static final Float DEFAULT_QUANTITY = 1F;
    private static final Float UPDATED_QUANTITY = 2F;
    private static final Float SMALLER_QUANTITY = 1F - 1F;

    private static final String DEFAULT_REMARKS = "AAAAAAAAAA";
    private static final String UPDATED_REMARKS = "BBBBBBBBBB";

    private static final TransactionType DEFAULT_TRANSACTION_TYPE = TransactionType.ISSUE;
    private static final TransactionType UPDATED_TRANSACTION_TYPE = TransactionType.ORDER;

    private static final Instant DEFAULT_TRANSACTION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_TRANSACTION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private TransactionMapper transactionMapper;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private TransactionQueryService transactionQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restItemTransactionMockMvc;

    private Transaction transaction;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Transaction createEntity(EntityManager em) {
        Transaction transaction = new Transaction()
            .quantity(DEFAULT_QUANTITY)
            .remarks(DEFAULT_REMARKS)
            .transactionType(DEFAULT_TRANSACTION_TYPE)
            .transactionDate(DEFAULT_TRANSACTION_DATE);
        // Add required entity
        ItemStock itemStock;
        if (TestUtil.findAll(em, ItemStock.class).isEmpty()) {
            itemStock = ItemStockResourceIT.createEntity(em);
            em.persist(itemStock);
            em.flush();
        } else {
            itemStock = TestUtil.findAll(em, ItemStock.class).get(0);
        }
        transaction.setItemStock(itemStock);
        // Add required entity
        User user;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            user = UserResourceIT.createEntity(em);
            em.persist(user);
            em.flush();
        } else {
            user = TestUtil.findAll(em, User.class).get(0);
        }
        transaction.setCreatedBy(user);
        return transaction;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Transaction createUpdatedEntity(EntityManager em) {
        Transaction transaction = new Transaction()
            .quantity(UPDATED_QUANTITY)
            .remarks(UPDATED_REMARKS)
            .transactionType(UPDATED_TRANSACTION_TYPE)
            .transactionDate(UPDATED_TRANSACTION_DATE);
        // Add required entity
        ItemStock itemStock;
        if (TestUtil.findAll(em, ItemStock.class).isEmpty()) {
            itemStock = ItemStockResourceIT.createUpdatedEntity(em);
            em.persist(itemStock);
            em.flush();
        } else {
            itemStock = TestUtil.findAll(em, ItemStock.class).get(0);
        }
        transaction.setItemStock(itemStock);
        // Add required entity
        User user;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            user = UserResourceIT.createUpdatedEntity(em);
            em.persist(user);
            em.flush();
        } else {
            user = TestUtil.findAll(em, User.class).get(0);
        }
        transaction.setCreatedBy(user);
        return transaction;
    }

    @BeforeEach
    public void initTest() {
        transaction = createEntity(em);
    }

    @Test
    @Transactional
    public void createItemTransaction() throws Exception {
        int databaseSizeBeforeCreate = transactionRepository.findAll().size();
        // Create the Transaction
        ItemTransactionDTO itemTransactionDTO = transactionMapper.toDto(transaction);
        restItemTransactionMockMvc.perform(post("/api/item-transactions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(itemTransactionDTO)))
            .andExpect(status().isCreated());

        // Validate the Transaction in the database
        List<Transaction> transactionList = transactionRepository.findAll();
        assertThat(transactionList).hasSize(databaseSizeBeforeCreate + 1);
        Transaction testTransaction = transactionList.get(transactionList.size() - 1);
        assertThat(testTransaction.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
        assertThat(testTransaction.getRemarks()).isEqualTo(DEFAULT_REMARKS);
        assertThat(testTransaction.getTransactionType()).isEqualTo(DEFAULT_TRANSACTION_TYPE);
        assertThat(testTransaction.getTransactionDate()).isEqualTo(DEFAULT_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    public void createItemTransactionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = transactionRepository.findAll().size();

        // Create the Transaction with an existing ID
        transaction.setId(1L);
        ItemTransactionDTO itemTransactionDTO = transactionMapper.toDto(transaction);

        // An entity with an existing ID cannot be created, so this API call must fail
        restItemTransactionMockMvc.perform(post("/api/item-transactions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(itemTransactionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Transaction in the database
        List<Transaction> transactionList = transactionRepository.findAll();
        assertThat(transactionList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkQuantityIsRequired() throws Exception {
        int databaseSizeBeforeTest = transactionRepository.findAll().size();
        // set the field null
        transaction.setQuantity(null);

        // Create the Transaction, which fails.
        ItemTransactionDTO itemTransactionDTO = transactionMapper.toDto(transaction);


        restItemTransactionMockMvc.perform(post("/api/item-transactions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(itemTransactionDTO)))
            .andExpect(status().isBadRequest());

        List<Transaction> transactionList = transactionRepository.findAll();
        assertThat(transactionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllItemTransactions() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the itemTransactionList
        restItemTransactionMockMvc.perform(get("/api/item-transactions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transaction.getId().intValue())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY.doubleValue())))
            .andExpect(jsonPath("$.[*].remarks").value(hasItem(DEFAULT_REMARKS)))
            .andExpect(jsonPath("$.[*].transactionType").value(hasItem(DEFAULT_TRANSACTION_TYPE.toString())))
            .andExpect(jsonPath("$.[*].transactionDate").value(hasItem(DEFAULT_TRANSACTION_DATE.toString())));
    }
    
    @Test
    @Transactional
    public void getItemTransaction() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get the transaction
        restItemTransactionMockMvc.perform(get("/api/item-transactions/{id}", transaction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(transaction.getId().intValue()))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY.doubleValue()))
            .andExpect(jsonPath("$.remarks").value(DEFAULT_REMARKS))
            .andExpect(jsonPath("$.transactionType").value(DEFAULT_TRANSACTION_TYPE.toString()))
            .andExpect(jsonPath("$.transactionDate").value(DEFAULT_TRANSACTION_DATE.toString()));
    }


    @Test
    @Transactional
    public void getItemTransactionsByIdFiltering() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        Long id = transaction.getId();

        defaultItemTransactionShouldBeFound("id.equals=" + id);
        defaultItemTransactionShouldNotBeFound("id.notEquals=" + id);

        defaultItemTransactionShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultItemTransactionShouldNotBeFound("id.greaterThan=" + id);

        defaultItemTransactionShouldBeFound("id.lessThanOrEqual=" + id);
        defaultItemTransactionShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllItemTransactionsByQuantityIsEqualToSomething() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the itemTransactionList where quantity equals to DEFAULT_QUANTITY
        defaultItemTransactionShouldBeFound("quantity.equals=" + DEFAULT_QUANTITY);

        // Get all the itemTransactionList where quantity equals to UPDATED_QUANTITY
        defaultItemTransactionShouldNotBeFound("quantity.equals=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllItemTransactionsByQuantityIsNotEqualToSomething() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the itemTransactionList where quantity not equals to DEFAULT_QUANTITY
        defaultItemTransactionShouldNotBeFound("quantity.notEquals=" + DEFAULT_QUANTITY);

        // Get all the itemTransactionList where quantity not equals to UPDATED_QUANTITY
        defaultItemTransactionShouldBeFound("quantity.notEquals=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllItemTransactionsByQuantityIsInShouldWork() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the itemTransactionList where quantity in DEFAULT_QUANTITY or UPDATED_QUANTITY
        defaultItemTransactionShouldBeFound("quantity.in=" + DEFAULT_QUANTITY + "," + UPDATED_QUANTITY);

        // Get all the itemTransactionList where quantity equals to UPDATED_QUANTITY
        defaultItemTransactionShouldNotBeFound("quantity.in=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllItemTransactionsByQuantityIsNullOrNotNull() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the itemTransactionList where quantity is not null
        defaultItemTransactionShouldBeFound("quantity.specified=true");

        // Get all the itemTransactionList where quantity is null
        defaultItemTransactionShouldNotBeFound("quantity.specified=false");
    }

    @Test
    @Transactional
    public void getAllItemTransactionsByQuantityIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the itemTransactionList where quantity is greater than or equal to DEFAULT_QUANTITY
        defaultItemTransactionShouldBeFound("quantity.greaterThanOrEqual=" + DEFAULT_QUANTITY);

        // Get all the itemTransactionList where quantity is greater than or equal to UPDATED_QUANTITY
        defaultItemTransactionShouldNotBeFound("quantity.greaterThanOrEqual=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllItemTransactionsByQuantityIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the itemTransactionList where quantity is less than or equal to DEFAULT_QUANTITY
        defaultItemTransactionShouldBeFound("quantity.lessThanOrEqual=" + DEFAULT_QUANTITY);

        // Get all the itemTransactionList where quantity is less than or equal to SMALLER_QUANTITY
        defaultItemTransactionShouldNotBeFound("quantity.lessThanOrEqual=" + SMALLER_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllItemTransactionsByQuantityIsLessThanSomething() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the itemTransactionList where quantity is less than DEFAULT_QUANTITY
        defaultItemTransactionShouldNotBeFound("quantity.lessThan=" + DEFAULT_QUANTITY);

        // Get all the itemTransactionList where quantity is less than UPDATED_QUANTITY
        defaultItemTransactionShouldBeFound("quantity.lessThan=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllItemTransactionsByQuantityIsGreaterThanSomething() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the itemTransactionList where quantity is greater than DEFAULT_QUANTITY
        defaultItemTransactionShouldNotBeFound("quantity.greaterThan=" + DEFAULT_QUANTITY);

        // Get all the itemTransactionList where quantity is greater than SMALLER_QUANTITY
        defaultItemTransactionShouldBeFound("quantity.greaterThan=" + SMALLER_QUANTITY);
    }


    @Test
    @Transactional
    public void getAllItemTransactionsByRemarksIsEqualToSomething() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the itemTransactionList where remarks equals to DEFAULT_REMARKS
        defaultItemTransactionShouldBeFound("remarks.equals=" + DEFAULT_REMARKS);

        // Get all the itemTransactionList where remarks equals to UPDATED_REMARKS
        defaultItemTransactionShouldNotBeFound("remarks.equals=" + UPDATED_REMARKS);
    }

    @Test
    @Transactional
    public void getAllItemTransactionsByRemarksIsNotEqualToSomething() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the itemTransactionList where remarks not equals to DEFAULT_REMARKS
        defaultItemTransactionShouldNotBeFound("remarks.notEquals=" + DEFAULT_REMARKS);

        // Get all the itemTransactionList where remarks not equals to UPDATED_REMARKS
        defaultItemTransactionShouldBeFound("remarks.notEquals=" + UPDATED_REMARKS);
    }

    @Test
    @Transactional
    public void getAllItemTransactionsByRemarksIsInShouldWork() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the itemTransactionList where remarks in DEFAULT_REMARKS or UPDATED_REMARKS
        defaultItemTransactionShouldBeFound("remarks.in=" + DEFAULT_REMARKS + "," + UPDATED_REMARKS);

        // Get all the itemTransactionList where remarks equals to UPDATED_REMARKS
        defaultItemTransactionShouldNotBeFound("remarks.in=" + UPDATED_REMARKS);
    }

    @Test
    @Transactional
    public void getAllItemTransactionsByRemarksIsNullOrNotNull() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the itemTransactionList where remarks is not null
        defaultItemTransactionShouldBeFound("remarks.specified=true");

        // Get all the itemTransactionList where remarks is null
        defaultItemTransactionShouldNotBeFound("remarks.specified=false");
    }
                @Test
    @Transactional
    public void getAllItemTransactionsByRemarksContainsSomething() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the itemTransactionList where remarks contains DEFAULT_REMARKS
        defaultItemTransactionShouldBeFound("remarks.contains=" + DEFAULT_REMARKS);

        // Get all the itemTransactionList where remarks contains UPDATED_REMARKS
        defaultItemTransactionShouldNotBeFound("remarks.contains=" + UPDATED_REMARKS);
    }

    @Test
    @Transactional
    public void getAllItemTransactionsByRemarksNotContainsSomething() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the itemTransactionList where remarks does not contain DEFAULT_REMARKS
        defaultItemTransactionShouldNotBeFound("remarks.doesNotContain=" + DEFAULT_REMARKS);

        // Get all the itemTransactionList where remarks does not contain UPDATED_REMARKS
        defaultItemTransactionShouldBeFound("remarks.doesNotContain=" + UPDATED_REMARKS);
    }


    @Test
    @Transactional
    public void getAllItemTransactionsByTransactionTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the itemTransactionList where transactionType equals to DEFAULT_TRANSACTION_TYPE
        defaultItemTransactionShouldBeFound("transactionType.equals=" + DEFAULT_TRANSACTION_TYPE);

        // Get all the itemTransactionList where transactionType equals to UPDATED_TRANSACTION_TYPE
        defaultItemTransactionShouldNotBeFound("transactionType.equals=" + UPDATED_TRANSACTION_TYPE);
    }

    @Test
    @Transactional
    public void getAllItemTransactionsByTransactionTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the itemTransactionList where transactionType not equals to DEFAULT_TRANSACTION_TYPE
        defaultItemTransactionShouldNotBeFound("transactionType.notEquals=" + DEFAULT_TRANSACTION_TYPE);

        // Get all the itemTransactionList where transactionType not equals to UPDATED_TRANSACTION_TYPE
        defaultItemTransactionShouldBeFound("transactionType.notEquals=" + UPDATED_TRANSACTION_TYPE);
    }

    @Test
    @Transactional
    public void getAllItemTransactionsByTransactionTypeIsInShouldWork() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the itemTransactionList where transactionType in DEFAULT_TRANSACTION_TYPE or UPDATED_TRANSACTION_TYPE
        defaultItemTransactionShouldBeFound("transactionType.in=" + DEFAULT_TRANSACTION_TYPE + "," + UPDATED_TRANSACTION_TYPE);

        // Get all the itemTransactionList where transactionType equals to UPDATED_TRANSACTION_TYPE
        defaultItemTransactionShouldNotBeFound("transactionType.in=" + UPDATED_TRANSACTION_TYPE);
    }

    @Test
    @Transactional
    public void getAllItemTransactionsByTransactionTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the itemTransactionList where transactionType is not null
        defaultItemTransactionShouldBeFound("transactionType.specified=true");

        // Get all the itemTransactionList where transactionType is null
        defaultItemTransactionShouldNotBeFound("transactionType.specified=false");
    }

    @Test
    @Transactional
    public void getAllItemTransactionsByTransactionDateIsEqualToSomething() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the itemTransactionList where transactionDate equals to DEFAULT_TRANSACTION_DATE
        defaultItemTransactionShouldBeFound("transactionDate.equals=" + DEFAULT_TRANSACTION_DATE);

        // Get all the itemTransactionList where transactionDate equals to UPDATED_TRANSACTION_DATE
        defaultItemTransactionShouldNotBeFound("transactionDate.equals=" + UPDATED_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    public void getAllItemTransactionsByTransactionDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the itemTransactionList where transactionDate not equals to DEFAULT_TRANSACTION_DATE
        defaultItemTransactionShouldNotBeFound("transactionDate.notEquals=" + DEFAULT_TRANSACTION_DATE);

        // Get all the itemTransactionList where transactionDate not equals to UPDATED_TRANSACTION_DATE
        defaultItemTransactionShouldBeFound("transactionDate.notEquals=" + UPDATED_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    public void getAllItemTransactionsByTransactionDateIsInShouldWork() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the itemTransactionList where transactionDate in DEFAULT_TRANSACTION_DATE or UPDATED_TRANSACTION_DATE
        defaultItemTransactionShouldBeFound("transactionDate.in=" + DEFAULT_TRANSACTION_DATE + "," + UPDATED_TRANSACTION_DATE);

        // Get all the itemTransactionList where transactionDate equals to UPDATED_TRANSACTION_DATE
        defaultItemTransactionShouldNotBeFound("transactionDate.in=" + UPDATED_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    public void getAllItemTransactionsByTransactionDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the itemTransactionList where transactionDate is not null
        defaultItemTransactionShouldBeFound("transactionDate.specified=true");

        // Get all the itemTransactionList where transactionDate is null
        defaultItemTransactionShouldNotBeFound("transactionDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllItemTransactionsByItemStockIsEqualToSomething() throws Exception {
        // Get already existing entity
        ItemStock itemStock = transaction.getItemStock();
        transactionRepository.saveAndFlush(transaction);
        Long itemStockId = itemStock.getId();

        // Get all the itemTransactionList where itemStock equals to itemStockId
        defaultItemTransactionShouldBeFound("itemStockId.equals=" + itemStockId);

        // Get all the itemTransactionList where itemStock equals to itemStockId + 1
        defaultItemTransactionShouldNotBeFound("itemStockId.equals=" + (itemStockId + 1));
    }


    @Test
    @Transactional
    public void getAllItemTransactionsByCreatedByIsEqualToSomething() throws Exception {
        // Get already existing entity
        User createdBy = transaction.getCreatedBy();
        transactionRepository.saveAndFlush(transaction);
        Long createdById = createdBy.getId();

        // Get all the itemTransactionList where createdBy equals to createdById
        defaultItemTransactionShouldBeFound("createdById.equals=" + createdById);

        // Get all the itemTransactionList where createdBy equals to createdById + 1
        defaultItemTransactionShouldNotBeFound("createdById.equals=" + (createdById + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultItemTransactionShouldBeFound(String filter) throws Exception {
        restItemTransactionMockMvc.perform(get("/api/item-transactions?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transaction.getId().intValue())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY.doubleValue())))
            .andExpect(jsonPath("$.[*].remarks").value(hasItem(DEFAULT_REMARKS)))
            .andExpect(jsonPath("$.[*].transactionType").value(hasItem(DEFAULT_TRANSACTION_TYPE.toString())))
            .andExpect(jsonPath("$.[*].transactionDate").value(hasItem(DEFAULT_TRANSACTION_DATE.toString())));

        // Check, that the count call also returns 1
        restItemTransactionMockMvc.perform(get("/api/item-transactions/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultItemTransactionShouldNotBeFound(String filter) throws Exception {
        restItemTransactionMockMvc.perform(get("/api/item-transactions?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restItemTransactionMockMvc.perform(get("/api/item-transactions/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingItemTransaction() throws Exception {
        // Get the transaction
        restItemTransactionMockMvc.perform(get("/api/item-transactions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateItemTransaction() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        int databaseSizeBeforeUpdate = transactionRepository.findAll().size();

        // Update the transaction
        Transaction updatedTransaction = transactionRepository.findById(transaction.getId()).get();
        // Disconnect from session so that the updates on updatedTransaction are not directly saved in db
        em.detach(updatedTransaction);
        updatedTransaction
            .quantity(UPDATED_QUANTITY)
            .remarks(UPDATED_REMARKS)
            .transactionType(UPDATED_TRANSACTION_TYPE)
            .transactionDate(UPDATED_TRANSACTION_DATE);
        ItemTransactionDTO itemTransactionDTO = transactionMapper.toDto(updatedTransaction);

        restItemTransactionMockMvc.perform(put("/api/item-transactions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(itemTransactionDTO)))
            .andExpect(status().isOk());

        // Validate the Transaction in the database
        List<Transaction> transactionList = transactionRepository.findAll();
        assertThat(transactionList).hasSize(databaseSizeBeforeUpdate);
        Transaction testTransaction = transactionList.get(transactionList.size() - 1);
        assertThat(testTransaction.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testTransaction.getRemarks()).isEqualTo(UPDATED_REMARKS);
        assertThat(testTransaction.getTransactionType()).isEqualTo(UPDATED_TRANSACTION_TYPE);
        assertThat(testTransaction.getTransactionDate()).isEqualTo(UPDATED_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingItemTransaction() throws Exception {
        int databaseSizeBeforeUpdate = transactionRepository.findAll().size();

        // Create the Transaction
        ItemTransactionDTO itemTransactionDTO = transactionMapper.toDto(transaction);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restItemTransactionMockMvc.perform(put("/api/item-transactions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(itemTransactionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Transaction in the database
        List<Transaction> transactionList = transactionRepository.findAll();
        assertThat(transactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteItemTransaction() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        int databaseSizeBeforeDelete = transactionRepository.findAll().size();

        // Delete the transaction
        restItemTransactionMockMvc.perform(delete("/api/item-transactions/{id}", transaction.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Transaction> transactionList = transactionRepository.findAll();
        assertThat(transactionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
