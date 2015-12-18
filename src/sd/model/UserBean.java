package sd.model;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.ArrayList;

import com.github.scribejava.apis.TumblrApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.Token;
import com.github.scribejava.core.oauth.OAuthService;

import rmiserver.RMIServerInterface;

public class UserBean {
	String API_APP_KEY;
	String API_APP_SECRET;
	
	public UserBean(){
		getAppKeys();
	}
	

	
	
	public int getAppKeys(){
		RMIServerInterface server;
		//hostname = configs.getServer1();
        //hostname2 = configs.getServer2();
        //registryNumber= configs.getRmi_port();
		try {
			//registry = LocateRegistry.getRegistry(configs.getRmi_port());
			//server = (RMIServerInterface) Naming.lookup("RMIServer");
			server = (RMIServerInterface) LocateRegistry.getRegistry("localhost", 7000).lookup("RMIServer");
			try {
				ArrayList <String> resp = server.getAPPKeys().getInfo();
				setAPI_APP_KEY(resp.get(0));
				setAPI_APP_SECRET(resp.get(1));
				
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		catch(NotBoundException|RemoteException e) {
			e.printStackTrace(); // what happens *after* we reach this line?
		}
		return 0;
	}
	
	
	public String getAuthorizationURL() {
		OAuthService service = new ServiceBuilder()
                .provider(TumblrApi.class)
                .apiKey(API_APP_KEY)
                .apiSecret(API_APP_SECRET)
                .callback("http://localhost:8080/SDProjectGit/callback")
                .build();
		

        Token requestToken = service.getRequestToken();
        return service.getAuthorizationUrl(requestToken);
	}


	public String getAPI_APP_KEY() {
		return API_APP_KEY;
	}


	public void setAPI_APP_KEY(String aPI_APP_KEY) {
		API_APP_KEY = aPI_APP_KEY;
	}


	public String getAPI_APP_SECRET() {
		return API_APP_SECRET;
	}


	public void setAPI_APP_SECRET(String aPI_APP_SECRET) {
		API_APP_SECRET = aPI_APP_SECRET;
	}


	
	
}
