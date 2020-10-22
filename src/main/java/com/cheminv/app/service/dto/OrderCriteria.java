package com.cheminv.app.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import com.cheminv.app.domain.enumeration.OrderStatus;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.InstantFilter;

/**
 * Criteria class for the {@link com.cheminv.app.domain.Order} entity. This class is used
 * in {@link com.cheminv.app.web.rest.OrderResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /orders?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class OrderCriteria implements Serializable, Criteria {
    /**
     * Class for filtering OrderStatus
     */
    public static class OrderStatusFilter extends Filter<OrderStatus> {

        public OrderStatusFilter() {
        }

        public OrderStatusFilter(OrderStatusFilter filter) {
            super(filter);
        }

        @Override
        public OrderStatusFilter copy() {
            return new OrderStatusFilter(this);
        }

    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private OrderStatusFilter orderStatus;

    private InstantFilter requestDate;

    private InstantFilter orderDate;

    private FloatFilter quantity;

    private LongFilter itemStockId;

    private LongFilter requestedById;

    public OrderCriteria() {
    }

    public OrderCriteria(OrderCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.orderStatus = other.orderStatus == null ? null : other.orderStatus.copy();
        this.requestDate = other.requestDate == null ? null : other.requestDate.copy();
        this.orderDate = other.orderDate == null ? null : other.orderDate.copy();
        this.quantity = other.quantity == null ? null : other.quantity.copy();
        this.itemStockId = other.itemStockId == null ? null : other.itemStockId.copy();
        this.requestedById = other.requestedById == null ? null : other.requestedById.copy();
    }

    @Override
    public OrderCriteria copy() {
        return new OrderCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public OrderStatusFilter getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatusFilter orderStatus) {
        this.orderStatus = orderStatus;
    }

    public InstantFilter getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(InstantFilter requestDate) {
        this.requestDate = requestDate;
    }

    public InstantFilter getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(InstantFilter orderDate) {
        this.orderDate = orderDate;
    }

    public FloatFilter getQuantity() {
        return quantity;
    }

    public void setQuantity(FloatFilter quantity) {
        this.quantity = quantity;
    }

    public LongFilter getItemStockId() {
        return itemStockId;
    }

    public void setItemStockId(LongFilter itemStockId) {
        this.itemStockId = itemStockId;
    }

    public LongFilter getRequestedById() {
        return requestedById;
    }

    public void setRequestedById(LongFilter requestedById) {
        this.requestedById = requestedById;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final OrderCriteria that = (OrderCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(orderStatus, that.orderStatus) &&
            Objects.equals(requestDate, that.requestDate) &&
            Objects.equals(orderDate, that.orderDate) &&
            Objects.equals(quantity, that.quantity) &&
            Objects.equals(itemStockId, that.itemStockId) &&
            Objects.equals(requestedById, that.requestedById);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        orderStatus,
        requestDate,
        orderDate,
        quantity,
        itemStockId,
        requestedById
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrderCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (orderStatus != null ? "orderStatus=" + orderStatus + ", " : "") +
                (requestDate != null ? "requestDate=" + requestDate + ", " : "") +
                (orderDate != null ? "orderDate=" + orderDate + ", " : "") +
                (quantity != null ? "quantity=" + quantity + ", " : "") +
                (itemStockId != null ? "itemStockId=" + itemStockId + ", " : "") +
                (requestedById != null ? "requestedById=" + requestedById + ", " : "") +
            "}";
    }

}
