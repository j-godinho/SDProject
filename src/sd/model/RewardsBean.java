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

public class RewardsBean {
	private RMIServerInterface server;
	static String hostname;
    static String hostname2;
    static int registryNumber;
	private Registry registry;
	
	//public Configs configs = new Configs();
	
	public RewardsBean(){
		
		getRewards();
	}
	
	
	public ArrayList<String> getRewards(){
		
		//hostname = configs.getServer1();
        //hostname2 = configs.getServer2();
        //registryNumber= configs.getRmi_port();
		Client client = new Client((String)session.get("username"), (String)session.get("password"));
		ArrayList <String> rewards = new ArrayList <String>();
		Response resp = new Response();
		try {
			//registry = LocateRegistry.getRegistry(configs.getRmi_port());
			//server = (RMIServerInterface) Naming.lookup("RMIServer");
			server = (RMIServerInterface) LocateRegistry.getRegistry("localhost", 7000).lookup("RMIServer");
			System.out.println("RMI connected");
			try {
				resp = server.getClientRewards(client);
				if(resp.isSuccess()){
					System.out.println("Success rewards");
					rewards = resp.getInfo();
				}
				else{
					
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
		//projects = server.getAvailableProjects();
		System.out.println("rewards: " +rewards);
		return rewards;
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}
	
	
	
}
