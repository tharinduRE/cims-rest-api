package com.cheminv.app.service.dto;

import java.io.Serializable;

/**
 * A DTO for the {@link com.cheminv.app.domain.InvUser} entity.
 */
public class InvUserDTO implements Serializable {
    
    private Long id;

    private String firstName;

    private String lastName;

    private String postTitle;

    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InvUserDTO)) {
            return false;
        }

        return id != null && id.equals(((InvUserDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InvUserDTO{" +
            "id=" + getId() +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", postTitle='" + getPostTitle() + "'" +
            "}";
    }
}
