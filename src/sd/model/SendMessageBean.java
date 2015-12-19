package sd.model;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.ArrayList;

import common.Client;
import common.Response;
import rmiserver.RMIServerInterface;
import websocket.WebSocketAnnotation;

public class SendMessageBean {

	private String message;
	private String projectID, from, to;
	private RMIServerInterface server;
	private WebSocketAnnotation wsAnnotation = new WebSocketAnnotation();

	public int send(String username) throws RemoteException {
		System.out.println("send SENDMESSAGEBEAN");
		Response resp = new Response();
		Response resp2 = new Response();
		from = username;
		//to = "hugo";

		try {
			server = (RMIServerInterface) LocateRegistry.getRegistry("localhost", 7000).lookup("RMIServer");
			
			try {
				resp = server.sendMessageToProject(new Client(username, null), Integer.parseInt(projectID), message);
				//from = username;
				resp2 = server.getAdministrator(Integer.parseInt(projectID));
				to = resp2.getInfo().get(0);
				System.out.println(from);
				System.out.println(to);
				wsAnnotation.newMessage(from, to);
				if (resp.isSuccess()) {
					return 0;
				}
				return -1;

			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (NotBoundException | RemoteException e) {
			e.printStackTrace();
		}
		return -1;

	}
	public ArrayList<String> getProjects() {
		System.out.println("getProjects DETAILS");
		// hostname = configs.getServer1();
		// hostname2 = configs.getServer2();
		// registryNumber= configs.getRmi_port();
		ArrayList<String> projects = new ArrayList<String>();
		Response resp = new Response();
		try {
			// registry = LocateRegistry.getRegistry(configs.getRmi_port());
			// server = (RMIServerInterface) Naming.lookup("RMIServer");
			server = (RMIServerInterface) LocateRegistry.getRegistry("localhost", 7000).lookup("RMIServer");
			System.out.println("RMI connected");
			try {
				resp = server.listProjects(0);
				if (resp.isSuccess()) {
					if (!resp.getInfo().isEmpty()) {
						projects = resp.getInfo();
					} else {
						projects.add("NENHUM");
					}

				}
				// projects = server.getAvailableProjects();

			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (NotBoundException | RemoteException e) {
			e.printStackTrace(); // what happens *after* we reach this line?
		}
		// projects = server.getAvailableProjects();
		System.out.println("projects: " + projects);
		return projects;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getProjectID() {
		return projectID;
	}

	public void setProjectID(String projectID) {
		System.out.println("projectID: " + projectID);
		this.projectID = projectID;
	}

	public RMIServerInterface getServer() {
		return server;
	}

	public void setServer(RMIServerInterface server) {
		this.server = server;
	}

}
