package com.cheminv.app.web.rest;

import com.cheminv.app.CimsApp;
import com.cheminv.app.domain.WasteVendor;
import com.cheminv.app.repository.WasteVendorRepository;
import com.cheminv.app.service.WasteVendorService;
import com.cheminv.app.service.dto.WasteVendorDTO;
import com.cheminv.app.service.mapper.WasteVendorMapper;

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
 * Integration tests for the {@link WasteVendorResource} REST controller.
 */
@SpringBootTest(classes = CimsApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class WasteVendorResourceIT {

    private static final String DEFAULT_VENDOR_NAME = "AAAAAAAAAA";
    private static final String UPDATED_VENDOR_NAME = "BBBBBBBBBB";

    private static final Instant DEFAULT_LAST_ISSUED_ON = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_ISSUED_ON = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private WasteVendorRepository wasteVendorRepository;

    @Mock
    private WasteVendorRepository wasteVendorRepositoryMock;

    @Autowired
    private WasteVendorMapper wasteVendorMapper;

    @Mock
    private WasteVendorService wasteVendorServiceMock;

    @Autowired
    private WasteVendorService wasteVendorService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restWasteVendorMockMvc;

    private WasteVendor wasteVendor;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WasteVendor createEntity(EntityManager em) {
        WasteVendor wasteVendor = new WasteVendor()
            .vendorName(DEFAULT_VENDOR_NAME)
            .lastIssuedOn(DEFAULT_LAST_ISSUED_ON);
        return wasteVendor;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WasteVendor createUpdatedEntity(EntityManager em) {
        WasteVendor wasteVendor = new WasteVendor()
            .vendorName(UPDATED_VENDOR_NAME)
            .lastIssuedOn(UPDATED_LAST_ISSUED_ON);
        return wasteVendor;
    }

    @BeforeEach
    public void initTest() {
        wasteVendor = createEntity(em);
    }

    @Test
    @Transactional
    public void createWasteVendor() throws Exception {
        int databaseSizeBeforeCreate = wasteVendorRepository.findAll().size();
        // Create the WasteVendor
        WasteVendorDTO wasteVendorDTO = wasteVendorMapper.toDto(wasteVendor);
        restWasteVendorMockMvc.perform(post("/api/waste-vendors")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(wasteVendorDTO)))
            .andExpect(status().isCreated());

        // Validate the WasteVendor in the database
        List<WasteVendor> wasteVendorList = wasteVendorRepository.findAll();
        assertThat(wasteVendorList).hasSize(databaseSizeBeforeCreate + 1);
        WasteVendor testWasteVendor = wasteVendorList.get(wasteVendorList.size() - 1);
        assertThat(testWasteVendor.getVendorName()).isEqualTo(DEFAULT_VENDOR_NAME);
        assertThat(testWasteVendor.getLastIssuedOn()).isEqualTo(DEFAULT_LAST_ISSUED_ON);
    }

    @Test
    @Transactional
    public void createWasteVendorWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = wasteVendorRepository.findAll().size();

        // Create the WasteVendor with an existing ID
        wasteVendor.setId(1L);
        WasteVendorDTO wasteVendorDTO = wasteVendorMapper.toDto(wasteVendor);

        // An entity with an existing ID cannot be created, so this API call must fail
        restWasteVendorMockMvc.perform(post("/api/waste-vendors")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(wasteVendorDTO)))
            .andExpect(status().isBadRequest());

        // Validate the WasteVendor in the database
        List<WasteVendor> wasteVendorList = wasteVendorRepository.findAll();
        assertThat(wasteVendorList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllWasteVendors() throws Exception {
        // Initialize the database
        wasteVendorRepository.saveAndFlush(wasteVendor);

        // Get all the wasteVendorList
        restWasteVendorMockMvc.perform(get("/api/waste-vendors?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(wasteVendor.getId().intValue())))
            .andExpect(jsonPath("$.[*].vendorName").value(hasItem(DEFAULT_VENDOR_NAME)))
            .andExpect(jsonPath("$.[*].lastIssuedOn").value(hasItem(DEFAULT_LAST_ISSUED_ON.toString())));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllWasteVendorsWithEagerRelationshipsIsEnabled() throws Exception {
        when(wasteVendorServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restWasteVendorMockMvc.perform(get("/api/waste-vendors?eagerload=true"))
            .andExpect(status().isOk());

        verify(wasteVendorServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllWasteVendorsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(wasteVendorServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restWasteVendorMockMvc.perform(get("/api/waste-vendors?eagerload=true"))
            .andExpect(status().isOk());

        verify(wasteVendorServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getWasteVendor() throws Exception {
        // Initialize the database
        wasteVendorRepository.saveAndFlush(wasteVendor);

        // Get the wasteVendor
        restWasteVendorMockMvc.perform(get("/api/waste-vendors/{id}", wasteVendor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(wasteVendor.getId().intValue()))
            .andExpect(jsonPath("$.vendorName").value(DEFAULT_VENDOR_NAME))
            .andExpect(jsonPath("$.lastIssuedOn").value(DEFAULT_LAST_ISSUED_ON.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingWasteVendor() throws Exception {
        // Get the wasteVendor
        restWasteVendorMockMvc.perform(get("/api/waste-vendors/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateWasteVendor() throws Exception {
        // Initialize the database
        wasteVendorRepository.saveAndFlush(wasteVendor);

        int databaseSizeBeforeUpdate = wasteVendorRepository.findAll().size();

        // Update the wasteVendor
        WasteVendor updatedWasteVendor = wasteVendorRepository.findById(wasteVendor.getId()).get();
        // Disconnect from session so that the updates on updatedWasteVendor are not directly saved in db
        em.detach(updatedWasteVendor);
        updatedWasteVendor
            .vendorName(UPDATED_VENDOR_NAME)
            .lastIssuedOn(UPDATED_LAST_ISSUED_ON);
        WasteVendorDTO wasteVendorDTO = wasteVendorMapper.toDto(updatedWasteVendor);

        restWasteVendorMockMvc.perform(put("/api/waste-vendors")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(wasteVendorDTO)))
            .andExpect(status().isOk());

        // Validate the WasteVendor in the database
        List<WasteVendor> wasteVendorList = wasteVendorRepository.findAll();
        assertThat(wasteVendorList).hasSize(databaseSizeBeforeUpdate);
        WasteVendor testWasteVendor = wasteVendorList.get(wasteVendorList.size() - 1);
        assertThat(testWasteVendor.getVendorName()).isEqualTo(UPDATED_VENDOR_NAME);
        assertThat(testWasteVendor.getLastIssuedOn()).isEqualTo(UPDATED_LAST_ISSUED_ON);
    }

    @Test
    @Transactional
    public void updateNonExistingWasteVendor() throws Exception {
        int databaseSizeBeforeUpdate = wasteVendorRepository.findAll().size();

        // Create the WasteVendor
        WasteVendorDTO wasteVendorDTO = wasteVendorMapper.toDto(wasteVendor);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWasteVendorMockMvc.perform(put("/api/waste-vendors")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(wasteVendorDTO)))
            .andExpect(status().isBadRequest());

        // Validate the WasteVendor in the database
        List<WasteVendor> wasteVendorList = wasteVendorRepository.findAll();
        assertThat(wasteVendorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteWasteVendor() throws Exception {
        // Initialize the database
        wasteVendorRepository.saveAndFlush(wasteVendor);

        int databaseSizeBeforeDelete = wasteVendorRepository.findAll().size();

        // Delete the wasteVendor
        restWasteVendorMockMvc.perform(delete("/api/waste-vendors/{id}", wasteVendor.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<WasteVendor> wasteVendorList = wasteVendorRepository.findAll();
        assertThat(wasteVendorList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
