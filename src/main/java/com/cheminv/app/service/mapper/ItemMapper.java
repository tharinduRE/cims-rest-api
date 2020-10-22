package com.cheminv.app.service.mapper;


import com.cheminv.app.domain.*;
import com.cheminv.app.service.dto.ItemDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Item} and its DTO {@link ItemDTO}.
 */
@Mapper(componentModel = "spring", uses = {HazardCodeMapper.class})
public interface ItemMapper extends EntityMapper<ItemDTO, Item> {


    @Mapping(target = "itemStocks", ignore = true)
    @Mapping(target = "removeItemStock", ignore = true)
    @Mapping(target = "removeHazardCode", ignore = true)
    Item toEntity(ItemDTO itemDTO);

    default Item fromId(Long id) {
        if (id == null) {
            return null;
        }
        Item item = new Item();
        item.setId(id);
        return item;
    }
}
