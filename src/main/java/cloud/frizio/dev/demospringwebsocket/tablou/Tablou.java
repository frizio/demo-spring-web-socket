package cloud.frizio.dev.demospringwebsocket.tablou;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

/**
 * Tablou:
 * Gestione del tabellone
 */
@SuppressWarnings("serial")
public class Tablou extends ArrayList<Integer> {

  private Random random = new Random( Calendar.getInstance().getTimeInMillis() );
  
  synchronized public Integer next() {
    Integer next = 0;
    do {
      next = random.nextInt( 90 ) + 1;
    } while ( contains( next ) );
    add( next );
    return next;
  }
  
  synchronized public boolean isCompleted() {
    return super.size() == 90;
  }
}
