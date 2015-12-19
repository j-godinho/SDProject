package sd.model;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

import common.Response;
import rmiserver.RMIServerInterface;

public class UserBean {
	private RMIServerInterface server;
	public UserBean(){
	}
	

	
	
	public String getAuthorizationURL() {
		System.out.println("Get authorization url");
		RMIServerInterface server;
		String temp;
		//hostname = configs.getServer1();
        //hostname2 = configs.getServer2();
        //registryNumber= configs.getRmi_port();
		try {
			//registry = LocateRegistry.getRegistry(configs.getRmi_port());
			//server = (RMIServerInterface) Naming.lookup("RMIServer");
			server = (RMIServerInterface) LocateRegistry.getRegistry("localhost", 7000).lookup("RMIServer");
			try {
				 temp = server.getAuthorizationURL();
				 return temp;

			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		}
		catch(NotBoundException|RemoteException e) {
			e.printStackTrace(); 
			return null;
		}
	}
	
	public Response loginTumblr(String authVerifier){
		System.out.println("login tumblr function bean");
		RMIServerInterface server;
		Response temp;
		//hostname = configs.getServer1();
        //hostname2 = configs.getServer2();
        //registryNumber= configs.getRmi_port();
		try {
			//registry = LocateRegistry.getRegistry(configs.getRmi_port());
			//server = (RMIServerInterface) Naming.lookup("RMIServer");
			server = (RMIServerInterface) LocateRegistry.getRegistry("localhost", 7000).lookup("RMIServer");
			try {
				 temp = server.loginTumblr(authVerifier);
				 
				 return temp;

			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		}
		catch(NotBoundException|RemoteException e) {
			e.printStackTrace(); 
			return null;
		}
	}
	
	public int logout(String username){
		Response resp = new Response();
		try {
			server = (RMIServerInterface) LocateRegistry.getRegistry("localhost", 7000).lookup("RMIServer");
			System.out.println("RMI Connected");
			try {
				resp = server.updateOnlineUsers(0, username);
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
	public int login(String username){
		Response resp = new Response();
		try {
			server = (RMIServerInterface) LocateRegistry.getRegistry("localhost", 7000).lookup("RMIServer");
			System.out.println("RMI Connected");
			try {
				resp = server.updateOnlineUsers(1, username);
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

	
}
