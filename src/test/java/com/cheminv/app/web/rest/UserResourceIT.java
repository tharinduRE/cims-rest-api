package com.cheminv.app.web.rest;

import com.cheminv.app.CimsApp;
import com.cheminv.app.domain.User;
import com.cheminv.app.domain.Store;
import com.cheminv.app.repository.UserRepository;
import com.cheminv.app.service.UserService;
import com.cheminv.app.service.dto.InvUserDTO;
import com.cheminv.app.service.mapper.UserMapper;

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
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link UserResource} REST controller.
 */
@SpringBootTest(classes = CimsApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class UserResourceIT {

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_POST_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_POST_TITLE = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_ON = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_ON = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_LAST_UPDATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_UPDATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_PASSWORD = "AAAAAAAAAA";
    private static final String UPDATED_PASSWORD = "BBBBBBBBBB";

    @Autowired
    private UserRepository userRepository;

    @Mock
    private UserRepository userRepositoryMock;

    @Autowired
    private UserMapper userMapper;

    @Mock
    private UserService userServiceMock;

    @Autowired
    private UserService userService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInvUserMockMvc;

    private User user;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static User createEntity(EntityManager em) {
        User user = new User()
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .postTitle(DEFAULT_POST_TITLE)
            .createdOn(DEFAULT_CREATED_ON)
            .lastUpdated(DEFAULT_LAST_UPDATED)
            .email(DEFAULT_EMAIL)
            .password(DEFAULT_PASSWORD);
        // Add required entity
        Store store;
        if (TestUtil.findAll(em, Store.class).isEmpty()) {
            store = StoreResourceIT.createEntity(em);
            em.persist(store);
            em.flush();
        } else {
            store = TestUtil.findAll(em, Store.class).get(0);
        }
        user.getInvStores().add(store);
        return user;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static User createUpdatedEntity(EntityManager em) {
        User user = new User()
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .postTitle(UPDATED_POST_TITLE)
            .createdOn(UPDATED_CREATED_ON)
            .lastUpdated(UPDATED_LAST_UPDATED)
            .email(UPDATED_EMAIL)
            .password(UPDATED_PASSWORD);
        // Add required entity
        Store store;
        if (TestUtil.findAll(em, Store.class).isEmpty()) {
            store = StoreResourceIT.createUpdatedEntity(em);
            em.persist(store);
            em.flush();
        } else {
            store = TestUtil.findAll(em, Store.class).get(0);
        }
        user.getInvStores().add(store);
        return user;
    }

    @BeforeEach
    public void initTest() {
        user = createEntity(em);
    }

    @Test
    @Transactional
    public void createInvUser() throws Exception {
        int databaseSizeBeforeCreate = userRepository.findAll().size();
        // Create the User
        InvUserDTO invUserDTO = userMapper.userToUserDTO(user);
        restInvUserMockMvc.perform(post("/api/inv-users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(invUserDTO)))
            .andExpect(status().isCreated());

        // Validate the User in the database
        List<User> userList = userRepository.findAll();
        assertThat(userList).hasSize(databaseSizeBeforeCreate + 1);
        User testUser = userList.get(userList.size() - 1);
        assertThat(testUser.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testUser.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testUser.getPostTitle()).isEqualTo(DEFAULT_POST_TITLE);
        assertThat(testUser.getCreatedOn()).isEqualTo(DEFAULT_CREATED_ON);
        assertThat(testUser.getLastUpdated()).isEqualTo(DEFAULT_LAST_UPDATED);
        assertThat(testUser.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testUser.getPassword()).isEqualTo(DEFAULT_PASSWORD);
    }

    @Test
    @Transactional
    public void createInvUserWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = userRepository.findAll().size();

        // Create the User with an existing ID
        user.setId(1L);
        InvUserDTO invUserDTO = userMapper.userToUserDTO(user);

        // An entity with an existing ID cannot be created, so this API call must fail
        restInvUserMockMvc.perform(post("/api/inv-users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(invUserDTO)))
            .andExpect(status().isBadRequest());

        // Validate the User in the database
        List<User> userList = userRepository.findAll();
        assertThat(userList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllInvUsers() throws Exception {
        // Initialize the database
        userRepository.saveAndFlush(user);

        // Get all the invUserList
        restInvUserMockMvc.perform(get("/api/inv-users?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(user.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].postTitle").value(hasItem(DEFAULT_POST_TITLE)))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())))
            .andExpect(jsonPath("$.[*].lastUpdated").value(hasItem(DEFAULT_LAST_UPDATED.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD)));
    }

  /*  @SuppressWarnings({"unchecked"})
    public void getAllInvUsersWithEagerRelationshipsIsEnabled() throws Exception {
        when(invUserServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restInvUserMockMvc.perform(get("/api/inv-users?eagerload=true"))
            .andExpect(status().isOk());

        verify(invUserServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllInvUsersWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(invUserServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restInvUserMockMvc.perform(get("/api/inv-users?eagerload=true"))
            .andExpect(status().isOk());

        verify(invUserServiceMock, times(1)).findAllWithEagerRelationships(any());
    }*/

    @Test
    @Transactional
    public void getInvUser() throws Exception {
        // Initialize the database
        userRepository.saveAndFlush(user);

        // Get the user
        restInvUserMockMvc.perform(get("/api/inv-users/{id}", user.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(user.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.postTitle").value(DEFAULT_POST_TITLE))
            .andExpect(jsonPath("$.createdOn").value(DEFAULT_CREATED_ON.toString()))
            .andExpect(jsonPath("$.lastUpdated").value(DEFAULT_LAST_UPDATED.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.password").value(DEFAULT_PASSWORD));
    }
    @Test
    @Transactional
    public void getNonExistingInvUser() throws Exception {
        // Get the user
        restInvUserMockMvc.perform(get("/api/inv-users/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInvUser() throws Exception {
        // Initialize the database
        userRepository.saveAndFlush(user);

        int databaseSizeBeforeUpdate = userRepository.findAll().size();

        // Update the user
        User updatedUser = userRepository.findById(user.getId()).get();
        // Disconnect from session so that the updates on updatedUser are not directly saved in db
        em.detach(updatedUser);
        updatedUser
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .postTitle(UPDATED_POST_TITLE)
            .createdOn(UPDATED_CREATED_ON)
            .lastUpdated(UPDATED_LAST_UPDATED)
            .email(UPDATED_EMAIL)
            .password(UPDATED_PASSWORD);
        InvUserDTO invUserDTO = userMapper.userToUserDTO(updatedUser);

        restInvUserMockMvc.perform(put("/api/inv-users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(invUserDTO)))
            .andExpect(status().isOk());

        // Validate the User in the database
        List<User> userList = userRepository.findAll();
        assertThat(userList).hasSize(databaseSizeBeforeUpdate);
        User testUser = userList.get(userList.size() - 1);
        assertThat(testUser.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testUser.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testUser.getPostTitle()).isEqualTo(UPDATED_POST_TITLE);
        assertThat(testUser.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
        assertThat(testUser.getLastUpdated()).isEqualTo(UPDATED_LAST_UPDATED);
        assertThat(testUser.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testUser.getPassword()).isEqualTo(UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    public void updateNonExistingInvUser() throws Exception {
        int databaseSizeBeforeUpdate = userRepository.findAll().size();

        // Create the User
        InvUserDTO invUserDTO = userMapper.userToUserDTO(user);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInvUserMockMvc.perform(put("/api/inv-users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(invUserDTO)))
            .andExpect(status().isBadRequest());

        // Validate the User in the database
        List<User> userList = userRepository.findAll();
        assertThat(userList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteInvUser() throws Exception {
        // Initialize the database
        userRepository.saveAndFlush(user);

        int databaseSizeBeforeDelete = userRepository.findAll().size();

        // Delete the user
        restInvUserMockMvc.perform(delete("/api/inv-users/{id}", user.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<User> userList = userRepository.findAll();
        assertThat(userList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
