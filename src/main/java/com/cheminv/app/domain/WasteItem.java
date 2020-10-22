package com.cheminv.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * A WasteItem.
 */
@Entity
@Table(name = "cims_waste_item")
public class WasteItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "item_quantity")
    private Float itemQuantity;

    @Column(name = "min_quantity")
    private Float minQuantity;

    @CreationTimestamp
    @Column(name = "created_on")
    private Instant createdOn;

    @UpdateTimestamp
    @Column(name = "last_updated")
    private Instant lastUpdated;

    @Column(name = "item_capacity")
    private Float itemCapacity;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = "wasteItems", allowSetters = true)
    private ItemStock itemStock;

    @ManyToMany(mappedBy = "wasteItems")
    @JsonIgnore
    private Set<WasteVendor> wasteVendors = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getItemQuantity() {
        return itemQuantity;
    }

    public WasteItem itemQuantity(Float itemQuantity) {
        this.itemQuantity = itemQuantity;
        return this;
    }

    public void setItemQuantity(Float itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    public Float getMinQuantity() {
        return minQuantity;
    }

    public WasteItem minQuantity(Float minQuantity) {
        this.minQuantity = minQuantity;
        return this;
    }

    public void setMinQuantity(Float minQuantity) {
        this.minQuantity = minQuantity;
    }

    public Instant getCreatedOn() {
        return createdOn;
    }

    public WasteItem createdOn(Instant createdOn) {
        this.createdOn = createdOn;
        return this;
    }

    public void setCreatedOn(Instant createdOn) {
        this.createdOn = createdOn;
    }

    public Instant getLastUpdated() {
        return lastUpdated;
    }

    public WasteItem lastUpdated(Instant lastUpdated) {
        this.lastUpdated = lastUpdated;
        return this;
    }

    public void setLastUpdated(Instant lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public Float getItemCapacity() {
        return itemCapacity;
    }

    public WasteItem itemCapacity(Float itemCapacity) {
        this.itemCapacity = itemCapacity;
        return this;
    }

    public void setItemCapacity(Float itemCapacity) {
        this.itemCapacity = itemCapacity;
    }

    public ItemStock getItemStock() {
        return itemStock;
    }

    public WasteItem itemStock(ItemStock itemStock) {
        this.itemStock = itemStock;
        return this;
    }

    public void setItemStock(ItemStock itemStock) {
        this.itemStock = itemStock;
    }

    public Set<WasteVendor> getWasteVendors() {
        return wasteVendors;
    }

    public WasteItem wasteVendors(Set<WasteVendor> wasteVendors) {
        this.wasteVendors = wasteVendors;
        return this;
    }

    public WasteItem addWasteVendor(WasteVendor wasteVendor) {
        this.wasteVendors.add(wasteVendor);
        wasteVendor.getWasteItems().add(this);
        return this;
    }

    public WasteItem removeWasteVendor(WasteVendor wasteVendor) {
        this.wasteVendors.remove(wasteVendor);
        wasteVendor.getWasteItems().remove(this);
        return this;
    }

    public void setWasteVendors(Set<WasteVendor> wasteVendors) {
        this.wasteVendors = wasteVendors;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WasteItem)) {
            return false;
        }
        return id != null && id.equals(((WasteItem) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WasteItem{" +
            "id=" + getId() +
            ", itemQuantity=" + getItemQuantity() +
            ", minQuantity=" + getMinQuantity() +
            ", createdOn='" + getCreatedOn() + "'" +
            ", lastUpdated='" + getLastUpdated() + "'" +
            ", itemCapacity=" + getItemCapacity() +
            "}";
    }
}
