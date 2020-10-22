package com.cheminv.app.service.mapper;


import com.cheminv.app.domain.*;
import com.cheminv.app.service.dto.InvReportDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link InvReport} and its DTO {@link InvReportDTO}.
 */
@Mapper(componentModel = "spring", uses = {InvUserMapper.class})
public interface InvReportMapper extends EntityMapper<InvReportDTO, InvReport> {

    @Mapping(source = "invUser.id", target = "invUserId")
    InvReportDTO toDto(InvReport invReport);

    @Mapping(source = "invUserId", target = "invUser")
    InvReport toEntity(InvReportDTO invReportDTO);

    default InvReport fromId(Long id) {
        if (id == null) {
            return null;
        }
        InvReport invReport = new InvReport();
        invReport.setId(id);
        return invReport;
    }
}
