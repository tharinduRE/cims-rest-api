package com.cheminv.app.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.cheminv.app.web.rest.TestUtil;

public class DepartmentDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(InvDepartmentDTO.class);
        InvDepartmentDTO invDepartmentDTO1 = new InvDepartmentDTO();
        invDepartmentDTO1.setId(1L);
        InvDepartmentDTO invDepartmentDTO2 = new InvDepartmentDTO();
        assertThat(invDepartmentDTO1).isNotEqualTo(invDepartmentDTO2);
        invDepartmentDTO2.setId(invDepartmentDTO1.getId());
        assertThat(invDepartmentDTO1).isEqualTo(invDepartmentDTO2);
        invDepartmentDTO2.setId(2L);
        assertThat(invDepartmentDTO1).isNotEqualTo(invDepartmentDTO2);
        invDepartmentDTO1.setId(null);
        assertThat(invDepartmentDTO1).isNotEqualTo(invDepartmentDTO2);
    }
}
