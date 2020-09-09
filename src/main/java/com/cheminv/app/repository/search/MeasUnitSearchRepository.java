package com.cheminv.app.repository.search;

import com.cheminv.app.domain.MeasUnit;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link MeasUnit} entity.
 */
public interface MeasUnitSearchRepository extends ElasticsearchRepository<MeasUnit, Long> {
}
