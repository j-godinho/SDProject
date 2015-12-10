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
	private Registry registry;
	private ArrayList<String> projects;
	private String choice;
	ArrayList <String> details = new ArrayList <String>();
	//public Configs configs = new Configs();
	
	public void setDetails(ArrayList<String> details) {
		this.details = details;
	}

	public ProjectDetailsBean(){
		getDetails();
	}
	
	public ArrayList<String> getDetails(){
		ArrayList <String> details = new ArrayList <String>();
		Response resp = new Response();
		try {
			//registry = LocateRegistry.getRegistry(configs.getRmi_port());
			//server = (RMIServerInterface) Naming.lookup("RMIServer");
			server = (RMIServerInterface) LocateRegistry.getRegistry("localhost", 7000).lookup("RMIServer");
			System.out.println("RMI connected");
			System.out.println("choice: " +choice);
			try {
				resp = server.projectDetails(Integer.parseInt("1"));
				if(resp.isSuccess()){
					if(!resp.getInfo().isEmpty()){
						details = resp.getInfo();
					}
					else{
						details.add("NENHUM");
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
		//projects = server.getAvailableProjects();
		System.out.println("details: " +details);
		return details;
	}
	


	public String getChoice() {
		return choice;
	}


	public void setChoice(String choice) {
		this.choice = choice;
	}
	
}
