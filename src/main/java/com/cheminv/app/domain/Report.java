package com.cheminv.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

import java.io.Serializable;
import java.time.Instant;

/**
 * A Report.
 */
@Entity
@Table(name = "cims_reports")
public class Report implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "created_on")
    private Instant createdOn;

    @Lob
    @Column(name = "report")
    private byte[] report;

    @Column(name = "report_content_type")
    private String reportContentType;

    @ManyToOne
    @JsonIgnoreProperties(value = "reports", allowSetters = true)
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Report name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Instant getCreatedOn() {
        return createdOn;
    }

    public Report createdOn(Instant createdOn) {
        this.createdOn = createdOn;
        return this;
    }

    public void setCreatedOn(Instant createdOn) {
        this.createdOn = createdOn;
    }

    public byte[] getReport() {
        return report;
    }

    public Report report(byte[] report) {
        this.report = report;
        return this;
    }

    public void setReport(byte[] report) {
        this.report = report;
    }

    public String getReportContentType() {
        return reportContentType;
    }

    public Report reportContentType(String reportContentType) {
        this.reportContentType = reportContentType;
        return this;
    }

    public void setReportContentType(String reportContentType) {
        this.reportContentType = reportContentType;
    }

    public User getInvUser() {
        return user;
    }

    public Report invUser(User user) {
        this.user = user;
        return this;
    }

    public void setInvUser(User user) {
        this.user = user;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Report)) {
            return false;
        }
        return id != null && id.equals(((Report) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Report{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", createdOn='" + getCreatedOn() + "'" +
            ", report='" + getReport() + "'" +
            ", reportContentType='" + getReportContentType() + "'" +
            "}";
    }
}
