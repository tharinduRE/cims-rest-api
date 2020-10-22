package com.cheminv.app.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class WasteVendorMapperTest {

    private WasteVendorMapper wasteVendorMapper;

    @BeforeEach
    public void setUp() {
        wasteVendorMapper = new WasteVendorMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(wasteVendorMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(wasteVendorMapper.fromId(null)).isNull();
    }
}
