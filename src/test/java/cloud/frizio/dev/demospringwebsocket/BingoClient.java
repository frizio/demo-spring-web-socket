package cloud.frizio.dev.demospringwebsocket;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import javax.websocket.DeploymentException;

import org.springframework.web.socket.client.WebSocketConnectionManager;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

/**
 * BingoClient
 */
public class BingoClient {

  public static void main(String[] args) throws DeploymentException, IOException, URISyntaxException {
    String webSocketServerUrl = "ws://localhost:8080/bingo/host/websocket";
    
    Set<WebSocketConnectionManager> connectionManagers = new HashSet<WebSocketConnectionManager>();
    
    for ( int i = 1; i <= 2; i++ ) {
      WebSocketConnectionManager connectionManager 
        = new WebSocketConnectionManager( new StandardWebSocketClient(), new BingoPlayer( i ), webSocketServerUrl,  args);
      
      connectionManagers.add( connectionManager );
      
      connectionManager.start();			
    }
    
    Scanner input = new Scanner(System.in);
    input.next();
    
    for ( WebSocketConnectionManager connectionManager : connectionManagers ) {
      connectionManager.stop();
    }
    
    input.close();
    
  }

}
