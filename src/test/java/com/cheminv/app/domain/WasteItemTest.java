package com.cheminv.app.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.cheminv.app.web.rest.TestUtil;

public class WasteItemTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WasteItem.class);
        WasteItem wasteItem1 = new WasteItem();
        wasteItem1.setId(1L);
        WasteItem wasteItem2 = new WasteItem();
        wasteItem2.setId(wasteItem1.getId());
        assertThat(wasteItem1).isEqualTo(wasteItem2);
        wasteItem2.setId(2L);
        assertThat(wasteItem1).isNotEqualTo(wasteItem2);
        wasteItem1.setId(null);
        assertThat(wasteItem1).isNotEqualTo(wasteItem2);
    }
}
