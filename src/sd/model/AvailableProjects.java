package sd.model;

import java.util.ArrayList;

import common.Configs;
import common.Response;

import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import rmiserver.RMIServerInterface;

public class AvailableProjects {
	private RMIServerInterface server;
	static String hostname;
	static String hostname2;
	static int registryNumber;
	private Registry registry;
	private ArrayList<String> projects;
	// public Configs configs = new Configs();

	public AvailableProjects() {
		getProjects();
	}

	/*
	 * public AvailableProjects() { try { registry =
	 * LocateRegistry.getRegistry(1099); server = (RMIServerInterface)
	 * Naming.lookup("server"); }
	 * catch(NotBoundException|MalformedURLException|RemoteException e) {
	 * e.printStackTrace(); // what happens *after* we reach this line? }
	 * 
	 * }
	 */

	public ArrayList<String> getProjects() {

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

}
