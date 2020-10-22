package com.cheminv.app.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.cheminv.app.web.rest.TestUtil;

public class HazardCodeDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(HazardCodeDTO.class);
        HazardCodeDTO hazardCodeDTO1 = new HazardCodeDTO();
        hazardCodeDTO1.setId(1L);
        HazardCodeDTO hazardCodeDTO2 = new HazardCodeDTO();
        assertThat(hazardCodeDTO1).isNotEqualTo(hazardCodeDTO2);
        hazardCodeDTO2.setId(hazardCodeDTO1.getId());
        assertThat(hazardCodeDTO1).isEqualTo(hazardCodeDTO2);
        hazardCodeDTO2.setId(2L);
        assertThat(hazardCodeDTO1).isNotEqualTo(hazardCodeDTO2);
        hazardCodeDTO1.setId(null);
        assertThat(hazardCodeDTO1).isNotEqualTo(hazardCodeDTO2);
    }
}
