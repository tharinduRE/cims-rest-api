package com.cheminv.app.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.cheminv.app.web.rest.TestUtil;

public class TransactionDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ItemTransactionDTO.class);
        ItemTransactionDTO itemTransactionDTO1 = new ItemTransactionDTO();
        itemTransactionDTO1.setId(1L);
        ItemTransactionDTO itemTransactionDTO2 = new ItemTransactionDTO();
        assertThat(itemTransactionDTO1).isNotEqualTo(itemTransactionDTO2);
        itemTransactionDTO2.setId(itemTransactionDTO1.getId());
        assertThat(itemTransactionDTO1).isEqualTo(itemTransactionDTO2);
        itemTransactionDTO2.setId(2L);
        assertThat(itemTransactionDTO1).isNotEqualTo(itemTransactionDTO2);
        itemTransactionDTO1.setId(null);
        assertThat(itemTransactionDTO1).isNotEqualTo(itemTransactionDTO2);
    }
}
