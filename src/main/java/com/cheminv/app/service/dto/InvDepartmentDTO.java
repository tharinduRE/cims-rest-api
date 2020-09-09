package com.cheminv.app.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A DTO for the {@link com.cheminv.app.domain.InvDepartment} entity.
 */
public class InvDepartmentDTO implements Serializable {
    
    private Long id;

    private String departmentName;

    private Set<InvUserDTO> invUsers = new HashSet<>();
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public Set<InvUserDTO> getInvUsers() {
        return invUsers;
    }

    public void setInvUsers(Set<InvUserDTO> invUsers) {
        this.invUsers = invUsers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InvDepartmentDTO)) {
            return false;
        }

        return id != null && id.equals(((InvDepartmentDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InvDepartmentDTO{" +
            "id=" + getId() +
            ", departmentName='" + getDepartmentName() + "'" +
            ", invUsers='" + getInvUsers() + "'" +
            "}";
    }
}
