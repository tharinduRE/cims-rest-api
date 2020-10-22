package com.cheminv.app.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.cheminv.app.web.rest.TestUtil;

public class CasNumberTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CasNumber.class);
        CasNumber casNumber1 = new CasNumber();
        casNumber1.setId(1L);
        CasNumber casNumber2 = new CasNumber();
        casNumber2.setId(casNumber1.getId());
        assertThat(casNumber1).isEqualTo(casNumber2);
        casNumber2.setId(2L);
        assertThat(casNumber1).isNotEqualTo(casNumber2);
        casNumber1.setId(null);
        assertThat(casNumber1).isNotEqualTo(casNumber2);
    }
}
