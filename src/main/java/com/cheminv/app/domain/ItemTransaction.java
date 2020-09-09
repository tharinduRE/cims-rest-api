package com.cheminv.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * A ItemTransaction.
 */
@Entity
@Table(name = "cims_item_transaction")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "itemtransaction")
public class ItemTransaction implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "remarks")
    private String remarks;

    @Type(type = "uuid-char")
    @Column(name = "transaction_uuid", length = 36)
    private UUID transactionUUID;

    @Column(name = "transaction_date")
    private LocalDate transactionDate;

    @ManyToMany(mappedBy = "itemTransactions")
    @JsonIgnore
    private Set<ItemStock> itemStocks = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public ItemTransaction quantity(Integer quantity) {
        this.quantity = quantity;
        return this;
    }

    public void setQuantity(Integer quantity) {
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

    public UUID getTransactionUUID() {
        return transactionUUID;
    }

    public ItemTransaction transactionUUID(UUID transactionUUID) {
        this.transactionUUID = transactionUUID;
        return this;
    }

    public void setTransactionUUID(UUID transactionUUID) {
        this.transactionUUID = transactionUUID;
    }

    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    public ItemTransaction transactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
        return this;
    }

    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
    }

    public Set<ItemStock> getItemStocks() {
        return itemStocks;
    }

    public ItemTransaction itemStocks(Set<ItemStock> itemStocks) {
        this.itemStocks = itemStocks;
        return this;
    }

    public ItemTransaction addItemStock(ItemStock itemStock) {
        this.itemStocks.add(itemStock);
        itemStock.getItemTransactions().add(this);
        return this;
    }

    public ItemTransaction removeItemStock(ItemStock itemStock) {
        this.itemStocks.remove(itemStock);
        itemStock.getItemTransactions().remove(this);
        return this;
    }

    public void setItemStocks(Set<ItemStock> itemStocks) {
        this.itemStocks = itemStocks;
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
            ", transactionUUID='" + getTransactionUUID() + "'" +
            ", transactionDate='" + getTransactionDate() + "'" +
            "}";
    }
}
