package sd.model;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

import common.Client;
import common.Response;
import rmiserver.RMIServerInterface;

public class DonateBean {

	private String ammount;
	private String projectID;
	private RMIServerInterface server;
	

	public int donate(String username) throws RemoteException {
				
		Response resp = new Response();
		
		try {
			server = (RMIServerInterface) LocateRegistry.getRegistry("localhost", 7000).lookup("RMIServer");
			try {
				resp = server.incrementProjectMoney(new Client(username, null), Integer.parseInt(projectID), Integer.parseInt(ammount));
				if(resp.isSuccess()){
					return 0;
				}
				return -1;
				
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		catch(NotBoundException|RemoteException e) {
			e.printStackTrace();
		}
		return -1;
		
	}






	public String getAmmount() {
		return ammount;
	}


	public void setAmmount(String ammount) {
		this.ammount = ammount;
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
