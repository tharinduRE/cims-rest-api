package com.cheminv.app.repository.search;

import com.cheminv.app.domain.ItemTransaction;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link ItemTransaction} entity.
 */
public interface ItemTransactionSearchRepository extends ElasticsearchRepository<ItemTransaction, Long> {
}
