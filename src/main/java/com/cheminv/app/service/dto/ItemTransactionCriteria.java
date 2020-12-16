package com.cheminv.app.service.dto;

import java.io.Serializable;
import java.util.Objects;

import io.github.jhipster.service.Criteria;
import com.cheminv.app.domain.enumeration.TransactionType;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.InstantFilter;

/**
 * Criteria class for the {@link com.cheminv.app.domain.ItemTransaction} entity. This class is used
 * in {@link com.cheminv.app.web.rest.ItemTransactionResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /item-transactions?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ItemTransactionCriteria implements Serializable, Criteria {
    /**
     * Class for filtering TransactionType
     */
    public static class TransactionTypeFilter extends Filter<TransactionType> {

        public TransactionTypeFilter() {
        }

        public TransactionTypeFilter(TransactionTypeFilter filter) {
            super(filter);
        }

        @Override
        public TransactionTypeFilter copy() {
            return new TransactionTypeFilter(this);
        }

    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private FloatFilter quantity;

    private StringFilter remarks;

    private TransactionTypeFilter transactionType;

    private InstantFilter transactionDate;

    private LongFilter itemStockId;

    private LongFilter createdById;

    public ItemTransactionCriteria() {
    }

    public ItemTransactionCriteria(ItemTransactionCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.quantity = other.quantity == null ? null : other.quantity.copy();
        this.remarks = other.remarks == null ? null : other.remarks.copy();
        this.transactionType = other.transactionType == null ? null : other.transactionType.copy();
        this.transactionDate = other.transactionDate == null ? null : other.transactionDate.copy();
        this.itemStockId = other.itemStockId == null ? null : other.itemStockId.copy();
        this.createdById = other.createdById == null ? null : other.createdById.copy();
    }

    @Override
    public ItemTransactionCriteria copy() {
        return new ItemTransactionCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public FloatFilter getQuantity() {
        return quantity;
    }

    public void setQuantity(FloatFilter quantity) {
        this.quantity = quantity;
    }

    public StringFilter getRemarks() {
        return remarks;
    }

    public void setRemarks(StringFilter remarks) {
        this.remarks = remarks;
    }

    public TransactionTypeFilter getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionTypeFilter transactionType) {
        this.transactionType = transactionType;
    }

    public InstantFilter getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(InstantFilter transactionDate) {
        this.transactionDate = transactionDate;
    }

    public LongFilter getItemStockId() {
        return itemStockId;
    }

    public void setItemStockId(LongFilter itemStockId) {
        this.itemStockId = itemStockId;
    }

    public LongFilter getCreatedById() {
        return createdById;
    }

    public void setCreatedById(LongFilter createdById) {
        this.createdById = createdById;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ItemTransactionCriteria that = (ItemTransactionCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(quantity, that.quantity) &&
            Objects.equals(remarks, that.remarks) &&
            Objects.equals(transactionType, that.transactionType) &&
            Objects.equals(transactionDate, that.transactionDate) &&
            Objects.equals(itemStockId, that.itemStockId) &&
            Objects.equals(createdById, that.createdById);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        quantity,
        remarks,
        transactionType,
        transactionDate,
        itemStockId,
        createdById
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ItemTransactionCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (quantity != null ? "quantity=" + quantity + ", " : "") +
                (remarks != null ? "remarks=" + remarks + ", " : "") +
                (transactionType != null ? "transactionType=" + transactionType + ", " : "") +
                (transactionDate != null ? "transactionDate=" + transactionDate + ", " : "") +
                (itemStockId != null ? "itemStockId=" + itemStockId + ", " : "") +
                (createdById != null ? "createdById=" + createdById + ", " : "") +
            "}";
    }

}
