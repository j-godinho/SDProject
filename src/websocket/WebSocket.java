package websocket;

import java.io.IOException;
import java.util.Set ;
import java.util.concurrent.CopyOnWriteArraySet ;
import javax.websocket.server.ServerEndpoint;
import javax.websocket.OnOpen;
import javax.servlet.http.HttpSession;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnError;
import javax.websocket.Session;

@ServerEndpoint(value = "/ws")
public class WebSocket {
    private String username;
    private Session session;
    private HttpSession httpSession;
    private static final Set<WebSocket> connections = new CopyOnWriteArraySet < >();
    
    public WebSocket() {
    }

    @OnOpen
    public void start(Session session, EndpointConfig config) {
        this.session = session;
        connections.add(this);
        this.httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
        this.username = (String) httpSession.getAttribute("username");
		
        String message = "*" + username + "* connected.";
        System.out.println(message);
        
        
        /*
        newDonation(	"hugo", 	"joao", 	200, 	"project#1");
        newDonation(	"joao", 	"hugo", 	100, 	"project#2");
        newMessage(		"hugo", 	"joao", 			"project#1");
        newMessage(		"joao", 	"hugo", 			"project#2");
        updateProjectMoney(100, 200, "joao");
    	*/
    
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
    	for(WebSocket client: connections){
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
    	String text = ("[0]"+donator + " sent " + donation+"� to your project: "+ projectName);
    	sendMessage(text, 2, administrator);
    }
    public void newMessage(String from, String to, String projectName)
    {
    	String text = ("[0]"+from + " sent a message to your project: "+projectName);
    	sendMessage(text, 2, to);
    }
}