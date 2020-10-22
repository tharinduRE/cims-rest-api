package com.cheminv.app.web.rest;

import com.cheminv.app.CimsApp;
import com.cheminv.app.domain.InvDepartment;
import com.cheminv.app.repository.InvDepartmentRepository;
import com.cheminv.app.service.InvDepartmentService;
import com.cheminv.app.service.dto.InvDepartmentDTO;
import com.cheminv.app.service.mapper.InvDepartmentMapper;

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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link InvDepartmentResource} REST controller.
 */
@SpringBootTest(classes = CimsApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class InvDepartmentResourceIT {

    private static final String DEFAULT_DEPARTMENT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_DEPARTMENT_NAME = "BBBBBBBBBB";

    @Autowired
    private InvDepartmentRepository invDepartmentRepository;

    @Mock
    private InvDepartmentRepository invDepartmentRepositoryMock;

    @Autowired
    private InvDepartmentMapper invDepartmentMapper;

    @Mock
    private InvDepartmentService invDepartmentServiceMock;

    @Autowired
    private InvDepartmentService invDepartmentService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInvDepartmentMockMvc;

    private InvDepartment invDepartment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InvDepartment createEntity(EntityManager em) {
        InvDepartment invDepartment = new InvDepartment()
            .departmentName(DEFAULT_DEPARTMENT_NAME);
        return invDepartment;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InvDepartment createUpdatedEntity(EntityManager em) {
        InvDepartment invDepartment = new InvDepartment()
            .departmentName(UPDATED_DEPARTMENT_NAME);
        return invDepartment;
    }

    @BeforeEach
    public void initTest() {
        invDepartment = createEntity(em);
    }

    @Test
    @Transactional
    public void createInvDepartment() throws Exception {
        int databaseSizeBeforeCreate = invDepartmentRepository.findAll().size();
        // Create the InvDepartment
        InvDepartmentDTO invDepartmentDTO = invDepartmentMapper.toDto(invDepartment);
        restInvDepartmentMockMvc.perform(post("/api/inv-departments")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(invDepartmentDTO)))
            .andExpect(status().isCreated());

        // Validate the InvDepartment in the database
        List<InvDepartment> invDepartmentList = invDepartmentRepository.findAll();
        assertThat(invDepartmentList).hasSize(databaseSizeBeforeCreate + 1);
        InvDepartment testInvDepartment = invDepartmentList.get(invDepartmentList.size() - 1);
        assertThat(testInvDepartment.getDepartmentName()).isEqualTo(DEFAULT_DEPARTMENT_NAME);
    }

    @Test
    @Transactional
    public void createInvDepartmentWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = invDepartmentRepository.findAll().size();

        // Create the InvDepartment with an existing ID
        invDepartment.setId(1L);
        InvDepartmentDTO invDepartmentDTO = invDepartmentMapper.toDto(invDepartment);

        // An entity with an existing ID cannot be created, so this API call must fail
        restInvDepartmentMockMvc.perform(post("/api/inv-departments")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(invDepartmentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the InvDepartment in the database
        List<InvDepartment> invDepartmentList = invDepartmentRepository.findAll();
        assertThat(invDepartmentList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllInvDepartments() throws Exception {
        // Initialize the database
        invDepartmentRepository.saveAndFlush(invDepartment);

        // Get all the invDepartmentList
        restInvDepartmentMockMvc.perform(get("/api/inv-departments?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(invDepartment.getId().intValue())))
            .andExpect(jsonPath("$.[*].departmentName").value(hasItem(DEFAULT_DEPARTMENT_NAME)));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllInvDepartmentsWithEagerRelationshipsIsEnabled() throws Exception {
        when(invDepartmentServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restInvDepartmentMockMvc.perform(get("/api/inv-departments?eagerload=true"))
            .andExpect(status().isOk());

        verify(invDepartmentServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllInvDepartmentsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(invDepartmentServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restInvDepartmentMockMvc.perform(get("/api/inv-departments?eagerload=true"))
            .andExpect(status().isOk());

        verify(invDepartmentServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getInvDepartment() throws Exception {
        // Initialize the database
        invDepartmentRepository.saveAndFlush(invDepartment);

        // Get the invDepartment
        restInvDepartmentMockMvc.perform(get("/api/inv-departments/{id}", invDepartment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(invDepartment.getId().intValue()))
            .andExpect(jsonPath("$.departmentName").value(DEFAULT_DEPARTMENT_NAME));
    }
    @Test
    @Transactional
    public void getNonExistingInvDepartment() throws Exception {
        // Get the invDepartment
        restInvDepartmentMockMvc.perform(get("/api/inv-departments/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInvDepartment() throws Exception {
        // Initialize the database
        invDepartmentRepository.saveAndFlush(invDepartment);

        int databaseSizeBeforeUpdate = invDepartmentRepository.findAll().size();

        // Update the invDepartment
        InvDepartment updatedInvDepartment = invDepartmentRepository.findById(invDepartment.getId()).get();
        // Disconnect from session so that the updates on updatedInvDepartment are not directly saved in db
        em.detach(updatedInvDepartment);
        updatedInvDepartment
            .departmentName(UPDATED_DEPARTMENT_NAME);
        InvDepartmentDTO invDepartmentDTO = invDepartmentMapper.toDto(updatedInvDepartment);

        restInvDepartmentMockMvc.perform(put("/api/inv-departments")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(invDepartmentDTO)))
            .andExpect(status().isOk());

        // Validate the InvDepartment in the database
        List<InvDepartment> invDepartmentList = invDepartmentRepository.findAll();
        assertThat(invDepartmentList).hasSize(databaseSizeBeforeUpdate);
        InvDepartment testInvDepartment = invDepartmentList.get(invDepartmentList.size() - 1);
        assertThat(testInvDepartment.getDepartmentName()).isEqualTo(UPDATED_DEPARTMENT_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingInvDepartment() throws Exception {
        int databaseSizeBeforeUpdate = invDepartmentRepository.findAll().size();

        // Create the InvDepartment
        InvDepartmentDTO invDepartmentDTO = invDepartmentMapper.toDto(invDepartment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInvDepartmentMockMvc.perform(put("/api/inv-departments")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(invDepartmentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the InvDepartment in the database
        List<InvDepartment> invDepartmentList = invDepartmentRepository.findAll();
        assertThat(invDepartmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteInvDepartment() throws Exception {
        // Initialize the database
        invDepartmentRepository.saveAndFlush(invDepartment);

        int databaseSizeBeforeDelete = invDepartmentRepository.findAll().size();

        // Delete the invDepartment
        restInvDepartmentMockMvc.perform(delete("/api/inv-departments/{id}", invDepartment.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<InvDepartment> invDepartmentList = invDepartmentRepository.findAll();
        assertThat(invDepartmentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
