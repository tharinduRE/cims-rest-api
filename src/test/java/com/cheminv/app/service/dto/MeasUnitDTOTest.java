package com.cheminv.app.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.cheminv.app.web.rest.TestUtil;

public class MeasUnitDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MeasUnitDTO.class);
        MeasUnitDTO measUnitDTO1 = new MeasUnitDTO();
        measUnitDTO1.setId(1L);
        MeasUnitDTO measUnitDTO2 = new MeasUnitDTO();
        assertThat(measUnitDTO1).isNotEqualTo(measUnitDTO2);
        measUnitDTO2.setId(measUnitDTO1.getId());
        assertThat(measUnitDTO1).isEqualTo(measUnitDTO2);
        measUnitDTO2.setId(2L);
        assertThat(measUnitDTO1).isNotEqualTo(measUnitDTO2);
        measUnitDTO1.setId(null);
        assertThat(measUnitDTO1).isNotEqualTo(measUnitDTO2);
    }
}
