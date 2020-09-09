package com.cheminv.app.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.cheminv.app.web.rest.TestUtil;

public class HazardCodeTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(HazardCode.class);
        HazardCode hazardCode1 = new HazardCode();
        hazardCode1.setId(1L);
        HazardCode hazardCode2 = new HazardCode();
        hazardCode2.setId(hazardCode1.getId());
        assertThat(hazardCode1).isEqualTo(hazardCode2);
        hazardCode2.setId(2L);
        assertThat(hazardCode1).isNotEqualTo(hazardCode2);
        hazardCode1.setId(null);
        assertThat(hazardCode1).isNotEqualTo(hazardCode2);
    }
}
