package eu.europa.ec.cc.academy.infrastructure;

import eu.europa.ec.cc.academy.eventhandler.TabReadModel;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface TabRepository extends ElasticsearchRepository<TabReadModel, String> {
}
