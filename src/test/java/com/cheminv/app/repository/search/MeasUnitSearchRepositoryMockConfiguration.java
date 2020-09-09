package com.cheminv.app.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link MeasUnitSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class MeasUnitSearchRepositoryMockConfiguration {

    @MockBean
    private MeasUnitSearchRepository mockMeasUnitSearchRepository;

}
