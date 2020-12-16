package com.cheminv.app.service.mapper;


import com.cheminv.app.domain.*;
import com.cheminv.app.service.dto.OrderDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Order} and its DTO {@link OrderDTO}.
 */
@Mapper(componentModel = "spring", uses = {ItemStockMapper.class, UserMapper.class})
public interface OrderMapper extends EntityMapper<OrderDTO, Order> {

    @Mapping(source = "itemStock.id", target = "itemStockId")
    @Mapping(source = "requestedBy.id", target = "requestedById")
    @Mapping(source = "requestedBy.firstName", target = "requestedBy")
    @Mapping(source = "itemStock.itemName", target = "itemName")
    @Mapping(source = "itemStock.itemCapacity", target = "itemCapacity")
    @Mapping(source = "itemStock.storageUnit.measUnit", target = "storageUnit")
    OrderDTO toDto(Order order);

    @Mapping(source = "itemStockId", target = "itemStock")
    @Mapping(source = "requestedById", target = "requestedBy")
    Order toEntity(OrderDTO orderDTO);

    default Order fromId(Long id) {
        if (id == null) {
            return null;
        }
        Order order = new Order();
        order.setId(id);
        return order;
    }
}
