package cloud.frizio.dev.demospringwebsocket.tablou;

import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Scheduled;

import cloud.frizio.dev.demospringwebsocket.event.BingoEvent;
import cloud.frizio.dev.demospringwebsocket.event.DrawEvent;
import cloud.frizio.dev.demospringwebsocket.event.StartEvent;

/**
 * TablouService:
 * Gestione delle estrazioni, che sono eseguite al ritmo di una al minuto.
 * La comunicazione degli eventi di estrazione è eseguita in modo asincrono (Utilizzo degli eventi di Spring):
 * 1) Gli eventi sono oggetti che estendono la classe ApplicationEvent.
 * 2) Il publisher emette gli eventi attraverso la classe ApplicationEventPublisher disponibile nel contesto di Spring ed iniettabile
 *    attraverso l’annotazione @Autowired.
 * 3) I listener degli eventi devono implementare l’interfaccia ApplicationListener
 * 
 * Sono necessarie tre classi che rappresentano i tre eventi gestiti dall’applicazione ovvero: 
 * l’avvio di una sessione di gioco, l’estrazione di un numero e il bingo da parte di uno dei partecipanti.
 * 
 * Questa classe emette (publish) gli eventi StartEvent e DrawEvent 
 * ed è in ascolto (receive) dell’evento BingoEvent al fine di interrompere l’estrazione.
 * 
 */
public class TablouService implements ApplicationListener<BingoEvent> {

  private static final Log log = LogFactory.getLog( TablouService.class );
  private AtomicBoolean bingo = new AtomicBoolean( false );
  private Tablou tablou = new Tablou();

  @Autowired
  private ApplicationEventPublisher applicationEventPublisher;
  

  @Scheduled( initialDelay = 10000, fixedDelay = 60000 )
  public void start() {
    log.info( "Started bingo session" );
    tablou.clear();
    bingo.set( false );
    applicationEventPublisher.publishEvent( new StartEvent(this) );
    Integer number;
    do {
      number = tablou.next();
      log.info( "Extracted: " + number );
      applicationEventPublisher.publishEvent( new DrawEvent( this, number ) );
      try {
        Thread.sleep( 250 );
      } catch (InterruptedException e) {
        log.error( "Sleep interrupted", e );
      }
    } while ( !tablou.isCompleted() && !bingo.get() );
    log.info( "Stopped bingo session" );
  }

  @Override
  public void onApplicationEvent(BingoEvent event) {
    bingo.set( true );
  }
}