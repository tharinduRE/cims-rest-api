package com.cheminv.app.domain;


import javax.persistence.*;

import java.io.Serializable;

/**
 * A CasNumber.
 */
@Entity
@Table(name = "cims_cas_number")
public class CasNumber implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cas_number")
    private String casNumber;

    @Column(name = "item_name")
    private String itemName;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCasNumber() {
        return casNumber;
    }

    public CasNumber casNumber(String casNumber) {
        this.casNumber = casNumber;
        return this;
    }

    public void setCasNumber(String casNumber) {
        this.casNumber = casNumber;
    }

    public String getItemName() {
        return itemName;
    }

    public CasNumber itemName(String itemName) {
        this.itemName = itemName;
        return this;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CasNumber)) {
            return false;
        }
        return id != null && id.equals(((CasNumber) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CasNumber{" +
            "id=" + getId() +
            ", casNumber='" + getCasNumber() + "'" +
            ", itemName='" + getItemName() + "'" +
            "}";
    }
}
