package websocket;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.Map;
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
    private Map<String, Object> sessionM;
    private static final Set<WebSocketAnnotation> connections = new CopyOnWriteArraySet < >();
    
    public WebSocketAnnotation() {
    	//this.username = (String) sessionM.get("username");
    	this.username = "hugo";
    }

    @OnOpen
    public void start(Session session) {
        this.session = session;
        connections.add(this);
        String message = "*" + username + "* connected.";
        System.out.println(message);
        
        //newDonation(	"hugo", 	"joao", 	200, 	"project#1");
        //newDonation(	"joao", 	"hugo", 	100, 	"project#2");
        //newMessage(		"hugo", 	"joao");
        //newMessage(		"joao", 	"hugo");
        //updateProjectMoney(100, 200, "joao");
    }

    @OnClose
    public void end() {
    	// clean up once the WebSocket connection is closed
    	connections.remove(this);
    	String message = "*" + username + "* disconnected.";
    	System.out.println(message);
    }

    @OnMessage
    public void receiveMessage(String message) {}
    
    @OnError
    public void handleError(Throwable t) {
    	t.printStackTrace();
    }

    private void sendMessage(String text, int option, String usernameToSend) {
    	for(WebSocketAnnotation client: connections){
    		try {
    			synchronized(client){
    				if(option==1)
    				{
    					//ainda nao funcional-> ver o project Money, atualizar o dinheiro de um projecto
    					client.session.getBasicRemote().sendText(text);
    				}
    				else
    				{
    					if(client.username.equals(usernameToSend))
    					{
    						client.session.getBasicRemote().sendText(text);
    					}
    				}
    				
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
    			System.out.println(message);
    		}
    	}
    	
    }
    
    public void updateProjectMoney(int received, int money, String administrator)
    {
    	String text = ("[1]"+received+"/"+money);
    	sendMessage(text, 1, administrator);
    }
    
    public void newDonation(String donator, String administrator, int donation, String projectName)
    {
    	String text = ("[0]"+donator + " sent " + donation+"€ to your project: "+ projectName);
    	sendMessage(text, 2, administrator);
    }
    public void newMessage(String from, String to)
    {
    	System.out.println("newMEssage > from:" +from +"to: "+to);
    	String text = ("[0]"+from + " sent a message");
    	sendMessage(text, 2, to);
    }

	public Map<String, Object> getSessionM() {
		return sessionM;
	}

	public void setSessionM(Map<String, Object> sessionM) {
		this.sessionM = sessionM;
	}
    
    
}
