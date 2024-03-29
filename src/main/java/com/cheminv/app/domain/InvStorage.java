package com.cheminv.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.cheminv.app.domain.enumeration.StorageLocation;

/**
 * A InvStorage.
 */
@Entity
@Table(name = "cims_inv_storage")
public class InvStorage implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "storage_code")
    private String storageCode;

    @Column(name = "storage_name")
    private String storageName;

    @Enumerated(EnumType.STRING)
    @Column(name = "storage_location")
    private StorageLocation storageLocation;

    @OneToMany(mappedBy = "invStorage")
    private Set<ItemStock> itemStocks = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = "invStorages", allowSetters = true)
    private Department department;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStorageCode() {
        return storageCode;
    }

    public InvStorage storageCode(String storageCode) {
        this.storageCode = storageCode;
        return this;
    }

    public void setStorageCode(String storageCode) {
        this.storageCode = storageCode;
    }

    public String getStorageName() {
        return storageName;
    }

    public InvStorage storageName(String storageName) {
        this.storageName = storageName;
        return this;
    }

    public void setStorageName(String storageName) {
        this.storageName = storageName;
    }

    public StorageLocation getStorageLocation() {
        return storageLocation;
    }

    public InvStorage storageLocation(StorageLocation storageLocation) {
        this.storageLocation = storageLocation;
        return this;
    }

    public void setStorageLocation(StorageLocation storageLocation) {
        this.storageLocation = storageLocation;
    }

    public Set<ItemStock> getItemStocks() {
        return itemStocks;
    }

    public InvStorage itemStocks(Set<ItemStock> itemStocks) {
        this.itemStocks = itemStocks;
        return this;
    }

    public InvStorage addItemStock(ItemStock itemStock) {
        this.itemStocks.add(itemStock);
        itemStock.setInvStorage(this);
        return this;
    }

    public InvStorage removeItemStock(ItemStock itemStock) {
        this.itemStocks.remove(itemStock);
        itemStock.setInvStorage(null);
        return this;
    }

    public void setItemStocks(Set<ItemStock> itemStocks) {
        this.itemStocks = itemStocks;
    }

    public Department getDepartment() {
        return department;
    }

    public InvStorage department(Department department) {
        this.department = department;
        return this;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InvStorage)) {
            return false;
        }
        return id != null && id.equals(((InvStorage) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InvStorage{" +
            "id=" + getId() +
            ", storageCode='" + getStorageCode() + "'" +
            ", storageName='" + getStorageName() + "'" +
            ", storageLocation='" + getStorageLocation() + "'" +
            "}";
    }
}
