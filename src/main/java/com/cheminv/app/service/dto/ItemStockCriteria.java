package com.cheminv.app.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import com.cheminv.app.domain.enumeration.ItemStatus;
import com.cheminv.app.domain.enumeration.StockStore;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.InstantFilter;

/**
 * Criteria class for the {@link com.cheminv.app.domain.ItemStock} entity. This class is used
 * in {@link com.cheminv.app.web.rest.ItemStockResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /item-stocks?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ItemStockCriteria implements Serializable, Criteria {
    /**
     * Class for filtering ItemStatus
     */
    public static class ItemStatusFilter extends Filter<ItemStatus> {

        public ItemStatusFilter() {
        }

        public ItemStatusFilter(ItemStatusFilter filter) {
            super(filter);
        }

        @Override
        public ItemStatusFilter copy() {
            return new ItemStatusFilter(this);
        }

    }
    /**
     * Class for filtering StockStore
     */
    public static class StockStoreFilter extends Filter<StockStore> {

        public StockStoreFilter() {
        }

        public StockStoreFilter(StockStoreFilter filter) {
            super(filter);
        }

        @Override
        public StockStoreFilter copy() {
            return new StockStoreFilter(this);
        }

    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter itemName;

    private StringFilter casNumber;

    private StringFilter stockBookFolio;

    private StringFilter itemManufacturer;

    private FloatFilter itemCapacity;

    private FloatFilter unitPrice;

    private FloatFilter totalQuantity;

    private FloatFilter minimumQuantity;

    private ItemStatusFilter itemStatus;

    private StockStoreFilter stockStore;

    private IntegerFilter creatorId;

    private InstantFilter createdOn;

    private InstantFilter lastUpdated;

    private LongFilter itemTransactionId;

    private LongFilter wasteItemId;

    private LongFilter hazardCodeId;

    private LongFilter invStorageId;

    private LongFilter storageUnitId;

    private LongFilter itemOrdersId;

    public ItemStockCriteria() {
    }

    public ItemStockCriteria(ItemStockCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.itemName = other.itemName == null ? null : other.itemName.copy();
        this.casNumber = other.casNumber == null ? null : other.casNumber.copy();
        this.stockBookFolio = other.stockBookFolio == null ? null : other.stockBookFolio.copy();
        this.itemManufacturer = other.itemManufacturer == null ? null : other.itemManufacturer.copy();
        this.itemCapacity = other.itemCapacity == null ? null : other.itemCapacity.copy();
        this.unitPrice = other.unitPrice == null ? null : other.unitPrice.copy();
        this.totalQuantity = other.totalQuantity == null ? null : other.totalQuantity.copy();
        this.minimumQuantity = other.minimumQuantity == null ? null : other.minimumQuantity.copy();
        this.itemStatus = other.itemStatus == null ? null : other.itemStatus.copy();
        this.stockStore = other.stockStore == null ? null : other.stockStore.copy();
        this.creatorId = other.creatorId == null ? null : other.creatorId.copy();
        this.createdOn = other.createdOn == null ? null : other.createdOn.copy();
        this.lastUpdated = other.lastUpdated == null ? null : other.lastUpdated.copy();
        this.itemTransactionId = other.itemTransactionId == null ? null : other.itemTransactionId.copy();
        this.wasteItemId = other.wasteItemId == null ? null : other.wasteItemId.copy();
        this.hazardCodeId = other.hazardCodeId == null ? null : other.hazardCodeId.copy();
        this.invStorageId = other.invStorageId == null ? null : other.invStorageId.copy();
        this.storageUnitId = other.storageUnitId == null ? null : other.storageUnitId.copy();
        this.itemOrdersId = other.itemOrdersId == null ? null : other.itemOrdersId.copy();
    }

    @Override
    public ItemStockCriteria copy() {
        return new ItemStockCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getItemName() {
        return itemName;
    }

    public void setItemName(StringFilter itemName) {
        this.itemName = itemName;
    }

    public StringFilter getCasNumber() {
        return casNumber;
    }

    public void setCasNumber(StringFilter casNumber) {
        this.casNumber = casNumber;
    }

    public StringFilter getStockBookFolio() {
        return stockBookFolio;
    }

    public void setStockBookFolio(StringFilter stockBookFolio) {
        this.stockBookFolio = stockBookFolio;
    }

    public StringFilter getItemManufacturer() {
        return itemManufacturer;
    }

    public void setItemManufacturer(StringFilter itemManufacturer) {
        this.itemManufacturer = itemManufacturer;
    }

    public FloatFilter getItemCapacity() {
        return itemCapacity;
    }

    public void setItemCapacity(FloatFilter itemCapacity) {
        this.itemCapacity = itemCapacity;
    }

    public FloatFilter getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(FloatFilter unitPrice) {
        this.unitPrice = unitPrice;
    }

    public FloatFilter getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(FloatFilter totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public FloatFilter getMinimumQuantity() {
        return minimumQuantity;
    }

    public void setMinimumQuantity(FloatFilter minimumQuantity) {
        this.minimumQuantity = minimumQuantity;
    }

    public ItemStatusFilter getItemStatus() {
        return itemStatus;
    }

    public void setItemStatus(ItemStatusFilter itemStatus) {
        this.itemStatus = itemStatus;
    }

    public StockStoreFilter getStockStore() {
        return stockStore;
    }

    public void setStockStore(StockStoreFilter stockStore) {
        this.stockStore = stockStore;
    }

    public IntegerFilter getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(IntegerFilter creatorId) {
        this.creatorId = creatorId;
    }

    public InstantFilter getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(InstantFilter createdOn) {
        this.createdOn = createdOn;
    }

    public InstantFilter getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(InstantFilter lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public LongFilter getItemTransactionId() {
        return itemTransactionId;
    }

    public void setItemTransactionId(LongFilter itemTransactionId) {
        this.itemTransactionId = itemTransactionId;
    }

    public LongFilter getWasteItemId() {
        return wasteItemId;
    }

    public void setWasteItemId(LongFilter wasteItemId) {
        this.wasteItemId = wasteItemId;
    }

    public LongFilter getHazardCodeId() {
        return hazardCodeId;
    }

    public void setHazardCodeId(LongFilter hazardCodeId) {
        this.hazardCodeId = hazardCodeId;
    }

    public LongFilter getInvStorageId() {
        return invStorageId;
    }

    public void setInvStorageId(LongFilter invStorageId) {
        this.invStorageId = invStorageId;
    }

    public LongFilter getStorageUnitId() {
        return storageUnitId;
    }

    public void setStorageUnitId(LongFilter storageUnitId) {
        this.storageUnitId = storageUnitId;
    }

    public LongFilter getItemOrdersId() {
        return itemOrdersId;
    }

    public void setItemOrdersId(LongFilter itemOrdersId) {
        this.itemOrdersId = itemOrdersId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ItemStockCriteria that = (ItemStockCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(itemName, that.itemName) &&
            Objects.equals(casNumber, that.casNumber) &&
            Objects.equals(stockBookFolio, that.stockBookFolio) &&
            Objects.equals(itemManufacturer, that.itemManufacturer) &&
            Objects.equals(itemCapacity, that.itemCapacity) &&
            Objects.equals(unitPrice, that.unitPrice) &&
            Objects.equals(totalQuantity, that.totalQuantity) &&
            Objects.equals(minimumQuantity, that.minimumQuantity) &&
            Objects.equals(itemStatus, that.itemStatus) &&
            Objects.equals(stockStore, that.stockStore) &&
            Objects.equals(creatorId, that.creatorId) &&
            Objects.equals(createdOn, that.createdOn) &&
            Objects.equals(lastUpdated, that.lastUpdated) &&
            Objects.equals(itemTransactionId, that.itemTransactionId) &&
            Objects.equals(wasteItemId, that.wasteItemId) &&
            Objects.equals(hazardCodeId, that.hazardCodeId) &&
            Objects.equals(invStorageId, that.invStorageId) &&
            Objects.equals(storageUnitId, that.storageUnitId) &&
            Objects.equals(itemOrdersId, that.itemOrdersId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        itemName,
        casNumber,
        stockBookFolio,
        itemManufacturer,
        itemCapacity,
        unitPrice,
        totalQuantity,
        minimumQuantity,
        itemStatus,
        stockStore,
        creatorId,
        createdOn,
        lastUpdated,
        itemTransactionId,
        wasteItemId,
        hazardCodeId,
        invStorageId,
        storageUnitId,
        itemOrdersId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ItemStockCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (itemName != null ? "itemName=" + itemName + ", " : "") +
                (casNumber != null ? "casNumber=" + casNumber + ", " : "") +
                (stockBookFolio != null ? "stockBookFolio=" + stockBookFolio + ", " : "") +
                (itemManufacturer != null ? "itemManufacturer=" + itemManufacturer + ", " : "") +
                (itemCapacity != null ? "itemCapacity=" + itemCapacity + ", " : "") +
                (unitPrice != null ? "unitPrice=" + unitPrice + ", " : "") +
                (totalQuantity != null ? "totalQuantity=" + totalQuantity + ", " : "") +
                (minimumQuantity != null ? "minimumQuantity=" + minimumQuantity + ", " : "") +
                (itemStatus != null ? "itemStatus=" + itemStatus + ", " : "") +
                (stockStore != null ? "stockStore=" + stockStore + ", " : "") +
                (creatorId != null ? "creatorId=" + creatorId + ", " : "") +
                (createdOn != null ? "createdOn=" + createdOn + ", " : "") +
                (lastUpdated != null ? "lastUpdated=" + lastUpdated + ", " : "") +
                (itemTransactionId != null ? "itemTransactionId=" + itemTransactionId + ", " : "") +
                (wasteItemId != null ? "wasteItemId=" + wasteItemId + ", " : "") +
                (hazardCodeId != null ? "hazardCodeId=" + hazardCodeId + ", " : "") +
                (invStorageId != null ? "invStorageId=" + invStorageId + ", " : "") +
                (storageUnitId != null ? "storageUnitId=" + storageUnitId + ", " : "") +
                (itemOrdersId != null ? "itemOrdersId=" + itemOrdersId + ", " : "") +
            "}";
    }

}
