package com.cheminv.app.service.dto;

import java.time.Instant;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A DTO for the {@link com.cheminv.app.domain.WasteVendor} entity.
 */
public class WasteVendorDTO implements Serializable {
    
    private Long id;

    private String vendorName;

    private Instant lastIssuedOn;

    private Set<WasteItemDTO> wasteItems = new HashSet<>();
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public Instant getLastIssuedOn() {
        return lastIssuedOn;
    }

    public void setLastIssuedOn(Instant lastIssuedOn) {
        this.lastIssuedOn = lastIssuedOn;
    }

    public Set<WasteItemDTO> getWasteItems() {
        return wasteItems;
    }

    public void setWasteItems(Set<WasteItemDTO> wasteItems) {
        this.wasteItems = wasteItems;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WasteVendorDTO)) {
            return false;
        }

        return id != null && id.equals(((WasteVendorDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WasteVendorDTO{" +
            "id=" + getId() +
            ", vendorName='" + getVendorName() + "'" +
            ", lastIssuedOn='" + getLastIssuedOn() + "'" +
            ", wasteItems='" + getWasteItems() + "'" +
            "}";
    }
}
