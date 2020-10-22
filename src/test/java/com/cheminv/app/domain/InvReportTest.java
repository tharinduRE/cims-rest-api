package com.cheminv.app.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.cheminv.app.web.rest.TestUtil;

public class InvReportTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InvReport.class);
        InvReport invReport1 = new InvReport();
        invReport1.setId(1L);
        InvReport invReport2 = new InvReport();
        invReport2.setId(invReport1.getId());
        assertThat(invReport1).isEqualTo(invReport2);
        invReport2.setId(2L);
        assertThat(invReport1).isNotEqualTo(invReport2);
        invReport1.setId(null);
        assertThat(invReport1).isNotEqualTo(invReport2);
    }
}
