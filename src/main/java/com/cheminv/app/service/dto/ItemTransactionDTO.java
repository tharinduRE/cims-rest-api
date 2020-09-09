package com.cheminv.app.service.dto;

import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.UUID;

/**
 * A DTO for the {@link com.cheminv.app.domain.ItemTransaction} entity.
 */
public class ItemTransactionDTO implements Serializable {
    
    private Long id;

    @NotNull
    private Integer quantity;

    private String remarks;

    private UUID transactionUUID;

    private LocalDate transactionDate;

    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public UUID getTransactionUUID() {
        return transactionUUID;
    }

    public void setTransactionUUID(UUID transactionUUID) {
        this.transactionUUID = transactionUUID;
    }

    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
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
            ", transactionUUID='" + getTransactionUUID() + "'" +
            ", transactionDate='" + getTransactionDate() + "'" +
            "}";
    }
}
