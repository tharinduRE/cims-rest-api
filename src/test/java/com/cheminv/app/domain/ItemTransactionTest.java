package com.cheminv.app.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.cheminv.app.web.rest.TestUtil;

public class ItemTransactionTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ItemTransaction.class);
        ItemTransaction itemTransaction1 = new ItemTransaction();
        itemTransaction1.setId(1L);
        ItemTransaction itemTransaction2 = new ItemTransaction();
        itemTransaction2.setId(itemTransaction1.getId());
        assertThat(itemTransaction1).isEqualTo(itemTransaction2);
        itemTransaction2.setId(2L);
        assertThat(itemTransaction1).isNotEqualTo(itemTransaction2);
        itemTransaction1.setId(null);
        assertThat(itemTransaction1).isNotEqualTo(itemTransaction2);
    }
}
