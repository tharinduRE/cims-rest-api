package com.cheminv.app.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class InvDepartmentMapperTest {

    private InvDepartmentMapper invDepartmentMapper;

    @BeforeEach
    public void setUp() {
        invDepartmentMapper = new InvDepartmentMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(invDepartmentMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(invDepartmentMapper.fromId(null)).isNull();
    }
}
