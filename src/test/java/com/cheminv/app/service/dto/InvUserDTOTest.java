package com.cheminv.app.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.cheminv.app.web.rest.TestUtil;

public class InvUserDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(InvUserDTO.class);
        InvUserDTO invUserDTO1 = new InvUserDTO();
        invUserDTO1.setId(1L);
        InvUserDTO invUserDTO2 = new InvUserDTO();
        assertThat(invUserDTO1).isNotEqualTo(invUserDTO2);
        invUserDTO2.setId(invUserDTO1.getId());
        assertThat(invUserDTO1).isEqualTo(invUserDTO2);
        invUserDTO2.setId(2L);
        assertThat(invUserDTO1).isNotEqualTo(invUserDTO2);
        invUserDTO1.setId(null);
        assertThat(invUserDTO1).isNotEqualTo(invUserDTO2);
    }
}
