package com.cheminv.app.web.rest;

import com.cheminv.app.CimsApp;
import com.cheminv.app.domain.InvUser;
import com.cheminv.app.domain.InvStore;
import com.cheminv.app.repository.InvUserRepository;
import com.cheminv.app.service.InvUserService;
import com.cheminv.app.service.dto.InvUserDTO;
import com.cheminv.app.service.mapper.InvUserMapper;

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
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link InvUserResource} REST controller.
 */
@SpringBootTest(classes = CimsApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class InvUserResourceIT {

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
    private InvUserRepository invUserRepository;

    @Mock
    private InvUserRepository invUserRepositoryMock;

    @Autowired
    private InvUserMapper invUserMapper;

    @Mock
    private InvUserService invUserServiceMock;

    @Autowired
    private InvUserService invUserService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInvUserMockMvc;

    private InvUser invUser;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InvUser createEntity(EntityManager em) {
        InvUser invUser = new InvUser()
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .postTitle(DEFAULT_POST_TITLE)
            .createdOn(DEFAULT_CREATED_ON)
            .lastUpdated(DEFAULT_LAST_UPDATED)
            .email(DEFAULT_EMAIL)
            .password(DEFAULT_PASSWORD);
        // Add required entity
        InvStore invStore;
        if (TestUtil.findAll(em, InvStore.class).isEmpty()) {
            invStore = InvStoreResourceIT.createEntity(em);
            em.persist(invStore);
            em.flush();
        } else {
            invStore = TestUtil.findAll(em, InvStore.class).get(0);
        }
        invUser.getInvStores().add(invStore);
        return invUser;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InvUser createUpdatedEntity(EntityManager em) {
        InvUser invUser = new InvUser()
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .postTitle(UPDATED_POST_TITLE)
            .createdOn(UPDATED_CREATED_ON)
            .lastUpdated(UPDATED_LAST_UPDATED)
            .email(UPDATED_EMAIL)
            .password(UPDATED_PASSWORD);
        // Add required entity
        InvStore invStore;
        if (TestUtil.findAll(em, InvStore.class).isEmpty()) {
            invStore = InvStoreResourceIT.createUpdatedEntity(em);
            em.persist(invStore);
            em.flush();
        } else {
            invStore = TestUtil.findAll(em, InvStore.class).get(0);
        }
        invUser.getInvStores().add(invStore);
        return invUser;
    }

    @BeforeEach
    public void initTest() {
        invUser = createEntity(em);
    }

    @Test
    @Transactional
    public void createInvUser() throws Exception {
        int databaseSizeBeforeCreate = invUserRepository.findAll().size();
        // Create the InvUser
        InvUserDTO invUserDTO = invUserMapper.userToUserDTO(invUser);
        restInvUserMockMvc.perform(post("/api/inv-users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(invUserDTO)))
            .andExpect(status().isCreated());

        // Validate the InvUser in the database
        List<InvUser> invUserList = invUserRepository.findAll();
        assertThat(invUserList).hasSize(databaseSizeBeforeCreate + 1);
        InvUser testInvUser = invUserList.get(invUserList.size() - 1);
        assertThat(testInvUser.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testInvUser.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testInvUser.getPostTitle()).isEqualTo(DEFAULT_POST_TITLE);
        assertThat(testInvUser.getCreatedOn()).isEqualTo(DEFAULT_CREATED_ON);
        assertThat(testInvUser.getLastUpdated()).isEqualTo(DEFAULT_LAST_UPDATED);
        assertThat(testInvUser.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testInvUser.getPassword()).isEqualTo(DEFAULT_PASSWORD);
    }

    @Test
    @Transactional
    public void createInvUserWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = invUserRepository.findAll().size();

        // Create the InvUser with an existing ID
        invUser.setId(1L);
        InvUserDTO invUserDTO = invUserMapper.userToUserDTO(invUser);

        // An entity with an existing ID cannot be created, so this API call must fail
        restInvUserMockMvc.perform(post("/api/inv-users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(invUserDTO)))
            .andExpect(status().isBadRequest());

        // Validate the InvUser in the database
        List<InvUser> invUserList = invUserRepository.findAll();
        assertThat(invUserList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllInvUsers() throws Exception {
        // Initialize the database
        invUserRepository.saveAndFlush(invUser);

        // Get all the invUserList
        restInvUserMockMvc.perform(get("/api/inv-users?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(invUser.getId().intValue())))
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
        invUserRepository.saveAndFlush(invUser);

        // Get the invUser
        restInvUserMockMvc.perform(get("/api/inv-users/{id}", invUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(invUser.getId().intValue()))
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
        // Get the invUser
        restInvUserMockMvc.perform(get("/api/inv-users/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInvUser() throws Exception {
        // Initialize the database
        invUserRepository.saveAndFlush(invUser);

        int databaseSizeBeforeUpdate = invUserRepository.findAll().size();

        // Update the invUser
        InvUser updatedInvUser = invUserRepository.findById(invUser.getId()).get();
        // Disconnect from session so that the updates on updatedInvUser are not directly saved in db
        em.detach(updatedInvUser);
        updatedInvUser
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .postTitle(UPDATED_POST_TITLE)
            .createdOn(UPDATED_CREATED_ON)
            .lastUpdated(UPDATED_LAST_UPDATED)
            .email(UPDATED_EMAIL)
            .password(UPDATED_PASSWORD);
        InvUserDTO invUserDTO = invUserMapper.userToUserDTO(updatedInvUser);

        restInvUserMockMvc.perform(put("/api/inv-users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(invUserDTO)))
            .andExpect(status().isOk());

        // Validate the InvUser in the database
        List<InvUser> invUserList = invUserRepository.findAll();
        assertThat(invUserList).hasSize(databaseSizeBeforeUpdate);
        InvUser testInvUser = invUserList.get(invUserList.size() - 1);
        assertThat(testInvUser.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testInvUser.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testInvUser.getPostTitle()).isEqualTo(UPDATED_POST_TITLE);
        assertThat(testInvUser.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
        assertThat(testInvUser.getLastUpdated()).isEqualTo(UPDATED_LAST_UPDATED);
        assertThat(testInvUser.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testInvUser.getPassword()).isEqualTo(UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    public void updateNonExistingInvUser() throws Exception {
        int databaseSizeBeforeUpdate = invUserRepository.findAll().size();

        // Create the InvUser
        InvUserDTO invUserDTO = invUserMapper.userToUserDTO(invUser);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInvUserMockMvc.perform(put("/api/inv-users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(invUserDTO)))
            .andExpect(status().isBadRequest());

        // Validate the InvUser in the database
        List<InvUser> invUserList = invUserRepository.findAll();
        assertThat(invUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteInvUser() throws Exception {
        // Initialize the database
        invUserRepository.saveAndFlush(invUser);

        int databaseSizeBeforeDelete = invUserRepository.findAll().size();

        // Delete the invUser
        restInvUserMockMvc.perform(delete("/api/inv-users/{id}", invUser.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<InvUser> invUserList = invUserRepository.findAll();
        assertThat(invUserList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
