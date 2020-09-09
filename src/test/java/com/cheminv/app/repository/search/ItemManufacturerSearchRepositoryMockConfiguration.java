package com.cheminv.app.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link ItemManufacturerSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class ItemManufacturerSearchRepositoryMockConfiguration {

    @MockBean
    private ItemManufacturerSearchRepository mockItemManufacturerSearchRepository;

}
