package com.cheminv.app.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.cheminv.app.web.rest.TestUtil;

public class WasteItemDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(WasteItemDTO.class);
        WasteItemDTO wasteItemDTO1 = new WasteItemDTO();
        wasteItemDTO1.setId(1L);
        WasteItemDTO wasteItemDTO2 = new WasteItemDTO();
        assertThat(wasteItemDTO1).isNotEqualTo(wasteItemDTO2);
        wasteItemDTO2.setId(wasteItemDTO1.getId());
        assertThat(wasteItemDTO1).isEqualTo(wasteItemDTO2);
        wasteItemDTO2.setId(2L);
        assertThat(wasteItemDTO1).isNotEqualTo(wasteItemDTO2);
        wasteItemDTO1.setId(null);
        assertThat(wasteItemDTO1).isNotEqualTo(wasteItemDTO2);
    }
}
