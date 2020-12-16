package com.cheminv.app.service.mapper;


import com.cheminv.app.domain.*;
import com.cheminv.app.service.dto.ItemTransactionDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Transaction} and its DTO {@link ItemTransactionDTO}.
 */
@Mapper(componentModel = "spring", uses = {ItemStockMapper.class, UserMapper.class})
public interface TransactionMapper extends EntityMapper<ItemTransactionDTO, Transaction> {

    @Mapping(source = "itemStock.id", target = "itemStockId")
    @Mapping(source = "createdBy.id", target = "createdById")
    @Mapping(source = "createdBy.firstName", target = "issuerName")
    @Mapping(source = "itemStock.itemName", target = "itemStockName")
    @Mapping(source = "itemStock.itemCapacity", target = "itemCapacity")
    @Mapping(source = "itemStock.storageUnit.measUnit", target = "storageUnit")
    ItemTransactionDTO toDto(Transaction transaction);

    @Mapping(source = "itemStockId", target = "itemStock")
    @Mapping(source = "createdById", target = "createdBy")
    Transaction toEntity(ItemTransactionDTO itemTransactionDTO);

    default Transaction fromId(Long id) {
        if (id == null) {
            return null;
        }
        Transaction transaction = new Transaction();
        transaction.setId(id);
        return transaction;
    }
}
