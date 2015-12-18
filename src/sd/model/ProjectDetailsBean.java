package sd.model;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;

import common.Response;
import rmiserver.RMIServerInterface;

public class ProjectDetailsBean {
	private RMIServerInterface server;
	static String hostname;
	static String hostname2;
	static int registryNumber;
	private String choice;
	ArrayList <String> details = new ArrayList <>();
	// public Configs configs = new Configs();


	public int getInfo() {
		Response resp = new Response();
		try {
			// registry = LocateRegistry.getRegistry(configs.getRmi_port());
			// server = (RMIServerInterface) Naming.lookup("RMIServer");
			server = (RMIServerInterface) LocateRegistry.getRegistry("localhost", 7000).lookup("RMIServer");
			System.out.println("RMI connected");
			System.out.println("choice: " + choice);
			try {
				resp = server.projectDetails(Integer.parseInt(choice));
				if (resp.isSuccess()) {
					if (!resp.getInfo().isEmpty()) {
						details = resp.getInfo();
						return 1;
					} else {
						details.add("NENHUM");
						return 1;
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
		System.out.println("details: " + details);
		return -1;
	}

	public ArrayList<String> getProjects() {
		System.out.println("getProjects DETAILS");
		// hostname = configs.getServer1();
		// hostname2 = configs.getServer2();
		// registryNumber= configs.getRmi_port();
		ArrayList<String> projects = new ArrayList<String>();
		Response resp = new Response();
		Response resp2 = new Response();
		try {
			// registry = LocateRegistry.getRegistry(configs.getRmi_port());
			// server = (RMIServerInterface) Naming.lookup("RMIServer");
			server = (RMIServerInterface) LocateRegistry.getRegistry("localhost", 7000).lookup("RMIServer");
			System.out.println("RMI connected");
			try {
				resp = server.listProjects(0);
				resp2 = server.listProjects(1);
				if (resp.isSuccess()) {
					if (!resp.getInfo().isEmpty()) {
						projects = resp.getInfo();
						projects.addAll(resp2.getInfo());
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
	
	public ArrayList<String> getDetails() {
		return details;
	}



	public void setDetails(ArrayList<String> details) {
		this.details = details;
	}



	public String getChoice() {
		System.out.println("get Choice: "+choice);
		return choice;
	}

	public void setChoice(String choice) {
		this.choice = choice;
	}

}
