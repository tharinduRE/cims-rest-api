package com.cheminv.app.repository.search;

import com.cheminv.app.domain.ItemManufacturer;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link ItemManufacturer} entity.
 */
public interface ItemManufacturerSearchRepository extends ElasticsearchRepository<ItemManufacturer, Long> {
}
