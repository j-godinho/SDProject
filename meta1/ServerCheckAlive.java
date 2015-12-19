
import java.io.*;
import java.net.*;


public class ServerCheckAlive implements Runnable{

	
	private int task;
	private Signal serverSwitch;
	public Thread t;
	private int thisSMPort;
	private String otherSMHostname;
	private int otherSMPort;
	// ###################################################################################
	//						CONSTRUCTORS
	// ###################################################################################
	public ServerCheckAlive(Signal serverSwitch, int thisSMPort, String otherSMHostname, int otherSMPort){
		this.serverSwitch = serverSwitch;
		this.t = new Thread(this);
		this.thisSMPort = thisSMPort;
		this.otherSMHostname = otherSMHostname;
		this.otherSMPort = otherSMPort;
		
		t.start();
	}
	

	public void run(){
		System.out.println("ServerCheckAlive a iniciar...");
		int tries = 0;
		DatagramSocket UDPSocket = null;
		
		try{
			UDPSocket = new DatagramSocket(thisSMPort);
		} catch(SocketException e){
			System.out.println(e.getMessage());
		}
		

			try{
				System.out.println("Envio de PING");
				
				byte[] msg = "PING".getBytes();
				DatagramPacket reply = new DatagramPacket(msg, msg.length, InetAddress.getByName(otherSMHostname), otherSMPort);
				UDPSocket.send(reply);
				
				System.out.println("A esperar por resposta...");
				byte[] buffer = new byte[256];
				DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
				UDPSocket.setSoTimeout(5000);	//Aguarda 5 segundos por resposta
				UDPSocket.receive(packet);
				System.out.println("[SCA - Init] # Resposta recebida");
				
				String s = new String(packet.getData(), 0, packet.getLength());
				

				if(s.compareTo("PING") == 0){	//executa como PRINCIPAL
					task = 1;
					synchronized(serverSwitch){
						serverSwitch.setStatus(true);
						serverSwitch.setType(1);
						serverSwitch.notify();
					}
				}
				else{							//executa como BACKUP
					synchronized(serverSwitch){
						serverSwitch.setStatus(true);
						serverSwitch.setType(2);
						serverSwitch.notify();
					}
					task = 2;
				}

			} catch(SocketTimeoutException e){
				System.out.println("PONG NAO RECEBIDO. executar como PRINCIPAL.");
				synchronized(serverSwitch){
					serverSwitch.setStatus(true);
					serverSwitch.setType(1);
					serverSwitch.notify();
				}
				task = 1;
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
		

		
		while(true){
			try{
				if(task == 1){				// Responder a SM de backup
					System.out.println("A escutar o servidor de backup...");
					byte[] buffer = new byte[256];
					DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
					System.out.println("A esperar por PING...");
					UDPSocket.setSoTimeout(0);
					UDPSocket.receive(packet);		//throws: IOException; SocketTimeoutException
					System.out.println("PING recebido");
					
					byte[] msg = "PONG".getBytes();
					DatagramPacket reply = new DatagramPacket(msg, msg.length, InetAddress.getByName(otherSMHostname), otherSMPort);
					UDPSocket.send(reply);			//throws: IOException; SocketTimeoutException
					System.out.println("PONG enviado.");
				}
				else if(task == 2){			// Verificar se a SM principal esta Online
					System.out.println("A controlar o servidor principal...");
					byte[] msg = "PING".getBytes();
					DatagramPacket packet = new DatagramPacket(msg, msg.length, InetAddress.getByName(otherSMHostname), otherSMPort);
					UDPSocket.send(packet);			//throws: IOException; SocketTimeoutException
					System.out.println("PING enviado. A aguardar PONG...");
					
					byte[] buffer = new byte[256];
					DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
					UDPSocket.setSoTimeout(5000);
					UDPSocket.receive(reply);
					System.out.println("PONG recebido: ");
					Thread.sleep(5000);
				}
			} catch(SocketTimeoutException e){
				System.out.println("[SCA - B] !!! PONG NAO RECEBIDO. SM vai executar como PRINCIPAL.");
				synchronized(serverSwitch){
					serverSwitch.setStatus(true);
					serverSwitch.setType(1);
					serverSwitch.notify();
				}
				task = 1;
			} catch (IOException e){
				System.out.println(e.getMessage());

			} catch (InterruptedException e) {
				System.out.println(e.getMessage());

			}
		}
	}
}
