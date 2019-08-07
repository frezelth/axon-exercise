package eu.europa.ec.cc.academy.domain.exception;

/**
 * @author <a href="thomas.frezel@ext.ec.europa.eu">Thomas Frezel</a>
 * @version $
 */
public class FoodNotPreparedException extends RuntimeException {

  public FoodNotPreparedException() {
    super("Food is not ready");
  }
}
