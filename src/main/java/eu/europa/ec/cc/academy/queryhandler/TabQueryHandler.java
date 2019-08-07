package eu.europa.ec.cc.academy.queryhandler;

import eu.europa.ec.cc.academy.infrastructure.TabRepository;
import lombok.RequiredArgsConstructor;
import org.axonframework.queryhandling.QueryHandler;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilterBuilder;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Component;

/**
 * @author <a href="thomas.frezel@ext.ec.europa.eu">Thomas Frezel</a>
 * @version $
 */
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TabQueryHandler {

  private final ElasticsearchRestTemplate elasticsearchTemplate;

  private final TabRepository repository;

  @QueryHandler
  public Page findTabs(FindTabsQuery findTabsQuery){

    BoolQueryBuilder criteriaQuery = QueryBuilders.boolQuery();
    if (findTabsQuery.isEmpty()) {
      // criteria object is empty, return everything
      criteriaQuery.must(QueryBuilders.matchAllQuery());
    } else {
      criteriaQuery.must(QueryBuilders.matchQuery("tableNumber", findTabsQuery.getTableNumber()));
    }

    NativeSearchQueryBuilder searchQueryBuilder = new NativeSearchQueryBuilder()
        .withSourceFilter(new FetchSourceFilterBuilder().build())
        .withPageable(findTabsQuery.getPageable())
        .withQuery(criteriaQuery);

    NativeSearchQuery searchQuery = searchQueryBuilder.build();

    return repository.search(searchQuery);
  }

}
