package com.cheminv.app.service.dto;

import java.io.Serializable;
import com.cheminv.app.domain.enumeration.StorageLocation;

/**
 * A DTO for the {@link com.cheminv.app.domain.InvStorage} entity.
 */
public class InvStorageDTO implements Serializable {
    
    private Long id;

    private String storageCode;

    private String storageName;

    private String storageDesc;

    private StorageLocation storageLocation;


    private Long departmentId;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStorageCode() {
        return storageCode;
    }

    public void setStorageCode(String storageCode) {
        this.storageCode = storageCode;
    }

    public String getStorageName() {
        return storageName;
    }

    public void setStorageName(String storageName) {
        this.storageName = storageName;
    }

    public String getStorageDesc() {
        return storageDesc;
    }

    public void setStorageDesc(String storageDesc) {
        this.storageDesc = storageDesc;
    }

    public StorageLocation getStorageLocation() {
        return storageLocation;
    }

    public void setStorageLocation(StorageLocation storageLocation) {
        this.storageLocation = storageLocation;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long invDepartmentId) {
        this.departmentId = invDepartmentId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InvStorageDTO)) {
            return false;
        }

        return id != null && id.equals(((InvStorageDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InvStorageDTO{" +
            "id=" + getId() +
            ", storageCode='" + getStorageCode() + "'" +
            ", storageName='" + getStorageName() + "'" +
            ", storageDesc='" + getStorageDesc() + "'" +
            ", storageLocation='" + getStorageLocation() + "'" +
            ", departmentId=" + getDepartmentId() +
            "}";
    }
}
