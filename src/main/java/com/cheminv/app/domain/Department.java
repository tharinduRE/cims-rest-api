package com.cheminv.app.domain;


import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Department.
 */
@Entity
@Table(name = "cims_inv_dept")
public class Department implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "department_name")
    private String departmentName;

    @OneToMany(mappedBy = "department")
    private Set<InvStorage> invStorages = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "cims_inv_dept_inv_user",
               joinColumns = @JoinColumn(name = "inv_department_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "inv_user_id", referencedColumnName = "id"))
    private Set<User> users = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public Department departmentName(String departmentName) {
        this.departmentName = departmentName;
        return this;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public Set<InvStorage> getInvStorages() {
        return invStorages;
    }

    public Department invStorages(Set<InvStorage> invStorages) {
        this.invStorages = invStorages;
        return this;
    }

    public Department addInvStorage(InvStorage invStorage) {
        this.invStorages.add(invStorage);
        invStorage.setDepartment(this);
        return this;
    }

    public Department removeInvStorage(InvStorage invStorage) {
        this.invStorages.remove(invStorage);
        invStorage.setDepartment(null);
        return this;
    }

    public void setInvStorages(Set<InvStorage> invStorages) {
        this.invStorages = invStorages;
    }

    public Set<User> getInvUsers() {
        return users;
    }

    public Department invUsers(Set<User> users) {
        this.users = users;
        return this;
    }

    public Department addInvUser(User user) {
        this.users.add(user);
        user.getInvDepartments().add(this);
        return this;
    }

    public Department removeInvUser(User user) {
        this.users.remove(user);
        user.getInvDepartments().remove(this);
        return this;
    }

    public void setInvUsers(Set<User> users) {
        this.users = users;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Department)) {
            return false;
        }
        return id != null && id.equals(((Department) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Department{" +
            "id=" + getId() +
            ", departmentName='" + getDepartmentName() + "'" +
            "}";
    }
}
