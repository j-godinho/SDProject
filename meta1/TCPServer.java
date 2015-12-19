

import java.io.*;
import java.util.*;

import common.Configs;


public class TCPServer {


    private static int serverMachineNumber;

    private static String thisSMHost;
    private static int thisSMPort;

    private static String otherSMHost;
    private static int otherSMPort;

    public static String RMIHostname;
    public static int RMIPort;

    private static Signal switchServer;

    private static ServerCheckAlive workerSCA;
    private static ServerTCP workerSTCP;

    public static void main(String[] args) {
        switchServer = new Signal(false);
        configServer();


        workerSCA = new ServerCheckAlive(switchServer, thisSMPort, otherSMHost, otherSMPort);
        workerSTCP = new ServerTCP(switchServer, thisSMPort);

        try {
            workerSCA.t.join();
            workerSTCP.t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }



    private static void configServer(){
        Configs configs = new Configs();
        int option;
        Scanner sc = new Scanner(System.in);
        System.out.println("Indique o numero deste servidor [1 ou 2]: ");
        option = sc.nextInt();

        InputStream in = null;

        try {

            switch(option){
                case 1:
                    serverMachineNumber = 1;
                    thisSMHost = configs.getServer1();
                    thisSMPort = configs.getTcp1();
                    otherSMHost = configs.getServer2();
                    otherSMPort = configs.getTcp2();
                    break;

                case 2:
                    serverMachineNumber = 2;
                    thisSMHost = configs.getServer2();
                    thisSMPort = configs.getTcp2();
                    otherSMHost = configs.getServer1();
                    otherSMPort = configs.getTcp1();
                    break;
            }

            RMIHostname = configs.getData_base();
            RMIPort = configs.getRmi_port();

        }

        finally {
            if(in != null) {
                try {
                    in.close();
                }
                catch(IOException e) { }
            }
        }

    }

}
