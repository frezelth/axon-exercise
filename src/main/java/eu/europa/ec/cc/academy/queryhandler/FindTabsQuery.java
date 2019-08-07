package eu.europa.ec.cc.academy.queryhandler;

import lombok.Data;
import org.springframework.data.domain.Pageable;

/**
 * @author <a href="thomas.frezel@ext.ec.europa.eu">Thomas Frezel</a>
 * @version $
 */
@Data
public class FindTabsQuery {

  private Integer tableNumber;

  private Pageable pageable;

  public boolean isEmpty(){
    return tableNumber == null;
  }

}
