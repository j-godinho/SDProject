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
						details = pretty2(resp.getInfo());
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
						projects = pretty(resp.getInfo());
						projects.addAll(pretty(resp2.getInfo()));
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
	
	private ArrayList<String> pretty2(ArrayList <String> array)
	{
		ArrayList <String> temp = new ArrayList<>();
		String canceled, finished;
		
		if(array.get(6).equals("0")){
            canceled = "Not canceled";
        }
        else{
            canceled = "Canceled";
        }
        if(array.get(7).equals("0")){
            finished = "Not finished";
        }
        else{
            finished = "Finished";
        }
        temp.add("Name: " + array.get(0));
        temp.add("Admin: " + array.get(1));
        temp.add("Description: " + array.get(2));
        temp.add("Money: " + array.get(3));
        temp.add("Goal: " + array.get(4));
        temp.add("Deadline: " + array.get(5));
        temp.add(canceled);
        temp.add(finished);
        
        int numRew = ((array.size() - 8)/4);
        System.out.println(numRew);
        int beg = 9;
        for(int i = 0; i < numRew; i++){
            temp.add("RewardID: " + array.get(beg));
            temp.add("Description: " + array.get(beg + 1));
            temp.add("Money: " + array.get(beg + 2));
            temp.add("ProjectID: " + array.get(beg + 3));
            beg = beg + 4;
        }
        temp.add("Most voted choive: " +array.get(beg+1));
        
        return temp;
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
