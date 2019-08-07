package eu.europa.ec.cc.academy.domain;

import lombok.*;

import java.math.BigDecimal;

/**
 * @author <a href="thomas.frezel@ext.ec.europa.eu">Thomas Frezel</a>
 * @version $
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderedItemVO {

  private Integer item;
  private String description;
  private boolean drink;
  private BigDecimal price;

}
