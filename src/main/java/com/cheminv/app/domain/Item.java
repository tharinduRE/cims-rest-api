package com.cheminv.app.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Item.
 */
@Entity
@Table(name = "cims_inv_item")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "item")
public class Item implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "item_name", nullable = false)
    private String itemName;

    @Column(name = "item_desc")
    private String itemDesc;

    @Column(name = "cas_number")
    private String casNumber;

    @Column(name = "stock_book_folio")
    private String stockBookFolio;

    @Column(name = "item_capacity")
    private Float itemCapacity;

    @Column(name = "unit_price")
    private Float unitPrice;

    @OneToMany(mappedBy = "item")
    private Set<ItemManufacturer> itemManufacturers = new HashSet<>();

    @OneToMany(mappedBy = "item")
    private Set<ItemStock> itemStocks = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "cims_inv_item_hazard_code",
               joinColumns = @JoinColumn(name = "item_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "hazard_code_id", referencedColumnName = "id"))
    private Set<HazardCode> hazardCodes = new HashSet<>();

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

    public Item itemName(String itemName) {
        this.itemName = itemName;
        return this;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemDesc() {
        return itemDesc;
    }

    public Item itemDesc(String itemDesc) {
        this.itemDesc = itemDesc;
        return this;
    }

    public void setItemDesc(String itemDesc) {
        this.itemDesc = itemDesc;
    }

    public String getCasNumber() {
        return casNumber;
    }

    public Item casNumber(String casNumber) {
        this.casNumber = casNumber;
        return this;
    }

    public void setCasNumber(String casNumber) {
        this.casNumber = casNumber;
    }

    public String getStockBookFolio() {
        return stockBookFolio;
    }

    public Item stockBookFolio(String stockBookFolio) {
        this.stockBookFolio = stockBookFolio;
        return this;
    }

    public void setStockBookFolio(String stockBookFolio) {
        this.stockBookFolio = stockBookFolio;
    }

    public Float getItemCapacity() {
        return itemCapacity;
    }

    public Item itemCapacity(Float itemCapacity) {
        this.itemCapacity = itemCapacity;
        return this;
    }

    public void setItemCapacity(Float itemCapacity) {
        this.itemCapacity = itemCapacity;
    }

    public Float getUnitPrice() {
        return unitPrice;
    }

    public Item unitPrice(Float unitPrice) {
        this.unitPrice = unitPrice;
        return this;
    }

    public void setUnitPrice(Float unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Set<ItemManufacturer> getItemManufacturers() {
        return itemManufacturers;
    }

    public Item itemManufacturers(Set<ItemManufacturer> itemManufacturers) {
        this.itemManufacturers = itemManufacturers;
        return this;
    }

    public Item addItemManufacturer(ItemManufacturer itemManufacturer) {
        this.itemManufacturers.add(itemManufacturer);
        itemManufacturer.setItem(this);
        return this;
    }

    public Item removeItemManufacturer(ItemManufacturer itemManufacturer) {
        this.itemManufacturers.remove(itemManufacturer);
        itemManufacturer.setItem(null);
        return this;
    }

    public void setItemManufacturers(Set<ItemManufacturer> itemManufacturers) {
        this.itemManufacturers = itemManufacturers;
    }

    public Set<ItemStock> getItemStocks() {
        return itemStocks;
    }

    public Item itemStocks(Set<ItemStock> itemStocks) {
        this.itemStocks = itemStocks;
        return this;
    }

    public Item addItemStock(ItemStock itemStock) {
        this.itemStocks.add(itemStock);
        itemStock.setItem(this);
        return this;
    }

    public Item removeItemStock(ItemStock itemStock) {
        this.itemStocks.remove(itemStock);
        itemStock.setItem(null);
        return this;
    }

    public void setItemStocks(Set<ItemStock> itemStocks) {
        this.itemStocks = itemStocks;
    }

    public Set<HazardCode> getHazardCodes() {
        return hazardCodes;
    }

    public Item hazardCodes(Set<HazardCode> hazardCodes) {
        this.hazardCodes = hazardCodes;
        return this;
    }

    public Item addHazardCode(HazardCode hazardCode) {
        this.hazardCodes.add(hazardCode);
        hazardCode.getItems().add(this);
        return this;
    }

    public Item removeHazardCode(HazardCode hazardCode) {
        this.hazardCodes.remove(hazardCode);
        hazardCode.getItems().remove(this);
        return this;
    }

    public void setHazardCodes(Set<HazardCode> hazardCodes) {
        this.hazardCodes = hazardCodes;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Item)) {
            return false;
        }
        return id != null && id.equals(((Item) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Item{" +
            "id=" + getId() +
            ", itemName='" + getItemName() + "'" +
            ", itemDesc='" + getItemDesc() + "'" +
            ", casNumber='" + getCasNumber() + "'" +
            ", stockBookFolio='" + getStockBookFolio() + "'" +
            ", itemCapacity=" + getItemCapacity() +
            ", unitPrice=" + getUnitPrice() +
            "}";
    }
}
