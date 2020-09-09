package com.cheminv.app.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class HazardCodeMapperTest {

    private HazardCodeMapper hazardCodeMapper;

    @BeforeEach
    public void setUp() {
        hazardCodeMapper = new HazardCodeMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(hazardCodeMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(hazardCodeMapper.fromId(null)).isNull();
    }
}
