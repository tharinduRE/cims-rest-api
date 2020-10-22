package com.cheminv.app.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.cheminv.app.web.rest.TestUtil;

public class InvStoreTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InvStore.class);
        InvStore invStore1 = new InvStore();
        invStore1.setId(1L);
        InvStore invStore2 = new InvStore();
        invStore2.setId(invStore1.getId());
        assertThat(invStore1).isEqualTo(invStore2);
        invStore2.setId(2L);
        assertThat(invStore1).isNotEqualTo(invStore2);
        invStore1.setId(null);
        assertThat(invStore1).isNotEqualTo(invStore2);
    }
}
