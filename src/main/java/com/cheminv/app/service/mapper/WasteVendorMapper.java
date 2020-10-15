package com.cheminv.app.service.mapper;


import com.cheminv.app.domain.*;
import com.cheminv.app.service.dto.WasteVendorDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link WasteVendor} and its DTO {@link WasteVendorDTO}.
 */
@Mapper(componentModel = "spring", uses = {WasteItemMapper.class})
public interface WasteVendorMapper extends EntityMapper<WasteVendorDTO, WasteVendor> {


    @Mapping(target = "removeWasteItem", ignore = true)

    default WasteVendor fromId(Long id) {
        if (id == null) {
            return null;
        }
        WasteVendor wasteVendor = new WasteVendor();
        wasteVendor.setId(id);
        return wasteVendor;
    }
}
