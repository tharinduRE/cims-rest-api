package com.cheminv.app.service;

import com.cheminv.app.domain.InvReport;
import com.cheminv.app.repository.InvReportRepository;
import com.cheminv.app.service.dto.InvReportDTO;
import com.cheminv.app.service.mapper.InvReportMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link InvReport}.
 */
@Service
@Transactional
public class InvReportService {

    private final Logger log = LoggerFactory.getLogger(InvReportService.class);

    private final InvReportRepository invReportRepository;

    private final InvReportMapper invReportMapper;

    public InvReportService(InvReportRepository invReportRepository, InvReportMapper invReportMapper) {
        this.invReportRepository = invReportRepository;
        this.invReportMapper = invReportMapper;
    }

    /**
     * Save a invReport.
     *
     * @param invReportDTO the entity to save.
     * @return the persisted entity.
     */
    public InvReportDTO save(InvReportDTO invReportDTO) {
        log.debug("Request to save InvReport : {}", invReportDTO);
        InvReport invReport = invReportMapper.toEntity(invReportDTO);
        invReport = invReportRepository.save(invReport);
        return invReportMapper.toDto(invReport);
    }

    /**
     * Get all the invReports.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<InvReportDTO> findAll(Pageable pageable) {
        log.debug("Request to get all InvReports");
        return invReportRepository.findAll(pageable)
            .map(invReportMapper::toDto);
    }


    /**
     * Get one invReport by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<InvReportDTO> findOne(Long id) {
        log.debug("Request to get InvReport : {}", id);
        return invReportRepository.findById(id)
            .map(invReportMapper::toDto);
    }

    /**
     * Delete the invReport by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete InvReport : {}", id);
        invReportRepository.deleteById(id);
    }
}
