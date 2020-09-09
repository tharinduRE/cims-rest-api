package com.cheminv.app.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.cheminv.app.web.rest.TestUtil;

public class InvDepartmentTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InvDepartment.class);
        InvDepartment invDepartment1 = new InvDepartment();
        invDepartment1.setId(1L);
        InvDepartment invDepartment2 = new InvDepartment();
        invDepartment2.setId(invDepartment1.getId());
        assertThat(invDepartment1).isEqualTo(invDepartment2);
        invDepartment2.setId(2L);
        assertThat(invDepartment1).isNotEqualTo(invDepartment2);
        invDepartment1.setId(null);
        assertThat(invDepartment1).isNotEqualTo(invDepartment2);
    }
}
