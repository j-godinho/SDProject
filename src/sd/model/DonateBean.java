package sd.model;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

import common.Client;
import common.Response;
import rmiserver.RMIServerInterface;
import websocket.WebSocketAnnotation;

public class DonateBean {

	private String rewardID;
	private String projectID, administrator, projectName;
	private int donation;
	private RMIServerInterface server;
	private WebSocketAnnotation wsAnnotation = new WebSocketAnnotation();
	
	public int donate(String username) throws RemoteException {

		Response resp = new Response();

		try {
			server = (RMIServerInterface) LocateRegistry.getRegistry("localhost", 7000).lookup("RMIServer");
			try {
				System.out.println("username: "+username + " :projectID: "+projectID+" rewardID: "+rewardID);
				resp = server.incrementProjectMoney(new Client(username, null), Integer.parseInt(projectID),Integer.parseInt(rewardID));
				//administratos = server.getAdministrator(projectID);
				//donation = server.getDonation(rewardID);
				//projectName = server.getProjectName(projectID);
				if (resp.isSuccess()) {
					//wsAnnotation.newDonation(username, administrator, donation, projectName);
					System.out.println("Sucesso");
					return 0;
				}
				System.out.println("Fracasso");
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


	public String getRewardID() {
		return rewardID;
	}


	public void setRewardID(String rewardID) {
		this.rewardID = rewardID;
	}


	public String getProjectID() {
		return projectID;
	}

	public void setProjectID(String projectID) {
		this.projectID = projectID;
	}

	public RMIServerInterface getServer() {
		return server;
	}

	public void setServer(RMIServerInterface server) {
		this.server = server;
	}

}
