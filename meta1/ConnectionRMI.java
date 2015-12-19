

import common.Configs;
import rmiserver.RMIServerInterface;

import java.io.InterruptedIOException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionRMI extends UnicastRemoteObject implements Runnable{


    static RMIServerInterface server;
    static String hostRMI;
    static int registryNumber;
    static boolean serverDown = false;
    Configs configs = new Configs();
    public ConnectionRMI(String hostRMI, int registryNumber) throws RemoteException{
        super();
        this.hostRMI = hostRMI;
        this.registryNumber = registryNumber;
        new Thread(this).start();
    }

    public void run(){
        Connection c = null;
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            c = DriverManager.getConnection("jdbc:postgresql://"+configs.getDb_address()+":"+configs.getDb_port()+"/"+configs.getData_base(), configs.getAdmin(), configs.getPass());

        } catch (SQLException e) {
            e.printStackTrace();
        }
        try{
            try{
                server = (RMIServerInterface) LocateRegistry.getRegistry(hostRMI, registryNumber).lookup("RMIServer");
                System.out.println("lookup(rmiserver)");
            }catch(NotBoundException e1){
                System.out.println("Cannot bind to registry yet");
            }
            while(true){
                try{
                    serverDown = false;
                    server.checkServer();
                    System.out.println("bota ping");
                    Thread.sleep(3000);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        }catch(RemoteException e){
            try{
                Thread.sleep(3000);
                try{
                    new ConnectionRMI(hostRMI, registryNumber);
                }catch (RemoteException e1){
                    e1.printStackTrace();
                }
            }catch (InterruptedException e1){
                e1.printStackTrace();
            }
            serverDown = true;
            System.out.println("Cannot connect to server");
        }
    }

}