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
			Client client = new Client("hugo", null);
			try {
				//resp = server.listProjects(0);
				resp = server.getClientProjects(client);
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

}
