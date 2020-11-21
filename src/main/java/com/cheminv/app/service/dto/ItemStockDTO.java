package com.cheminv.app.service.dto;

import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Lob;
import com.cheminv.app.domain.enumeration.ItemStatus;
import com.cheminv.app.domain.enumeration.StockStore;

/**
 * A DTO for the {@link com.cheminv.app.domain.ItemStock} entity.
 */
public class ItemStockDTO implements Serializable {

    private Long id;

    @NotNull
    private String itemName;

    private String casNumber;

    private String stockBookFolio;

    private String itemManufacturer;

    private Float itemCapacity;

    private Float unitPrice;

    private Float totalQuantity;

    private Float minimumQuantity;

    private ItemStatus itemStatus;

    private StockStore stockStore;

    private String store;

    private Integer creatorId;

    private Instant createdOn;

    private Instant lastUpdated;

    @Lob
    private byte[] sdsfile;

    private String sdsfileContentType;
    private Set<HazardCodeDTO> hazardCodes = new HashSet<>();

    private Long invStorageId;

    private Long storageUnitId;

    private String storageUnit;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getCasNumber() {
        return casNumber;
    }

    public void setCasNumber(String casNumber) {
        this.casNumber = casNumber;
    }

    public String getStockBookFolio() {
        return stockBookFolio;
    }

    public void setStockBookFolio(String stockBookFolio) {
        this.stockBookFolio = stockBookFolio;
    }

    public String getItemManufacturer() {
        return itemManufacturer;
    }

    public void setItemManufacturer(String itemManufacturer) {
        this.itemManufacturer = itemManufacturer;
    }

    public Float getItemCapacity() {
        return itemCapacity;
    }

    public void setItemCapacity(Float itemCapacity) {
        this.itemCapacity = itemCapacity;
    }

    public Float getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Float unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Float getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(Float totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public Float getMinimumQuantity() {
        return minimumQuantity;
    }

    public void setMinimumQuantity(Float minimumQuantity) {
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

    public Instant getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Instant lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public byte[] getSdsfile() {
        return sdsfile;
    }

    public void setSdsfile(byte[] sdsfile) {
        this.sdsfile = sdsfile;
    }

    public String getSdsfileContentType() {
        return sdsfileContentType;
    }

    public void setSdsfileContentType(String sdsfileContentType) {
        this.sdsfileContentType = sdsfileContentType;
    }

    public Set<HazardCodeDTO> getHazardCodes() {
        return hazardCodes;
    }

    public void setHazardCodes(Set<HazardCodeDTO> hazardCodes) {
        this.hazardCodes = hazardCodes;
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

    public String getStorageUnit() {
        return storageUnit;
    }

    public void setStorageUnit(String storageUnit) {
        this.storageUnit = storageUnit;
    }

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }

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
            ", itemName='" + getItemName() + "'" +
            ", casNumber='" + getCasNumber() + "'" +
            ", stockBookFolio='" + getStockBookFolio() + "'" +
            ", itemManufacturer='" + getItemManufacturer() + "'" +
            ", itemCapacity=" + getItemCapacity() +
            ", unitPrice=" + getUnitPrice() +
            ", totalQuantity=" + getTotalQuantity() +
            ", minimumQuantity=" + getMinimumQuantity() +
            ", itemStatus='" + getItemStatus() + "'" +
            ", stockStore='" + getStockStore() + "'" +
            ", creatorId=" + getCreatorId() +
            ", createdOn='" + getCreatedOn() + "'" +
            ", lastUpdated='" + getLastUpdated() + "'" +
            ", sdsfile='" + getSdsfile() + "'" +
            ", hazardCodes='" + getHazardCodes() + "'" +
            ", invStorageId=" + getInvStorageId() +
            ", storageUnitId=" + getStorageUnitId() +
            "}";
    }
}
