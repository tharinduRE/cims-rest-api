package com.cheminv.app.service.mapper;


import com.cheminv.app.domain.*;
import com.cheminv.app.service.dto.InvReportDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Report} and its DTO {@link InvReportDTO}.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface ReportMapper extends EntityMapper<InvReportDTO, Report> {

    @Mapping(source = "invUser.id", target = "invUserId")
    @Mapping(source = "invUser.firstName", target = "createdBy")
    InvReportDTO toDto(Report report);

    @Mapping(source = "invUserId", target = "invUser")
    Report toEntity(InvReportDTO invReportDTO);

    default Report fromId(Long id) {
        if (id == null) {
            return null;
        }
        Report report = new Report();
        report.setId(id);
        return report;
    }
}
