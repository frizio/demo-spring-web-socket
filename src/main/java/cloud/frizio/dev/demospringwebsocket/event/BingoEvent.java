package cloud.frizio.dev.demospringwebsocket.event;

import org.springframework.context.ApplicationEvent;

/**
 * BingoEvent: bingo da parte di uno dei partecipanti
 */
@SuppressWarnings("serial")
public class BingoEvent extends ApplicationEvent {
  
  public BingoEvent(Object source) {
    super(source);
  }
  
}
