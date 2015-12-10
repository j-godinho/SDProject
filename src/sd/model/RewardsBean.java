package sd.model;
import java.util.ArrayList;

import common.Client;
import common.Configs;
import common.Response;

import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import rmiserver.RMIServerInterface;

public class RewardsBean{
	private RMIServerInterface server;
	static String hostname;
    static String hostname2;
    static int registryNumber;
	private Registry registry;
	private ArrayList<String> rewards;
	//public Configs configs = new Configs();
	



	public RewardsBean(String username){
		getClientRewards(username);
	}
	
	
	
	public void getClientRewards(String username){
		
		//hostname = configs.getServer1();
        //hostname2 = configs.getServer2();
        //registryNumber= configs.getRmi_port();
		Response resp = new Response();
		try {
			//registry = LocateRegistry.getRegistry(configs.getRmi_port());
			//server = (RMIServerInterface) Naming.lookup("RMIServer");
			server = (RMIServerInterface) LocateRegistry.getRegistry("localhost", 7000).lookup("RMIServer");
			System.out.println("RMI connected");
			try {
				resp = server.getClientRewards(new Client(username, null));
				if(resp.isSuccess()){
					if(!resp.getInfo().isEmpty()){
						rewards = resp.getInfo();
						System.out.println("Rewards: " + rewards);
					}
					
				}
				//projects = server.getAvailableProjects();
				
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		catch(NotBoundException|RemoteException e) {
			e.printStackTrace(); // what happens *after* we reach this line?
		}
	}
	
	
	public ArrayList<String> getRewards() {
		return rewards;
	}

	public void setRewards(ArrayList<String> rewards) {
		this.rewards = rewards;
	}

	
	
}
