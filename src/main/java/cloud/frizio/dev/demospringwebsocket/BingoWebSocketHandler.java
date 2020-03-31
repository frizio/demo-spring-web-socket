package cloud.frizio.dev.demospringwebsocket;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import cloud.frizio.dev.demospringwebsocket.event.BingoEvent;
import cloud.frizio.dev.demospringwebsocket.event.DrawEvent;
import cloud.frizio.dev.demospringwebsocket.event.StartEvent;

/**
 * BingoWebSocketHandler
 * Definisce l'handler della WebSocket, che gestisce il ciclo di vita della comunicazione con i client.
 * 
 * L’handler di una WebSocket andrà ad estendere le classi:
 * - BinaryWebSocketHandler: classe che ammette esclusivamente messaggi di tipo binario;
 * - TextWebSocketHandler:   classe che ammette esclusivamente messaggi di tipo testo;
 */
public class BingoWebSocketHandler extends TextWebSocketHandler implements ApplicationListener<ApplicationEvent> {

  private static final Log log = LogFactory.getLog( BingoWebSocketHandler.class );

  private static Map<String, WebSocketSession> sessions = new HashMap<String, WebSocketSession>();
  
  @Autowired
  private ApplicationEventPublisher applicationEventPublisher;
  
  //---------------------------------------------------------------------------------------
  // Methods managing WebSocket lifecycle
  //---------------------------------------------------------------------------------------
  /*
   * Metodo invocato quando la connessione WebSocket aperta e pronto per l’uso.
   */ 
  @Override
  public void afterConnectionEstablished(WebSocketSession session) throws Exception {
    log.info( "New player id " + session.getId() );
    synchronized (sessions) {
      sessions.put( session.getId(), session );
      try {
        session.sendMessage( new TextMessage( "A game every minute ... please wait" ) );
      } catch (IOException e) {
        log.error( "Error sending greetings", e );
      }
    }
  }
  /*
   * Metodo invocato quando arriva un nuovo messaggio dalla connessione WebSocket.
   */
  @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
    if ( message.getPayload().equalsIgnoreCase("bingo") ) {
      log.info( "Bingo from player id " + session.getId() );
      applicationEventPublisher.publishEvent( new BingoEvent( this ) );
      sendToAll( "bingo" );
    }
  }
  /*
   * 	Metodo invocato quando la connessione WebSocket è stata chiusa da entrambi i lati 
   * o dopo che si è verificato un errore di trasporto.
   */
  @Override
  public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
    log.info( "Abandoned player id " + session.getId() );
    synchronized (sessions) {
      sessions.remove( session.getId() );
    }
  }
  /*
   * Metodo che gestione un eventuale errore di trasporto del messaggio nella WebSocket sottostante.
   */
  @Override
  public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
    log.error( exception.getMessage() );
  }

  //---------------------------------------------------------------------------------------
  // Methods managing bingo events
  //---------------------------------------------------------------------------------------
  @Override
  public void onApplicationEvent(ApplicationEvent event) {
    if ( event instanceof DrawEvent ) {
      sendToAll( ((DrawEvent) event).getNumber().toString() );
    } else if ( event instanceof StartEvent ) {
      sendToAll( "start" );
    }
  }
  
  //---------------------------------------------------------------------------------------
  // Methods managing communication to the clients
  //---------------------------------------------------------------------------------------
  private void sendToAll( String message ) {
    try {
      if ( sessions != null && !sessions.isEmpty() ) {
        synchronized (sessions) {
          for ( WebSocketSession session : sessions.values() ) {
            if ( session.isOpen() ) {
              session.sendMessage( new TextMessage( message ) );
            }
          }
        }
      }
    } catch (IOException e) {
      log.error("Error sending message", e);
    }
  }

}
