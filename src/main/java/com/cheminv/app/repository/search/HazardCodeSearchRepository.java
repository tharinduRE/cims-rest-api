package com.cheminv.app.repository.search;

import com.cheminv.app.domain.HazardCode;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link HazardCode} entity.
 */
public interface HazardCodeSearchRepository extends ElasticsearchRepository<HazardCode, Long> {
}
