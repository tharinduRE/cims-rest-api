package com.cheminv.app.service.mapper;


import com.cheminv.app.domain.*;
import com.cheminv.app.service.dto.InvDepartmentDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Department} and its DTO {@link InvDepartmentDTO}.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface DepartmentMapper extends EntityMapper<InvDepartmentDTO, Department> {


    @Mapping(target = "invStorages", ignore = true)
    @Mapping(target = "removeInvStorage", ignore = true)
    @Mapping(target = "removeInvUser", ignore = true)
    Department toEntity(InvDepartmentDTO invDepartmentDTO);

    default Department fromId(Long id) {
        if (id == null) {
            return null;
        }
        Department department = new Department();
        department.setId(id);
        return department;
    }
}
