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

public class CreateProjectBean{
	private RMIServerInterface server;
	static String hostname;
    static String hostname2;
    static int registryNumber;
	private Registry registry;
	Project project;
	private String name;
    private Client admin;
    private String description, moneyString, mainGoalString, yearString, monthString, dayString;
   	private int money;
    private int mainGoal;
    private Data deadline;
    private Answer choices;
    private ArrayList <String> rewardsString = new ArrayList<>();
    private ArrayList <Integer> rewardsInt = new ArrayList<>();
    private ArrayList <Reward> rewards = new ArrayList<>();
    //private ArrayList <String> goals;
    private ArrayList <Message> messages = new ArrayList<>();
    
    private String question;
    private ArrayList<String> answers;
	
	//public Configs configs = new Configs();	
	
	
	public int insertProject(String username){
		System.out.println("getProjects");
		//hostname = configs.getServer1();
        //hostname2 = configs.getServer2();
        //registryNumber= configs.getRmi_port();
		/*Reward aux;
		for(int i = 0; i < rewardsString.size(); i++){
			aux = new Reward(rewardsString.get(i), rewardsInt.get(i));
			rewards.add(aux);
		}*/
		System.out.println("name: "+name);
		admin = new Client(username, null);
		money = 0;
		deadline = new Data(Integer.parseInt(yearString), Integer.parseInt(monthString), Integer.parseInt(dayString));
		mainGoal = Integer.parseInt(mainGoalString);
		project = new Project(name, admin, description, money, deadline,  mainGoal, rewards);
		Response resp = new Response();
		try {
			//registry = LocateRegistry.getRegistry(configs.getRmi_port());
			//server = (RMIServerInterface) Naming.lookup("RMIServer");
			server = (RMIServerInterface) LocateRegistry.getRegistry("localhost", 7000).lookup("RMIServer");
			System.out.println("RMI connected");
			try {
				resp = server.insertNewProject(project);
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



	public RMIServerInterface getServer() {
		return server;
	}



	public void setServer(RMIServerInterface server) {
		this.server = server;
	}



	public Registry getRegistry() {
		return registry;
	}



	public void setRegistry(Registry registry) {
		this.registry = registry;
	}



	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public Client getAdmin() {
		return admin;
	}



	public void setAdmin(Client admin) {
		this.admin = admin;
	}



	public String getDescription() {
		return description;
	}



	public void setDescription(String description) {
		this.description = description;
	}



	public int getMoney() {
		return money;
	}



	public void setMoney(int money) {
		this.money = money;
	}



	public int getMainGoal() {
		return mainGoal;
	}



	public void setMainGoal(int mainGoal) {
		this.mainGoal = mainGoal;
	}



	public Data getDeadline() {
		return deadline;
	}



	public void setDeadline(Data deadline) {
		this.deadline = deadline;
	}



	public ArrayList<Reward> getRewards() {
		return rewards;
	}



	public void setRewards(ArrayList<Reward> rewards) {
		this.rewards = rewards;
	}



	public ArrayList<Message> getMessages() {
		return messages;
	}



	public void setMessages(ArrayList<Message> messages) {
		this.messages = messages;
	}
	
	public String getMoneyString() {
		return moneyString;
	}



	public void setMoneyString(String moneyString) {
		this.moneyString = moneyString;
	}



	public String getMainGoalString() {
		return mainGoalString;
	}



	public void setMainGoalString(String mainGoalString) {
		this.mainGoalString = mainGoalString;
	}



	public String getYearString() {
		return yearString;
	}



	public void setYearString(String yearString) {
		this.yearString = yearString;
	}



	public String getMonthString() {
		return monthString;
	}



	public void setMonthString(String monthString) {
		this.monthString = monthString;
	}



	public String getDayString() {
		return dayString;
	}



	public void setDayString(String dayString) {
		this.dayString = dayString;
	}
	

	
	
}
