package sd.action;

import com.opensymphony.xwork2.ActionSupport;
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
	
	

    public CallbackAction() {}

    @Override
    public String execute() {
        System.out.println("user logado com tumblr");
        System.out.println("OAuthToken: " + getOauth_token());
        System.out.println("Verifier: " + getOauth_verifier());
        
        
        return SUCCESS;
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