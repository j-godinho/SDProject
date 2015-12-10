/**
 * Raul Barbosa 2014-11-07
 */
package sd.model;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;

import rmiserver.RMIServerInterface;

public class PrimesBean {
	private int number;

	private RMIServerInterface server;
	private Registry registry;

	/*
	 * public PrimesBean() { try { registry = LocateRegistry.getRegistry(1099);
	 * server = (RMIServerInterface) Naming.lookup("server");
	 * System.out.println("RMI connected"); }
	 * catch(NotBoundException|MalformedURLException|RemoteException e) {
	 * e.printStackTrace(); // what happens *after* we reach this line? }
	 * 
	 * }
	 */
	public int getNumber() {
		return this.number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public ArrayList<String> getPrimes() {
		ArrayList<String> primes = new ArrayList<String>();
		try {
			registry = LocateRegistry.getRegistry(1099);
			server = (RMIServerInterface) Naming.lookup("server");
			System.out.println("RMI connected");
			try {

				primes = server.getPrimes(number);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (NotBoundException | MalformedURLException | RemoteException e) {
			e.printStackTrace(); // what happens *after* we reach this line?
		}

		return primes;
		/*
		 * System.out.println("RMIServer - getPrimes()"); ArrayList<String>
		 * primes = new ArrayList<String>(); int candidate = 2; for(int count =
		 * 0; count < number; candidate++) if(isPrime(candidate)) {
		 * primes.add((new Integer(candidate)).toString()); count++; }
		 * primes.add("1"); primes.add("2"); return primes;
		 */
	}

	private boolean isPrime(int number) {
		if ((number & 1) == 0)
			return number == 2;
		for (int i = 3; number >= i * i; i += 2)
			if ((number % i) == 0)
				return false;
		return true;
	}
}
