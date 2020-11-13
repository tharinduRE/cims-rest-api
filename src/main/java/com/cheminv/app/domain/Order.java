package com.cheminv.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;

import com.cheminv.app.domain.enumeration.OrderStatus;

/**
 * A Order.
 */
@Entity
@Table(name = "cims_orders")
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status")
    private OrderStatus orderStatus;

    @Column(name = "request_date")
    private Instant requestDate;

    @Column(name = "order_date")
    private Instant orderDate;

    @NotNull
    @Column(name = "quantity", nullable = false)
    private Float quantity;

    @Column(name = "cancel_date")
    private Instant cancelDate;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = "orders", allowSetters = true)
    private ItemStock itemStock;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = "orders", allowSetters = true)
    private InvUser requestedBy;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public Order orderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
        return this;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Instant getRequestDate() {
        return requestDate;
    }

    public Order requestDate(Instant requestDate) {
        this.requestDate = requestDate;
        return this;
    }

    public void setRequestDate(Instant requestDate) {
        this.requestDate = requestDate;
    }

    public Instant getOrderDate() {
        return orderDate;
    }

    public Order orderDate(Instant orderDate) {
        this.orderDate = orderDate;
        return this;
    }

    public void setOrderDate(Instant orderDate) {
        this.orderDate = orderDate;
    }

    public Float getQuantity() {
        return quantity;
    }

    public Order quantity(Float quantity) {
        this.quantity = quantity;
        return this;
    }

    public void setQuantity(Float quantity) {
        this.quantity = quantity;
    }

    public Instant getCancelDate() {
        return cancelDate;
    }

    public Order cancelDate(Instant cancelDate) {
        this.cancelDate = cancelDate;
        return this;
    }

    public void setCancelDate(Instant cancelDate) {
        this.cancelDate = cancelDate;
    }

    public ItemStock getItemStock() {
        return itemStock;
    }

    public Order itemStock(ItemStock itemStock) {
        this.itemStock = itemStock;
        return this;
    }

    public void setItemStock(ItemStock itemStock) {
        this.itemStock = itemStock;
    }

    public InvUser getRequestedBy() {
        return requestedBy;
    }

    public Order requestedBy(InvUser invUser) {
        this.requestedBy = invUser;
        return this;
    }

    public void setRequestedBy(InvUser invUser) {
        this.requestedBy = invUser;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Order)) {
            return false;
        }
        return id != null && id.equals(((Order) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Order{" +
            "id=" + getId() +
            ", orderStatus='" + getOrderStatus() + "'" +
            ", requestDate='" + getRequestDate() + "'" +
            ", orderDate='" + getOrderDate() + "'" +
            ", quantity=" + getQuantity() +
            ", cancelDate='" + getCancelDate() + "'" +
            "}";
    }
}
