package com.cheminv.app.domain;


import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A MeasUnit.
 */
@Entity
@Table(name = "cims_meas_units")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "measunit")
public class MeasUnit implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "meas_unit")
    private String measUnit;

    @Column(name = "meas_desc")
    private String measDesc;

    @OneToMany(mappedBy = "storageUnit")
    private Set<ItemStock> itemStocks = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMeasUnit() {
        return measUnit;
    }

    public MeasUnit measUnit(String measUnit) {
        this.measUnit = measUnit;
        return this;
    }

    public void setMeasUnit(String measUnit) {
        this.measUnit = measUnit;
    }

    public String getMeasDesc() {
        return measDesc;
    }

    public MeasUnit measDesc(String measDesc) {
        this.measDesc = measDesc;
        return this;
    }

    public void setMeasDesc(String measDesc) {
        this.measDesc = measDesc;
    }

    public Set<ItemStock> getItemStocks() {
        return itemStocks;
    }

    public MeasUnit itemStocks(Set<ItemStock> itemStocks) {
        this.itemStocks = itemStocks;
        return this;
    }

    public MeasUnit addItemStock(ItemStock itemStock) {
        this.itemStocks.add(itemStock);
        itemStock.setStorageUnit(this);
        return this;
    }

    public MeasUnit removeItemStock(ItemStock itemStock) {
        this.itemStocks.remove(itemStock);
        itemStock.setStorageUnit(null);
        return this;
    }

    public void setItemStocks(Set<ItemStock> itemStocks) {
        this.itemStocks = itemStocks;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MeasUnit)) {
            return false;
        }
        return id != null && id.equals(((MeasUnit) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MeasUnit{" +
            "id=" + getId() +
            ", measUnit='" + getMeasUnit() + "'" +
            ", measDesc='" + getMeasDesc() + "'" +
            "}";
    }
}
