package com.cheminv.app.web.rest;

import com.cheminv.app.CimsApp;
import com.cheminv.app.domain.MeasUnit;
import com.cheminv.app.repository.MeasUnitRepository;
import com.cheminv.app.service.MeasUnitService;
import com.cheminv.app.service.dto.MeasUnitDTO;
import com.cheminv.app.service.mapper.MeasUnitMapper;

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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link MeasUnitResource} REST controller.
 */
@SpringBootTest(classes = CimsApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class MeasUnitResourceIT {

    private static final String DEFAULT_MEAS_UNIT = "AAAAAAAAAA";
    private static final String UPDATED_MEAS_UNIT = "BBBBBBBBBB";

    private static final String DEFAULT_MEAS_DESC = "AAAAAAAAAA";
    private static final String UPDATED_MEAS_DESC = "BBBBBBBBBB";

    @Autowired
    private MeasUnitRepository measUnitRepository;

    @Autowired
    private MeasUnitMapper measUnitMapper;

    @Autowired
    private MeasUnitService measUnitService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMeasUnitMockMvc;

    private MeasUnit measUnit;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MeasUnit createEntity(EntityManager em) {
        MeasUnit measUnit = new MeasUnit()
            .measUnit(DEFAULT_MEAS_UNIT)
            .measDesc(DEFAULT_MEAS_DESC);
        return measUnit;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MeasUnit createUpdatedEntity(EntityManager em) {
        MeasUnit measUnit = new MeasUnit()
            .measUnit(UPDATED_MEAS_UNIT)
            .measDesc(UPDATED_MEAS_DESC);
        return measUnit;
    }

    @BeforeEach
    public void initTest() {
        measUnit = createEntity(em);
    }

    @Test
    @Transactional
    public void createMeasUnit() throws Exception {
        int databaseSizeBeforeCreate = measUnitRepository.findAll().size();
        // Create the MeasUnit
        MeasUnitDTO measUnitDTO = measUnitMapper.toDto(measUnit);
        restMeasUnitMockMvc.perform(post("/api/meas-units")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(measUnitDTO)))
            .andExpect(status().isCreated());

        // Validate the MeasUnit in the database
        List<MeasUnit> measUnitList = measUnitRepository.findAll();
        assertThat(measUnitList).hasSize(databaseSizeBeforeCreate + 1);
        MeasUnit testMeasUnit = measUnitList.get(measUnitList.size() - 1);
        assertThat(testMeasUnit.getMeasUnit()).isEqualTo(DEFAULT_MEAS_UNIT);
        assertThat(testMeasUnit.getMeasDesc()).isEqualTo(DEFAULT_MEAS_DESC);
    }

    @Test
    @Transactional
    public void createMeasUnitWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = measUnitRepository.findAll().size();

        // Create the MeasUnit with an existing ID
        measUnit.setId(1L);
        MeasUnitDTO measUnitDTO = measUnitMapper.toDto(measUnit);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMeasUnitMockMvc.perform(post("/api/meas-units")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(measUnitDTO)))
            .andExpect(status().isBadRequest());

        // Validate the MeasUnit in the database
        List<MeasUnit> measUnitList = measUnitRepository.findAll();
        assertThat(measUnitList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllMeasUnits() throws Exception {
        // Initialize the database
        measUnitRepository.saveAndFlush(measUnit);

        // Get all the measUnitList
        restMeasUnitMockMvc.perform(get("/api/meas-units?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(measUnit.getId().intValue())))
            .andExpect(jsonPath("$.[*].measUnit").value(hasItem(DEFAULT_MEAS_UNIT)))
            .andExpect(jsonPath("$.[*].measDesc").value(hasItem(DEFAULT_MEAS_DESC)));
    }
    
    @Test
    @Transactional
    public void getMeasUnit() throws Exception {
        // Initialize the database
        measUnitRepository.saveAndFlush(measUnit);

        // Get the measUnit
        restMeasUnitMockMvc.perform(get("/api/meas-units/{id}", measUnit.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(measUnit.getId().intValue()))
            .andExpect(jsonPath("$.measUnit").value(DEFAULT_MEAS_UNIT))
            .andExpect(jsonPath("$.measDesc").value(DEFAULT_MEAS_DESC));
    }
    @Test
    @Transactional
    public void getNonExistingMeasUnit() throws Exception {
        // Get the measUnit
        restMeasUnitMockMvc.perform(get("/api/meas-units/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMeasUnit() throws Exception {
        // Initialize the database
        measUnitRepository.saveAndFlush(measUnit);

        int databaseSizeBeforeUpdate = measUnitRepository.findAll().size();

        // Update the measUnit
        MeasUnit updatedMeasUnit = measUnitRepository.findById(measUnit.getId()).get();
        // Disconnect from session so that the updates on updatedMeasUnit are not directly saved in db
        em.detach(updatedMeasUnit);
        updatedMeasUnit
            .measUnit(UPDATED_MEAS_UNIT)
            .measDesc(UPDATED_MEAS_DESC);
        MeasUnitDTO measUnitDTO = measUnitMapper.toDto(updatedMeasUnit);

        restMeasUnitMockMvc.perform(put("/api/meas-units")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(measUnitDTO)))
            .andExpect(status().isOk());

        // Validate the MeasUnit in the database
        List<MeasUnit> measUnitList = measUnitRepository.findAll();
        assertThat(measUnitList).hasSize(databaseSizeBeforeUpdate);
        MeasUnit testMeasUnit = measUnitList.get(measUnitList.size() - 1);
        assertThat(testMeasUnit.getMeasUnit()).isEqualTo(UPDATED_MEAS_UNIT);
        assertThat(testMeasUnit.getMeasDesc()).isEqualTo(UPDATED_MEAS_DESC);
    }

    @Test
    @Transactional
    public void updateNonExistingMeasUnit() throws Exception {
        int databaseSizeBeforeUpdate = measUnitRepository.findAll().size();

        // Create the MeasUnit
        MeasUnitDTO measUnitDTO = measUnitMapper.toDto(measUnit);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMeasUnitMockMvc.perform(put("/api/meas-units")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(measUnitDTO)))
            .andExpect(status().isBadRequest());

        // Validate the MeasUnit in the database
        List<MeasUnit> measUnitList = measUnitRepository.findAll();
        assertThat(measUnitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteMeasUnit() throws Exception {
        // Initialize the database
        measUnitRepository.saveAndFlush(measUnit);

        int databaseSizeBeforeDelete = measUnitRepository.findAll().size();

        // Delete the measUnit
        restMeasUnitMockMvc.perform(delete("/api/meas-units/{id}", measUnit.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MeasUnit> measUnitList = measUnitRepository.findAll();
        assertThat(measUnitList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
