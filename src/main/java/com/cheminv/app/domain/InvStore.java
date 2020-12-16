package com.cheminv.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A InvStore.
 */
@Entity
@Table(name = "cims_inv_store")
public class InvStore implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(value = "storeId")
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToOne
    private InvStore parentStore;

    @OneToMany(mappedBy = "parentStore")
    @JsonIgnore
    private Set<InvStore> subStores = new HashSet<>();

    @ManyToMany(mappedBy = "invStores")
    @JsonIgnore
    private Set<InvUser> invUsers = new HashSet<>();

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

    public InvStore name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<InvUser> getInvUsers() {
        return invUsers;
    }

    public InvStore invUsers(Set<InvUser> invUsers) {
        this.invUsers = invUsers;
        return this;
    }

    public InvStore addInvUser(InvUser invUser) {
        this.invUsers.add(invUser);
        invUser.getInvStores().add(this);
        return this;
    }

    public InvStore removeInvUser(InvUser invUser) {
        this.invUsers.remove(invUser);
        invUser.getInvStores().remove(this);
        return this;
    }

    public void setInvUsers(Set<InvUser> invUsers) {
        this.invUsers = invUsers;
    }

    public Set<InvStore> getSubStores() {
        return subStores;
    }

    public InvStore subStores(Set<InvStore> invStores) {
        this.subStores = invStores;
        return this;
    }

    public InvStore addSubStore(InvStore invStore) {
        this.subStores.add(invStore);
        invStore.setParentStore(this);
        return this;
    }

    public InvStore removeSubStore(InvStore invStore) {
        this.subStores.remove(invStore);
        invStore.setParentStore(null);
        return this;
    }

    public void setSubStores(Set<InvStore> invStores) {
        this.subStores = invStores;
    }

    public InvStore getParentStore() {
        return parentStore;
    }

    public InvStore parentStore(InvStore invStore) {
        this.parentStore = invStore;
        return this;
    }

    public void setParentStore(InvStore invStore) {
        this.parentStore = invStore;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InvStore)) {
            return false;
        }
        return id != null && id.equals(((InvStore) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InvStore{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
