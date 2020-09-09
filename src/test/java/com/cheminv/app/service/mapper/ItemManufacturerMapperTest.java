package com.cheminv.app.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class ItemManufacturerMapperTest {

    private ItemManufacturerMapper itemManufacturerMapper;

    @BeforeEach
    public void setUp() {
        itemManufacturerMapper = new ItemManufacturerMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(itemManufacturerMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(itemManufacturerMapper.fromId(null)).isNull();
    }
}
