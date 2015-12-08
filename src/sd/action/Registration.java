package sd.action;

import com.opensymphony.xwork2.ActionSupport;

import sd.model.RegistrationBean;

import java.rmi.RemoteException;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

public class Registration extends ActionSupport implements SessionAware{
	private static final long serialVersionUID = 1175671191841904797L;
	private Map<String, Object> session;
	private RegistrationBean registrationBean;
	
	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}
	
	
	@Override
	public String execute() throws Exception {
		
		if(!getRegistrationBean().getUsername().equals("")&& !getRegistrationBean().getPassword1().equals(""))
		{
			try{
				if(getRegistrationBean().register()==0)
				{
					session.put("username", getRegistrationBean().getUsername());
					System.out.println("Registered user");
				}
				else
				{
					//tratar do erro
					System.out.println("Error on registering user");
				}
				
			}catch(RemoteException e){
				e.printStackTrace();
			}
		}
		return SUCCESS;
	}


	public RegistrationBean getRegistrationBean() {
		return registrationBean;
	}


	public void setRegistrationBean(RegistrationBean registrationBean) {
		this.registrationBean = registrationBean;
	}


	public Map<String, Object> getSession() {
		return session;
	}

	
	
}
