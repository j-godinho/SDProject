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
	private int choice;
	//public Configs configs = new Configs();
	
	public ProjectDetailsBean(){
		getProjects();
		getDetails();
	}
	
	public ArrayList<String> getDetails(){
		ArrayList<String> detail = null;
		Response resp = new Response();
		try{
			resp = server.projectDetails(choice);
			if(resp.isSuccess()){
				detail = resp.getInfo();
			}
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return detail;
	}
	
	public ArrayList<String> getProjects(){
		
		//hostname = configs.getServer1();
        //hostname2 = configs.getServer2();
        //registryNumber= configs.getRmi_port();
		ArrayList <String> projects = new ArrayList <String>();
		Response resp = new Response();
		try {
			//registry = LocateRegistry.getRegistry(configs.getRmi_port());
			//server = (RMIServerInterface) Naming.lookup("RMIServer");
			server = (RMIServerInterface) LocateRegistry.getRegistry("localhost", 7000).lookup("RMIServer");
			System.out.println("RMI connected");
			try {
				resp = server.listProjects(0);
				if(resp.isSuccess()){
					if(!resp.getInfo().isEmpty()){
						projects = resp.getInfo();
					}
					else{
						projects.add("NENHUM");
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
		System.out.println("projects: " +projects);
		return projects;
	}


	public int getChoice() {
		return choice;
	}


	public void setChoice(int choice) {
		this.choice = choice;
	}
	
}
