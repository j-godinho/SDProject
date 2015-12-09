package sd.action;

import com.opensymphony.xwork2.ActionSupport;

import common.Client;
import common.Response;
import rmiserver.RMIServerInterface;
import sd.model.AuthenticationBean;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

public class AccountMoneyAction extends ActionSupport implements SessionAware{
	private static final long serialVersionUID = 1175671191841904797L;
	private Map<String, Object> session;
	private RMIServerInterface server;
	
	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}
	
	
	 public String execute() throws RemoteException{
		
		
		Response resp = new Response();
		
		Client client = new Client((String)session.get("username"), (String)session.get("password"));
		try {
			server = (RMIServerInterface) LocateRegistry.getRegistry("localhost", 7000).lookup("RMIServer");
			try {
				resp = server.getMoneyAvailable(client);
				if(resp.isSuccess()){
					session.put("clientMoney", resp.getMoney());
					return SUCCESS;
				}
				
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		catch(NotBoundException|RemoteException e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
		
	
	

}
