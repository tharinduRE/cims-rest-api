package com.cheminv.app.service.dto;

import java.io.Serializable;
import java.util.Objects;

public class ReportDTO  implements Serializable {

    private Long userId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReportDTO reportDTO = (ReportDTO) o;
        return userId == reportDTO.userId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }
}
