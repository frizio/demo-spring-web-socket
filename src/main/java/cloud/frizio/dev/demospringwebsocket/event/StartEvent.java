package cloud.frizio.dev.demospringwebsocket.event;

import org.springframework.context.ApplicationEvent;

/**
 * StartEvent: avvio di una sessione di gioco.
 */
@SuppressWarnings("serial")
public class StartEvent extends ApplicationEvent {

  public StartEvent(Object source) {
    super(source);
  }

}