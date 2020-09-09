package com.cheminv.app.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class InvStorageMapperTest {

    private InvStorageMapper invStorageMapper;

    @BeforeEach
    public void setUp() {
        invStorageMapper = new InvStorageMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(invStorageMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(invStorageMapper.fromId(null)).isNull();
    }
}
