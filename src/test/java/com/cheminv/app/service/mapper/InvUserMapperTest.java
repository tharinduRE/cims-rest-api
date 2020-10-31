package com.cheminv.app.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class InvUserMapperTest {

    private InvUserMapper invUserMapper;

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(invUserMapper.userFromId(id).getId()).isEqualTo(id);
        assertThat(invUserMapper.userFromId(null)).isNull();
    }
}
