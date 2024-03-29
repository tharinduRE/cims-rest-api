package com.cheminv.app.service.dto;

import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;

import com.cheminv.app.domain.Transaction;
import com.cheminv.app.domain.enumeration.TransactionType;

/**
 * A DTO for the {@link Transaction} entity.
 */
public class ItemTransactionDTO implements Serializable {

    private Long id;

    @NotNull
    private Float quantity;

    private String remarks;

    private TransactionType transactionType;

    private Instant transactionDate;

    private String issuerName;

    private Long itemStockId;

    private String itemStockName;

    private Float itemCapacity;

    private String  storageUnit;

    private Long createdById;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getQuantity() {
        return quantity;
    }

    public void setQuantity(Float quantity) {
        this.quantity = quantity;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public Instant getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Instant transactionDate) {
        this.transactionDate = transactionDate;
    }

    public Long getItemStockId() {
        return itemStockId;
    }

    public void setItemStockId(Long itemStockId) {
        this.itemStockId = itemStockId;
    }

    public Long getCreatedById() {
        return createdById;
    }

    public void setCreatedById(Long invUserId) {
        this.createdById = invUserId;
    }

    public String getIssuerName() {
        return issuerName;
    }

    public void setIssuerName(String issuerName) {
        this.issuerName = issuerName;
    }

    public String getItemStockName() {
        return itemStockName;
    }

    public void setItemStockName(String itemStockName) {
        this.itemStockName = itemStockName;
    }

    public Float getItemCapacity() {
        return itemCapacity;
    }

    public void setItemCapacity(Float itemCapacity) {
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
        if (!(o instanceof ItemTransactionDTO)) {
            return false;
        }

        return id != null && id.equals(((ItemTransactionDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ItemTransactionDTO{" +
            "id=" + getId() +
            ", quantity=" + getQuantity() +
            ", remarks='" + getRemarks() + "'" +
            ", transactionType='" + getTransactionType() + "'" +
            ", transactionDate='" + getTransactionDate() + "'" +
            ", itemStockId=" + getItemStockId() +
            ", createdById=" + getCreatedById() +
            "}";
    }
}
