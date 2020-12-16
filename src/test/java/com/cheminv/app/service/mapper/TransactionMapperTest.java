package com.cheminv.app.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class TransactionMapperTest {

    private ItemTransactionMapper itemTransactionMapper;

    @BeforeEach
    public void setUp() {
        itemTransactionMapper = new ItemTransactionMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(itemTransactionMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(itemTransactionMapper.fromId(null)).isNull();
    }
}
