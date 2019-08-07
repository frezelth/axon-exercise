package eu.europa.ec.cc.academy.eventhandler;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.List;

/**
 * @author <a href="thomas.frezel@ext.ec.europa.eu">Thomas Frezel</a>
 * @version $
 */
@Data
@Document(indexName = "#{ elasticSearchConfig.indexName }", type="_doc")
public class TabReadModel {

  @Id
  @Field(type = FieldType.Keyword)
  private String tabId;

  @Field(type = FieldType.Integer)
  private Integer tableNumber;

  @Field(type = FieldType.Keyword)
  private String waiter;

  @Field(type = FieldType.Float)
  private Float amountPaid;

  @Field(type = FieldType.Float)
  private Float tipValue;

  @Field(type = FieldType.Float)
  private Float orderPrice = 0f;

  @Field(type = FieldType.Nested)
  private List<TabOrderedItemReadModel> outstanding;

  @Field(type = FieldType.Nested)
  private List<TabOrderedItemReadModel> prepared;

  @Field(type = FieldType.Nested)
  private List<TabOrderedItemReadModel> served;

  @Field(type = FieldType.Boolean)
  private Boolean opened;

}
