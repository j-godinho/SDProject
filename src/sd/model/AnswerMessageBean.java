package sd.model;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.ArrayList;

import common.Client;
import common.Response;
import rmiserver.RMIServerInterface;
import websocket.WebSocketAnnotation;

public class AnswerMessageBean {

	private String text;
	private String answerID;
	private ArrayList <String> messages;
	private RMIServerInterface server;
	private WebSocketAnnotation wsAnnotation = new WebSocketAnnotation();

	public int listMessages(String username) throws RemoteException {
		System.out.println("list messages function");
		Response resp = new Response();
		
		try {
			System.out.println("text: " + text + " answerID: "+ answerID);
			server = (RMIServerInterface) LocateRegistry.getRegistry("localhost", 7000).lookup("RMIServer");
			try {
				resp = server.showMessages(new Client(username, null));
				if (resp.isSuccess()) {
					if(!resp.getInfo().isEmpty()){
						messages = pretty(resp.getInfo());
						System.out.println("Messages: "+ messages);
						return 1;
					}
					else
					{
						messages.add("No messages");
						return 1;
					}
				}
				else{
					return -1;
				}	

			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (NotBoundException | RemoteException e) {
			e.printStackTrace();
		}
		return 1;

	}
	
	private ArrayList<String> pretty(ArrayList <String> array)
	{
		
		ArrayList <String> temp = new ArrayList <String>();
        int numProj = (array.size() + 1) / 5;
        int res = 0;
        for (int i = 0; i < numProj; i++) {
        	temp.add("ID: "+ array.get(res));
        	temp.add("SENDER: "+ array.get(res+1));
        	temp.add("PROJECTID: "+array.get(res+3));
        	temp.add("TEXT: "+ array.get(res+4));
            res += 5;
        }
        return temp;
	}
	
	public int answer(String username) throws RemoteException {

		Response resp = new Response();
		Response resp2 = new Response();
		//	public synchronized Response showMessages(Client client) {
		try {
			server = (RMIServerInterface) LocateRegistry.getRegistry("localhost", 7000).lookup("RMIServer");
			
			try {
				resp = server.answerQuestion(new Client(username, null), Integer.parseInt(answerID), text);
				resp2 = server.getSenderName(Integer.parseInt(answerID));
				System.out.println("HI");
				wsAnnotation.newMessage(username, resp2.getInfo().get(0));
				if (resp.isSuccess()) {
					return 1;
				}
				else{
					return -1;
				}	

			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (NotBoundException | RemoteException e) {
			e.printStackTrace();
		}
		return -1;

	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getAnswerID() {
		return answerID;
	}

	public void setAnswerID(String answerID) {
		this.answerID = answerID;
	}

	public ArrayList<String> getMessages() {
		return messages;
	}

	public void setMessages(ArrayList<String> messages) {
		this.messages = messages;
	}

	public RMIServerInterface getServer() {
		return server;
	}

	public void setServer(RMIServerInterface server) {
		this.server = server;
	}

	public WebSocketAnnotation getWsAnnotation() {
		return wsAnnotation;
	}

	public void setWsAnnotation(WebSocketAnnotation wsAnnotation) {
		this.wsAnnotation = wsAnnotation;
	}


	
	
	
}
