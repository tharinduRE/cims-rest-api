package com.cheminv.app.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;

/**
 * A DTO for the {@link com.cheminv.app.domain.WasteItem} entity.
 */
public class WasteItemDTO implements Serializable {
    
    private Long id;

    private Float itemQuantity;

    private Float minQuantity;


    private Long itemStockId;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(Float itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    public Float getMinQuantity() {
        return minQuantity;
    }

    public void setMinQuantity(Float minQuantity) {
        this.minQuantity = minQuantity;
    }

    public Long getItemStockId() {
        return itemStockId;
    }

    public void setItemStockId(Long itemStockId) {
        this.itemStockId = itemStockId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WasteItemDTO)) {
            return false;
        }

        return id != null && id.equals(((WasteItemDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WasteItemDTO{" +
            "id=" + getId() +
            ", itemQuantity=" + getItemQuantity() +
            ", minQuantity=" + getMinQuantity() +
            ", itemStockId=" + getItemStockId() +
            "}";
    }
}
