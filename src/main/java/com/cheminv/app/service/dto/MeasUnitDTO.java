package com.cheminv.app.service.dto;

import java.io.Serializable;

/**
 * A DTO for the {@link com.cheminv.app.domain.MeasUnit} entity.
 */
public class MeasUnitDTO implements Serializable {
    
    private Long id;

    private String measUnit;

    private String measDesc;

    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMeasUnit() {
        return measUnit;
    }

    public void setMeasUnit(String measUnit) {
        this.measUnit = measUnit;
    }

    public String getMeasDesc() {
        return measDesc;
    }

    public void setMeasDesc(String measDesc) {
        this.measDesc = measDesc;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MeasUnitDTO)) {
            return false;
        }

        return id != null && id.equals(((MeasUnitDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MeasUnitDTO{" +
            "id=" + getId() +
            ", measUnit='" + getMeasUnit() + "'" +
            ", measDesc='" + getMeasDesc() + "'" +
            "}";
    }
}
