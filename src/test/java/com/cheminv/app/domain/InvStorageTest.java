package com.cheminv.app.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.cheminv.app.web.rest.TestUtil;

public class InvStorageTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InvStorage.class);
        InvStorage invStorage1 = new InvStorage();
        invStorage1.setId(1L);
        InvStorage invStorage2 = new InvStorage();
        invStorage2.setId(invStorage1.getId());
        assertThat(invStorage1).isEqualTo(invStorage2);
        invStorage2.setId(2L);
        assertThat(invStorage1).isNotEqualTo(invStorage2);
        invStorage1.setId(null);
        assertThat(invStorage1).isNotEqualTo(invStorage2);
    }
}
