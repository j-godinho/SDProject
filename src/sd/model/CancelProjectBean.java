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

public class CancelProjectBean {
	private RMIServerInterface server;
	static String hostname;
	static String hostname2;
	static int registryNumber;
	private Registry registry;
	private String choice;
	private ArrayList<String> rewards;
	private ArrayList<String> projects;
	// public Configs configs = new Configs();

	
	public int cancelProject(String username) {

		// hostname = configs.getServer1();
		// hostname2 = configs.getServer2();
		// registryNumber= configs.getRmi_port();
		Response resp = new Response();
		try {
			// registry = LocateRegistry.getRegistry(configs.getRmi_port());
			// server = (RMIServerInterface) Naming.lookup("RMIServer");
			server = (RMIServerInterface) LocateRegistry.getRegistry("localhost", 7000).lookup("RMIServer");
			System.out.println("RMI connected");
			System.out.println("choice: " +choice);
			try {
				resp = server.removeProject(Integer.parseInt(choice));
				if (resp.isSuccess()) {
					System.out.println("canceled project");
					return 1;

				}
				else{
					return 0;
				}

			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (NotBoundException | RemoteException e) {
			e.printStackTrace(); // what happens *after* we reach this line?
		}
		return 0;
	}
	public int getProjects(String username) {
		System.out.println("getProjects DETAILS");
		// hostname = configs.getServer1();
		// hostname2 = configs.getServer2();
		// registryNumber= configs.getRmi_port();
		Response resp = new Response();
		try {
			// registry = LocateRegistry.getRegistry(configs.getRmi_port());
			// server = (RMIServerInterface) Naming.lookup("RMIServer");
			server = (RMIServerInterface) LocateRegistry.getRegistry("localhost", 7000).lookup("RMIServer");
			System.out.println("RMI connected");
			Client client = new Client(username, null);
			try {
				//resp = server.listProjects(0);
				resp = server.getClientProjects(client);
				if (resp.isSuccess()) {
					if (!resp.getInfo().isEmpty()) {
						projects = pretty(resp.getInfo());
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
		return 1;
	}
	public String getChoice() {
		return choice;
	}

	public void setChoice(String choice) {
		this.choice = choice;
	}

	public ArrayList<String> getRewards() {
		return rewards;
	}

	public void setRewards(ArrayList<String> rewards) {
		this.rewards = rewards;
	}
	public ArrayList<String> getProjects() {
		return projects;
	}
	public void setProjects(ArrayList<String> projects) {
		this.projects = projects;
	}
	
	private ArrayList<String> pretty(ArrayList <String> array)
	{
		
		ArrayList <String> temp = new ArrayList <String>();
        int numProj = (array.size() + 1) / 5;
        int res = 0;
        for (int i = 0; i < numProj; i++) {
        	temp.add("ID: "+ array.get(res));
        	temp.add("NAME: "+ array.get(res+1));
        	temp.add("DATE: "+ array.get(res+2));
        	temp.add("MONEY: "+array.get(res+3));
        	temp.add("GOAL: "+ array.get(res+4));
            res += 5;
        }
        return temp;
	}
	
	

}
