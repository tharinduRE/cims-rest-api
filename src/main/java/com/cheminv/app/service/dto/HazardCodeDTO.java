package com.cheminv.app.service.dto;

import java.io.Serializable;

/**
 * A DTO for the {@link com.cheminv.app.domain.HazardCode} entity.
 */
public class HazardCodeDTO implements Serializable {
    
    private Long id;

    private Integer hazardCode;

    private String hazardCodeDesc;

    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getHazardCode() {
        return hazardCode;
    }

    public void setHazardCode(Integer hazardCode) {
        this.hazardCode = hazardCode;
    }

    public String getHazardCodeDesc() {
        return hazardCodeDesc;
    }

    public void setHazardCodeDesc(String hazardCodeDesc) {
        this.hazardCodeDesc = hazardCodeDesc;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HazardCodeDTO)) {
            return false;
        }

        return id != null && id.equals(((HazardCodeDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HazardCodeDTO{" +
            "id=" + getId() +
            ", hazardCode=" + getHazardCode() +
            ", hazardCodeDesc='" + getHazardCodeDesc() + "'" +
            "}";
    }
}
