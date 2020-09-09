package com.cheminv.app.service.mapper;


import com.cheminv.app.domain.*;
import com.cheminv.app.service.dto.ItemManufacturerDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link ItemManufacturer} and its DTO {@link ItemManufacturerDTO}.
 */
@Mapper(componentModel = "spring", uses = {ItemMapper.class})
public interface ItemManufacturerMapper extends EntityMapper<ItemManufacturerDTO, ItemManufacturer> {

    @Mapping(source = "item.id", target = "itemId")
    ItemManufacturerDTO toDto(ItemManufacturer itemManufacturer);

    @Mapping(source = "itemId", target = "item")
    ItemManufacturer toEntity(ItemManufacturerDTO itemManufacturerDTO);

    default ItemManufacturer fromId(Long id) {
        if (id == null) {
            return null;
        }
        ItemManufacturer itemManufacturer = new ItemManufacturer();
        itemManufacturer.setId(id);
        return itemManufacturer;
    }
}
