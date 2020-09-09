package com.cheminv.app.repository.search;

import com.cheminv.app.domain.ItemStock;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link ItemStock} entity.
 */
public interface ItemStockSearchRepository extends ElasticsearchRepository<ItemStock, Long> {
}
