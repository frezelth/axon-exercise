package eu.europa.ec.cc.academy;

import eu.europa.ec.cc.academy.eventhandler.TabReadModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.stereotype.Component;

/**
 * @author <a href="thomas.frezel@ext.ec.europa.eu">Thomas Frezel</a>
 * @version $
 */
@Component
@Profile("localdev")
public class ElasticsearchCleanIndex {

  @Autowired
  private ElasticsearchRestTemplate elasticsearchTemplate;

  @EventListener
  public void handleContextRefresh(ApplicationReadyEvent event) {
    elasticsearchTemplate.deleteIndex(TabReadModel.class);
    elasticsearchTemplate.createIndex(TabReadModel.class);
    elasticsearchTemplate.putMapping(TabReadModel.class);
    elasticsearchTemplate.refresh(TabReadModel.class);
  }

}
