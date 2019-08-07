package eu.europa.ec.cc.academy.api.command;

import eu.europa.ec.cc.academy.domain.OrderedItemVO;
import lombok.*;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.util.Collection;
import java.util.UUID;

/**
 * @author <a href="thomas.frezel@ext.ec.europa.eu">Thomas Frezel</a>
 * @version $
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlaceOrder {

  @TargetAggregateIdentifier
  private UUID tabId;

  private @Singular Collection<OrderedItemVO> items;

}
