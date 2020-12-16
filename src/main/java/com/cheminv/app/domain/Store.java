package com.cheminv.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Store.
 */
@Entity
@Table(name = "cims_inv_store")
public class Store implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JsonIgnoreProperties(value = "subStores", allowSetters = true)
    private Store parentStore;

    @OneToMany(mappedBy = "parentStore")
    @JsonIgnore
    private Set<Store> subStores = new HashSet<>();

    @ManyToMany(mappedBy = "stores")
    @JsonIgnore
    private Set<User> users = new HashSet<>();

    @OneToMany(mappedBy = "store",fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<ItemStock> itemStocks = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Store name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<User> getInvUsers() {
        return users;
    }

    public Store invUsers(Set<User> users) {
        this.users = users;
        return this;
    }

    public Store addInvUser(User user) {
        this.users.add(user);
        user.getInvStores().add(this);
        return this;
    }

    public Store removeInvUser(User user) {
        this.users.remove(user);
        user.getInvStores().remove(this);
        return this;
    }

    public void setInvUsers(Set<User> users) {
        this.users = users;
    }

    public Set<Store> getSubStores() {
        return subStores;
    }

    public Store subStores(Set<Store> stores) {
        this.subStores = stores;
        return this;
    }

    public Store addSubStore(Store store) {
        this.subStores.add(store);
        store.setParentStore(this);
        return this;
    }

    public Store removeSubStore(Store store) {
        this.subStores.remove(store);
        store.setParentStore(null);
        return this;
    }

    public void setSubStores(Set<Store> stores) {
        this.subStores = stores;
    }

    public Store getParentStore() {
        return parentStore;
    }

    public Store parentStore(Store store) {
        this.parentStore = store;
        return this;
    }

    public void setParentStore(Store store) {
        this.parentStore = store;
    }

    public Set<ItemStock> getItemStocks() {
        return itemStocks;
    }

    public Store itemStocks(Set<ItemStock> itemStocks) {
        this.itemStocks = itemStocks;
        return this;
    }

    public Store addItemStock(ItemStock itemStock) {
        this.itemStocks.add(itemStock);
        itemStock.setStore(this);
        return this;
    }

    public Store removeItemStock(ItemStock itemStock) {
        this.itemStocks.remove(itemStock);
        itemStock.setStore(null);
        return this;
    }

    public void setItemStocks(Set<ItemStock> itemStocks) {
        this.itemStocks = itemStocks;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Store)) {
            return false;
        }
        return id != null && id.equals(((Store) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Store{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
