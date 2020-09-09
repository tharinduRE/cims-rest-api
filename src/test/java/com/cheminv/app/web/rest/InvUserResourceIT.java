package com.cheminv.app.web.rest;

import com.cheminv.app.CimsApp;
import com.cheminv.app.domain.InvUser;
import com.cheminv.app.repository.InvUserRepository;
import com.cheminv.app.repository.search.InvUserSearchRepository;
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
 * Integration tests for the {@link InvUserResource} REST controller.
 */
@SpringBootTest(classes = CimsApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class InvUserResourceIT {

    private static final String DEFAULT_POST_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_POST_TITLE = "BBBBBBBBBB";

    @Autowired
    private InvUserRepository invUserRepository;

    @Autowired
    private InvUserMapper invUserMapper;

    @Autowired
    private InvUserService invUserService;

    /**
     * This repository is mocked in the com.cheminv.app.repository.search test package.
     *
     * @see com.cheminv.app.repository.search.InvUserSearchRepositoryMockConfiguration
     */
    @Autowired
    private InvUserSearchRepository mockInvUserSearchRepository;

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
            .postTitle(DEFAULT_POST_TITLE);
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
            .postTitle(UPDATED_POST_TITLE);
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
        InvUserDTO invUserDTO = invUserMapper.toDto(invUser);
        restInvUserMockMvc.perform(post("/api/inv-users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(invUserDTO)))
            .andExpect(status().isCreated());

        // Validate the InvUser in the database
        List<InvUser> invUserList = invUserRepository.findAll();
        assertThat(invUserList).hasSize(databaseSizeBeforeCreate + 1);
        InvUser testInvUser = invUserList.get(invUserList.size() - 1);
        assertThat(testInvUser.getPostTitle()).isEqualTo(DEFAULT_POST_TITLE);

        // Validate the InvUser in Elasticsearch
        verify(mockInvUserSearchRepository, times(1)).save(testInvUser);
    }

    @Test
    @Transactional
    public void createInvUserWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = invUserRepository.findAll().size();

        // Create the InvUser with an existing ID
        invUser.setId(1L);
        InvUserDTO invUserDTO = invUserMapper.toDto(invUser);

        // An entity with an existing ID cannot be created, so this API call must fail
        restInvUserMockMvc.perform(post("/api/inv-users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(invUserDTO)))
            .andExpect(status().isBadRequest());

        // Validate the InvUser in the database
        List<InvUser> invUserList = invUserRepository.findAll();
        assertThat(invUserList).hasSize(databaseSizeBeforeCreate);

        // Validate the InvUser in Elasticsearch
        verify(mockInvUserSearchRepository, times(0)).save(invUser);
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
            .andExpect(jsonPath("$.[*].postTitle").value(hasItem(DEFAULT_POST_TITLE)));
    }
    
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
            .andExpect(jsonPath("$.postTitle").value(DEFAULT_POST_TITLE));
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
            .postTitle(UPDATED_POST_TITLE);
        InvUserDTO invUserDTO = invUserMapper.toDto(updatedInvUser);

        restInvUserMockMvc.perform(put("/api/inv-users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(invUserDTO)))
            .andExpect(status().isOk());

        // Validate the InvUser in the database
        List<InvUser> invUserList = invUserRepository.findAll();
        assertThat(invUserList).hasSize(databaseSizeBeforeUpdate);
        InvUser testInvUser = invUserList.get(invUserList.size() - 1);
        assertThat(testInvUser.getPostTitle()).isEqualTo(UPDATED_POST_TITLE);

        // Validate the InvUser in Elasticsearch
        verify(mockInvUserSearchRepository, times(1)).save(testInvUser);
    }

    @Test
    @Transactional
    public void updateNonExistingInvUser() throws Exception {
        int databaseSizeBeforeUpdate = invUserRepository.findAll().size();

        // Create the InvUser
        InvUserDTO invUserDTO = invUserMapper.toDto(invUser);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInvUserMockMvc.perform(put("/api/inv-users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(invUserDTO)))
            .andExpect(status().isBadRequest());

        // Validate the InvUser in the database
        List<InvUser> invUserList = invUserRepository.findAll();
        assertThat(invUserList).hasSize(databaseSizeBeforeUpdate);

        // Validate the InvUser in Elasticsearch
        verify(mockInvUserSearchRepository, times(0)).save(invUser);
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

        // Validate the InvUser in Elasticsearch
        verify(mockInvUserSearchRepository, times(1)).deleteById(invUser.getId());
    }

    @Test
    @Transactional
    public void searchInvUser() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        invUserRepository.saveAndFlush(invUser);
        when(mockInvUserSearchRepository.search(queryStringQuery("id:" + invUser.getId())))
            .thenReturn(Collections.singletonList(invUser));

        // Search the invUser
        restInvUserMockMvc.perform(get("/api/_search/inv-users?query=id:" + invUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(invUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].postTitle").value(hasItem(DEFAULT_POST_TITLE)));
    }
}
