package websocket;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.Set ;
import java.util.concurrent.CopyOnWriteArraySet ;
import javax.websocket.server.ServerEndpoint;
import javax.websocket.OnOpen;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnError;
import javax.websocket.Session;
import javax.websocket.*;


@ServerEndpoint(value = "/ws")
public class WebSocketAnnotation {
    private static final AtomicInteger sequence = new AtomicInteger(1);
    private final String username;
    private Session session;
    private static final Set<WebSocketAnnotation> connections = new CopyOnWriteArraySet < >();
    
    public WebSocketAnnotation() {
        username = "User" + sequence.getAndIncrement();
    }

    @OnOpen
    public void start(Session session) {
        this.session = session;
        connections.add(this);
        String message = "*" + username + "* connected.";
        sendMessage(message);
    }

    @OnClose
    public void end() {
    	// clean up once the WebSocket connection is closed
    	connections.remove(this);
    	String message = "*" + username + "* disconnected.";
    	sendMessage(message);
    }

    @OnMessage
    public void receiveMessage(String message) {
		// one should never trust the client, and sensitive HTML
        // characters should be replaced with &lt; &gt; &quot; &amp;
    	//String reversedMessage = new StringBuffer(message).reverse().toString();
    	sendMessage("[" + username + "] " + message);
    }
    
    @OnError
    public void handleError(Throwable t) {
    	t.printStackTrace();
    }

    private void sendMessage(String text) {
    	// uses *this* object's session to call sendText()
    	for(WebSocketAnnotation client: connections){
    		try {
    			synchronized(client){
    				client.session.getBasicRemote().sendText(text);
    			}	
    		} catch (IOException e) {
    			// clean up once the WebSocket connection is closed
    			connections.remove(client);
    			try {
    				client.session.close();
    			} catch (IOException e1) {
    				e1.printStackTrace();
    			}
    			String message = String.format ("* %s %s" , client.username , " has   been   disconnected ." );
    			sendMessage( message );
    		}
    	}
    	
    }
}
