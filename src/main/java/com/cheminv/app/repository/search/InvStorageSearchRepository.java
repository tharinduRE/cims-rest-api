package com.cheminv.app.repository.search;

import com.cheminv.app.domain.InvStorage;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link InvStorage} entity.
 */
public interface InvStorageSearchRepository extends ElasticsearchRepository<InvStorage, Long> {
}
