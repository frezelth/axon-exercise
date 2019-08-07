package eu.europa.ec.cc.academy.api.command;

import lombok.*;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

/**
 * @author <a href="thomas.frezel@ext.ec.europa.eu">Thomas Frezel</a>
 * @version $
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MarkFoodServed {

  @TargetAggregateIdentifier
  private UUID tabId;

  @NotNull
  private @Singular List<Integer> items;

}
