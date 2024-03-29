package rmiserver;

import java.io.IOException;
import java.io.InputStream;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import com.github.scribejava.apis.RenrenApi;
import com.github.scribejava.apis.TumblrApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Token;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.model.Verifier;
import com.github.scribejava.core.oauth.OAuthService;

import common.Answer;
import common.Client;
import common.ClientInterface;
import common.Configs;
import common.Project;
import common.Response;

public class RMIServer extends UnicastRemoteObject implements RMIServerInterface, Runnable {
	private int number;
	public ClientInterface clientIF;
	private DataBaseConstants consts;
	public Configs configs = new Configs();

	Connection c = null;
	Calendar calendar = Calendar.getInstance();
	Timer time = new Timer();
	
	OAuthService service;
	Token requestToken;
	Token accessToken;
	
	RMIServer() throws RemoteException, IOException, ClassNotFoundException, SQLException {
		super();

		Class.forName("org.postgresql.Driver");
		c = DriverManager.getConnection("jdbc:postgresql://" + configs.getDb_address() + ":" + configs.getDb_port()
				+ "/" + configs.getData_base(), configs.getAdmin(), configs.getPass());

		/*
		 * Registry registry =
		 * LocateRegistry.createRegistry(configs.getRmi_port());
		 * registry.rebind("RMIServer", (RMIServerInterface)new RMIServer());
		 * System.out.println("Rmi Server Running...");
		 */
		setService();
		
		
		RMIServer s = this;
		Registry r = LocateRegistry.createRegistry(configs.getRmi_port());
		r.rebind("RMIServer", s);

		initDataBase();

		new Thread(this).start();

		calendar.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);

		time.schedule(new DateController(c, consts), calendar.getTime(), TimeUnit.HOURS.toMillis(24));

