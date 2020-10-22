package com.cheminv.app.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.cheminv.app.web.rest.TestUtil;

public class WasteVendorTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WasteVendor.class);
        WasteVendor wasteVendor1 = new WasteVendor();
        wasteVendor1.setId(1L);
        WasteVendor wasteVendor2 = new WasteVendor();
        wasteVendor2.setId(wasteVendor1.getId());
        assertThat(wasteVendor1).isEqualTo(wasteVendor2);
        wasteVendor2.setId(2L);
        assertThat(wasteVendor1).isNotEqualTo(wasteVendor2);
        wasteVendor1.setId(null);
        assertThat(wasteVendor1).isNotEqualTo(wasteVendor2);
    }
}
