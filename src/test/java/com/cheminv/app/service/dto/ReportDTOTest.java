package com.cheminv.app.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.cheminv.app.web.rest.TestUtil;

public class ReportDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(InvReportDTO.class);
        InvReportDTO invReportDTO1 = new InvReportDTO();
        invReportDTO1.setId(1L);
        InvReportDTO invReportDTO2 = new InvReportDTO();
        assertThat(invReportDTO1).isNotEqualTo(invReportDTO2);
        invReportDTO2.setId(invReportDTO1.getId());
        assertThat(invReportDTO1).isEqualTo(invReportDTO2);
        invReportDTO2.setId(2L);
        assertThat(invReportDTO1).isNotEqualTo(invReportDTO2);
        invReportDTO1.setId(null);
        assertThat(invReportDTO1).isNotEqualTo(invReportDTO2);
    }
}