		System.out.println("RMISERVER");

	}

	public static void main(String args[]) throws SQLException, ClassNotFoundException, IOException {
		new RMIServer();
	}
	
	public Response updateOnlineUsers(int choice, String username)
	{
		Response temp = new Response();
		System.out.println("UPDATE ONLINE USERS FUNCTION");
		PreparedStatement ps; 
		try {
			if(choice==0)
			{
				ps = c.prepareStatement(consts.removeOnlineUser);
				System.out.println("remove");
			}
			else{
				ps = c.prepareStatement(consts.addOnlineUser);
				System.out.println("add");
			}
			
			ps.setString(1, username);
			
			c.setAutoCommit(false);
			ps.execute();
			c.commit();
			
			temp.setSuccess(true);
		
		} catch (SQLException e) {
			e.printStackTrace();
			temp.setSuccess(false);
		}
		
		return temp;
		
	}
	
	//hugo
	
	public Response getSenderName(int messageID) {
		System.out.println("getSenderName");
		Response temp = new Response();
		try {
			PreparedStatement ps = c.prepareStatement(consts.getSender);
			ps.setInt(1, messageID);
			
			ResultSet result= ps.executeQuery();
			temp.setInfo(toArrayList(result));
			
			temp.setSuccess(true);
		
		} catch (SQLException e) {
			e.printStackTrace();
			temp.setSuccess(false);
		}
		
		return temp;
	}
	
	public Response getAdministrator(int projectID) {
		Response temp = new Response();
		try {
			PreparedStatement ps = c.prepareStatement(consts.getAdmin);
			ps.setInt(1, projectID);
			
			ResultSet result= ps.executeQuery();
			temp.setInfo(toArrayList(result));
			
			temp.setSuccess(true);
		
		} catch (SQLException e) {
			e.printStackTrace();
			temp.setSuccess(false);
		}
		
		return temp;
	}
	
	public Response getDonation(int rewardID){
		Response temp = new Response();
		try {
			PreparedStatement ps = c.prepareStatement(consts.getValue);
			ps.setInt(1, rewardID);
			
			ResultSet result= ps.executeQuery();
			temp.setInfo(toArrayList(result));
			
			temp.setSuccess(true);
		
		} catch (SQLException e) {
			e.printStackTrace();
			temp.setSuccess(false);
		}
		
		return temp;
	}
	
	public Response getProjectName(int projectID) {
		Response temp = new Response();
		try {
			PreparedStatement ps = c.prepareStatement(consts.getProjectName);
			ps.setInt(1, projectID);
			
			ResultSet result= ps.executeQuery();
			temp.setInfo(toArrayList(result));
			
			temp.setSuccess(true);
		
		} catch (SQLException e) {
			e.printStackTrace();
			temp.setSuccess(false);
		}
		
		return temp;
	}
	
	//
	
	//TUMBLR FUNCTIONS

    
	public Response registerUserTumblr(Client client) {
		System.out.println("registerTumblr Function");
		Response temp = new Response();
		try {
			if (checkIfExists(client).isValue() == false) {

				PreparedStatement ps = c.prepareStatement(consts.registerTumblr);
				ps.setString(1, client.getName());

				c.setAutoCommit(false);

				ps.execute();
				c.commit();

				temp.setSuccess(true);
			} else {
				temp.setSuccess(false);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			temp.setSuccess(false);
		}
		return temp;
	}

     
	public void setService(){
		System.out.println("Get service");
		service = new ServiceBuilder()
                .provider(TumblrApi.class)
                .apiKey(configs.getAppKey())
                .apiSecret(configs.getAppSecret())
                .callback("http://localhost:8080/SDProjectGit/callback")
                .build();
		
	}
	
	public String getAuthorizationURL(){
		requestToken = service.getRequestToken();
		return service.getAuthorizationUrl(requestToken);
        
	}

	
	public Response loginTumblr(String authVerifier){
		System.out.println("login tumblr");
		Response resp = new Response();
		resp.setInfo(new ArrayList<String>());
		
		//get acessToken
		Verifier verifier = new Verifier(authVerifier);
		accessToken = service.getAccessToken(requestToken, verifier);

		
        //get username info
		System.out.println("Get Username Info");
		OAuthRequest request = new OAuthRequest(Verb.GET, "https://api.tumblr.com/v2/user/info", service);
		service.signRequest(accessToken, request);
		com.github.scribejava.core.model.Response response = request.send();
		
		JSONObject body = (JSONObject) JSONValue.parse(response.getBody());
		JSONObject message = (JSONObject) body.get("response");
		JSONObject user = (JSONObject) message.get("user");
		String username= user.get("name").toString();
		System.out.println(username);
		resp.getInfo().add(username);
		
		System.out.println(resp);
		
		Client client = new Client(username, null);
		
		registerUserTumblr(client);
		
		return resp;
	}
	
	public int isTumblrAccount(Client user)
	{
		try {
			PreparedStatement ps = c.prepareStatement(consts.isTumblrAccount);
			ps.setString(1, user.getName());
		
			ResultSet result = ps.executeQuery();
			
			while(result.next()){
				if(result.getString("tumblr")!=null){
					return 1;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return 0;
	}
	
	//tumblr function finish
	
	
	public void run() {
		while (true) {

			try {
				if (clientIF != null) {
					System.out.println("client != null");
					clientIF.checkServer();
					System.out.println("Connected");
				}
			} catch (RemoteException e) {
				try {
					new RMIServer();
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (ClassNotFoundException e1) {
					e1.printStackTrace();
				} catch (SQLException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}

			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public ArrayList<String> getAvailableProjects() throws RemoteException {
		System.out.println("RMI - getAvailableProjects");
		ArrayList<String> projects = new ArrayList<String>();
		projects.add("primeiro");
		projects.add("a tua mae");
		return projects;

	}

	public int getNumber() {
		return this.number;
	}

	public void setNumber(int number) throws RemoteException {
		this.number = number;
	}

	public ArrayList<String> getPrimes(int number) throws RemoteException {
		System.out.println("RMIServer - getPrimes() number: " + number);
		ArrayList<String> primes = new ArrayList<String>();
		int candidate = 2;
		for (int count = 0; count < number; candidate++)
			if (isPrime(candidate)) {
				primes.add((new Integer(candidate)).toString());
				count++;
			}
		primes.add("69");
		System.out.println(primes);
		return primes;
	}

	public boolean isPrime(int number) throws RemoteException {
		System.out.println("RMI - isPrime()");
		if ((number & 1) == 0)
			return number == 2;
		for (int i = 3; number >= i * i; i += 2)
			if ((number % i) == 0)
				return false;
		return true;
	}

	public void initDataBase() throws ClassNotFoundException, SQLException {
		this.consts = new DataBaseConstants();

		System.out.println("-------------------------Creation of all tables: -----------------------------");
		createTable(consts.usersTableCreation);
		createTable(consts.projectsTableCreation);
		createTable(consts.rewardsTableCreation);
		createTable(consts.choicesTableCreation);
		createTable(consts.clientRewardsTableCreation);
		createTable(consts.messagesTableCreation);
		createTable(consts.onlineUsersTableCreation);
		

	}

	public synchronized Response getQuestion(int id) {
		Response temp = new Response();
		try {
			PreparedStatement ps = c.prepareStatement(consts.getQuestion);
			ps.setInt(1, id);
			ResultSet result = ps.executeQuery();
			temp.setInfo(toArrayList(result));
			temp.setSuccess(true);
		} catch (SQLException e) {
			e.printStackTrace();
			temp.setSuccess(false);
		}
		return temp;
	}

	public synchronized Response getClientProjects(Client client) {
		Response temp = new Response();
		try {
			PreparedStatement ps = c.prepareStatement(consts.getClientProjects);
			ps.setString(1, client.getName());
			ResultSet result = ps.executeQuery();

			temp.setInfo(toArrayList(result));
			System.out.println("Temp: " + temp.getInfo().toString());
			temp.setSuccess(true);
		} catch (SQLException e) {
			e.printStackTrace();
			temp.setSuccess(false);
		}
		return temp;
	}

	public boolean projectIDExists(int id) throws SQLException {
		PreparedStatement ps = c.prepareStatement(consts.projectIDExists);
		ps.setInt(1, id);
		ResultSet result = ps.executeQuery();
		while (result.next()) {
			if (result.getInt("id") == id) {
				return true;
			}
		}
		return false;
	}

	public boolean messageIDExists(int id) throws SQLException {
		PreparedStatement ps = c.prepareStatement(consts.messageIDExists);
		ps.setInt(1, id);
		ResultSet result = ps.executeQuery();
		while (result.next()) {
			if (result.getInt("id") == id) {
				return true;
			}
		}
		return false;
	}

	public boolean rewardIDExists(int id) throws SQLException {
		PreparedStatement ps = c.prepareStatement(consts.rewardIDExists);
		ps.setInt(1, id);
		ResultSet result = ps.executeQuery();
		while (result.next()) {
			if (result.getInt("id") == id) {
				return true;
			}
		}
		return false;
	}

	public boolean projectIDExistsAvailable(int id) throws SQLException {
		PreparedStatement ps = c.prepareStatement(consts.projectIDExistsAvailable);
		ps.setInt(1, id);
		ResultSet result = ps.executeQuery();
		while (result.next()) {
			if (result.getInt("id") == id) {
				return true;
			}
		}
		return false;
	}

	public synchronized Response insertNewChoice(Answer answer) {
		Response temp = new Response();
		try {

			for (int i = 0; i < answer.getAnswers().size(); i++) {
				PreparedStatement ps = c.prepareStatement(consts.insertNewChoice);
				ps.setString(1, answer.getQuestion());
				ps.setString(2, answer.getAnswers().get(i));
				ps.execute();
			}

			temp.setSuccess(true);
		} catch (SQLException e) {
			e.printStackTrace();
			temp.setSuccess(false);
		}

		return temp;
	}

	public synchronized Response incrementCount(int id) {
		Response temp = new Response();
		try {

			PreparedStatement ps = c.prepareStatement(consts.incrementCount);
			ps.setInt(1, id);
			ps.execute();
			temp.setSuccess(true);
		} catch (SQLException e) {
			e.printStackTrace();
			temp.setSuccess(false);
		}
		return temp;
	}

	public synchronized Response getMostVoted(int id) {
		Response temp = new Response();
		try {

			PreparedStatement ps = c.prepareStatement(consts.getMostVoted);
			ps.setInt(1, id);
			ResultSet result = ps.executeQuery();

			temp.setInfo(toArrayList(result));

			temp.setSuccess(true);
		} catch (SQLException e) {
			e.printStackTrace();
			temp.setSuccess(false);
		}
		return temp;
	}

	public synchronized Response getChoices(int id) {
		Response temp = new Response();
		try {

			PreparedStatement ps = c.prepareStatement(consts.getChoices);
			ps.setInt(1, id);
			ResultSet result = ps.executeQuery();

			temp.setInfo(toArrayList(result));
			temp.setSuccess(true);
		} catch (SQLException e) {
			e.printStackTrace();
			temp.setSuccess(false);
		}
		return temp;
	}

	public synchronized Response projectDetails(int id) {
		System.out.println("projectDetails Function");
		Response temp = new Response();

		try {
			if (projectIDExists(id)) {
				PreparedStatement ps = c.prepareStatement(consts.getProjectDetails);
				ps.setInt(1, id);
				ResultSet result = ps.executeQuery();

				// project info
				temp.setInfo(toArrayList(result));
				temp.getInfo().add("Rewards");

				// rewards
				ps = c.prepareStatement(consts.getProjectRewards);
				ps.setInt(1, id);
				result = ps.executeQuery();
				temp.getInfo().addAll(toArrayList(result));

				// CHOICES
				ps = c.prepareStatement(consts.getMostVoted);
				ps.setInt(1, id);
				ps.setInt(2, id);
				result = ps.executeQuery();
				temp.getInfo().addAll(toArrayList(result));

				temp.setSuccess(true);
			} else {
				temp.setSuccess(false);
				return temp;
			}

		} catch (SQLException e) {
			e.printStackTrace();
			temp.setSuccess(false);
		}

		return temp;
	}

	public synchronized Response getClientRewards(Client client) {
		System.out.println("getClientRewards function");
		Response temp = new Response();
		temp.setInfo(new ArrayList<>());
		try {
			PreparedStatement ps = null;

			ps = c.prepareStatement(consts.getRewardsAvailable);
			ps.setString(1, client.getName());
			ResultSet result = ps.executeQuery();

			temp.getInfo().add("Available");
			temp.getInfo().addAll(toArrayList(result));
			temp.getInfo().add("Picked");

			ps = c.prepareStatement(consts.getRewardsReceived);
			ps.setString(1, client.getName());
			result = ps.executeQuery();
			temp.getInfo().addAll(toArrayList(result));

			temp.setSuccess(true);

		} catch (SQLException e) {
			e.printStackTrace();
			temp.setSuccess(false);

		}
		return temp;
	}

	public synchronized Response getProjectRewards(int id) {
		System.out.println("getProjectRewards Function");
		Response temp = new Response();
		try {
			if (projectIDExists(id)) {
				PreparedStatement ps = null;
				ps = c.prepareStatement(consts.getProjectRewards);
				ps.setInt(1, id);
				ResultSet result = ps.executeQuery();

				temp.setInfo(toArrayList(result));
				temp.setSuccess(true);
			} else {
				temp.setSuccess(false);
				return temp;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			temp.setSuccess(false);

		}
		return temp;
	}

	public synchronized Response listProjects(int choice) {
		System.out.println("ListProjects Function");
		Response temp = new Response();
		try {
			PreparedStatement ps = null;
			if (choice == 0) {
				ps = c.prepareStatement(consts.getAvailableProjects);
			} else if (choice == 1) {
				ps = c.prepareStatement(consts.getOlderProjects);
			}

			ResultSet result = ps.executeQuery();

			temp.setInfo(toArrayList(result));
			temp.setSuccess(true);

		} catch (SQLException e) {
			e.printStackTrace();
			temp.setSuccess(false);
		}

		return temp;
	}

	public ArrayList<String> toArrayList(ResultSet result) throws SQLException {
		ArrayList<String> temp = new ArrayList<>();
		ResultSetMetaData metadata = result.getMetaData();
		int columns = metadata.getColumnCount();
		while (result.next()) {
			int i = 1;
			while (i <= columns) {
				temp.add(result.getString(i++));
			}
		}
		return temp;
	}

	public synchronized Response sendMessageToProject(Client client, int id, String message) {
		System.out.println("sendMessageToProject function");
		Response temp = new Response();
		try {
			if (projectIDExists(id)) {
				PreparedStatement ps = c.prepareStatement(consts.sendMessage);
				ps.setString(1, client.getName());
				ps.setInt(2, id);
				ps.setInt(3, id);
				ps.setString(4, message);

				c.setAutoCommit(false);
				ps.execute();

				c.commit();
				temp.setSuccess(true);
			} else {
				temp.setSuccess(false);
				return temp;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			temp.setSuccess(false);

		}

		return temp;
	}

	public synchronized Response projectExists(Project project) {
		System.out.println("projectExists function");
		Response temp = new Response();
		try {
			PreparedStatement ps = c.prepareStatement(consts.projectExists);
			ps.setString(1, project.getName());

			c.setAutoCommit(false);
			ResultSet result = ps.executeQuery();
			c.commit();
			while (result.next()) {
				if (result.getString("NAME").equals(project.getName())) {
					temp.setValue(true);
					temp.setSuccess(true);
					return temp;
				}
			}
			temp.setValue(false);
			temp.setSuccess(true);
		} catch (SQLException e) {
			e.printStackTrace();
			temp.setSuccess(false);

		}

		return temp;
	}

	public synchronized Response getProjectMessages(int id) {
		System.out.println("getProjectMessages");
		Response temp = new Response();
		try {
			if (projectIDExists(id)) {
				PreparedStatement ps = c.prepareStatement(consts.getProjectMessages);
				ps.setInt(1, id);
				ps.setInt(2, id);
				ResultSet result = ps.executeQuery();

				temp.setInfo(toArrayList(result));
				temp.setSuccess(true);
			} else {
				temp.setSuccess(false);
				return temp;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			temp.setSuccess(false);
		}

		return temp;
	}

	public synchronized Response showMessages(Client client) {
		System.out.println("showMessages function");
		Response temp = new Response();

		try {
			PreparedStatement ps = c.prepareStatement(consts.getClientMessages);
			ps.setString(1, client.getName());
			ResultSet result = ps.executeQuery();
			temp.setInfo(toArrayList(result));
			temp.setSuccess(true);
		} catch (SQLException e) {
			e.printStackTrace();
			temp.setSuccess(false);
		}
		return temp;
	}


    
	public synchronized Response answerQuestion(Client sender, int answerID, String text) {
		System.out.println("answerQuestion function");
		Response temp = new Response();
		try {
			if (messageIDExists(answerID)) {
				PreparedStatement ps = c.prepareStatement(consts.answerQuestion);
				ps.setString(1, sender.getName());
				ps.setInt(2, answerID);
				ps.setInt(3, answerID);
				ps.setString(4, text);

				c.setAutoCommit(false);
				ps.execute();
				c.commit();
				temp.setSuccess(true);
			} else {
				temp.setSuccess(false);
				return temp;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			temp.setSuccess(false);
		}

		return temp;
	}

	public synchronized Response removeProject(int id) {
		System.out.println("removeProject Function");
		Response temp = new Response();
		// Devolver notations
		try {
			if (projectIDExists(id)) {
				PreparedStatement ps = c.prepareStatement(consts.returnClientMoney);
				ps.setInt(1, id);
				c.setAutoCommit(false);
				ps.execute();

				ps = c.prepareStatement(consts.takeProjectMoney);
				ps.setInt(1, id);
				ps.execute();

				ps = c.prepareStatement(consts.removeClientRewards);
				ps.setInt(1, id);
				ps.execute();

				// remover o projecto
				ps = c.prepareStatement(consts.cancelProject);
				ps.setInt(1, id);
				ps.execute();

				c.commit();
				temp.setSuccess(true);
			} else {
				temp.setSuccess(false);
				return temp;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			temp.setSuccess(false);
		}

		return temp;

	}

	public synchronized Response removeReward(int pid, int rid) {
		System.out.println("removeRewards Function");
		Response temp = new Response();
		try {
			if (rewardIDExists(rid) && projectIDExists(pid)) {

				PreparedStatement ps = c.prepareStatement(consts.takeProjectRewardMoney);
				ps.setInt(1, pid);
				ps.setInt(2, pid);
				ps.setInt(3, rid);

				ps.execute();

				ps = c.prepareStatement(consts.returnRewardMoney);
				ps.setInt(1, pid);
				ps.setInt(2, rid);

				ps.execute();

				ps = c.prepareStatement(consts.removeClientReward);
				ps.setInt(1, pid);
				ps.setInt(2, rid);
				ps.execute();

				ps = c.prepareStatement(consts.removeRewards);
				ps.setInt(1, rid);
				c.setAutoCommit(false);
				ps.execute();

				c.commit();

				temp.setSuccess(true);
				return temp;
			}

			else {
				temp.setSuccess(false);
				return temp;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			temp.setSuccess(false);
		}

		return temp;

	}

	public synchronized Response insertReward(int id, String descr, int value) {
		System.out.println("insertReward Function");
		Response temp = new Response();
		try {
			if (projectIDExists(id)) {
				PreparedStatement ps = c.prepareStatement(consts.addRewards);
				ps.setString(1, descr);
				ps.setInt(2, value);
				ps.setInt(3, id);
				c.setAutoCommit(false);
				ps.execute();
				c.commit();
				ps.close();
				temp.setSuccess(true);
			} else {
				temp.setSuccess(false);
				return temp;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			temp.setSuccess(false);
		}
		return temp;
	}

	public int likeProjectPost(int pid)
	{
		String id = null;
		String reblogKey = null;
		try {
			PreparedStatement ps = c.prepareStatement(consts.getPostInfo);
			ps.setInt(1, pid);

			ResultSet result = ps.executeQuery();
			while(result.next())
			{
				reblogKey = result.getString("REBLOG");
				id = result.getString("POSTID");
			}
			System.out.println("ReblogKey: " + reblogKey + "| postID: "+ id);
			if(reblogKey==null || id==null)
			{
				return 0;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}		
		
		System.out.println("Like Project Post");
		OAuthRequest request = new OAuthRequest(Verb.POST, "https://api.tumblr.com/v2/user/like", service);

		request.addBodyParameter("id" , id);
		request.addBodyParameter("reblog_key" , reblogKey);
		
		
		service.signRequest(accessToken, request);
		
		com.github.scribejava.core.model.Response response = request.send();
		if(response.getCode()==200)
		{
			System.out.println("Project post liked");
		}
		else
		{
			System.out.println("ERROR");
			return 0;
		}
		return 1;
	
	}
	
	
	public synchronized Response incrementProjectMoney(Client client, int idProject, int idReward, int idChoice) {
		System.out.println("incrementProjectMoney function");
		Response temp = new Response();
		
		ResultSet result = null;
		PreparedStatement ps = null;
		
		try {
			if (projectIDExistsAvailable(idProject)) {
				// getValue
				ps = c.prepareStatement("SELECT VALUE FROM REWARDS WHERE ID=?;");
				ps.setInt(1, idReward);
				result = ps.executeQuery();
				int value = 0;
				while (result.next()) {
					value = result.getInt("value");
				}
				// getDescr
				ps = c.prepareStatement("SELECT DESCR FROM REWARDS WHERE ID=?;");
				ps.setInt(1, idReward);
				result = ps.executeQuery();
				String descr = "";
				while (result.next()) {
					descr = result.getString("descr");
				}
				// increment project money
				ps = c.prepareStatement(consts.incrementProjectMoney);
				ps.setInt(1, value);
				ps.setInt(2, idProject);

				c.setAutoCommit(false);
				ps.execute();

				// tirar dinheiro ao client (not done yet)
				ps = c.prepareStatement(consts.takeClientMoney);
				ps.setInt(1, value);
				ps.setString(2, client.getName());

				ps.execute();

				// = "INSERT INTO DONATIONS (NAME, VALUE, PROJECTID) VALUES
				// (?,?,?);";
				ps = c.prepareStatement(consts.addDonation);
				ps.setString(1, client.getName());
				ps.setString(2, descr);
				ps.setInt(3, value);
				ps.setInt(4, idProject);

				ps.execute();
				
				//increment choice count
                ps = c.prepareStatement(consts.incrementCount);
                ps.setInt(1, idChoice);
                ps.execute();
				
				c.commit();

				//like projectPost
				likeProjectPost(idProject);
				

				temp.setSuccess(true);
			} else {
				temp.setSuccess(false);
				return temp;
			}

		} catch (SQLException e) {
			e.printStackTrace();
			temp.setSuccess(false);
		}

		return temp;

	}

	public synchronized Response getMoneyAvailable(Client client) {
		System.out.println("getMoneyAvailable");
		System.out.println("Username: " + isTumblrAccount(client));
		Response temp = new Response();
		try {
			PreparedStatement ps = c.prepareStatement(consts.getUserMoney);
			ps.setString(1, client.getName());

			Statement stmt = c.createStatement();

			ResultSet result = ps.executeQuery();

			result.next();
			temp.setMoney(result.getInt("money"));
			stmt.close();
			temp.setSuccess(true);

		} catch (SQLException e) {
			e.printStackTrace();
			temp.setSuccess(false);
		}

		return temp;

	}

	public Response insertNewProject(Project project) {		
		System.out.println("insertNewProject");
		Response temp = new Response();

		if (!projectExists(project).isValue()) {
			try {

				// DATE
				PreparedStatement ps = c.prepareStatement(consts.insertProject);
				ps.setString(1, project.getName());
				ps.setString(2, project.getAdmin().getName());
				ps.setString(3, project.getDescription());
				ps.setInt(4, project.getMainGoal());
				Calendar cal = Calendar.getInstance();
				cal.set(project.getDeadline().getYear(), project.getDeadline().getMonth(),
						project.getDeadline().getDay());
				ps.setDate(5, new Date(cal.getTimeInMillis()));
				c.setAutoCommit(false);

				ps.execute();
				System.out.println("1");
				
				ps = c.prepareStatement("SELECT ID FROM PROJECTS WHERE PROJECTS.NAME=?;");
				ps.setString(1, project.getName());
				ResultSet result = ps.executeQuery();
				
				System.out.println("2");
				
				int projectID = 1;
				while (result.next()) {
					projectID = result.getInt("id");
				}

				System.out.println("3");
				for (int i = 0; i < project.getRewards().size(); i++) {
					ps = c.prepareStatement(consts.inProjectRewards);
					ps.setString(1, project.getRewards().get(i).getDescr());
					ps.setInt(2, project.getRewards().get(i).getValue());
					ps.setInt(3, projectID);

					ps.execute();
					
				}
				System.out.println("4");
				
				for (int i = 0; i < project.getChoices().getAnswers().size(); i++) {
					System.out.println("CHOICES: " + project.getChoices().getQuestion()+" "+project.getChoices().getAnswers().get(i));
				}
				
				for (int i = 0; i < project.getChoices().getAnswers().size(); i++) {
					ps = c.prepareStatement(consts.insertNewChoice);
					ps.setString(1, project.getChoices().getQuestion());
					ps.setString(2, project.getChoices().getAnswers().get(i));
					ps.setInt(3, projectID);
					ps.execute();
				}
				System.out.println("5");
				if(isTumblrAccount(project.getAdmin())==1)
				{
					System.out.println("is Tumblr Account");
					if(postOnTumblr(project.getName(), project.getDescription(), project.getAdmin().getName())==1){
						System.out.println("postOnTumblr==1");
						temp.setSuccess(true);
					}
					else{
						temp.setSuccess(false);
					}
				}
				else
				{
					System.out.println("is not TumblrAccount");
				}
				System.out.println("6");
				
				c.commit();

				temp.setSuccess(true);

			} catch (SQLException e) {
				e.printStackTrace();
				temp.setSuccess(false);
			}

		} else {
			temp.setSuccess(false);
		}
		return temp;
	}
	
	private int postOnTumblr(String projectName, String description, String username){
		System.out.println("PostOnTumblr function");
		String blogName = getPrimaryBlogURL();
		
		OAuthRequest request = new OAuthRequest(Verb.POST, "https://api.tumblr.com/v2/blog/"+blogName+"/post", service);

		request.addBodyParameter("type" , "text");
		request.addBodyParameter("body" , description);
		request.addBodyParameter("title" , projectName);
		
		
		service.signRequest(accessToken, request);
		
		com.github.scribejava.core.model.Response response = request.send();
		
		if(response.getCode()==201)
		{
			JSONObject idJSON = (JSONObject) JSONValue.parse(response.getBody());
			JSONObject message = (JSONObject) idJSON.get("response");
			
			System.out.println("Created post [id-"+message.get("id").toString()+"][reblog_key-"+getReblogKey(message.get("id").toString(), blogName)+"]");
			insertPostDatabase(message.get("id").toString(), getReblogKey(message.get("id").toString(), blogName), projectName);
			return 1;
		}
		else
		{
			System.out.println("Error");
			return 0;
		}

	}
	
	private int insertPostDatabase(String id, String reblog, String projectName){
		
		System.out.println("insertPostDatabase function");
		
		try {
			PreparedStatement ps = c.prepareStatement(consts.insertPost);
			ps.setString(1, id);
			ps.setString(2,  reblog);
			ps.setString(3, projectName);
			

			ps.execute();
			return 1;
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}

	}
	
	private String getPrimaryBlogURL()
	{
		JSONObject obj= (JSONObject) JSONValue.parse(getUsernameInfo().getBody());
		JSONObject resp= (JSONObject) obj.get("response");
		JSONObject user = (JSONObject) resp.get("user");
		JSONArray blogs = (JSONArray) user.get("blogs");
		for(int i = 0;i<blogs.size();i++)
		{
			if ((boolean)((JSONObject)blogs.get(i)).get("primary"))
			{
				System.out.println("primary blog url: "+ ((JSONObject) blogs.get(i)).get("name").toString()+".tumblr.com");
				return ((JSONObject) blogs.get(i)).get("name").toString()+".tumblr.com";
			}
		}
		return null;
	}
	public com.github.scribejava.core.model.Response getUsernameInfo()
	{
		System.out.println("Get Username Info");
		

	    OAuthRequest request = new OAuthRequest(Verb.GET, "https://api.tumblr.com/v2/user/info", service);
		service.signRequest(accessToken, request);
		
		com.github.scribejava.core.model.Response response = request.send();
		return response;
		
	}
	
	public String getReblogKey(String id, String blogURL)
	{
		OAuthRequest request = new OAuthRequest(Verb.POST, "https://api.tumblr.com/v2/blog/"+blogURL+"/posts", service);

		request.addBodyParameter("id" ,id);
		
		service.signRequest(accessToken, request);
		
		
		com.github.scribejava.core.model.Response response = request.send();
		if(response.getCode()>=200)
		{
			JSONObject obj= (JSONObject) JSONValue.parse(response.getBody());
			
			JSONObject resp= (JSONObject)obj.get("response");
			JSONArray posts = (JSONArray)resp.get("posts");
			String reblog_key = ((JSONObject)posts.get(0)).get("reblog_key").toString();
			return reblog_key;
			
		}
		else
		{
			System.out.println("Error");
			return null;
		}

	}

	
	public Response checkIfExists(Client client) {
		System.out.println("checkIfExists function");
		Response temp = new Response();
		try {
			PreparedStatement ps = c.prepareStatement(consts.checkIfExists);
			ps.setString(1, client.getName());

			ResultSet result = ps.executeQuery();

			while (result.next()) {
				if (result.getString("name").equals(client.getName())) {
					temp.setValue(true);
					temp.setSuccess(true);
					return temp;
				}
			}
			temp.setValue(false);
			temp.setSuccess(true);
		} catch (SQLException e) {
			e.printStackTrace();
			temp.setSuccess(false);
		}

		return temp;
	}

	public Response checkUser(Client client) {
		System.out.println("checkUser Function");
		Response temp = new Response();
		System.out.println(client.getName() + " " + client.getPassword());

		try {
			PreparedStatement ps = c.prepareStatement(consts.checkLogin);

			ps.setString(1, client.getName());
			ps.setString(2, client.getPassword());

			c.setAutoCommit(false);
			ResultSet result = ps.executeQuery();
			c.commit();

			while (result.next()) {
				if (result.getString("name").equals(client.getName())) {
					System.out.println("Cliente authenticado");
					temp.setValue(true);
					temp.setSuccess(true);
					return temp;
				}

			}
			temp.setValue(false);
			temp.setSuccess(true);
		} catch (SQLException e) {
			e.printStackTrace();
			temp.setSuccess(false);
		}
		return temp;
	}

	public void createTable(String command) throws SQLException {
		c.setAutoCommit(false);
		Statement stmt = c.createStatement();

		stmt.executeUpdate(command);
		stmt.close();
		c.commit();
	}

	public Response registerUser(Client client) {
		System.out.println("registerUser Function");
		Response temp = new Response();
		try {
			if (checkIfExists(client).isValue() == false) {

				PreparedStatement ps = c.prepareStatement(consts.registerNewAccount);
				ps.setString(1, client.getName());
				ps.setString(2, client.getPassword());

				c.setAutoCommit(false);
				Statement stmt = c.createStatement();

				ps.execute();
				c.commit();

				stmt.close();
				temp.setSuccess(true);
			} else {
				temp.setSuccess(false);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			temp.setSuccess(false);
		}
		return temp;
	}

	@Override
	public void checkServer() throws RemoteException {
		// TODO Auto-generated method stub

	}

	

}
