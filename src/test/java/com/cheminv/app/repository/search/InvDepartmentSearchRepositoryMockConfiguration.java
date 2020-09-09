package com.cheminv.app.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link InvDepartmentSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class InvDepartmentSearchRepositoryMockConfiguration {

    @MockBean
    private InvDepartmentSearchRepository mockInvDepartmentSearchRepository;

}
