package com.cheminv.app.service.mapper;


import com.cheminv.app.domain.*;
import com.cheminv.app.service.dto.MeasUnitDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link MeasUnit} and its DTO {@link MeasUnitDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface MeasUnitMapper extends EntityMapper<MeasUnitDTO, MeasUnit> {


    @Mapping(target = "itemStocks", ignore = true)
    @Mapping(target = "removeItemStock", ignore = true)
    MeasUnit toEntity(MeasUnitDTO measUnitDTO);

    default MeasUnit fromId(Long id) {
        if (id == null) {
            return null;
        }
        MeasUnit measUnit = new MeasUnit();
        measUnit.setId(id);
        return measUnit;
    }
}
