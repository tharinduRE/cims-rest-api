package com.cheminv.app.service.dto;

import java.time.Instant;
import java.io.Serializable;
import javax.persistence.Lob;

/**
 * A DTO for the {@link com.cheminv.app.domain.InvReport} entity.
 */
public class InvReportDTO implements Serializable {

    private Long id;

    private String name;

    private Instant createdOn;

    @Lob
    private byte[] report;

    private String reportContentType;

    private Long invUserId;

    private String createdBy;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Instant getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Instant createdOn) {
        this.createdOn = createdOn;
    }

    public byte[] getReport() {
        return report;
    }

    public void setReport(byte[] report) {
        this.report = report;
    }

    public String getReportContentType() {
        return reportContentType;
    }

    public void setReportContentType(String reportContentType) {
        this.reportContentType = reportContentType;
    }

    public Long getInvUserId() {
        return invUserId;
    }

    public void setInvUserId(Long invUserId) {
        this.invUserId = invUserId;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InvReportDTO)) {
            return false;
        }

        return id != null && id.equals(((InvReportDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InvReportDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", createdOn='" + getCreatedOn() + "'" +
            ", report='" + getReport() + "'" +
            ", invUserId=" + getInvUserId() +
            "}";
    }
}
