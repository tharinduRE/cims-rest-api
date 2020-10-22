package com.cheminv.app.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.cheminv.app.web.rest.TestUtil;

public class ItemStockTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ItemStock.class);
        ItemStock itemStock1 = new ItemStock();
        itemStock1.setId(1L);
        ItemStock itemStock2 = new ItemStock();
        itemStock2.setId(itemStock1.getId());
        assertThat(itemStock1).isEqualTo(itemStock2);
        itemStock2.setId(2L);
        assertThat(itemStock1).isNotEqualTo(itemStock2);
        itemStock1.setId(null);
        assertThat(itemStock1).isNotEqualTo(itemStock2);
    }
}
