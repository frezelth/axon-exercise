package eu.europa.ec.cc.academy.api.event;

import lombok.Builder;
import lombok.Value;
import org.axonframework.serialization.Revision;

import java.util.UUID;

/**
 * @author <a href="thomas.frezel@ext.ec.europa.eu">Thomas Frezel</a>
 * @version $
 */
@Builder
@Value
@Revision("0.1")
public class TabOpened {

  private UUID tabId;
  private String waiter;
  private int tableNumber;

}
