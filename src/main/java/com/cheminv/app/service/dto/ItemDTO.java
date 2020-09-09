package com.cheminv.app.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A DTO for the {@link com.cheminv.app.domain.Item} entity.
 */
public class ItemDTO implements Serializable {
    
    private Long id;

    @NotNull
    private String itemName;

    private String itemDesc;

    private String casNumber;

    private String stockBookFolio;

    private Float itemCapacity;

    private Float unitPrice;

    private Set<HazardCodeDTO> hazardCodes = new HashSet<>();
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemDesc() {
        return itemDesc;
    }

    public void setItemDesc(String itemDesc) {
        this.itemDesc = itemDesc;
    }

    public String getCasNumber() {
        return casNumber;
    }

    public void setCasNumber(String casNumber) {
        this.casNumber = casNumber;
    }

    public String getStockBookFolio() {
        return stockBookFolio;
    }

    public void setStockBookFolio(String stockBookFolio) {
        this.stockBookFolio = stockBookFolio;
    }

    public Float getItemCapacity() {
        return itemCapacity;
    }

    public void setItemCapacity(Float itemCapacity) {
        this.itemCapacity = itemCapacity;
    }

    public Float getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Float unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Set<HazardCodeDTO> getHazardCodes() {
        return hazardCodes;
    }

    public void setHazardCodes(Set<HazardCodeDTO> hazardCodes) {
        this.hazardCodes = hazardCodes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ItemDTO)) {
            return false;
        }

        return id != null && id.equals(((ItemDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ItemDTO{" +
            "id=" + getId() +
            ", itemName='" + getItemName() + "'" +
            ", itemDesc='" + getItemDesc() + "'" +
            ", casNumber='" + getCasNumber() + "'" +
            ", stockBookFolio='" + getStockBookFolio() + "'" +
            ", itemCapacity=" + getItemCapacity() +
            ", unitPrice=" + getUnitPrice() +
            ", hazardCodes='" + getHazardCodes() + "'" +
            "}";
    }
}
