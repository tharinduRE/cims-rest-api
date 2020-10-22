package com.cheminv.app.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class WasteItemMapperTest {

    private WasteItemMapper wasteItemMapper;

    @BeforeEach
    public void setUp() {
        wasteItemMapper = new WasteItemMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(wasteItemMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(wasteItemMapper.fromId(null)).isNull();
    }
}
