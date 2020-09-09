package com.cheminv.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;

/**
 * A ItemManufacturer.
 */
@Entity
@Table(name = "cims_item_manufacturer")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "itemmanufacturer")
public class ItemManufacturer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "manufacturer_name")
    private String manufacturerName;

    @Column(name = "manufacturer_desc")
    private String manufacturerDesc;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = "itemManufacturers", allowSetters = true)
    private Item item;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getManufacturerName() {
        return manufacturerName;
    }

    public ItemManufacturer manufacturerName(String manufacturerName) {
        this.manufacturerName = manufacturerName;
        return this;
    }

    public void setManufacturerName(String manufacturerName) {
        this.manufacturerName = manufacturerName;
    }

    public String getManufacturerDesc() {
        return manufacturerDesc;
    }

    public ItemManufacturer manufacturerDesc(String manufacturerDesc) {
        this.manufacturerDesc = manufacturerDesc;
        return this;
    }

    public void setManufacturerDesc(String manufacturerDesc) {
        this.manufacturerDesc = manufacturerDesc;
    }

    public Item getItem() {
        return item;
    }

    public ItemManufacturer item(Item item) {
        this.item = item;
        return this;
    }

    public void setItem(Item item) {
        this.item = item;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ItemManufacturer)) {
            return false;
        }
        return id != null && id.equals(((ItemManufacturer) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ItemManufacturer{" +
            "id=" + getId() +
            ", manufacturerName='" + getManufacturerName() + "'" +
            ", manufacturerDesc='" + getManufacturerDesc() + "'" +
            "}";
    }
}
