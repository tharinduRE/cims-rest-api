package com.cheminv.app.service.mapper;


import com.cheminv.app.domain.*;
import com.cheminv.app.service.dto.HazardCodeDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link HazardCode} and its DTO {@link HazardCodeDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface HazardCodeMapper extends EntityMapper<HazardCodeDTO, HazardCode> {


    @Mapping(target = "itemStocks", ignore = true)
    @Mapping(target = "removeItemStock", ignore = true)
    HazardCode toEntity(HazardCodeDTO hazardCodeDTO);

    default HazardCode fromId(Long id) {
        if (id == null) {
            return null;
        }
        HazardCode hazardCode = new HazardCode();
        hazardCode.setId(id);
        return hazardCode;
    }
}
