package sd.model;
import java.util.ArrayList;

import common.Answer;
import common.Client;
import common.Configs;
import common.Data;
import common.Message;
import common.Project;
import common.Response;
import common.Reward;

import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import rmiserver.RMIServerInterface;

public class UpdateRewardsBean{
	private RMIServerInterface server;
	static String hostname;
    static String hostname2;
    static int registryNumber;
	private Registry registry;
	private String choice, projID;
    private Reward reward;
    private String rewardType, rewardValue, rewardID;
	
	//public Configs configs = new Configs();	
	
	
	public int addReward(String username){
		System.out.println("addRewardBean");
		//hostname = configs.getServer1();
        //hostname2 = configs.getServer2();
        //registryNumber= configs.getRmi_port();
		Response resp = new Response();
		try {
			//registry = LocateRegistry.getRegistry(configs.getRmi_port());
			//server = (RMIServerInterface) Naming.lookup("RMIServer");
			server = (RMIServerInterface) LocateRegistry.getRegistry("localhost", 7000).lookup("RMIServer");
			System.out.println("RMI connected");
			System.out.println("id: "+choice+" rewardType: "+rewardType+" rewardValue: "+rewardValue);
			try {
				resp = server.insertReward(Integer.parseInt(projID), rewardType, Integer.parseInt(rewardValue));
				if(resp.isSuccess()){
					return 0;
					
				}
				return -1;
				//projects = server.getAvailableProjects();
				
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		catch(NotBoundException|RemoteException e) {
			e.printStackTrace(); // what happens *after* we reach this line?
		}
		return 0;
	}
	public int removeReward(String username){
		System.out.println("removeReward");
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
				resp = server.removeReward(Integer.parseInt(choice), Integer.parseInt(rewardID));
				if(resp.isSuccess()){
					return 0;
					
				}
				return -1;
				//projects = server.getAvailableProjects();
				
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		catch(NotBoundException|RemoteException e) {
			e.printStackTrace(); // what happens *after* we reach this line?
		}
		return 0;
	}
	public String getChoice() {
		return choice;
	}
	public void setChoice(String choice) {
		this.choice = choice;
	}
	public String getProjID() {
		return projID;
	}
	public void setProjID(String projID) {
		this.projID = projID;
	}
	public String getRewardType() {
		return rewardType;
	}
	public void setRewardType(String rewardType) {
		this.rewardType = rewardType;
	}
	public String getRewardValue() {
		return rewardValue;
	}
	public void setRewardValue(String rewardValue) {
		this.rewardValue = rewardValue;
	}
	public String getRewardID() {
		return rewardID;
	}
	public void setRewardID(String rewardID) {
		this.rewardID = rewardID;
	}


	
	
}
