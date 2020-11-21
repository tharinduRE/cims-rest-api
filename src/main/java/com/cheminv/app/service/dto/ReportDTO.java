package com.cheminv.app.service.dto;

import com.cheminv.app.domain.enumeration.StockStore;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Objects;

public class ReportDTO  implements Serializable {

    @NotBlank
    private Long userId;

    @NotBlank
    private StockStore stockStore;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public StockStore getStockStore() {
        return stockStore;
    }

    public void setStockStore(StockStore stockStore) {
        this.stockStore = stockStore;
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
