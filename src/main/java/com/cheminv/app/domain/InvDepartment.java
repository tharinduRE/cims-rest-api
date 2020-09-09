package com.cheminv.app.domain;


import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A InvDepartment.
 */
@Entity
@Table(name = "cims_inv_dept")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "invdepartment")
public class InvDepartment implements Serializable {

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
    private Set<InvUser> invUsers = new HashSet<>();

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

    public InvDepartment departmentName(String departmentName) {
        this.departmentName = departmentName;
        return this;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public Set<InvStorage> getInvStorages() {
        return invStorages;
    }

    public InvDepartment invStorages(Set<InvStorage> invStorages) {
        this.invStorages = invStorages;
        return this;
    }

    public InvDepartment addInvStorage(InvStorage invStorage) {
        this.invStorages.add(invStorage);
        invStorage.setDepartment(this);
        return this;
    }

    public InvDepartment removeInvStorage(InvStorage invStorage) {
        this.invStorages.remove(invStorage);
        invStorage.setDepartment(null);
        return this;
    }

    public void setInvStorages(Set<InvStorage> invStorages) {
        this.invStorages = invStorages;
    }

    public Set<InvUser> getInvUsers() {
        return invUsers;
    }

    public InvDepartment invUsers(Set<InvUser> invUsers) {
        this.invUsers = invUsers;
        return this;
    }

    public InvDepartment addInvUser(InvUser invUser) {
        this.invUsers.add(invUser);
        invUser.getInvDepartments().add(this);
        return this;
    }

    public InvDepartment removeInvUser(InvUser invUser) {
        this.invUsers.remove(invUser);
        invUser.getInvDepartments().remove(this);
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
        if (!(o instanceof InvDepartment)) {
            return false;
        }
        return id != null && id.equals(((InvDepartment) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InvDepartment{" +
            "id=" + getId() +
            ", departmentName='" + getDepartmentName() + "'" +
            "}";
    }
}
