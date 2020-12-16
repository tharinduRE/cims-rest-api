package com.cheminv.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;

import com.cheminv.app.domain.enumeration.TransactionType;
import org.hibernate.annotations.CreationTimestamp;

/**
 * A ItemTransaction.
 */
@Entity
@Table(name = "cims_item_transaction")
public class ItemTransaction implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "quantity", nullable = false)
    private Float quantity;

    @Column(name = "remarks")
    private String remarks;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type")
    private TransactionType transactionType;

    @Column(name = "transaction_date")
    @CreationTimestamp
    private Instant transactionDate;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = "itemTransactions", allowSetters = true)
    private ItemStock itemStock;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = "itemTransactions", allowSetters = true)
    private User createdBy;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getQuantity() {
        return quantity;
    }

    public ItemTransaction quantity(Float quantity) {
        this.quantity = quantity;
        return this;
    }

    public void setQuantity(Float quantity) {
        this.quantity = quantity;
    }

    public String getRemarks() {
        return remarks;
    }

    public ItemTransaction remarks(String remarks) {
        this.remarks = remarks;
        return this;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public ItemTransaction transactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
        return this;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public Instant getTransactionDate() {
        return transactionDate;
    }

    public ItemTransaction transactionDate(Instant transactionDate) {
        this.transactionDate = transactionDate;
        return this;
    }

    public void setTransactionDate(Instant transactionDate) {
        this.transactionDate = transactionDate;
    }

    public ItemStock getItemStock() {
        return itemStock;
    }

    public ItemTransaction itemStock(ItemStock itemStock) {
        this.itemStock = itemStock;
        return this;
    }

    public void setItemStock(ItemStock itemStock) {
        this.itemStock = itemStock;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public ItemTransaction createdBy(User user) {
        this.createdBy = user;
        return this;
    }

    public void setCreatedBy(User user) {
        this.createdBy = user;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ItemTransaction)) {
            return false;
        }
        return id != null && id.equals(((ItemTransaction) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ItemTransaction{" +
            "id=" + getId() +
            ", quantity=" + getQuantity() +
            ", remarks='" + getRemarks() + "'" +
            ", transactionType='" + getTransactionType() + "'" +
            ", transactionDate='" + getTransactionDate() + "'" +
            "}";
    }
}
