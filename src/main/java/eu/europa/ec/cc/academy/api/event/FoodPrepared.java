package eu.europa.ec.cc.academy.api.event;

import lombok.Builder;
import lombok.Value;
import org.axonframework.serialization.Revision;

import java.util.List;
import java.util.UUID;

/**
 * @author <a href="thomas.frezel@ext.ec.europa.eu">Thomas Frezel</a>
 * @version $
 */
@Value
@Builder
@Revision("0.1")
public class FoodPrepared {

  private UUID tabId;

  private List<Integer> items;

}
