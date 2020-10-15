package com.cheminv.app.service.mapper;


import com.cheminv.app.domain.*;
import com.cheminv.app.service.dto.WasteItemDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link WasteItem} and its DTO {@link WasteItemDTO}.
 */
@Mapper(componentModel = "spring", uses = {ItemStockMapper.class})
public interface WasteItemMapper extends EntityMapper<WasteItemDTO, WasteItem> {

    @Mapping(source = "itemStock.id", target = "itemStockId")
    WasteItemDTO toDto(WasteItem wasteItem);

    @Mapping(source = "itemStockId", target = "itemStock")
    @Mapping(target = "wasteVendors", ignore = true)
    @Mapping(target = "removeWasteVendor", ignore = true)
    WasteItem toEntity(WasteItemDTO wasteItemDTO);

    default WasteItem fromId(Long id) {
        if (id == null) {
            return null;
        }
        WasteItem wasteItem = new WasteItem();
        wasteItem.setId(id);
        return wasteItem;
    }
}
