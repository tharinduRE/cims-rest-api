package com.cheminv.app.service.dto;

import java.time.Instant;
import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import com.cheminv.app.domain.enumeration.ItemStatus;
import com.cheminv.app.domain.enumeration.StockStore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A DTO for the {@link com.cheminv.app.domain.ItemStock} entity.
 */
public class ItemStockDTO implements Serializable {

    private Long id;

    private Integer totalQuantity;

    private Integer minimumQuantity;

    private ItemStatus itemStatus;

    private StockStore stockStore;

    private LocalDate entryDate;

    private LocalDate expiryDate;

    private Integer creatorId;

    private Instant createdOn;

    private String sdsfile;

    private Set<ItemTransactionDTO> itemTransactions = new HashSet<>();

    private Long invStorageId;

    private Long storageUnitId;

    public ItemDTO getItem() {
        return item;
    }

    public void setItem(ItemDTO item) {
        this.item = item;
    }

    private Long itemId;

    @JsonProperty("item")
    private ItemDTO item;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(Integer totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public Integer getMinimumQuantity() {
        return minimumQuantity;
    }

    public void setMinimumQuantity(Integer minimumQuantity) {
        this.minimumQuantity = minimumQuantity;
    }

    public ItemStatus getItemStatus() {
        return itemStatus;
    }

    public void setItemStatus(ItemStatus itemStatus) {
        this.itemStatus = itemStatus;
    }

    public StockStore getStockStore() {
        return stockStore;
    }

    public void setStockStore(StockStore stockStore) {
        this.stockStore = stockStore;
    }

    public LocalDate getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(LocalDate entryDate) {
        this.entryDate = entryDate;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Integer getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Integer creatorId) {
        this.creatorId = creatorId;
    }

    public Instant getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Instant createdOn) {
        this.createdOn = createdOn;
    }

    public String getSdsfile() {
        return sdsfile;
    }

    public void setSdsfile(String sdsfile) {
        this.sdsfile = sdsfile;
    }

    public Set<ItemTransactionDTO> getItemTransactions() {
        return itemTransactions;
    }

    public void setItemTransactions(Set<ItemTransactionDTO> itemTransactions) {
        this.itemTransactions = itemTransactions;
    }

    public Long getInvStorageId() {
        return invStorageId;
    }

    public void setInvStorageId(Long invStorageId) {
        this.invStorageId = invStorageId;
    }

    public Long getStorageUnitId() {
        return storageUnitId;
    }

    public void setStorageUnitId(Long measUnitId) {
        this.storageUnitId = measUnitId;
    }

   /* public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }*/

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ItemStockDTO)) {
            return false;
        }

        return id != null && id.equals(((ItemStockDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ItemStockDTO{" +
            "id=" + getId() +
            ", totalQuantity=" + getTotalQuantity() +
            ", minimumQuantity=" + getMinimumQuantity() +
            ", itemStatus='" + getItemStatus() + "'" +
            ", stockStore='" + getStockStore() + "'" +
            ", entryDate='" + getEntryDate() + "'" +
            ", expiryDate='" + getExpiryDate() + "'" +
            ", creatorId=" + getCreatorId() +
            ", createdOn='" + getCreatedOn() + "'" +
            ", sdsfile='" + getSdsfile() + "'" +
            ", itemTransactions='" + getItemTransactions() + "'" +
            ", invStorageId=" + getInvStorageId() +
            ", storageUnitId=" + getStorageUnitId() +
            //", itemId=" + getItemId() +
            "}";
    }
}
