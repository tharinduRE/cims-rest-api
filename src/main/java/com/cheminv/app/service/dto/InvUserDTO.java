package com.cheminv.app.service.dto;

import com.cheminv.app.domain.Authority;
import com.cheminv.app.domain.InvStore;
import com.cheminv.app.domain.InvUser;

import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A DTO for the {@link com.cheminv.app.domain.InvUser} entity.
 */
public class InvUserDTO implements Serializable {

    private Long id;

    private String firstName;

    private String lastName;

    private String postTitle;

    private Instant createdOn;

    private Instant lastUpdated;

    @NotBlank
    @Email
    @Size(min = 5, max = 254)
    private String email;

    private Set<String> authorities = new HashSet<>();

    private Set<InvStore> authStores = new HashSet<>();

    public InvUserDTO() {
    }

    public InvUserDTO(InvUser user) {
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.postTitle = user.getPostTitle();
        this.createdOn = user.getCreatedOn();
        this.lastUpdated = user.getLastUpdated();
        this.email = user.getEmail();
        this.authStores = user.getInvStores();
        this.authorities = user.getAuthorities().stream()
            .map(Authority::getName)
            .collect(Collectors.toSet());
    }

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

    public Instant getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Instant createdOn) {
        this.createdOn = createdOn;
    }

    public Instant getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Instant lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<String> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<String> authorities) {
        this.authorities = authorities;
    }

    public Set<InvStore> getAuthStores() {
        return authStores;
    }

    public void setAuthStores(Set<InvStore> authStores) {
        this.authStores = authStores;
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
            ", createdOn='" + getCreatedOn() + "'" +
            ", lastUpdated='" + getLastUpdated() + "'" +
            ", email='" + getEmail() + "'" +
            "}";
    }
}
