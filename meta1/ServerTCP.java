
import java.io.IOException;
import java.net.*;



public class ServerTCP implements Runnable{

	
	private int task;
	private Signal serverSwitch;
	public Thread t;
	private int thisSMPort;

	private int numero;

	public ServerTCP(Signal serverSwitch, int thisSMPort){
		this.serverSwitch = serverSwitch;
		this.thisSMPort = thisSMPort;
		this.t = new Thread(this);
		t.start();
	}


	public void run(){
		synchronized(serverSwitch){
			while(!serverSwitch.getStatus()){
				try {
					serverSwitch.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			serverSwitch.setStatus(false);
			task = serverSwitch.getType();
		}
		
		if(task == 2){
			System.out.println("Executar como BACKUP");
			
			synchronized(serverSwitch){
				while(!serverSwitch.getStatus()){
					try {
						serverSwitch.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				
				serverSwitch.setStatus(false);
				task = serverSwitch.getType();
			}
		}
		
		if(task == 1){
			System.out.println("Executar como PRINCIPAL");
			System.out.println("ONLINE");
			

			try{
				@SuppressWarnings("resource")
				ServerSocket listenSocket = new ServerSocket(this.thisSMPort);
				System.out.println("ServerSocket ON");
				while(true){
					Socket clientSocket = listenSocket.accept();	//BLOQUEANTE
					System.out.println("Novo cliente");
					new ConnectionTCP(clientSocket, numero, "RMIServer");
				}
			} catch(IOException e){
				System.out.println( e.getMessage());
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e1) {
					System.out.println(e1.getMessage());

				}
			}
				
		}
	}
}
