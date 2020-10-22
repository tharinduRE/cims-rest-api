package com.cheminv.app.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.cheminv.app.web.rest.TestUtil;

public class WasteVendorDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(WasteVendorDTO.class);
        WasteVendorDTO wasteVendorDTO1 = new WasteVendorDTO();
        wasteVendorDTO1.setId(1L);
        WasteVendorDTO wasteVendorDTO2 = new WasteVendorDTO();
        assertThat(wasteVendorDTO1).isNotEqualTo(wasteVendorDTO2);
        wasteVendorDTO2.setId(wasteVendorDTO1.getId());
        assertThat(wasteVendorDTO1).isEqualTo(wasteVendorDTO2);
        wasteVendorDTO2.setId(2L);
        assertThat(wasteVendorDTO1).isNotEqualTo(wasteVendorDTO2);
        wasteVendorDTO1.setId(null);
        assertThat(wasteVendorDTO1).isNotEqualTo(wasteVendorDTO2);
    }
}
