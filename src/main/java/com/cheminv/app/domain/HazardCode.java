package com.cheminv.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A HazardCode.
 */
@Entity
@Table(name = "cims_item_hazard_code")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "hazardcode")
public class HazardCode implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "hazard_code")
    private Integer hazardCode;

    @Column(name = "hazard_code_desc")
    private String hazardCodeDesc;

    @ManyToMany(mappedBy = "hazardCodes")
    @JsonIgnore
    private Set<Item> items = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getHazardCode() {
        return hazardCode;
    }

    public HazardCode hazardCode(Integer hazardCode) {
        this.hazardCode = hazardCode;
        return this;
    }

    public void setHazardCode(Integer hazardCode) {
        this.hazardCode = hazardCode;
    }

    public String getHazardCodeDesc() {
        return hazardCodeDesc;
    }

    public HazardCode hazardCodeDesc(String hazardCodeDesc) {
        this.hazardCodeDesc = hazardCodeDesc;
        return this;
    }

    public void setHazardCodeDesc(String hazardCodeDesc) {
        this.hazardCodeDesc = hazardCodeDesc;
    }

    public Set<Item> getItems() {
        return items;
    }

    public HazardCode items(Set<Item> items) {
        this.items = items;
        return this;
    }

    public HazardCode addItem(Item item) {
        this.items.add(item);
        item.getHazardCodes().add(this);
        return this;
    }

    public HazardCode removeItem(Item item) {
        this.items.remove(item);
        item.getHazardCodes().remove(this);
        return this;
    }

    public void setItems(Set<Item> items) {
        this.items = items;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HazardCode)) {
            return false;
        }
        return id != null && id.equals(((HazardCode) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HazardCode{" +
            "id=" + getId() +
            ", hazardCode=" + getHazardCode() +
            ", hazardCodeDesc='" + getHazardCodeDesc() + "'" +
            "}";
    }
}
