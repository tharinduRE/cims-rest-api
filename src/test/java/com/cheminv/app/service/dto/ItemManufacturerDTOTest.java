package com.cheminv.app.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.cheminv.app.web.rest.TestUtil;

public class ItemManufacturerDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ItemManufacturerDTO.class);
        ItemManufacturerDTO itemManufacturerDTO1 = new ItemManufacturerDTO();
        itemManufacturerDTO1.setId(1L);
        ItemManufacturerDTO itemManufacturerDTO2 = new ItemManufacturerDTO();
        assertThat(itemManufacturerDTO1).isNotEqualTo(itemManufacturerDTO2);
        itemManufacturerDTO2.setId(itemManufacturerDTO1.getId());
        assertThat(itemManufacturerDTO1).isEqualTo(itemManufacturerDTO2);
        itemManufacturerDTO2.setId(2L);
        assertThat(itemManufacturerDTO1).isNotEqualTo(itemManufacturerDTO2);
        itemManufacturerDTO1.setId(null);
        assertThat(itemManufacturerDTO1).isNotEqualTo(itemManufacturerDTO2);
    }
}
