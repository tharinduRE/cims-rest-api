package com.cheminv.app.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;

/**
 * A DTO for the {@link com.cheminv.app.domain.ItemManufacturer} entity.
 */
public class ItemManufacturerDTO implements Serializable {
    
    private Long id;

    private String manufacturerName;

    private String manufacturerDesc;


    private Long itemId;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getManufacturerName() {
        return manufacturerName;
    }

    public void setManufacturerName(String manufacturerName) {
        this.manufacturerName = manufacturerName;
    }

    public String getManufacturerDesc() {
        return manufacturerDesc;
    }

    public void setManufacturerDesc(String manufacturerDesc) {
        this.manufacturerDesc = manufacturerDesc;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ItemManufacturerDTO)) {
            return false;
        }

        return id != null && id.equals(((ItemManufacturerDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ItemManufacturerDTO{" +
            "id=" + getId() +
            ", manufacturerName='" + getManufacturerName() + "'" +
            ", manufacturerDesc='" + getManufacturerDesc() + "'" +
            ", itemId=" + getItemId() +
            "}";
    }
}
