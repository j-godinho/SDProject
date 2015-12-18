package sd.action;

import com.github.scribejava.core.model.Token;
import com.opensymphony.xwork2.ActionSupport;

import common.Response;
import sd.model.UserBean;

import org.apache.struts2.interceptor.SessionAware;

import java.util.Map;


public class CallbackAction extends ActionSupport implements SessionAware {
    /**
	 * 
	 */
	private static final long serialVersionUID = 258169472079318273L;
	private Map<String, Object> session;
	private String oauth_token;
	private String oauth_verifier;
	private UserBean userBean;
	

    public CallbackAction() {}

    @Override
    public String execute() {
    	
        System.out.println("user logado com tumblr");
        Response resp;
        if(getOauth_token()==null || getOauth_verifier()==null)
        {
        	System.out.println("HERE1");
        	return ERROR;
        }
        else
        {
        	System.out.println("HERE2");
        	resp = getUserBean().loginTumblr(getOauth_token(), getOauth_verifier());
        }
        
        session.put("accessToken", resp.getAccessToken());
        session.put("username", resp.getInfo().get(0));
        
        return SUCCESS;
    }
    

    public UserBean getUserBean() {
		return userBean;
	}

	public void setUserBean(UserBean userBean) {
		this.userBean = userBean;
	}

	@Override
    public void setSession(Map<String, Object> session) {
        this.session = session;
    }

	public Map<String, Object> getSession() {
		return session;
	}

	public String getOauth_token() {
		return oauth_token;
	}

	public void setOauth_token(String oauth_token) {
		this.oauth_token = oauth_token;
	}

	public String getOauth_verifier() {
		return oauth_verifier;
	}

	public void setOauth_verifier(String oauth_verifier) {
		this.oauth_verifier = oauth_verifier;
	}
    
	
}