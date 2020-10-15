package com.cheminv.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A InvUser.
 */
@Entity
@Table(name = "cims_user")
public class InvUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "post_title")
    private String postTitle;

    @OneToMany(mappedBy = "createdBy")
    private Set<ItemTransaction> itemTransactions = new HashSet<>();

    @ManyToMany(mappedBy = "invUsers")
    @JsonIgnore
    private Set<InvDepartment> invDepartments = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public InvUser firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public InvUser lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public InvUser postTitle(String postTitle) {
        this.postTitle = postTitle;
        return this;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public Set<ItemTransaction> getItemTransactions() {
        return itemTransactions;
    }

    public InvUser itemTransactions(Set<ItemTransaction> itemTransactions) {
        this.itemTransactions = itemTransactions;
        return this;
    }

    public InvUser addItemTransaction(ItemTransaction itemTransaction) {
        this.itemTransactions.add(itemTransaction);
        itemTransaction.setCreatedBy(this);
        return this;
    }

    public InvUser removeItemTransaction(ItemTransaction itemTransaction) {
        this.itemTransactions.remove(itemTransaction);
        itemTransaction.setCreatedBy(null);
        return this;
    }

    public void setItemTransactions(Set<ItemTransaction> itemTransactions) {
        this.itemTransactions = itemTransactions;
    }

    public Set<InvDepartment> getInvDepartments() {
        return invDepartments;
    }

    public InvUser invDepartments(Set<InvDepartment> invDepartments) {
        this.invDepartments = invDepartments;
        return this;
    }

    public InvUser addInvDepartment(InvDepartment invDepartment) {
        this.invDepartments.add(invDepartment);
        invDepartment.getInvUsers().add(this);
        return this;
    }

    public InvUser removeInvDepartment(InvDepartment invDepartment) {
        this.invDepartments.remove(invDepartment);
        invDepartment.getInvUsers().remove(this);
        return this;
    }

    public void setInvDepartments(Set<InvDepartment> invDepartments) {
        this.invDepartments = invDepartments;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InvUser)) {
            return false;
        }
        return id != null && id.equals(((InvUser) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InvUser{" +
            "id=" + getId() +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", postTitle='" + getPostTitle() + "'" +
            "}";
    }
}
