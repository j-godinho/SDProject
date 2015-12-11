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

import common.Answer;
import common.Client;
import common.ClientInterface;
import common.Configs;
import common.Project;
import common.Response;

public class RMIServer extends UnicastRemoteObject implements RMIServerInterface, Runnable{
	private int number;
	public ClientInterface clientIF;
	private Properties properties = new Properties();
	private DataBaseConstants consts;
    public Configs configs = new Configs();

    Connection c = null;
    Calendar calendar = Calendar.getInstance();
    Timer time = new Timer();

	RMIServer() throws RemoteException, IOException, ClassNotFoundException, SQLException
	{
		super();

        Class.forName("org.postgresql.Driver");
        c = DriverManager.getConnection("jdbc:postgresql://"+configs.getDb_address()+":"+configs.getDb_port()+"/"+configs.getData_base(), configs.getAdmin(), configs.getPass());

    	/*Registry registry = LocateRegistry.createRegistry(configs.getRmi_port());
		registry.rebind("RMIServer", (RMIServerInterface)new RMIServer());
		System.out.println("Rmi Server Running...");*/
        

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
	
	public ArrayList<String> getAvailableProjects() throws RemoteException{
		System.out.println("RMI - getAvailableProjects");
		ArrayList<String> projects = new ArrayList<String>();
		projects.add("primeiro");
		projects.add("a tua mae");
		return projects;
		
	}
	public int getNumber() {
		return this.number;
	}

	public void setNumber(int number) throws RemoteException{
		this.number = number;
	}
	public ArrayList<String> getPrimes(int number) throws RemoteException{
		System.out.println("RMIServer - getPrimes() number: " +number);
		ArrayList<String> primes = new ArrayList<String>();
		int candidate = 2;
		for(int count = 0; count < number; candidate++)
			if(isPrime(candidate)) {
				primes.add((new Integer(candidate)).toString());
				count++;
			}
		primes.add("69");
		System.out.println(primes);
		return primes;
	}
	
	public boolean isPrime(int number) throws RemoteException{
		System.out.println("RMI - isPrime()");
		if((number & 1) == 0)
			return number == 2;
		for(int i = 3; number >= i*i; i += 2)
			if((number % i) == 0)
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


    }

    public synchronized  Response getQuestion(int id)
    {
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



    public synchronized Response getClientProjects(Client client)
    {
        Response temp = new Response();
        try{
            PreparedStatement ps = c.prepareStatement(consts.getClientProjects);
            ps.setString(1, client.getName());
            ResultSet result = ps.executeQuery();

            temp.setInfo(toArrayList(result));
            temp.setSuccess(true);
        }catch(SQLException e)
        {
            e.printStackTrace();
            temp.setSuccess(false);
        }
        return temp;
    }

    public boolean projectIDExists(int id) throws SQLException {
        PreparedStatement ps = c.prepareStatement(consts.projectIDExists);
        ps.setInt(1, id);
        ResultSet result = ps.executeQuery();
        while(result.next())
        {
            if(result.getInt("id")==id)
            {
                return true;
            }
        }
        return false;
    }

    public boolean messageIDExists(int id) throws SQLException {
        PreparedStatement ps = c.prepareStatement(consts.messageIDExists);
        ps.setInt(1, id);
        ResultSet result = ps.executeQuery();
        while(result.next())
        {
            if(result.getInt("id")==id)
            {
                return true;
            }
        }
        return false;
    }

    public boolean rewardIDExists(int id) throws SQLException {
        PreparedStatement ps = c.prepareStatement(consts.rewardIDExists);
        ps.setInt(1, id);
        ResultSet result = ps.executeQuery();
        while(result.next())
        {
            if(result.getInt("id")==id)
            {
                return true;
            }
        }
        return false;
    }

    public boolean projectIDExistsAvailable(int id) throws SQLException {
        PreparedStatement ps = c.prepareStatement(consts.projectIDExistsAvailable);
        ps.setInt(1, id);
        ResultSet result = ps.executeQuery();
        while(result.next())
        {
            if(result.getInt("id")==id)
            {
                return true;
            }
        }
        return false;
    }



    public synchronized Response insertNewChoice(Answer answer)
    {
        Response temp = new Response();
        try{

            for(int i = 0;i<answer.getAnswers().size();i++)
            {
                PreparedStatement ps = c.prepareStatement(consts.insertNewChoice);
                ps.setString(1, answer.getQuestion());
                ps.setString(2, answer.getAnswers().get(i));
                ps.execute();
            }

            temp.setSuccess(true);
        }catch(SQLException e)
        {
            e.printStackTrace();
            temp.setSuccess(false);
        }

        return temp;
    }

    public synchronized Response incrementCount(int id)
    {
        Response temp = new Response();
        try{

            PreparedStatement ps = c.prepareStatement(consts.incrementCount);
            ps.setInt(1, id);
            ps.execute();
            temp.setSuccess(true);
        }catch(SQLException e)
        {
            e.printStackTrace();
            temp.setSuccess(false);
        }
        return temp;
    }

    public synchronized Response getMostVoted(int id)
    {
        Response temp = new Response();
        try{

            PreparedStatement ps = c.prepareStatement(consts.getMostVoted);
            ps.setInt(1, id);
            ResultSet result = ps.executeQuery();

                temp.setInfo(toArrayList(result));

            temp.setSuccess(true);
        }catch(SQLException e)
        {
            e.printStackTrace();
            temp.setSuccess(false);
        }
        return temp;
    }
    public synchronized Response getChoices(int id)
    {
        Response temp = new Response();
        try{

            PreparedStatement ps = c.prepareStatement(consts.getChoices);
            ps.setInt(1, id);
            ResultSet result = ps.executeQuery();

            temp.setInfo(toArrayList(result));
            temp.setSuccess(true);
        }catch(SQLException e)
        {
            e.printStackTrace();
            temp.setSuccess(false);
        }
        return temp;
    }


    public synchronized Response projectDetails(int id) {
        System.out.println("projectDetails Function");
        Response temp = new Response();


        try {
            if(projectIDExists(id))
            {
                PreparedStatement ps = c.prepareStatement(consts.getProjectDetails);
                ps.setInt(1, id);
                ResultSet result = ps.executeQuery();


                //project info
                temp.setInfo(toArrayList(result));
                temp.getInfo().add("Rewards");

                //rewards
                ps = c.prepareStatement(consts.getProjectRewards);
                ps.setInt(1, id);
                result = ps.executeQuery();
                temp.getInfo().addAll(toArrayList(result));

                //CHOICES
                ps=c.prepareStatement(consts.getMostVoted);
                ps.setInt(1,id);
                ps.setInt(2,id);
                result=ps.executeQuery();
                temp.getInfo().addAll(toArrayList(result));



                temp.setSuccess(true);
            }
            else
            {
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
            if(projectIDExists(id)) {
                PreparedStatement ps = null;
                ps = c.prepareStatement(consts.getProjectRewards);
                ps.setInt(1, id);
                ResultSet result = ps.executeQuery();


                temp.setInfo(toArrayList(result));
                temp.setSuccess(true);
            }
            else{
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
            if(projectIDExists(id)) {
                PreparedStatement ps = c.prepareStatement(consts.sendMessage);
                ps.setString(1, client.getName());
                ps.setInt(2, id);
                ps.setInt(3, id);
                ps.setString(4, message);

                c.setAutoCommit(false);
                ps.execute();

                c.commit();
                temp.setSuccess(true);
            }
            else{
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
            if(projectIDExists(id)) {
                PreparedStatement ps = c.prepareStatement(consts.getProjectMessages);
                ps.setInt(1, id);
                ps.setInt(2, id);
                ResultSet result = ps.executeQuery();

                temp.setInfo(toArrayList(result));
                temp.setSuccess(true);
            }
            else
            {
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
            if(messageIDExists(answerID)) {
                PreparedStatement ps = c.prepareStatement(consts.answerQuestion);
                ps.setString(1, sender.getName());
                ps.setInt(2, answerID);
                ps.setInt(3, answerID);
                ps.setString(4, text);

                c.setAutoCommit(false);
                ps.execute();
                c.commit();
                temp.setSuccess(true);
            }
            else{
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
        //Devolver notations
        try {
            if(projectIDExists(id)) {
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

                //remover o projecto
                ps = c.prepareStatement(consts.cancelProject);
                ps.setInt(1, id);
                ps.execute();


                c.commit();
                temp.setSuccess(true);
            }
            else{
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
            if(rewardIDExists(rid) && projectIDExists(pid)) {

                PreparedStatement ps=c.prepareStatement(consts.takeProjectRewardMoney);
                ps.setInt(1, pid);
                ps.setInt(2, pid);
                ps.setInt(3, rid);

                ps.execute();

                ps=c.prepareStatement(consts.returnRewardMoney);
                ps.setInt(1, pid);
                ps.setInt(2, rid);

                ps.execute();

                ps=c.prepareStatement(consts.removeClientReward);
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


            else{
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
            if(projectIDExists(id)) {
                PreparedStatement ps = c.prepareStatement(consts.addRewards);
                ps.setString(1, descr);
                ps.setInt(2, value);
                ps.setInt(3, id);
                c.setAutoCommit(false);
                ps.execute();
                c.commit();
                ps.close();
                temp.setSuccess(true);
            }
            else{
                temp.setSuccess(false);
                return temp;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            temp.setSuccess(false);
        }
        return temp;
    }


    public synchronized Response incrementProjectMoney(Client client, int idProject, int idReward) {
        System.out.println("incrementProjectMoney function");
        Response temp = new Response();

        Statement stmt = null;
        ResultSet result = null;
        PreparedStatement ps = null;
        try {
            if(projectIDExistsAvailable(idProject)) {
                //getValue
                ps = c.prepareStatement("SELECT VALUE FROM REWARDS WHERE ID=?;");
                ps.setInt(1, idReward);
                result = ps.executeQuery();
                int value = 0;
                while (result.next()) {
                    value = result.getInt("value");
                }
                //getDescr
                ps = c.prepareStatement("SELECT DESCR FROM REWARDS WHERE ID=?;");
                ps.setInt(1, idReward);
                result = ps.executeQuery();
                String descr = "";
                while (result.next()) {
                    descr = result.getString("descr");
                }
                //increment project money
                ps = c.prepareStatement(consts.incrementProjectMoney);
                ps.setInt(1, value);
                ps.setInt(2, idProject);

                c.setAutoCommit(false);
                ps.execute();


                //tirar dinheiro ao client (not done yet)
                ps = c.prepareStatement(consts.takeClientMoney);
                ps.setInt(1, value);
                ps.setString(2, client.getName());

                ps.execute();


                // = "INSERT INTO DONATIONS (NAME, VALUE, PROJECTID) VALUES (?,?,?);";
                ps = c.prepareStatement(consts.addDonation);
                ps.setString(1, client.getName());
                ps.setString(2, descr);
                ps.setInt(3, value);
                ps.setInt(4, idProject);

                ps.execute();
                c.commit();


                ps.close();

                temp.setSuccess(true);
            }
            else
            {
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
        System.out.println("Name: "+project.getName()+" Desc: "+project.getDescription());
        Response temp = new Response();


        if (!projectExists(project).isValue()) {
            try {

                //DATE
                PreparedStatement ps = c.prepareStatement(consts.insertProject);
                ps.setString(1, project.getName());
                ps.setString(2, project.getAdmin().getName());
                ps.setString(3, project.getDescription());
                ps.setInt(4, project.getMainGoal());
                Calendar cal = Calendar.getInstance();
                cal.set(2015, 12, 20);
                //cal.set(project.getDeadline().getYear(), project.getDeadline().getMonth(), project.getDeadline().getDay());
                ps.setDate(5, new Date(cal.getTimeInMillis()));
                c.setAutoCommit(false);

                ps.execute();


                ps = c.prepareStatement("SELECT ID FROM PROJECTS WHERE PROJECTS.NAME=?;");
                ps.setString(1, project.getName());
                ResultSet result = ps.executeQuery();

                int projectID = 1;
                while (result.next()) {
                    projectID = result.getInt("id");
                }



                for(int i = 0;i<project.getRewards().size();i++)
                {
                    ps=c.prepareStatement(consts.inProjectRewards);
                    ps.setString(1, project.getRewards().get(i).getDescr());
                    ps.setInt(2, project.getRewards().get(i).getValue());
                    ps.setInt(3, projectID);

                    ps.execute();

                }

                for(int i = 0;i<project.getChoices().getAnswers().size();i++)
                {
                    ps = c.prepareStatement(consts.insertNewChoice);
                    ps.setString(1, project.getChoices().getQuestion());
                    ps.setString(2, project.getChoices().getAnswers().get(i));
                    ps.setInt(3, projectID);
                    ps.execute();
                }

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
        System.out.println(client.getName()+" "+client.getPassword());
        
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
