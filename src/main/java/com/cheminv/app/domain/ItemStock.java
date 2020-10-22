package com.cheminv.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import com.cheminv.app.domain.enumeration.ItemStatus;

import com.cheminv.app.domain.enumeration.StockStore;

/**
 * A ItemStock.
 */
@Entity
@Table(name = "cims_item_stock")
public class ItemStock implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "item_name", nullable = false)
    private String itemName;

    @Column(name = "cas_number")
    private String casNumber;

    @Column(name = "stock_book_folio")
    private String stockBookFolio;

    @Column(name = "item_manufacturer")
    private String itemManufacturer;

    @Column(name = "item_capacity")
    private Float itemCapacity;

    @Column(name = "unit_price")
    private Float unitPrice;

    @Column(name = "total_quantity")
    private Float totalQuantity;

    @Column(name = "minimum_quantity")
    private Float minimumQuantity;

    @Enumerated(EnumType.STRING)
    @Column(name = "item_status")
    private ItemStatus itemStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "stock_store")
    private StockStore stockStore;

    @Column(name = "creator_id")
    private Integer creatorId;

    @Column(name = "created_on")
    @CreationTimestamp
    private Instant createdOn;

    @Column(name = "last_updated")
    @UpdateTimestamp
    private Instant lastUpdated;

    @Lob
    @Column(name = "sdsfile")
    private byte[] sdsfile;

    @Column(name = "sdsfile_content_type")
    private String sdsfileContentType;

    @OneToMany(mappedBy = "itemStock")
    private Set<ItemTransaction> itemTransactions = new HashSet<>();

    @OneToMany(mappedBy = "itemStock")
    private Set<WasteItem> wasteItems = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "cims_item_stock_hazard_code",
               joinColumns = @JoinColumn(name = "item_stock_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "hazard_code_id", referencedColumnName = "id"))
    private Set<HazardCode> hazardCodes = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = "itemStocks", allowSetters = true)
    private InvStorage invStorage;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = "itemStocks", allowSetters = true)
    private MeasUnit storageUnit;

    @OneToMany(mappedBy = "itemStock")
    private Set<Order> itemOrders = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getItemName() {
        return itemName;
    }

    public ItemStock itemName(String itemName) {
        this.itemName = itemName;
        return this;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getCasNumber() {
        return casNumber;
    }

    public ItemStock casNumber(String casNumber) {
        this.casNumber = casNumber;
        return this;
    }

    public void setCasNumber(String casNumber) {
        this.casNumber = casNumber;
    }

    public String getStockBookFolio() {
        return stockBookFolio;
    }

    public ItemStock stockBookFolio(String stockBookFolio) {
        this.stockBookFolio = stockBookFolio;
        return this;
    }

    public void setStockBookFolio(String stockBookFolio) {
        this.stockBookFolio = stockBookFolio;
    }

    public String getItemManufacturer() {
        return itemManufacturer;
    }

    public ItemStock itemManufacturer(String itemManufacturer) {
        this.itemManufacturer = itemManufacturer;
        return this;
    }

    public void setItemManufacturer(String itemManufacturer) {
        this.itemManufacturer = itemManufacturer;
    }

    public Float getItemCapacity() {
        return itemCapacity;
    }

    public ItemStock itemCapacity(Float itemCapacity) {
        this.itemCapacity = itemCapacity;
        return this;
    }

    public void setItemCapacity(Float itemCapacity) {
        this.itemCapacity = itemCapacity;
    }

    public Float getUnitPrice() {
        return unitPrice;
    }

    public ItemStock unitPrice(Float unitPrice) {
        this.unitPrice = unitPrice;
        return this;
    }

    public void setUnitPrice(Float unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Float getTotalQuantity() {
        return totalQuantity;
    }

    public ItemStock totalQuantity(Float totalQuantity) {
        this.totalQuantity = totalQuantity;
        return this;
    }

    public void setTotalQuantity(Float totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public Float getMinimumQuantity() {
        return minimumQuantity;
    }

    public ItemStock minimumQuantity(Float minimumQuantity) {
        this.minimumQuantity = minimumQuantity;
        return this;
    }

    public void setMinimumQuantity(Float minimumQuantity) {
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

    public Instant getLastUpdated() {
        return lastUpdated;
    }

    public ItemStock lastUpdated(Instant lastUpdated) {
        this.lastUpdated = lastUpdated;
        return this;
    }

    public void setLastUpdated(Instant lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public byte[] getSdsfile() {
        return sdsfile;
    }

    public ItemStock sdsfile(byte[] sdsfile) {
        this.sdsfile = sdsfile;
        return this;
    }

    public void setSdsfile(byte[] sdsfile) {
        this.sdsfile = sdsfile;
    }

    public String getSdsfileContentType() {
        return sdsfileContentType;
    }

    public ItemStock sdsfileContentType(String sdsfileContentType) {
        this.sdsfileContentType = sdsfileContentType;
        return this;
    }

    public void setSdsfileContentType(String sdsfileContentType) {
        this.sdsfileContentType = sdsfileContentType;
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
        itemTransaction.setItemStock(this);
        return this;
    }

    public ItemStock removeItemTransaction(ItemTransaction itemTransaction) {
        this.itemTransactions.remove(itemTransaction);
        itemTransaction.setItemStock(null);
        return this;
    }

    public void setItemTransactions(Set<ItemTransaction> itemTransactions) {
        this.itemTransactions = itemTransactions;
    }

    public Set<WasteItem> getWasteItems() {
        return wasteItems;
    }

    public ItemStock wasteItems(Set<WasteItem> wasteItems) {
        this.wasteItems = wasteItems;
        return this;
    }

    public ItemStock addWasteItem(WasteItem wasteItem) {
        this.wasteItems.add(wasteItem);
        wasteItem.setItemStock(this);
        return this;
    }

    public ItemStock removeWasteItem(WasteItem wasteItem) {
        this.wasteItems.remove(wasteItem);
        wasteItem.setItemStock(null);
        return this;
    }

    public void setWasteItems(Set<WasteItem> wasteItems) {
        this.wasteItems = wasteItems;
    }

    public Set<HazardCode> getHazardCodes() {
        return hazardCodes;
    }

    public ItemStock hazardCodes(Set<HazardCode> hazardCodes) {
        this.hazardCodes = hazardCodes;
        return this;
    }

    public ItemStock addHazardCode(HazardCode hazardCode) {
        this.hazardCodes.add(hazardCode);
        hazardCode.getItemStocks().add(this);
        return this;
    }

    public ItemStock removeHazardCode(HazardCode hazardCode) {
        this.hazardCodes.remove(hazardCode);
        hazardCode.getItemStocks().remove(this);
        return this;
    }

    public void setHazardCodes(Set<HazardCode> hazardCodes) {
        this.hazardCodes = hazardCodes;
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

    public Set<Order> getItemOrders() {
        return itemOrders;
    }

    public ItemStock itemOrders(Set<Order> orders) {
        this.itemOrders = orders;
        return this;
    }

    public ItemStock addItemOrders(Order order) {
        this.itemOrders.add(order);
        order.setItemStock(this);
        return this;
    }

    public ItemStock removeItemOrders(Order order) {
        this.itemOrders.remove(order);
        order.setItemStock(null);
        return this;
    }

    public void setItemOrders(Set<Order> orders) {
        this.itemOrders = orders;
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
            ", sdsfileContentType='" + getSdsfileContentType() + "'" +
            "}";
    }
}
