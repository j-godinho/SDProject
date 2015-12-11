package sd.model;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

import common.Client;
import common.Response;
import rmiserver.RMIServerInterface;

public class DonateBean {

	private String rewardID;
	private String projectID;
	private RMIServerInterface server;

	public int donate(String username) throws RemoteException {

		Response resp = new Response();

		try {
			server = (RMIServerInterface) LocateRegistry.getRegistry("localhost", 7000).lookup("RMIServer");
			try {
				System.out.println("username: "+username + " :projectID: "+projectID+" rewardID: "+rewardID);
				resp = server.incrementProjectMoney(new Client(username, null), Integer.parseInt(projectID),Integer.parseInt(rewardID));
				if (resp.isSuccess()) {
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
