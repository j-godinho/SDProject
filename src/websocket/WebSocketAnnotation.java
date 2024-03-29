package websocket;
import sd.model.*;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set ;
import java.util.concurrent.CopyOnWriteArraySet ;
import javax.websocket.server.ServerEndpoint;


import javax.websocket.OnOpen;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnError;
import javax.websocket.Session;
import javax.servlet.http.HttpSession;
import javax.websocket.*;


@ServerEndpoint(value = "/ws" , configurator = GetHttpSessionConfigurator.class)
public class WebSocketAnnotation {
    private static final AtomicInteger sequence = new AtomicInteger(1);
    private String username = null;
    private Session session;
    private HttpSession sessionHTTP, httpSession;
    private Map<String, Object> sessionM;
    private static final Map<String, WebSocketAnnotation> connecT = Collections.synchronizedMap(new HashMap<String, WebSocketAnnotation>());
    private static final Set<WebSocketAnnotation> connections = new CopyOnWriteArraySet < >();
    private AuthenticationBean authenticationBean;
    
    public WebSocketAnnotation() {
    	//this.username = (String) sessionM.get("username");
    	//this.username = "joao";
    }

    @OnOpen
    public void start(Session session, EndpointConfig config) {
        /*this.session = session;
        this.sessionHTTP = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
        connections.add(this);
        if (this.sessionHTTP!=null)
            System.out.println("here");
            this.username = ((AuthenticationBean)this.sessionHTTP.getAttribute("authenticationBean")).getUsername();
         */ 
    	//this.session = session;
        //connections.add(this);
    	this.session = session;
		this.httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
		this.username = (String) httpSession.getAttribute("username");
		this.authenticationBean = ((AuthenticationBean) httpSession.getAttribute("authenticationBean"));
            
            
		System.out.println("[WS - " + this.username + "] Registration: " + username);
		if(connecT.containsKey(username)){
			connecT.replace(username, this);
			System.out.println("[WS - " + this.username + "] Registration: " + username + " - UPDATED");
		}
		else{
			connecT.put(username, this);
		}
		System.out.println("[WS - " + this.username + "] Registration: " + username + " - DONE");
		
		System.out.println("[WS - " + this.username + "] new user");
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

    private void sendMessage(String text, int option, String username) {
    	/*for(WebSocketAnnotation client: connections){
    		
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
    	}*/
    	System.out.println("[WS] @ sendMessage - destinatario: " + username);
		WebSocketAnnotation ws = connecT.get(username);
		
		if(ws != null){
			if(ws.session.isOpen()){
				System.out.println("[WS] WS open");
			
			
				try {
					System.out.println("[WS] Send Message # " + username + " : " + text);
					ws.session.getBasicRemote().sendText(text);
					System.out.println("[WS] Send Message # " + username + " : " + text + " - DONE");
				} catch (IOException e) {
					System.out.println("[WS] !!! IO : impossivel enviar mensagem.");
					
					
					try {
						System.out.println("[WS] Close WebSocket # " + username);
						ws.session.close();
						System.out.println("[WS] Close WebSocket # " + username + " - DONE");
					} catch (IOException e1) {
						System.out.println("[WS] !!! IO : impossivel fechar WebSocket.");
						
						
					}
					
				}
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
    	System.out.println("donator: "+donator+" administrator: "+administrator+" donation: "+donation+" projName: " +projectName);
    	String text = ("[0]"+donator + " sent " + donation+"� to your project: "+ projectName);
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
