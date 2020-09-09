package com.cheminv.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import com.cheminv.app.domain.enumeration.ItemStatus;

import com.cheminv.app.domain.enumeration.StockStore;

/**
 * A ItemStock.
 */
@Entity
@Table(name = "cims_item_stock")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "itemstock")
public class ItemStock implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "total_quantity")
    private Integer totalQuantity;

    @Column(name = "minimum_quantity")
    private Integer minimumQuantity;

    @Enumerated(EnumType.STRING)
    @Column(name = "item_status")
    private ItemStatus itemStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "stock_store")
    private StockStore stockStore;

    @Column(name = "entry_date")
    private LocalDate entryDate;

    @Column(name = "expiry_date")
    private LocalDate expiryDate;

    @Column(name = "creator_id")
    private Integer creatorId;

    @Column(name = "created_on")
    private Instant createdOn;

    @Column(name = "sdsfile")
    private String sdsfile;

    @ManyToMany
    @JoinTable(name = "cims_item_stock_item_transaction",
               joinColumns = @JoinColumn(name = "item_stock_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "item_transaction_id", referencedColumnName = "id"))
    private Set<ItemTransaction> itemTransactions = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = "itemStocks", allowSetters = true)
    private InvStorage invStorage;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = "itemStocks", allowSetters = true)
    private MeasUnit storageUnit;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = "itemStocks", allowSetters = true)
    private Item item;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getTotalQuantity() {
        return totalQuantity;
    }

    public ItemStock totalQuantity(Integer totalQuantity) {
        this.totalQuantity = totalQuantity;
        return this;
    }

    public void setTotalQuantity(Integer totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public Integer getMinimumQuantity() {
        return minimumQuantity;
    }

    public ItemStock minimumQuantity(Integer minimumQuantity) {
        this.minimumQuantity = minimumQuantity;
        return this;
    }

    public void setMinimumQuantity(Integer minimumQuantity) {
        this.minimumQuantity = minimumQuantity;
    }

    public ItemStatus getItemStatus() {
        return itemStatus;
    }

    public ItemStock itemStatus(ItemStatus itemStatus) {
        this.itemStatus = itemStatus;
        return this;
    }

    public void setItemStatus(ItemStatus itemStatus) {
        this.itemStatus = itemStatus;
    }

    public StockStore getStockStore() {
        return stockStore;
    }

    public ItemStock stockStore(StockStore stockStore) {
        this.stockStore = stockStore;
        return this;
    }

    public void setStockStore(StockStore stockStore) {
        this.stockStore = stockStore;
    }

    public LocalDate getEntryDate() {
        return entryDate;
    }

    public ItemStock entryDate(LocalDate entryDate) {
        this.entryDate = entryDate;
        return this;
    }

    public void setEntryDate(LocalDate entryDate) {
        this.entryDate = entryDate;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public ItemStock expiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
        return this;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Integer getCreatorId() {
        return creatorId;
    }

    public ItemStock creatorId(Integer creatorId) {
        this.creatorId = creatorId;
        return this;
    }

    public void setCreatorId(Integer creatorId) {
        this.creatorId = creatorId;
    }

    public Instant getCreatedOn() {
        return createdOn;
    }

    public ItemStock createdOn(Instant createdOn) {
        this.createdOn = createdOn;
        return this;
    }

    public void setCreatedOn(Instant createdOn) {
        this.createdOn = createdOn;
    }

    public String getSdsfile() {
        return sdsfile;
    }

    public ItemStock sdsfile(String sdsfile) {
        this.sdsfile = sdsfile;
        return this;
    }

    public void setSdsfile(String sdsfile) {
        this.sdsfile = sdsfile;
    }

    public Set<ItemTransaction> getItemTransactions() {
        return itemTransactions;
    }

    public ItemStock itemTransactions(Set<ItemTransaction> itemTransactions) {
        this.itemTransactions = itemTransactions;
        return this;
    }

    public ItemStock addItemTransaction(ItemTransaction itemTransaction) {
        this.itemTransactions.add(itemTransaction);
        itemTransaction.getItemStocks().add(this);
        return this;
    }

    public ItemStock removeItemTransaction(ItemTransaction itemTransaction) {
        this.itemTransactions.remove(itemTransaction);
        itemTransaction.getItemStocks().remove(this);
        return this;
    }

    public void setItemTransactions(Set<ItemTransaction> itemTransactions) {
        this.itemTransactions = itemTransactions;
    }

    public InvStorage getInvStorage() {
        return invStorage;
    }

    public ItemStock invStorage(InvStorage invStorage) {
        this.invStorage = invStorage;
        return this;
    }

    public void setInvStorage(InvStorage invStorage) {
        this.invStorage = invStorage;
    }

    public MeasUnit getStorageUnit() {
        return storageUnit;
    }

    public ItemStock storageUnit(MeasUnit measUnit) {
        this.storageUnit = measUnit;
        return this;
    }

    public void setStorageUnit(MeasUnit measUnit) {
        this.storageUnit = measUnit;
    }

    public Item getItem() {
        return item;
    }

    public ItemStock item(Item item) {
        this.item = item;
        return this;
    }

    public void setItem(Item item) {
        this.item = item;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ItemStock)) {
            return false;
        }
        return id != null && id.equals(((ItemStock) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ItemStock{" +
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
            "}";
    }
}
