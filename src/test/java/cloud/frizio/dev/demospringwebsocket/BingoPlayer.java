package cloud.frizio.dev.demospringwebsocket;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

/**
 * BingoPlayer
 */
public class BingoPlayer extends TextWebSocketHandler {
  
  private static Random random = new Random( Calendar.getInstance().getTimeInMillis() );
  
  private int id;

  private Set<String> card = new HashSet<String>();
  
  private int found = 0;
  
  private boolean started = false;
    
  public BingoPlayer(){}
  public BingoPlayer(int number ) {
    this.id = number;	
  }

  public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
    if ( message.getPayload().equalsIgnoreCase( "start" ) ) {
      /* Generate the card */
      card.clear();
      for ( int i = 0; i < 5; i++ ) {
        card.add( String.valueOf( random.nextInt( 90 ) + 1 ) );
      }
      found = 0;
      started = true;
      System.out.println( "Player " + id + ": " + card );
    } else if ( started && card.contains( message.getPayload() ) ) {
      System.out.println( "Player " + id + ": found " + message.getPayload() );
      found++;
      if ( found == card.size() ) {
        System.out.println( "Player " + id + ": BINGO !!!" );
        session.sendMessage( new TextMessage( "bingo" ) );
      } 
    } else if ( started && message.getPayload().equalsIgnoreCase( "bingo" ) ) {
      started = false;
    }
    session.sendMessage( new TextMessage( "ok" ) );
  }
}
