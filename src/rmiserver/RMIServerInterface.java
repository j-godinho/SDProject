
package rmiserver;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

import com.github.scribejava.core.oauth.OAuthService;

import common.Answer;
import common.Client;
import common.Project;
import common.Response;

public interface RMIServerInterface extends Remote {
	public ArrayList<String> getAvailableProjects() throws RemoteException;

	public boolean isPrime(int number) throws RemoteException;

	public ArrayList<String> getPrimes(int number) throws RemoteException;

	public void setNumber(int number) throws RemoteException;

	public int getNumber() throws RemoteException;

	void checkServer() throws RemoteException;

	Response getClientProjects(Client client) throws RemoteException;

	Response getClientRewards(Client client) throws RemoteException;

	Response getProjectRewards(int id) throws RemoteException;

	Response projectDetails(int id) throws RemoteException;

	Response listProjects(int choice) throws RemoteException;

	Response sendMessageToProject(Client client, int id, String message) throws RemoteException;

	Response projectExists(Project project) throws RemoteException;

	Response getProjectMessages(int id) throws RemoteException;

	Response showMessages(Client client) throws RemoteException;

	Response answerQuestion(Client sender, int answerID, String text) throws RemoteException;

	Response removeProject(int id) throws RemoteException;

	Response insertReward(int id, String descr, int value) throws RemoteException;

	Response removeReward(int pid, int rid) throws RemoteException;

	Response incrementProjectMoney(Client client, int idProject, int idReward) throws RemoteException;

	Response getMoneyAvailable(Client client) throws RemoteException;

	Response insertNewProject(Project project) throws RemoteException;

	Response checkIfExists(Client client) throws RemoteException;

	Response checkUser(Client client) throws RemoteException;

	Response registerUser(Client client) throws RemoteException;

	// choices
	Response getChoices(int id) throws RemoteException;

	Response getMostVoted(int id) throws RemoteException;

	Response incrementCount(int id) throws RemoteException;

	Response insertNewChoice(Answer answer) throws RemoteException;

	Response getQuestion(int id) throws RemoteException;
	
	//tumblr
	Response registerUserTumblr(Client client) throws RemoteException;
	String getAuthorizationURL() throws RemoteException;
	Response loginTumblr(String authVerifier) throws RemoteException;
	
	//hugo
	Response getAdministrator(int projectID) throws RemoteException;
	Response getDonation(int rewardID) throws RemoteException;
	Response getProjectName(int projectID) throws RemoteException;
	
	Response updateOnlineUsers(int choice, String username) throws RemoteException;
	
}
