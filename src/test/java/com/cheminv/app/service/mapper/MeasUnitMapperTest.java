package com.cheminv.app.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class MeasUnitMapperTest {

    private MeasUnitMapper measUnitMapper;

    @BeforeEach
    public void setUp() {
        measUnitMapper = new MeasUnitMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(measUnitMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(measUnitMapper.fromId(null)).isNull();
    }
}
