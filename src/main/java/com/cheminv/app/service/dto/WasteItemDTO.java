package com.cheminv.app.service.dto;

import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;

/**
 * A DTO for the {@link com.cheminv.app.domain.WasteItem} entity.
 */
public class WasteItemDTO implements Serializable {
    
    private Long id;

    private Float itemQuantity;

    private Float minQuantity;

    private Instant createdOn;

    private Instant lastUpdated;

    private Float itemCapacity;


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

    public Float getItemCapacity() {
        return itemCapacity;
    }

    public void setItemCapacity(Float itemCapacity) {
        this.itemCapacity = itemCapacity;
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
            ", createdOn='" + getCreatedOn() + "'" +
            ", lastUpdated='" + getLastUpdated() + "'" +
            ", itemCapacity=" + getItemCapacity() +
            ", itemStockId=" + getItemStockId() +
            "}";
    }
}
