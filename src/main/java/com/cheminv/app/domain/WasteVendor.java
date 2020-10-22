package com.cheminv.app.domain;


import javax.persistence.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * A WasteVendor.
 */
@Entity
@Table(name = "cims_waste_vendor")
public class WasteVendor implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "vendor_name")
    private String vendorName;

    @Column(name = "last_issued_on")
    private Instant lastIssuedOn;

    @Column(name = "vendor_address")
    private String vendorAddress;

    @Column(name = "vendor_contact")
    private String vendorContact;

    @Column(name = "registered_on")
    private Instant registeredOn;

    @ManyToMany
    @JoinTable(name = "cims_waste_vendor_waste_item",
               joinColumns = @JoinColumn(name = "waste_vendor_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "waste_item_id", referencedColumnName = "id"))
    private Set<WasteItem> wasteItems = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVendorName() {
        return vendorName;
    }

    public WasteVendor vendorName(String vendorName) {
        this.vendorName = vendorName;
        return this;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public Instant getLastIssuedOn() {
        return lastIssuedOn;
    }

    public WasteVendor lastIssuedOn(Instant lastIssuedOn) {
        this.lastIssuedOn = lastIssuedOn;
        return this;
    }

    public void setLastIssuedOn(Instant lastIssuedOn) {
        this.lastIssuedOn = lastIssuedOn;
    }

    public String getVendorAddress() {
        return vendorAddress;
    }

    public WasteVendor vendorAddress(String vendorAddress) {
        this.vendorAddress = vendorAddress;
        return this;
    }

    public void setVendorAddress(String vendorAddress) {
        this.vendorAddress = vendorAddress;
    }

    public String getVendorContact() {
        return vendorContact;
    }

    public WasteVendor vendorContact(String vendorContact) {
        this.vendorContact = vendorContact;
        return this;
    }

    public void setVendorContact(String vendorContact) {
        this.vendorContact = vendorContact;
    }

    public Instant getRegisteredOn() {
        return registeredOn;
    }

    public WasteVendor registeredOn(Instant registeredOn) {
        this.registeredOn = registeredOn;
        return this;
    }

    public void setRegisteredOn(Instant registeredOn) {
        this.registeredOn = registeredOn;
    }

    public Set<WasteItem> getWasteItems() {
        return wasteItems;
    }

    public WasteVendor wasteItems(Set<WasteItem> wasteItems) {
        this.wasteItems = wasteItems;
        return this;
    }

    public WasteVendor addWasteItem(WasteItem wasteItem) {
        this.wasteItems.add(wasteItem);
        wasteItem.getWasteVendors().add(this);
        return this;
    }

    public WasteVendor removeWasteItem(WasteItem wasteItem) {
        this.wasteItems.remove(wasteItem);
        wasteItem.getWasteVendors().remove(this);
        return this;
    }

    public void setWasteItems(Set<WasteItem> wasteItems) {
        this.wasteItems = wasteItems;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WasteVendor)) {
            return false;
        }
        return id != null && id.equals(((WasteVendor) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WasteVendor{" +
            "id=" + getId() +
            ", vendorName='" + getVendorName() + "'" +
            ", lastIssuedOn='" + getLastIssuedOn() + "'" +
            ", vendorAddress='" + getVendorAddress() + "'" +
            ", vendorContact='" + getVendorContact() + "'" +
            ", registeredOn='" + getRegisteredOn() + "'" +
            "}";
    }
}
