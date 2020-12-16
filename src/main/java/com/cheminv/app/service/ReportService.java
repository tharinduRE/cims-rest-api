package com.cheminv.app.service;

import com.cheminv.app.domain.Report;
import com.cheminv.app.repository.ReportRepository;
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
 * Service Implementation for managing {@link Report}.
 */
@Service
@Transactional
public class ReportService {

    private final Logger log = LoggerFactory.getLogger(ReportService.class);

    private final ReportRepository reportRepository;

    private final InvReportMapper invReportMapper;

    public ReportService(ReportRepository reportRepository, InvReportMapper invReportMapper) {
        this.reportRepository = reportRepository;
        this.invReportMapper = invReportMapper;
    }

    /**
     * Save a invReport.
     *
     * @param invReportDTO the entity to save.
     * @return the persisted entity.
     */
    public InvReportDTO save(InvReportDTO invReportDTO) {
        log.debug("Request to save Report : {}", invReportDTO);
        Report report = invReportMapper.toEntity(invReportDTO);
        report = reportRepository.save(report);
        return invReportMapper.toDto(report);
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
        return reportRepository.findAll(pageable)
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
        log.debug("Request to get Report : {}", id);
        return reportRepository.findById(id)
            .map(invReportMapper::toDto);
    }

    /**
     * Delete the invReport by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Report : {}", id);
        reportRepository.deleteById(id);
    }
}
