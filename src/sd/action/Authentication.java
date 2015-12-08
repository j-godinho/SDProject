package sd.action;

import com.opensymphony.xwork2.ActionSupport;

import sd.model.AuthenticationBean;

import java.rmi.RemoteException;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

public class Authentication extends ActionSupport implements SessionAware{
	private static final long serialVersionUID = 1175671191841904797L;
	private Map<String, Object> session;
	
	
	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}
	
	
	@Override
	public String execute() throws Exception {
		
		/*
		String loggedUser = null;
		
		if(session.containsKey("username")){
			loggedUser = (String)session.get("username");
		}
		
		if(loggedUser != null)
		{
			return SUCCESS;
		}
		*/
		
		if(!getAuthenticationBean().getUsername().equals("")&& !getAuthenticationBean().getPassword().equals(""))
		{
			try{
				if(getAuthenticationBean().authenticate()==0)
				{
					session.put("username", getAuthenticationBean().getUsername());
					System.out.println("User logged in");
				}
				else
				{
					session.put("AuthenticationError", "Invalid Username or Password");
					System.out.println("Wrong credentials");
					
				}
				
			}catch(RemoteException e){
				e.printStackTrace();
			}
		}
		return SUCCESS;
	}
	
	public AuthenticationBean getAuthenticationBean() throws RemoteException{
		if(!session.containsKey("authenticationBean"))
			this.setAuthenticationBean(new AuthenticationBean());
		return (AuthenticationBean) session.get("authenticationBean");
	}
	
	public void setAuthenticationBean(AuthenticationBean authenticationBean){
		this.session.put("authenticationBean", authenticationBean);
	}
	
	
	

}
