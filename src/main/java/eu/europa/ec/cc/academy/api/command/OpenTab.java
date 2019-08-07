package eu.europa.ec.cc.academy.api.command;

import lombok.*;
import lombok.Builder.Default;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 * @author <a href="thomas.frezel@ext.ec.europa.eu">Thomas Frezel</a>
 * @version $
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OpenTab {

  @TargetAggregateIdentifier
  @Default
  private UUID tabId = UUID.randomUUID();

  @NotNull
  private Integer tableNumber;

  @NotNull
  private String waiter;

}
