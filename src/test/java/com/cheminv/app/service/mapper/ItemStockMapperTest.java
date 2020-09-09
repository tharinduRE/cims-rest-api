package com.cheminv.app.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class ItemStockMapperTest {

    private ItemStockMapper itemStockMapper;

    @BeforeEach
    public void setUp() {
        itemStockMapper = new ItemStockMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(itemStockMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(itemStockMapper.fromId(null)).isNull();
    }
}
