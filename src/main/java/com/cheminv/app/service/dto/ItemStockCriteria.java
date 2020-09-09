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
import io.github.jhipster.service.filter.LocalDateFilter;

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

    private IntegerFilter totalQuantity;

    private IntegerFilter minimumQuantity;

    private ItemStatusFilter itemStatus;

    private StockStoreFilter stockStore;

    private LocalDateFilter entryDate;

    private LocalDateFilter expiryDate;

    private IntegerFilter creatorId;

    private InstantFilter createdOn;

    private StringFilter sdsfile;

    private LongFilter itemTransactionId;

    private LongFilter invStorageId;

    private LongFilter storageUnitId;

    private LongFilter itemId;

    public ItemStockCriteria() {
    }

    public ItemStockCriteria(ItemStockCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.totalQuantity = other.totalQuantity == null ? null : other.totalQuantity.copy();
        this.minimumQuantity = other.minimumQuantity == null ? null : other.minimumQuantity.copy();
        this.itemStatus = other.itemStatus == null ? null : other.itemStatus.copy();
        this.stockStore = other.stockStore == null ? null : other.stockStore.copy();
        this.entryDate = other.entryDate == null ? null : other.entryDate.copy();
        this.expiryDate = other.expiryDate == null ? null : other.expiryDate.copy();
        this.creatorId = other.creatorId == null ? null : other.creatorId.copy();
        this.createdOn = other.createdOn == null ? null : other.createdOn.copy();
        this.sdsfile = other.sdsfile == null ? null : other.sdsfile.copy();
        this.itemTransactionId = other.itemTransactionId == null ? null : other.itemTransactionId.copy();
        this.invStorageId = other.invStorageId == null ? null : other.invStorageId.copy();
        this.storageUnitId = other.storageUnitId == null ? null : other.storageUnitId.copy();
        this.itemId = other.itemId == null ? null : other.itemId.copy();
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

    public IntegerFilter getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(IntegerFilter totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public IntegerFilter getMinimumQuantity() {
        return minimumQuantity;
    }

    public void setMinimumQuantity(IntegerFilter minimumQuantity) {
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

    public LocalDateFilter getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(LocalDateFilter entryDate) {
        this.entryDate = entryDate;
    }

    public LocalDateFilter getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDateFilter expiryDate) {
        this.expiryDate = expiryDate;
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

    public StringFilter getSdsfile() {
        return sdsfile;
    }

    public void setSdsfile(StringFilter sdsfile) {
        this.sdsfile = sdsfile;
    }

    public LongFilter getItemTransactionId() {
        return itemTransactionId;
    }

    public void setItemTransactionId(LongFilter itemTransactionId) {
        this.itemTransactionId = itemTransactionId;
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

    public LongFilter getItemId() {
        return itemId;
    }

    public void setItemId(LongFilter itemId) {
        this.itemId = itemId;
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
            Objects.equals(totalQuantity, that.totalQuantity) &&
            Objects.equals(minimumQuantity, that.minimumQuantity) &&
            Objects.equals(itemStatus, that.itemStatus) &&
            Objects.equals(stockStore, that.stockStore) &&
            Objects.equals(entryDate, that.entryDate) &&
            Objects.equals(expiryDate, that.expiryDate) &&
            Objects.equals(creatorId, that.creatorId) &&
            Objects.equals(createdOn, that.createdOn) &&
            Objects.equals(sdsfile, that.sdsfile) &&
            Objects.equals(itemTransactionId, that.itemTransactionId) &&
            Objects.equals(invStorageId, that.invStorageId) &&
            Objects.equals(storageUnitId, that.storageUnitId) &&
            Objects.equals(itemId, that.itemId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        totalQuantity,
        minimumQuantity,
        itemStatus,
        stockStore,
        entryDate,
        expiryDate,
        creatorId,
        createdOn,
        sdsfile,
        itemTransactionId,
        invStorageId,
        storageUnitId,
        itemId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ItemStockCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (totalQuantity != null ? "totalQuantity=" + totalQuantity + ", " : "") +
                (minimumQuantity != null ? "minimumQuantity=" + minimumQuantity + ", " : "") +
                (itemStatus != null ? "itemStatus=" + itemStatus + ", " : "") +
                (stockStore != null ? "stockStore=" + stockStore + ", " : "") +
                (entryDate != null ? "entryDate=" + entryDate + ", " : "") +
                (expiryDate != null ? "expiryDate=" + expiryDate + ", " : "") +
                (creatorId != null ? "creatorId=" + creatorId + ", " : "") +
                (createdOn != null ? "createdOn=" + createdOn + ", " : "") +
                (sdsfile != null ? "sdsfile=" + sdsfile + ", " : "") +
                (itemTransactionId != null ? "itemTransactionId=" + itemTransactionId + ", " : "") +
                (invStorageId != null ? "invStorageId=" + invStorageId + ", " : "") +
                (storageUnitId != null ? "storageUnitId=" + storageUnitId + ", " : "") +
                (itemId != null ? "itemId=" + itemId + ", " : "") +
            "}";
    }

}
