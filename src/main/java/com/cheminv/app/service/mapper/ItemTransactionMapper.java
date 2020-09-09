package com.cheminv.app.service.mapper;


import com.cheminv.app.domain.*;
import com.cheminv.app.service.dto.ItemTransactionDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link ItemTransaction} and its DTO {@link ItemTransactionDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ItemTransactionMapper extends EntityMapper<ItemTransactionDTO, ItemTransaction> {


    @Mapping(target = "itemStocks", ignore = true)
    @Mapping(target = "removeItemStock", ignore = true)
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
