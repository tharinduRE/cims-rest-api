package com.cheminv.app.service.mapper;


import com.cheminv.app.domain.*;
import com.cheminv.app.service.dto.InvUserDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link InvUser} and its DTO {@link InvUserDTO}.
 */
@Mapper(componentModel = "spring")
public interface InvUserMapper extends EntityMapper<InvUserDTO, InvUser> {


    @Mapping(target = "itemTransactions", ignore = true)
    @Mapping(target = "removeItemTransaction", ignore = true)
    @Mapping(target = "invDepartments", ignore = true)
    @Mapping(target = "removeInvDepartment", ignore = true)
    @Mapping(target = "invReports", ignore = true)
    @Mapping(target = "removeInvReport", ignore = true)
    @Mapping(target = "removeAuthority", ignore = true)
    @Mapping(target = "removeInvStore", ignore = true)
    @Mapping(target = "itemOrders", ignore = true)
    @Mapping(target = "removeItemOrders", ignore = true)
    InvUser toEntity(InvUserDTO invUserDTO);

    default InvUser fromId(Long id) {
        if (id == null) {
            return null;
        }
        InvUser invUser = new InvUser();
        invUser.setId(id);
        return invUser;
    }
}
