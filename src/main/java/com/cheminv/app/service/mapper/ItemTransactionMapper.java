package com.cheminv.app.service.mapper;


import com.cheminv.app.domain.*;
import com.cheminv.app.service.dto.ItemTransactionDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link ItemTransaction} and its DTO {@link ItemTransactionDTO}.
 */
@Mapper(componentModel = "spring", uses = {ItemStockMapper.class})
public interface ItemTransactionMapper extends EntityMapper<ItemTransactionDTO, ItemTransaction> {

    @Mapping(source = "itemStock.id", target = "itemStockId")
    ItemTransactionDTO toDto(ItemTransaction itemTransaction);

    @Mapping(source = "itemStockId", target = "itemStock")
    ItemTransaction toEntity(ItemTransactionDTO itemTransactionDTO);

    default ItemTransaction fromId(Long id) {
        if (id == null) {
            return null;
        }
        ItemTransaction itemTransaction = new ItemTransaction();
        itemTransaction.setId(id);
        return itemTransaction;
    }
}
