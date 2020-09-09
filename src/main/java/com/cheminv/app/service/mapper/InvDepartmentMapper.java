package com.cheminv.app.service.mapper;


import com.cheminv.app.domain.*;
import com.cheminv.app.service.dto.InvDepartmentDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link InvDepartment} and its DTO {@link InvDepartmentDTO}.
 */
@Mapper(componentModel = "spring", uses = {InvUserMapper.class})
public interface InvDepartmentMapper extends EntityMapper<InvDepartmentDTO, InvDepartment> {


    @Mapping(target = "invStorages", ignore = true)
    @Mapping(target = "removeInvStorage", ignore = true)
    @Mapping(target = "removeInvUser", ignore = true)
    InvDepartment toEntity(InvDepartmentDTO invDepartmentDTO);

    default InvDepartment fromId(Long id) {
        if (id == null) {
            return null;
        }
        InvDepartment invDepartment = new InvDepartment();
        invDepartment.setId(id);
        return invDepartment;
    }
}
