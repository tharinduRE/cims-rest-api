package com.cheminv.app.repository.search;

import com.cheminv.app.domain.InvDepartment;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link InvDepartment} entity.
 */
public interface InvDepartmentSearchRepository extends ElasticsearchRepository<InvDepartment, Long> {
}
