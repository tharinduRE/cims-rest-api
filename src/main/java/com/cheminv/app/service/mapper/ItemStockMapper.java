package com.cheminv.app.service.mapper;


import com.cheminv.app.domain.*;
import com.cheminv.app.service.dto.ItemStockDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link ItemStock} and its DTO {@link ItemStockDTO}.
 */
@Mapper(componentModel = "spring", uses = {HazardCodeMapper.class, InvStorageMapper.class, MeasUnitMapper.class})
public interface ItemStockMapper extends EntityMapper<ItemStockDTO, ItemStock> {

    @Mapping(source = "invStorage.id", target = "invStorageId")
    @Mapping(source = "storageUnit.id", target = "storageUnitId")
    @Mapping(source = "storageUnit.measUnit", target = "storageUnit")
    ItemStockDTO toDto(ItemStock itemStock);

    @Mapping(target = "itemTransactions", ignore = true)
    @Mapping(target = "removeItemTransaction", ignore = true)
    @Mapping(target = "wasteItems", ignore = true)
    @Mapping(target = "removeWasteItem", ignore = true)
    @Mapping(target = "removeHazardCode", ignore = true)
    @Mapping(source = "invStorageId", target = "invStorage")
    @Mapping(source = "storageUnitId", target = "storageUnit")
    ItemStock toEntity(ItemStockDTO itemStockDTO);

    default ItemStock fromId(Long id) {
        if (id == null) {
            return null;
        }
        ItemStock itemStock = new ItemStock();
        itemStock.setId(id);
        return itemStock;
    }
}
