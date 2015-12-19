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
	

    public CallbackAction() {
    	userBean = new UserBean();
    }

    @Override
    public String execute() {
    	
        System.out.println("user logado com tumblr");
        Response resp;
        if(getOauth_token()=="" || getOauth_verifier()=="")
        {
        	System.out.println("HERE1");
        	return ERROR;
        }
        else
        {
        	System.out.println("HERE2");
        	resp = getUserBean().loginTumblr(getOauth_verifier());
        }
        if(session.get("accessToken")!=null){
        	session.replace("accessToken", resp.getAccessToken());
        }
        if(session.get("username")!=null)
        {
        	session.replace("username", resp.getInfo().get(0));
        }
        
        return SUCCESS;
    }

	public Map<String, Object> getSession() {
		return session;
	}

	public void setSession(Map<String, Object> session) {
		this.session = session;
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

	public UserBean getUserBean() {
		return userBean;
	}

	public void setUserBean(UserBean userBean) {
		this.userBean = userBean;
	}
    

	
}