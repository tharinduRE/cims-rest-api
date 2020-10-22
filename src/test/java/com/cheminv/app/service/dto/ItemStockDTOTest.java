package com.cheminv.app.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.cheminv.app.web.rest.TestUtil;

public class ItemStockDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ItemStockDTO.class);
        ItemStockDTO itemStockDTO1 = new ItemStockDTO();
        itemStockDTO1.setId(1L);
        ItemStockDTO itemStockDTO2 = new ItemStockDTO();
        assertThat(itemStockDTO1).isNotEqualTo(itemStockDTO2);
        itemStockDTO2.setId(itemStockDTO1.getId());
        assertThat(itemStockDTO1).isEqualTo(itemStockDTO2);
        itemStockDTO2.setId(2L);
        assertThat(itemStockDTO1).isNotEqualTo(itemStockDTO2);
        itemStockDTO1.setId(null);
        assertThat(itemStockDTO1).isNotEqualTo(itemStockDTO2);
    }
}
