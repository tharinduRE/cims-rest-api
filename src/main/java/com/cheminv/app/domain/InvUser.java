package com.cheminv.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;
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

    @Column(name = "created_on")
    private Instant createdOn;

    @Column(name = "last_updated")
    private Instant lastUpdated;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @OneToMany(mappedBy = "createdBy")
    private Set<ItemTransaction> itemTransactions = new HashSet<>();

    @ManyToMany(mappedBy = "invUsers")
    @JsonIgnore
    private Set<InvDepartment> invDepartments = new HashSet<>();

    @OneToMany(mappedBy = "invUser")
    private Set<InvReport> invReports = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "cims_user_authority",
               joinColumns = @JoinColumn(name = "inv_user_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "authority_id", referencedColumnName = "id"))
    private Set<Authority> authorities = new HashSet<>();

    @ManyToMany
    @NotNull
    @JoinTable(name = "cims_user_inv_store",
               joinColumns = @JoinColumn(name = "inv_user_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "inv_store_id", referencedColumnName = "id"))
    private Set<InvStore> invStores = new HashSet<>();

    @OneToMany(mappedBy = "requestedBy")
    private Set<Order> itemOrders = new HashSet<>();

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

    public Instant getCreatedOn() {
        return createdOn;
    }

    public InvUser createdOn(Instant createdOn) {
        this.createdOn = createdOn;
        return this;
    }

    public void setCreatedOn(Instant createdOn) {
        this.createdOn = createdOn;
    }

    public Instant getLastUpdated() {
        return lastUpdated;
    }

    public InvUser lastUpdated(Instant lastUpdated) {
        this.lastUpdated = lastUpdated;
        return this;
    }

    public void setLastUpdated(Instant lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getEmail() {
        return email;
    }

    public InvUser email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public InvUser password(String password) {
        this.password = password;
        return this;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public Set<InvReport> getInvReports() {
        return invReports;
    }

    public InvUser invReports(Set<InvReport> invReports) {
        this.invReports = invReports;
        return this;
    }

    public InvUser addInvReport(InvReport invReport) {
        this.invReports.add(invReport);
        invReport.setInvUser(this);
        return this;
    }

    public InvUser removeInvReport(InvReport invReport) {
        this.invReports.remove(invReport);
        invReport.setInvUser(null);
        return this;
    }

    public void setInvReports(Set<InvReport> invReports) {
        this.invReports = invReports;
    }

    public Set<Authority> getAuthorities() {
        return authorities;
    }

    public InvUser authorities(Set<Authority> authorities) {
        this.authorities = authorities;
        return this;
    }

    public InvUser addAuthority(Authority authority) {
        this.authorities.add(authority);
        authority.getInvUsers().add(this);
        return this;
    }

    public InvUser removeAuthority(Authority authority) {
        this.authorities.remove(authority);
        authority.getInvUsers().remove(this);
        return this;
    }

    public void setAuthorities(Set<Authority> authorities) {
        this.authorities = authorities;
    }

    public Set<InvStore> getInvStores() {
        return invStores;
    }

    public InvUser invStores(Set<InvStore> invStores) {
        this.invStores = invStores;
        return this;
    }

    public InvUser addInvStore(InvStore invStore) {
        this.invStores.add(invStore);
        invStore.getInvUsers().add(this);
        return this;
    }

    public InvUser removeInvStore(InvStore invStore) {
        this.invStores.remove(invStore);
        invStore.getInvUsers().remove(this);
        return this;
    }

    public void setInvStores(Set<InvStore> invStores) {
        this.invStores = invStores;
    }

    public Set<Order> getItemOrders() {
        return itemOrders;
    }

    public InvUser itemOrders(Set<Order> orders) {
        this.itemOrders = orders;
        return this;
    }

    public InvUser addItemOrders(Order order) {
        this.itemOrders.add(order);
        order.setRequestedBy(this);
        return this;
    }

    public InvUser removeItemOrders(Order order) {
        this.itemOrders.remove(order);
        order.setRequestedBy(null);
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
            ", createdOn='" + getCreatedOn() + "'" +
            ", lastUpdated='" + getLastUpdated() + "'" +
            ", email='" + getEmail() + "'" +
            ", password='" + getPassword() + "'" +
            "}";
    }
}
