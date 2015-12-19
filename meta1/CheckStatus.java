import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;


public class CheckStatus extends Thread{

    int firstPort;
    int secondPort;
    String reply;
    String hostnameTCP;

    CheckStatus(int firstPort, int secondPort, String hostnameTCP){
        System.out.println("checkStatus");
        this.firstPort = firstPort;
        this.secondPort = secondPort;
        this.hostnameTCP = hostnameTCP;
        reply = "I_AM_ALIVE";
        start();
    }

    public void run(){
        DatagramSocket socket = null;
        try{
            System.out.println("Sending I_AM_ALIVE packages");
            byte[] b = reply.getBytes();
            byte[] buffer = new byte[256];
            socket = new DatagramSocket(firstPort);

            while(true){
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                packet = new DatagramPacket(b, b.length, InetAddress.getByName(hostnameTCP), secondPort);
                socket.send(packet);
            }
        } catch (IOException e){
            if(socket != null){
                socket.close();
            }
            System.out.println("Stopped sending I_AM_ALIVE packages");
        }
    }


}