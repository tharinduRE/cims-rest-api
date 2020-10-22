package com.cheminv.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Authority.
 */
@Entity
@Table(name = "cims_authority")
public class Authority implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToMany(mappedBy = "authorities")
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

    public Authority name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<InvUser> getInvUsers() {
        return invUsers;
    }

    public Authority invUsers(Set<InvUser> invUsers) {
        this.invUsers = invUsers;
        return this;
    }

    public Authority addInvUser(InvUser invUser) {
        this.invUsers.add(invUser);
        invUser.getAuthorities().add(this);
        return this;
    }

    public Authority removeInvUser(InvUser invUser) {
        this.invUsers.remove(invUser);
        invUser.getAuthorities().remove(this);
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
        if (!(o instanceof Authority)) {
            return false;
        }
        return id != null && id.equals(((Authority) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Authority{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
