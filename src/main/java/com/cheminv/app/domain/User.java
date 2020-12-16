package com.cheminv.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * A User.
 */
@Entity
@Table(name = "cims_user")
public class User implements Serializable {

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

    @CreationTimestamp
    @Column(name = "created_on")
    private Instant createdOn;

    @UpdateTimestamp
    @Column(name = "last_updated")
    private Instant lastUpdated;

    @Column(name = "email")
    private String email;

    @JsonIgnore
    @Column(name = "password")
    private String password;

    @Column(name = "avatar_url")
    private String avatarUrl;

    @OneToMany(mappedBy = "createdBy",cascade = CascadeType.ALL)
    private Set<Transaction> transactions = new HashSet<>();

    @ManyToMany(mappedBy = "users")
    @JsonIgnore
    private Set<Department> departments = new HashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<Report> reports = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "cims_user_authority",
               joinColumns = @JoinColumn(name = "inv_user_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "authority_id", referencedColumnName = "id"))
    private Set<Authority> authorities = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "cims_user_inv_store",
               joinColumns = @JoinColumn(name = "inv_user_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "inv_store_id", referencedColumnName = "id"))
    private Set<Store> stores = new HashSet<>();

    @OneToMany(mappedBy = "requestedBy",cascade = CascadeType.ALL)
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

    public User firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public User lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public User postTitle(String postTitle) {
        this.postTitle = postTitle;
        return this;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public Instant getCreatedOn() {
        return createdOn;
    }

    public User createdOn(Instant createdOn) {
        this.createdOn = createdOn;
        return this;
    }

    public void setCreatedOn(Instant createdOn) {
        this.createdOn = createdOn;
    }

    public Instant getLastUpdated() {
        return lastUpdated;
    }

    public User lastUpdated(Instant lastUpdated) {
        this.lastUpdated = lastUpdated;
        return this;
    }

    public void setLastUpdated(Instant lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getEmail() {
        return email;
    }

    public User email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public User password(String password) {
        this.password = password;
        return this;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public User avatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
        return this;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public Set<Transaction> getItemTransactions() {
        return transactions;
    }

    public User itemTransactions(Set<Transaction> transactions) {
        this.transactions = transactions;
        return this;
    }

    public User addItemTransaction(Transaction transaction) {
        this.transactions.add(transaction);
        transaction.setCreatedBy(this);
        return this;
    }

    public User removeItemTransaction(Transaction transaction) {
        this.transactions.remove(transaction);
        transaction.setCreatedBy(null);
        return this;
    }

    public void setItemTransactions(Set<Transaction> transactions) {
        this.transactions = transactions;
    }

    public Set<Department> getInvDepartments() {
        return departments;
    }

    public User invDepartments(Set<Department> departments) {
        this.departments = departments;
        return this;
    }

    public User addInvDepartment(Department department) {
        this.departments.add(department);
        department.getInvUsers().add(this);
        return this;
    }

    public User removeInvDepartment(Department department) {
        this.departments.remove(department);
        department.getInvUsers().remove(this);
        return this;
    }

    public void setInvDepartments(Set<Department> departments) {
        this.departments = departments;
    }

    public Set<Report> getInvReports() {
        return reports;
    }

    public User reports(Set<Report> reports) {
        this.reports = reports;
        return this;
    }

    public User addInvReport(Report report) {
        this.reports.add(report);
        report.setInvUser(this);
        return this;
    }

    public User removeInvReport(Report report) {
        this.reports.remove(report);
        report.setInvUser(null);
        return this;
    }

    public void setInvReports(Set<Report> reports) {
        this.reports = reports;
    }

    public Set<Authority> getAuthorities() {
        return authorities;
    }

    public User authorities(Set<Authority> authorities) {
        this.authorities = authorities;
        return this;
    }

    public User addAuthority(Authority authority) {
        this.authorities.add(authority);
        authority.getInvUsers().add(this);
        return this;
    }

    public User removeAuthority(Authority authority) {
        this.authorities.remove(authority);
        authority.getInvUsers().remove(this);
        return this;
    }

    public void setAuthorities(Set<Authority> authorities) {
        this.authorities = authorities;
    }

    public Set<Store> getInvStores() {
        return stores;
    }

    public User invStores(Set<Store> stores) {
        this.stores = stores;
        return this;
    }

    public User addInvStore(Store store) {
        this.stores.add(store);
        store.getInvUsers().add(this);
        return this;
    }

    public User removeInvStore(Store store) {
        this.stores.remove(store);
        store.getInvUsers().remove(this);
        return this;
    }

    public void setInvStores(Set<Store> stores) {
        this.stores = stores;
    }

    public Set<Order> getItemOrders() {
        return itemOrders;
    }

    public User itemOrders(Set<Order> orders) {
        this.itemOrders = orders;
        return this;
    }

    public User addItemOrders(Order order) {
        this.itemOrders.add(order);
        order.setRequestedBy(this);
        return this;
    }

    public User removeItemOrders(Order order) {
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
        if (!(o instanceof User)) {
            return false;
        }
        return id != null && id.equals(((User) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "User{" +
            "id=" + getId() +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", postTitle='" + getPostTitle() + "'" +
            ", createdOn='" + getCreatedOn() + "'" +
            ", lastUpdated='" + getLastUpdated() + "'" +
            ", email='" + getEmail() + "'" +
            ", authStores='" + getInvStores() + "'" +
            "}";
    }
}
