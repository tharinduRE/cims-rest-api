package com.cheminv.app.service.dto;

import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import com.cheminv.app.domain.enumeration.OrderStatus;

/**
 * A DTO for the {@link com.cheminv.app.domain.Order} entity.
 */
public class OrderDTO implements Serializable {

    private Long id;

    private OrderStatus orderStatus;

    private Instant requestDate;

    private Instant orderDate;

    @NotNull
    private Float quantity;


    private Long itemStockId;

    private Long requestedById;

    private String requestedBy;

    private String itemName;

    private String itemCapacity;

    private String storageUnit;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Instant getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(Instant requestDate) {
        this.requestDate = requestDate;
    }

    public Instant getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Instant orderDate) {
        this.orderDate = orderDate;
    }

    public Float getQuantity() {
        return quantity;
    }

    public void setQuantity(Float quantity) {
        this.quantity = quantity;
    }

    public Long getItemStockId() {
        return itemStockId;
    }

    public void setItemStockId(Long itemStockId) {
        this.itemStockId = itemStockId;
    }

    public Long getRequestedById() {
        return requestedById;
    }

    public void setRequestedById(Long invUserId) {
        this.requestedById = invUserId;
    }

    public String getRequestedBy() {
        return requestedBy;
    }

    public void setRequestedBy(String requestedBy) {
        this.requestedBy = requestedBy;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemCapacity() {
        return itemCapacity;
    }

    public void setItemCapacity(String itemCapacity) {
        this.itemCapacity = itemCapacity;
    }

    public String getStorageUnit() {
        return storageUnit;
    }

    public void setStorageUnit(String storageUnit) {
        this.storageUnit = storageUnit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrderDTO)) {
            return false;
        }

        return id != null && id.equals(((OrderDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrderDTO{" +
            "id=" + getId() +
            ", orderStatus='" + getOrderStatus() + "'" +
            ", requestDate='" + getRequestDate() + "'" +
            ", orderDate='" + getOrderDate() + "'" +
            ", quantity=" + getQuantity() +
            ", itemStockId=" + getItemStockId() +
            ", requestedById=" + getRequestedById() +
            "}";
    }
}
