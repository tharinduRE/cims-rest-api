package com.cheminv.app.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.cheminv.app.web.rest.TestUtil;

public class MeasUnitTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MeasUnit.class);
        MeasUnit measUnit1 = new MeasUnit();
        measUnit1.setId(1L);
        MeasUnit measUnit2 = new MeasUnit();
        measUnit2.setId(measUnit1.getId());
        assertThat(measUnit1).isEqualTo(measUnit2);
        measUnit2.setId(2L);
        assertThat(measUnit1).isNotEqualTo(measUnit2);
        measUnit1.setId(null);
        assertThat(measUnit1).isNotEqualTo(measUnit2);
    }
}
