package com.cheminv.app.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.cheminv.app.web.rest.TestUtil;

public class InvUserTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InvUser.class);
        InvUser invUser1 = new InvUser();
        invUser1.setId(1L);
        InvUser invUser2 = new InvUser();
        invUser2.setId(invUser1.getId());
        assertThat(invUser1).isEqualTo(invUser2);
        invUser2.setId(2L);
        assertThat(invUser1).isNotEqualTo(invUser2);
        invUser1.setId(null);
        assertThat(invUser1).isNotEqualTo(invUser2);
    }
}
