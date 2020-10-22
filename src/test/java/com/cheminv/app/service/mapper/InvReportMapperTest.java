package com.cheminv.app.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class InvReportMapperTest {

    private InvReportMapper invReportMapper;

    @BeforeEach
    public void setUp() {
        invReportMapper = new InvReportMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(invReportMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(invReportMapper.fromId(null)).isNull();
    }
}
