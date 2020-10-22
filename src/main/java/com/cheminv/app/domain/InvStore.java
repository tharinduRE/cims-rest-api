package com.cheminv.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.cheminv.app.domain.enumeration.StockStore;

/**
 * A InvStore.
 */
@Entity
@Table(name = "cims_inv_store")
public class InvStore implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "code")
    private StockStore code;

    @Column(name = "name")
    private String name;

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

    public StockStore getCode() {
        return code;
    }

    public InvStore code(StockStore code) {
        this.code = code;
        return this;
    }

    public void setCode(StockStore code) {
        this.code = code;
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
            ", code='" + getCode() + "'" +
            ", name='" + getName() + "'" +
            "}";
    }
}
