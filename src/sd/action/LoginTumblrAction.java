package sd.action;


import com.opensymphony.xwork2.ActionSupport;

import sd.model.UserBean;

import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

public class LoginTumblrAction extends ActionSupport implements SessionAware {
	private static final long serialVersionUID = 1175671191841904797L;
	private Map<String, Object> session;
	
	private static final String REDIRECT = "redirect";
    
    String authorizationURL;

    public LoginTumblrAction(){}
    
	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

	@Override
	public String execute(){

		System.out.println("HERE");
		UserBean userBean = new UserBean();
		setAuthorizationURL(userBean.getAuthorizationURL());
		
		return REDIRECT;
	}

	public String getAuthorizationURL() {
		return authorizationURL;
	}

	public void setAuthorizationURL(String authorizationURL) {
		this.authorizationURL = authorizationURL;
	}

	public Map<String, Object> getSession() {
		return session;
	}





}
