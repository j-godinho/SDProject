package sd.action;

import com.github.scribejava.apis.TumblrApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.Token;
import com.github.scribejava.core.oauth.OAuthService;
import com.opensymphony.xwork2.ActionSupport;

import sd.model.AuthenticationBean;
import sd.model.LoginTumblrBean;

import java.rmi.RemoteException;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

public class LoginTumblrAction extends ActionSupport implements SessionAware {
	private static final long serialVersionUID = 1175671191841904797L;
	private Map<String, Object> session;
	
	private static final String REDIRECT = "redirect";
    
	String API_APP_KEY = "kgyF9Ws3EPUQQSdZl2BQ4SzFUXSKFfP06MQNZPJulFw87qTqOA";
    String API_APP_SECRET = "iSVrbibjtgGO1qyeRVFL2pjeYJQBFLEe5I0nfYTxG4kosr9H9u";
    
    
    OAuthService service;
    String authorizationURL;

    public LoginTumblrAction(){}
    
	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

	@Override
	public String execute() throws Exception {

        service = new ServiceBuilder()
                .provider(TumblrApi.class)
                .apiKey(API_APP_KEY)
                .apiSecret(API_APP_SECRET)
                .callback("http://localhost:8080/SDProjectGit/callback")
                .build();

        Token requestToken = service.getRequestToken();
        authorizationURL = service.getAuthorizationUrl(requestToken);
        
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
