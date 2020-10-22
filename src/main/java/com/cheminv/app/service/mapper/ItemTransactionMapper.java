package com.cheminv.app.service.mapper;


import com.cheminv.app.domain.*;
import com.cheminv.app.service.dto.ItemTransactionDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link ItemTransaction} and its DTO {@link ItemTransactionDTO}.
 */
@Mapper(componentModel = "spring", uses = {ItemStockMapper.class, InvUserMapper.class})
public interface ItemTransactionMapper extends EntityMapper<ItemTransactionDTO, ItemTransaction> {

    @Mapping(source = "itemStock.id", target = "itemStockId")
    @Mapping(source = "createdBy.id", target = "createdById")
    @Mapping(source = "createdBy.firstName", target = "issuerName")
    @Mapping(source = "itemStock.itemName", target = "itemStockName")
    @Mapping(source = "itemStock.itemCapacity", target = "itemCapacity")
    @Mapping(source = "itemStock.storageUnit.measUnit", target = "storageUnit")
    ItemTransactionDTO toDto(ItemTransaction itemTransaction);

    @Mapping(source = "itemStockId", target = "itemStock")
    @Mapping(source = "createdById", target = "createdBy")
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
