package eu.europa.ec.cc.academy;

import lombok.SneakyThrows;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableAsync;

import java.lang.reflect.Method;

/**
 * @author <a href="thomas.frezel@ext.ec.europa.eu">Thomas Frezel</a>
 * @version $
 */
@SpringBootApplication
@EnableAsync
@EnableCaching
public class Application {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  @Profile("localdev")
  @Bean
  @SneakyThrows
  public Object h2TcpServer(Environment environment) {
    // so we can easily start multiple instances locally by passing h2.port (to test the client side failover)
    String port = environment.getProperty("h2.port", "9092");
    Class<?> h2Server = Class.forName("org.h2.tools.Server");
    Method createTcpServer = h2Server.getDeclaredMethod("createTcpServer", String[].class);
    Object tcpServer = createTcpServer
        .invoke(null, new Object[]{new String[]{"-tcp", "-tcpAllowOthers", "-tcpPort", port}});
    Method startMethod = tcpServer.getClass().getMethod("start");
    return startMethod.invoke(tcpServer);
  }

}
