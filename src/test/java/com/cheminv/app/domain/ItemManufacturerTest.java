package com.cheminv.app.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.cheminv.app.web.rest.TestUtil;

public class ItemManufacturerTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ItemManufacturer.class);
        ItemManufacturer itemManufacturer1 = new ItemManufacturer();
        itemManufacturer1.setId(1L);
        ItemManufacturer itemManufacturer2 = new ItemManufacturer();
        itemManufacturer2.setId(itemManufacturer1.getId());
        assertThat(itemManufacturer1).isEqualTo(itemManufacturer2);
        itemManufacturer2.setId(2L);
        assertThat(itemManufacturer1).isNotEqualTo(itemManufacturer2);
        itemManufacturer1.setId(null);
        assertThat(itemManufacturer1).isNotEqualTo(itemManufacturer2);
    }
}
