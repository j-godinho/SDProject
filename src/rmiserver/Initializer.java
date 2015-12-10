package rmiserver;

import java.io.IOException;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.SQLException;

public class Initializer {
	private static Registry registry;
	private static RMIServerInterface rmiServer;

	public static void main(String[] args) throws NotBoundException, ClassNotFoundException, SQLException {
		// For simplification purposes we are not using a policy file in this
		// example
		try {
			Registry registry = LocateRegistry.createRegistry(1099);
			registry.rebind("server", (RMIServerInterface) new RMIServer());
			System.out.println("Rmi Server Running...");
		} catch (AccessException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
