package com.cheminv.app.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.cheminv.app.web.rest.TestUtil;

public class InvStorageDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(InvStorageDTO.class);
        InvStorageDTO invStorageDTO1 = new InvStorageDTO();
        invStorageDTO1.setId(1L);
        InvStorageDTO invStorageDTO2 = new InvStorageDTO();
        assertThat(invStorageDTO1).isNotEqualTo(invStorageDTO2);
        invStorageDTO2.setId(invStorageDTO1.getId());
        assertThat(invStorageDTO1).isEqualTo(invStorageDTO2);
        invStorageDTO2.setId(2L);
        assertThat(invStorageDTO1).isNotEqualTo(invStorageDTO2);
        invStorageDTO1.setId(null);
        assertThat(invStorageDTO1).isNotEqualTo(invStorageDTO2);
    }
}
