package eu.europa.ec.cc.academy;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@EnableElasticsearchRepositories(basePackages = "eu.europa.ec")
public class ElasticSearchConfig extends AbstractElasticsearchConfiguration {

    @Value("${elasticsearch.index-name}")
    private String indexName;

    @Autowired
    private RestHighLevelClient client;

    public String getIndexName(){
        return indexName;
    }

    public ElasticSearchConfig() {
//    System.setProperty("es.set.netty.runtime.available.processors", "false");
    }

    /**
     * Return the {@link RestHighLevelClient} instance used to connect to the cluster. <br /> Annotate
     * with {@link Bean} in case you want to expose a {@link RestHighLevelClient} instance to the
     * {@link ApplicationContext}.
     *
     * @return never {@literal null}.
     */
    @Override
    public RestHighLevelClient elasticsearchClient() {
        return client;
    }

    @Bean("elasticsearchTemplate")
    public ElasticsearchRestTemplate elasticsearchTemplate() {
        return new ElasticsearchRestTemplate(elasticsearchClient(), elasticsearchConverter(), resultsMapper());
    }
}
