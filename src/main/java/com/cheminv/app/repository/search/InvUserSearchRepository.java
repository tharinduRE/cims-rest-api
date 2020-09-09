package com.cheminv.app.repository.search;

import com.cheminv.app.domain.InvUser;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link InvUser} entity.
 */
public interface InvUserSearchRepository extends ElasticsearchRepository<InvUser, Long> {
}
