package cloud.frizio.dev.demospringwebsocket.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import cloud.frizio.dev.demospringwebsocket.BingoWebSocketHandler;

/**
 * WebSockeConfig: 
 * 1) Abilitare nel progetto l’utilizzo delle WebSocket di Spring;
 * 2) definire l’handler che gestirà il ciclo di vita della comunicazione con i client.
 * 
 * Socket URL: ws://localhost:8080/bingo/host/websocket
 */
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
  
  private static final Log log = LogFactory.getLog( WebSocketConfig.class );

  @Override
  public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
    log.info( "Registering ws bingo handler ");
    registry.addHandler(bingotHandler(), "/host")
        .setAllowedOrigins("*") // Avoid 403
         .withSockJS();
  }

  @Bean
    public WebSocketHandler bingotHandler() {
      // Definire l’handler di una WebSocket significa implementare l’interfaccia WebSocketHandler 
      // (o meglio estendere una delle classi BinaryWebSocketHandler o TextWebSocketHandler).
        return new BingoWebSocketHandler();
    }
}
