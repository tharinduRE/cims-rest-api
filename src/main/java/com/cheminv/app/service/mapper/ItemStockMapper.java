package com.cheminv.app.service.mapper;


import com.cheminv.app.domain.*;
import com.cheminv.app.service.dto.ItemStockDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link ItemStock} and its DTO {@link ItemStockDTO}.
 */
@Mapper(componentModel = "spring", uses = {ItemTransactionMapper.class, InvStorageMapper.class, MeasUnitMapper.class, ItemMapper.class})
public interface ItemStockMapper extends EntityMapper<ItemStockDTO, ItemStock> {

    @Mapping(source = "invStorage.id", target = "invStorageId")
    @Mapping(source = "storageUnit.id", target = "storageUnitId")
    //@Mapping(source = "item.id", target = "itemId")
    ItemStockDTO toDto(ItemStock itemStock);

    @Mapping(target = "removeItemTransaction", ignore = true)
    @Mapping(source = "invStorageId", target = "invStorage")
    @Mapping(source = "storageUnitId", target = "storageUnit")
    @Mapping(source = "item", target = "item")
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
