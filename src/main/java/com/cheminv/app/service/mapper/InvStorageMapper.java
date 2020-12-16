package com.cheminv.app.service.mapper;


import com.cheminv.app.domain.*;
import com.cheminv.app.service.dto.InvStorageDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link InvStorage} and its DTO {@link InvStorageDTO}.
 */
@Mapper(componentModel = "spring", uses = {DepartmentMapper.class})
public interface InvStorageMapper extends EntityMapper<InvStorageDTO, InvStorage> {

    @Mapping(source = "department.id", target = "departmentId")
    InvStorageDTO toDto(InvStorage invStorage);

    @Mapping(target = "itemStocks", ignore = true)
    @Mapping(target = "removeItemStock", ignore = true)
    @Mapping(source = "departmentId", target = "department")
    InvStorage toEntity(InvStorageDTO invStorageDTO);

    default InvStorage fromId(Long id) {
        if (id == null) {
            return null;
        }
        InvStorage invStorage = new InvStorage();
        invStorage.setId(id);
        return invStorage;
    }
}
