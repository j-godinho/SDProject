

import java.io.*;
import java.net.Socket;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

import common.*;
import rmiserver.RMIServerInterface;


class ConnectionTCP extends UnicastRemoteObject  implements ClientInterface, Runnable{


    DataInputStream in;
    DataOutputStream out;
    ObjectOutputStream outObject;
    ObjectInputStream inObject;
    Socket clientSocket;
    Client user;
    static ArrayList<Client> onlineUsers = new ArrayList<Client>();

    Boolean userON = false;
    static RMIServerInterface svInterface;
    static ClientInterface clInterface;
    static String hostname;
    static String hostname2;
    static int registryNumber;
    int thread_number;
    Configs configs = new Configs();

    public ConnectionTCP(Socket aClientSocket, int numero, String hostRMI) throws RemoteException {

        super();
        hostname = configs.getServer1();
        hostname2 = configs.getServer2();
        registryNumber= configs.getRmi_port();


        thread_number = numero;
        clInterface = this;
        try{
            try{
                svInterface = (RMIServerInterface) LocateRegistry.getRegistry(
                        hostname, registryNumber).lookup("RMIServer");
            } catch(NotBoundException e){
                System.out.println("Server did not bind the registry yet...");
            }catch(RemoteException e1){
                System.out.println("Remote");
                try{
                    clInterface = this;
                    hostname2 = hostRMI;
                    svInterface = (RMIServerInterface) LocateRegistry.getRegistry(
                            hostname2, registryNumber).lookup("RMIServer");
                } catch (RemoteException e2) {
                    System.out.println("Cannot connect to the server...");

                } catch (NotBoundException e2) {
                    System.out
                            .println("Server did not bind the registry yet...");
                }
            }
            //svInterface
            clientSocket = aClientSocket;
            in = new DataInputStream(clientSocket.getInputStream());
            out = new DataOutputStream(clientSocket.getOutputStream());
            new Thread(this).start();
        }catch(IOException e){System.out.println("ConnectionTCP:" + e.getMessage());
        }
    }
    //=============================
    public void run(){
        System.out.println("run");
        String resposta;
        try{
            while(true){
                //an echo server

                String data = in.readUTF();
                System.out.println("T["+thread_number + "] Recebeu: "+data);
                if(!userON){
                    if(data.equals("1")){
                        //show available projects
                        System.out.println("!userOn list");
                        listProjects(0);
                    }
                    else if(data.equals("2")){
                        //show older projects
                        listProjects(1);
                    }
                    else if(data.equals("3")){
                        //show available projects
                        listProjects(0);
                        projectDetails();
                        //get the ID
                        //check project details
                    }
                    else if(data.equals("4")){
                        checkLogin();
                    }
                    else if(data.equals("5")){
                        sendRegister();
                    }
                    else if (data.equals("6")) {
                        exit();
                    }
                }else{
                    if(data.equals("1")){
                        //show available projects
                        listProjects(0);
                    }
                    else if(data.equals("2")){
                        //show older projects
                        listProjects(1);
                    }
                    else if(data.equals("3")){
                        listProjects(0);
                        projectDetails();
                    }
                    else if(data.equals("4")){
                        consultAccount();
                    }
                    else if(data.equals("5")){
                        consultRewards();
                    }
                    else if(data.equals("6")){
                        listProjects(0);
                        donateMoney();
                    }
                    else if(data.equals("7")){
                        listProjects(0);
                        sendMessage();
                    }
                    else if(data.equals("8")){
                        createProject();
                    }
                    else if(data.equals("9")){
                        if(showClientProjects()){
                            changeRewards();
                        }

                    }
                    else if(data.equals("10")){
                        if(showClientProjects()){
                            cancelProject();
                        }

                    }
                    else if(data.equals("11")){
                        if(showMessages()){
                            answerQuestion();
                        }

                    }
                    else if(data.equals("12")){
                        //show older projects
                        logout();
                    }
                }

            }
        }catch(EOFException e) {
            System.out.println("EOF:" + e);
        }catch(IOException e){
            //System.out.println("IO:" + e);
        }
    }
    public void sendRegister() throws IOException {
        System.out.println("sever - 5");
        Boolean info;
        Client newUser;
        inObject = new ObjectInputStream(clientSocket.getInputStream());
        try {
            newUser = (Client) inObject.readObject();
            System.out.println(newUser.getName());
            info = svInterface.registerUser(newUser).isSuccess();
            if(info){
                out.writeUTF("Registo efectuado com sucesso");
                out.flush();
            }
            else{
                out.writeUTF("Username already in use");
                out.flush();
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
    public void checkLogin() throws IOException{
        System.out.println("server - 4");
        inObject = new ObjectInputStream(clientSocket.getInputStream());
        try {
            user = (Client) inObject.readObject();
            System.out.println(user.getName());
            //out.writeUTF("Registo efectuado com sucesso");
            //out.flush();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        Response resp;
        resp = svInterface.checkUser(user);
        if(resp.isSuccess())
        {
            if(resp.isValue())
            {
                userON = true;
                onlineUsers.add(user);
                out.writeUTF("Welcome");
                out.flush();
            }
            else
            {
                out.writeUTF("Wrong credentials");
                out.flush();
                //checkLogin();
            }
        }
        else {
            System.out.println("Insuccess!");
        }


    }
    public void createProject() throws IOException {
        System.out.println("server - 8");
        inObject = new ObjectInputStream(clientSocket.getInputStream());
        Project project;
        Response resp;
        try {
            project = (Project) inObject.readObject();
            System.out.println(project.getName());
            resp = svInterface.insertNewProject(project);
            if(resp.isSuccess()){
                out.writeUTF("Project created");
                out.flush();
            }
            else{
                //out.writeUTF("Cannot create the project");
                //out.flush();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    public void consultAccount() throws IOException {
        System.out.println("server - 4");
        inObject = new ObjectInputStream(clientSocket.getInputStream());
        Client user;
        Response resp;
        try{
            user = (Client)inObject.readObject();
            System.out.println(user.getName());
            resp = svInterface.getMoneyAvailable(user);
            if(resp.isSuccess()){
                out.writeUTF(Integer.toString(resp.getMoney()));
                out.flush();
            }
        } catch (ClassNotFoundException e){
            e.printStackTrace();
        }
    }
    public void consultRewards() throws IOException {
        System.out.println("Server -5");
        inObject = new ObjectInputStream(clientSocket.getInputStream());
        outObject = new ObjectOutputStream(clientSocket.getOutputStream());
        Client user;
        Response resp;
        try{
            user = (Client)inObject.readObject();
            System.out.println(user.getName());
            resp = svInterface.getClientRewards(user);
            if(resp.isSuccess()){
                outObject.writeObject(resp);
                outObject.flush();
            }
        } catch (ClassNotFoundException e){
            e.printStackTrace();
        }
    }
    public void projectDetails() throws IOException {
        System.out.println("project details");
        String id = null;
        id = in.readUTF();
        Response resp;
        outObject = new ObjectOutputStream(clientSocket.getOutputStream());
        try{
            resp = svInterface.projectDetails(Integer.parseInt(id));
            if(resp.isSuccess()){
                outObject.writeObject(resp);
                outObject.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void changeRewards() throws IOException {
        String info = null;
        int idProj, value, rewardID;
        String descr;
        info = in.readUTF();
        Response resp, newResp;
        outObject = new ObjectOutputStream(clientSocket.getOutputStream());
        if(info.equals("1")){
            idProj = Integer.parseInt(in.readUTF());
            descr = in.readUTF();
            value = Integer.parseInt(in.readUTF());
            resp = svInterface.insertReward(idProj, descr, value);
            outObject.writeObject(resp);
            outObject.flush();
        }
        else if(info.equals("2")){
            System.out.println("RemoveReward");
            idProj = Integer.parseInt(in.readUTF());
            System.out.println("idProj: "+idProj);
            resp = svInterface.getProjectRewards(idProj);
            if(resp.isSuccess()){
                outObject.writeObject(resp);
                outObject.flush();
            }
            System.out.println("After rewards");
            rewardID = Integer.parseInt(in.readUTF());
            newResp = svInterface.removeReward(idProj, rewardID);
            System.out.println(newResp.isSuccess());
            if(newResp.isSuccess()){
                out.writeUTF("Reward removed");
                out.flush();
            }
            else{
                out.writeUTF("Cannot remove reward");
                out.flush();
            }

        }
    }
    public void sendMessage() throws IOException {
        System.out.println("sendMEssage");
        String idProj, message;
        Response resp;
        idProj = in.readUTF();
        message = in.readUTF();
        outObject = new ObjectOutputStream(clientSocket.getOutputStream());
        try {
            resp = svInterface.sendMessageToProject(user, Integer.parseInt(idProj), message);
            System.out.println(resp.isSuccess());

            if(resp.isSuccess()){
                System.out.println("sending to client");
                outObject.writeObject(resp);
                outObject.flush();
                //out.writeUTF("Message sent");
                System.out.println("message");
                //out.flush();
            }

        } catch (IOException e){
            e.printStackTrace();
        }

    }
    public boolean showMessages() throws IOException {
        System.out.println("showMessage");
        outObject = new ObjectOutputStream(clientSocket.getOutputStream());
        Response resp;
        try {
            resp = svInterface.showMessages(user);
            if(resp.isSuccess()){
                System.out.println(resp.getInfo().size());
                outObject.writeObject(resp);
                outObject.flush();
                if(resp.getInfo().size()==0){
                    return false;
                }
                else{
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
    public void answerQuestion() throws IOException {
        System.out.println("answerQuestion");
        String idMessage, text;
        Response resp;
        idMessage = in.readUTF();
        text = in.readUTF();
        outObject = new ObjectOutputStream(clientSocket.getOutputStream());
        try{
            resp = svInterface.answerQuestion(user, Integer.parseInt(idMessage), text);
            System.out.println(resp.isSuccess());
            if(resp.isSuccess()){
                System.out.println("sending to client");
                outObject.writeObject(resp);
                outObject.flush();
                //out.writeUTF("Message sent");
                System.out.println("message");
                //out.flush();
            }
            else{

            }
        } catch (IOException e){
            e.printStackTrace();
        }

    }
    public void donateMoney() throws IOException{
        System.out.println("donateMoney");
        String idProj = null, idReward = null;
        Response resp, newResp;
        idProj = in.readUTF();
        outObject = new ObjectOutputStream(clientSocket.getOutputStream());
        try{
            resp = svInterface.getProjectRewards(Integer.parseInt(idProj));
            if(resp.isSuccess()){
                outObject.writeObject(resp);
                outObject.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        idReward = in.readUTF();
        System.out.println("idReward: "+idReward);
        try{
            System.out.println("question");
            resp = svInterface.getQuestion(Integer.parseInt(idProj));
            outObject.writeObject(resp);
            outObject.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }

        try{
            System.out.println("choices");
            resp = svInterface.getChoices(Integer.parseInt(idProj));

            System.out.println(resp.getInfo());
            outObject.writeObject(resp);
            outObject.flush();
            System.out.println("enviou");

        } catch (IOException e) {
            e.printStackTrace();
        }


        System.out.println("option: ");
        String option = in.readUTF();
        System.out.println(option);

        try{
            resp = svInterface.incrementProjectMoney(user, Integer.parseInt(idProj), Integer.parseInt(idReward), Integer.parseInt(option));
            System.out.println(resp.isSuccess());
            if(resp.isSuccess()){
                out.writeUTF("Thank you for the donation");
                out.flush();
            }
            else
            {
                out.writeUTF("Failed to Donate");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    public void listProjects(int choice) throws IOException {
        System.out.println("availableProjects "+choice);
        outObject = new ObjectOutputStream(clientSocket.getOutputStream());
        Response resp;
        try {
            resp = svInterface.listProjects(choice);
            if(resp.isSuccess()){
                outObject.writeObject(resp);
                outObject.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public boolean showClientProjects() throws IOException {
        System.out.println("showClientProjects");
        outObject = new ObjectOutputStream(clientSocket.getOutputStream());
        Response resp;
        try {
            resp = svInterface.getClientProjects(user);
            if(resp.isSuccess()){
                outObject.writeObject(resp);
                outObject.flush();
                if(resp.getInfo().size() == 0){
                    return false;
                }
                else{

                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
    public void cancelProject() throws IOException {
        System.out.println("cancelProject");
        String id = null;
        Response resp, newResp;
        id = in.readUTF();
        outObject = new ObjectOutputStream(clientSocket.getOutputStream());

        resp = svInterface.removeProject(Integer.parseInt(id));
        System.out.println(resp.isSuccess());
        outObject.writeObject(resp);
        outObject.flush();
    }
    public void logout(){
        userON = false;
        onlineUsers.remove(user);

    }
    public void exit(){
        try {
            clientSocket.close();
            in.close();
            out.close();
            //inObject.close();
            //outObject.close();
            System.out.println("[" + thread_number
                    + "]Streams and socket closed");
        } catch (IOException e) {
            //e.printStackTrace();
        }
    }
    @Override
    public void checkServer() throws RemoteException {

    }
}