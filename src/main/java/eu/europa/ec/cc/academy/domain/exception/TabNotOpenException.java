package eu.europa.ec.cc.academy.domain.exception;

/**
 * @author <a href="thomas.frezel@ext.ec.europa.eu">Thomas Frezel</a>
 * @version $
 */
public class TabNotOpenException extends RuntimeException {

  public TabNotOpenException() {
    super("Tab is not open");
  }
}
