package cloud.frizio.dev.demospringwebsocket.event;

import org.springframework.context.ApplicationEvent;

/**
 * DrawEvent: estrazione di un numero
 */
@SuppressWarnings("serial")
public class DrawEvent extends ApplicationEvent {
  
  private Integer number;

  public DrawEvent(Object source, Integer number) {
    super(source);
    this.number = number;
  }
  
  public Integer getNumber() {
    return number;
  }
}
