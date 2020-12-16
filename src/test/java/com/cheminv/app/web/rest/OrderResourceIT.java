package com.cheminv.app.web.rest;

import com.cheminv.app.CimsApp;
import com.cheminv.app.domain.Order;
import com.cheminv.app.domain.ItemStock;
import com.cheminv.app.domain.User;
import com.cheminv.app.repository.OrderRepository;
import com.cheminv.app.service.OrderService;
import com.cheminv.app.service.dto.OrderDTO;
import com.cheminv.app.service.mapper.OrderMapper;
import com.cheminv.app.service.OrderQueryService;

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

import com.cheminv.app.domain.enumeration.OrderStatus;
/**
 * Integration tests for the {@link OrderResource} REST controller.
 */
@SpringBootTest(classes = CimsApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class OrderResourceIT {

    private static final OrderStatus DEFAULT_ORDER_STATUS = OrderStatus.COMPLETED;
    private static final OrderStatus UPDATED_ORDER_STATUS = OrderStatus.CANCELLED;

    private static final Instant DEFAULT_REQUEST_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_REQUEST_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_ORDER_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ORDER_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Float DEFAULT_QUANTITY = 1F;
    private static final Float UPDATED_QUANTITY = 2F;
    private static final Float SMALLER_QUANTITY = 1F - 1F;

    private static final Instant DEFAULT_CANCEL_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CANCEL_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderQueryService orderQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrderMockMvc;

    private Order order;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Order createEntity(EntityManager em) {
        Order order = new Order()
            .orderStatus(DEFAULT_ORDER_STATUS)
            .requestDate(DEFAULT_REQUEST_DATE)
            .orderDate(DEFAULT_ORDER_DATE)
            .quantity(DEFAULT_QUANTITY)
            .cancelDate(DEFAULT_CANCEL_DATE);
        // Add required entity
        ItemStock itemStock;
        if (TestUtil.findAll(em, ItemStock.class).isEmpty()) {
            itemStock = ItemStockResourceIT.createEntity(em);
            em.persist(itemStock);
            em.flush();
        } else {
            itemStock = TestUtil.findAll(em, ItemStock.class).get(0);
        }
        order.setItemStock(itemStock);
        // Add required entity
        User user;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            user = UserResourceIT.createEntity(em);
            em.persist(user);
            em.flush();
        } else {
            user = TestUtil.findAll(em, User.class).get(0);
        }
        order.setRequestedBy(user);
        return order;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Order createUpdatedEntity(EntityManager em) {
        Order order = new Order()
            .orderStatus(UPDATED_ORDER_STATUS)
            .requestDate(UPDATED_REQUEST_DATE)
            .orderDate(UPDATED_ORDER_DATE)
            .quantity(UPDATED_QUANTITY)
            .cancelDate(UPDATED_CANCEL_DATE);
        // Add required entity
        ItemStock itemStock;
        if (TestUtil.findAll(em, ItemStock.class).isEmpty()) {
            itemStock = ItemStockResourceIT.createUpdatedEntity(em);
            em.persist(itemStock);
            em.flush();
        } else {
            itemStock = TestUtil.findAll(em, ItemStock.class).get(0);
        }
        order.setItemStock(itemStock);
        // Add required entity
        User user;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            user = UserResourceIT.createUpdatedEntity(em);
            em.persist(user);
            em.flush();
        } else {
            user = TestUtil.findAll(em, User.class).get(0);
        }
        order.setRequestedBy(user);
        return order;
    }

    @BeforeEach
    public void initTest() {
        order = createEntity(em);
    }

    @Test
    @Transactional
    public void createOrder() throws Exception {
        int databaseSizeBeforeCreate = orderRepository.findAll().size();
        // Create the Order
        OrderDTO orderDTO = orderMapper.toDto(order);
        restOrderMockMvc.perform(post("/api/orders")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(orderDTO)))
            .andExpect(status().isCreated());

        // Validate the Order in the database
        List<Order> orderList = orderRepository.findAll();
        assertThat(orderList).hasSize(databaseSizeBeforeCreate + 1);
        Order testOrder = orderList.get(orderList.size() - 1);
        assertThat(testOrder.getOrderStatus()).isEqualTo(DEFAULT_ORDER_STATUS);
        assertThat(testOrder.getRequestDate()).isEqualTo(DEFAULT_REQUEST_DATE);
        assertThat(testOrder.getOrderDate()).isEqualTo(DEFAULT_ORDER_DATE);
        assertThat(testOrder.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
        assertThat(testOrder.getCancelDate()).isEqualTo(DEFAULT_CANCEL_DATE);
    }

    @Test
    @Transactional
    public void createOrderWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = orderRepository.findAll().size();

        // Create the Order with an existing ID
        order.setId(1L);
        OrderDTO orderDTO = orderMapper.toDto(order);

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrderMockMvc.perform(post("/api/orders")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(orderDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Order in the database
        List<Order> orderList = orderRepository.findAll();
        assertThat(orderList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkQuantityIsRequired() throws Exception {
        int databaseSizeBeforeTest = orderRepository.findAll().size();
        // set the field null
        order.setQuantity(null);

        // Create the Order, which fails.
        OrderDTO orderDTO = orderMapper.toDto(order);


        restOrderMockMvc.perform(post("/api/orders")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(orderDTO)))
            .andExpect(status().isBadRequest());

        List<Order> orderList = orderRepository.findAll();
        assertThat(orderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllOrders() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList
        restOrderMockMvc.perform(get("/api/orders?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(order.getId().intValue())))
            .andExpect(jsonPath("$.[*].orderStatus").value(hasItem(DEFAULT_ORDER_STATUS.toString())))
            .andExpect(jsonPath("$.[*].requestDate").value(hasItem(DEFAULT_REQUEST_DATE.toString())))
            .andExpect(jsonPath("$.[*].orderDate").value(hasItem(DEFAULT_ORDER_DATE.toString())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY.doubleValue())))
            .andExpect(jsonPath("$.[*].cancelDate").value(hasItem(DEFAULT_CANCEL_DATE.toString())));
    }
    
    @Test
    @Transactional
    public void getOrder() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get the order
        restOrderMockMvc.perform(get("/api/orders/{id}", order.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(order.getId().intValue()))
            .andExpect(jsonPath("$.orderStatus").value(DEFAULT_ORDER_STATUS.toString()))
            .andExpect(jsonPath("$.requestDate").value(DEFAULT_REQUEST_DATE.toString()))
            .andExpect(jsonPath("$.orderDate").value(DEFAULT_ORDER_DATE.toString()))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY.doubleValue()))
            .andExpect(jsonPath("$.cancelDate").value(DEFAULT_CANCEL_DATE.toString()));
    }


    @Test
    @Transactional
    public void getOrdersByIdFiltering() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        Long id = order.getId();

        defaultOrderShouldBeFound("id.equals=" + id);
        defaultOrderShouldNotBeFound("id.notEquals=" + id);

        defaultOrderShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultOrderShouldNotBeFound("id.greaterThan=" + id);

        defaultOrderShouldBeFound("id.lessThanOrEqual=" + id);
        defaultOrderShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllOrdersByOrderStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where orderStatus equals to DEFAULT_ORDER_STATUS
        defaultOrderShouldBeFound("orderStatus.equals=" + DEFAULT_ORDER_STATUS);

        // Get all the orderList where orderStatus equals to UPDATED_ORDER_STATUS
        defaultOrderShouldNotBeFound("orderStatus.equals=" + UPDATED_ORDER_STATUS);
    }

    @Test
    @Transactional
    public void getAllOrdersByOrderStatusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where orderStatus not equals to DEFAULT_ORDER_STATUS
        defaultOrderShouldNotBeFound("orderStatus.notEquals=" + DEFAULT_ORDER_STATUS);

        // Get all the orderList where orderStatus not equals to UPDATED_ORDER_STATUS
        defaultOrderShouldBeFound("orderStatus.notEquals=" + UPDATED_ORDER_STATUS);
    }

    @Test
    @Transactional
    public void getAllOrdersByOrderStatusIsInShouldWork() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where orderStatus in DEFAULT_ORDER_STATUS or UPDATED_ORDER_STATUS
        defaultOrderShouldBeFound("orderStatus.in=" + DEFAULT_ORDER_STATUS + "," + UPDATED_ORDER_STATUS);

        // Get all the orderList where orderStatus equals to UPDATED_ORDER_STATUS
        defaultOrderShouldNotBeFound("orderStatus.in=" + UPDATED_ORDER_STATUS);
    }

    @Test
    @Transactional
    public void getAllOrdersByOrderStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where orderStatus is not null
        defaultOrderShouldBeFound("orderStatus.specified=true");

        // Get all the orderList where orderStatus is null
        defaultOrderShouldNotBeFound("orderStatus.specified=false");
    }

    @Test
    @Transactional
    public void getAllOrdersByRequestDateIsEqualToSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where requestDate equals to DEFAULT_REQUEST_DATE
        defaultOrderShouldBeFound("requestDate.equals=" + DEFAULT_REQUEST_DATE);

        // Get all the orderList where requestDate equals to UPDATED_REQUEST_DATE
        defaultOrderShouldNotBeFound("requestDate.equals=" + UPDATED_REQUEST_DATE);
    }

    @Test
    @Transactional
    public void getAllOrdersByRequestDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where requestDate not equals to DEFAULT_REQUEST_DATE
        defaultOrderShouldNotBeFound("requestDate.notEquals=" + DEFAULT_REQUEST_DATE);

        // Get all the orderList where requestDate not equals to UPDATED_REQUEST_DATE
        defaultOrderShouldBeFound("requestDate.notEquals=" + UPDATED_REQUEST_DATE);
    }

    @Test
    @Transactional
    public void getAllOrdersByRequestDateIsInShouldWork() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where requestDate in DEFAULT_REQUEST_DATE or UPDATED_REQUEST_DATE
        defaultOrderShouldBeFound("requestDate.in=" + DEFAULT_REQUEST_DATE + "," + UPDATED_REQUEST_DATE);

        // Get all the orderList where requestDate equals to UPDATED_REQUEST_DATE
        defaultOrderShouldNotBeFound("requestDate.in=" + UPDATED_REQUEST_DATE);
    }

    @Test
    @Transactional
    public void getAllOrdersByRequestDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where requestDate is not null
        defaultOrderShouldBeFound("requestDate.specified=true");

        // Get all the orderList where requestDate is null
        defaultOrderShouldNotBeFound("requestDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllOrdersByOrderDateIsEqualToSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where orderDate equals to DEFAULT_ORDER_DATE
        defaultOrderShouldBeFound("orderDate.equals=" + DEFAULT_ORDER_DATE);

        // Get all the orderList where orderDate equals to UPDATED_ORDER_DATE
        defaultOrderShouldNotBeFound("orderDate.equals=" + UPDATED_ORDER_DATE);
    }

    @Test
    @Transactional
    public void getAllOrdersByOrderDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where orderDate not equals to DEFAULT_ORDER_DATE
        defaultOrderShouldNotBeFound("orderDate.notEquals=" + DEFAULT_ORDER_DATE);

        // Get all the orderList where orderDate not equals to UPDATED_ORDER_DATE
        defaultOrderShouldBeFound("orderDate.notEquals=" + UPDATED_ORDER_DATE);
    }

    @Test
    @Transactional
    public void getAllOrdersByOrderDateIsInShouldWork() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where orderDate in DEFAULT_ORDER_DATE or UPDATED_ORDER_DATE
        defaultOrderShouldBeFound("orderDate.in=" + DEFAULT_ORDER_DATE + "," + UPDATED_ORDER_DATE);

        // Get all the orderList where orderDate equals to UPDATED_ORDER_DATE
        defaultOrderShouldNotBeFound("orderDate.in=" + UPDATED_ORDER_DATE);
    }

    @Test
    @Transactional
    public void getAllOrdersByOrderDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where orderDate is not null
        defaultOrderShouldBeFound("orderDate.specified=true");

        // Get all the orderList where orderDate is null
        defaultOrderShouldNotBeFound("orderDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllOrdersByQuantityIsEqualToSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where quantity equals to DEFAULT_QUANTITY
        defaultOrderShouldBeFound("quantity.equals=" + DEFAULT_QUANTITY);

        // Get all the orderList where quantity equals to UPDATED_QUANTITY
        defaultOrderShouldNotBeFound("quantity.equals=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllOrdersByQuantityIsNotEqualToSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where quantity not equals to DEFAULT_QUANTITY
        defaultOrderShouldNotBeFound("quantity.notEquals=" + DEFAULT_QUANTITY);

        // Get all the orderList where quantity not equals to UPDATED_QUANTITY
        defaultOrderShouldBeFound("quantity.notEquals=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllOrdersByQuantityIsInShouldWork() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where quantity in DEFAULT_QUANTITY or UPDATED_QUANTITY
        defaultOrderShouldBeFound("quantity.in=" + DEFAULT_QUANTITY + "," + UPDATED_QUANTITY);

        // Get all the orderList where quantity equals to UPDATED_QUANTITY
        defaultOrderShouldNotBeFound("quantity.in=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllOrdersByQuantityIsNullOrNotNull() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where quantity is not null
        defaultOrderShouldBeFound("quantity.specified=true");

        // Get all the orderList where quantity is null
        defaultOrderShouldNotBeFound("quantity.specified=false");
    }

    @Test
    @Transactional
    public void getAllOrdersByQuantityIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where quantity is greater than or equal to DEFAULT_QUANTITY
        defaultOrderShouldBeFound("quantity.greaterThanOrEqual=" + DEFAULT_QUANTITY);

        // Get all the orderList where quantity is greater than or equal to UPDATED_QUANTITY
        defaultOrderShouldNotBeFound("quantity.greaterThanOrEqual=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllOrdersByQuantityIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where quantity is less than or equal to DEFAULT_QUANTITY
        defaultOrderShouldBeFound("quantity.lessThanOrEqual=" + DEFAULT_QUANTITY);

        // Get all the orderList where quantity is less than or equal to SMALLER_QUANTITY
        defaultOrderShouldNotBeFound("quantity.lessThanOrEqual=" + SMALLER_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllOrdersByQuantityIsLessThanSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where quantity is less than DEFAULT_QUANTITY
        defaultOrderShouldNotBeFound("quantity.lessThan=" + DEFAULT_QUANTITY);

        // Get all the orderList where quantity is less than UPDATED_QUANTITY
        defaultOrderShouldBeFound("quantity.lessThan=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllOrdersByQuantityIsGreaterThanSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where quantity is greater than DEFAULT_QUANTITY
        defaultOrderShouldNotBeFound("quantity.greaterThan=" + DEFAULT_QUANTITY);

        // Get all the orderList where quantity is greater than SMALLER_QUANTITY
        defaultOrderShouldBeFound("quantity.greaterThan=" + SMALLER_QUANTITY);
    }


    @Test
    @Transactional
    public void getAllOrdersByCancelDateIsEqualToSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where cancelDate equals to DEFAULT_CANCEL_DATE
        defaultOrderShouldBeFound("cancelDate.equals=" + DEFAULT_CANCEL_DATE);

        // Get all the orderList where cancelDate equals to UPDATED_CANCEL_DATE
        defaultOrderShouldNotBeFound("cancelDate.equals=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    public void getAllOrdersByCancelDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where cancelDate not equals to DEFAULT_CANCEL_DATE
        defaultOrderShouldNotBeFound("cancelDate.notEquals=" + DEFAULT_CANCEL_DATE);

        // Get all the orderList where cancelDate not equals to UPDATED_CANCEL_DATE
        defaultOrderShouldBeFound("cancelDate.notEquals=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    public void getAllOrdersByCancelDateIsInShouldWork() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where cancelDate in DEFAULT_CANCEL_DATE or UPDATED_CANCEL_DATE
        defaultOrderShouldBeFound("cancelDate.in=" + DEFAULT_CANCEL_DATE + "," + UPDATED_CANCEL_DATE);

        // Get all the orderList where cancelDate equals to UPDATED_CANCEL_DATE
        defaultOrderShouldNotBeFound("cancelDate.in=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    public void getAllOrdersByCancelDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where cancelDate is not null
        defaultOrderShouldBeFound("cancelDate.specified=true");

        // Get all the orderList where cancelDate is null
        defaultOrderShouldNotBeFound("cancelDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllOrdersByItemStockIsEqualToSomething() throws Exception {
        // Get already existing entity
        ItemStock itemStock = order.getItemStock();
        orderRepository.saveAndFlush(order);
        Long itemStockId = itemStock.getId();

        // Get all the orderList where itemStock equals to itemStockId
        defaultOrderShouldBeFound("itemStockId.equals=" + itemStockId);

        // Get all the orderList where itemStock equals to itemStockId + 1
        defaultOrderShouldNotBeFound("itemStockId.equals=" + (itemStockId + 1));
    }


    @Test
    @Transactional
    public void getAllOrdersByRequestedByIsEqualToSomething() throws Exception {
        // Get already existing entity
        User requestedBy = order.getRequestedBy();
        orderRepository.saveAndFlush(order);
        Long requestedById = requestedBy.getId();

        // Get all the orderList where requestedBy equals to requestedById
        defaultOrderShouldBeFound("requestedById.equals=" + requestedById);

        // Get all the orderList where requestedBy equals to requestedById + 1
        defaultOrderShouldNotBeFound("requestedById.equals=" + (requestedById + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultOrderShouldBeFound(String filter) throws Exception {
        restOrderMockMvc.perform(get("/api/orders?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(order.getId().intValue())))
            .andExpect(jsonPath("$.[*].orderStatus").value(hasItem(DEFAULT_ORDER_STATUS.toString())))
            .andExpect(jsonPath("$.[*].requestDate").value(hasItem(DEFAULT_REQUEST_DATE.toString())))
            .andExpect(jsonPath("$.[*].orderDate").value(hasItem(DEFAULT_ORDER_DATE.toString())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY.doubleValue())))
            .andExpect(jsonPath("$.[*].cancelDate").value(hasItem(DEFAULT_CANCEL_DATE.toString())));

        // Check, that the count call also returns 1
        restOrderMockMvc.perform(get("/api/orders/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultOrderShouldNotBeFound(String filter) throws Exception {
        restOrderMockMvc.perform(get("/api/orders?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restOrderMockMvc.perform(get("/api/orders/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingOrder() throws Exception {
        // Get the order
        restOrderMockMvc.perform(get("/api/orders/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOrder() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        int databaseSizeBeforeUpdate = orderRepository.findAll().size();

        // Update the order
        Order updatedOrder = orderRepository.findById(order.getId()).get();
        // Disconnect from session so that the updates on updatedOrder are not directly saved in db
        em.detach(updatedOrder);
        updatedOrder
            .orderStatus(UPDATED_ORDER_STATUS)
            .requestDate(UPDATED_REQUEST_DATE)
            .orderDate(UPDATED_ORDER_DATE)
            .quantity(UPDATED_QUANTITY)
            .cancelDate(UPDATED_CANCEL_DATE);
        OrderDTO orderDTO = orderMapper.toDto(updatedOrder);

        restOrderMockMvc.perform(put("/api/orders")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(orderDTO)))
            .andExpect(status().isOk());

        // Validate the Order in the database
        List<Order> orderList = orderRepository.findAll();
        assertThat(orderList).hasSize(databaseSizeBeforeUpdate);
        Order testOrder = orderList.get(orderList.size() - 1);
        assertThat(testOrder.getOrderStatus()).isEqualTo(UPDATED_ORDER_STATUS);
        assertThat(testOrder.getRequestDate()).isEqualTo(UPDATED_REQUEST_DATE);
        assertThat(testOrder.getOrderDate()).isEqualTo(UPDATED_ORDER_DATE);
        assertThat(testOrder.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testOrder.getCancelDate()).isEqualTo(UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingOrder() throws Exception {
        int databaseSizeBeforeUpdate = orderRepository.findAll().size();

        // Create the Order
        OrderDTO orderDTO = orderMapper.toDto(order);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderMockMvc.perform(put("/api/orders")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(orderDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Order in the database
        List<Order> orderList = orderRepository.findAll();
        assertThat(orderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteOrder() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        int databaseSizeBeforeDelete = orderRepository.findAll().size();

        // Delete the order
        restOrderMockMvc.perform(delete("/api/orders/{id}", order.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Order> orderList = orderRepository.findAll();
        assertThat(orderList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
