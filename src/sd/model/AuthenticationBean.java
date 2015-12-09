package sd.model;

import java.io.UnsupportedEncodingException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import common.Client;
import common.Response;
import rmiserver.RMIServerInterface;

public class AuthenticationBean {

	private String username, password;
	private RMIServerInterface server;
	
	
	public String encryptPassword(String base) {
        MessageDigest digest = null;
		try {
			digest = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        byte[] hash = null;
		try {
			hash = digest.digest(base.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        StringBuffer hexString = new StringBuffer();

        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if(hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    
    
	}

	
	public int authenticate() throws RemoteException{
		
		Response resp = new Response();
		Client client = new Client(username, encryptPassword(password));
		try {
			server = (RMIServerInterface) LocateRegistry.getRegistry("localhost", 7000).lookup("RMIServer");
			System.out.println("RMI Connected");
			try {
				resp = server.checkUser(client);
				if(resp.isSuccess()){
					if(resp.isValue()){
						System.out.println("User/password correct");
						return 0;
					}
					else{
						System.out.println("User/password incorrect");
						return -1;
					}
					
				}
				return -1;
				
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		catch(NotBoundException|RemoteException e) {
			e.printStackTrace();
		}
		return -1;
		
	}
	
	


	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public RMIServerInterface getServer() {
		return server;
	}


	public void setServer(RMIServerInterface server) {
		this.server = server;
	}
	
	
}
