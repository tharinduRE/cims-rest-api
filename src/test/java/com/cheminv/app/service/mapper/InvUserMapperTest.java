package com.cheminv.app.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class InvUserMapperTest {

    private InvUserMapper invUserMapper;

    @BeforeEach
    public void setUp() {
        invUserMapper = new InvUserMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(invUserMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(invUserMapper.fromId(null)).isNull();
    }
}
