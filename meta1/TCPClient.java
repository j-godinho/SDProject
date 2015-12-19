

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;

import common.Answer;
import common.Client;
import common.Configs;
import common.Data;
import common.Project;
import common.Response;
import common.Reward;

import java.security.MessageDigest;

public class TCPClient extends Thread{

    private Client currentUser;
    private boolean logged = false;
    private boolean receivingUsers = false;

    static Socket socket = null;
    static int serversocket;
    static int serversocket2;
    static DataInputStream in;
    static DataOutputStream out;

    static ObjectOutputStream outObject;
    static ObjectInputStream inObject;

    //server variables
    static String hostDestination1 ;
    static String hostDestination2 ;
    private int connection = 0;
    private boolean online = true;
    private int pos = 0;
    private int counter = -1;
    private int retry = 0;
    private boolean full = false;


    TCPClient() {
        Configs configs = new Configs();
        hostDestination1 = configs.getServer1();
        hostDestination2 = configs.getServer2();
        serversocket = configs.getTcp1();
        serversocket2=configs.getTcp2();

        try {
            socket = new Socket(hostDestination1, serversocket);

            System.out.println("SOCKET=" + socket);

            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            start();
            begin();
        } catch (UnknownHostException e) {
            System.out.println("Sock: Cannot connect to primary server... trying secondary server" + e.getMessage());
        } catch (EOFException e) {
            System.out.println("EOF: Cannot connect to primary server... trying secondary server" + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO: Cannot connect to primary server... trying secondary server" + e.getMessage());
        }try{
            socket = new Socket(hostDestination2, serversocket2);
            System.out.println("SOCKET=" + socket);
            connection += 1;
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            start();
            begin();
        }catch(IOException e1){
            System.out.println("Cannot connect to server...");
        }
    }
    public static void main(String args[]){

        new TCPClient();
    }
    public void run(){
        try{
            while(true){
                try {
                    if (counter <= pos && online == false) {
                        counter += 1;
                    } else if (counter > pos && online == false) {
                        System.out.println("Online");
                        online = true;
                        pos = 0;
                        counter = -1;
                        userMenu();

                    }
                    if (receivingUsers) {
                        Client user = (Client) inObject.readObject();
                        userMenu();
                    } else {
                        //begin();
                    }
                }catch (ClassNotFoundException e){
                    e.printStackTrace();
                }
            }
        }catch(IOException e){
            connection += 1;
            if (logged) {
                System.out.println("");
                System.out
                        .println("ConnectionTCP with the server is down...trying to reconnect");
                System.out.println(">> ");
                try {
                    online = false;

                    Thread.sleep(10000);
                    System.out.println("Retry: " + retry);
                    socket.close();
                    in.close();
                    out.close();
                    reconnect();
                } catch (InterruptedException e2) {

                } catch (IOException e1) {
                    System.out.println("Can't reconnect");
                }
            }
        }
    }
    // try to connect to secondary server
    private void reconnect() {

        connection += 1;

        while (true) {
            try {
                if(connection %2 == 0){
                    socket = new Socket(hostDestination1, serversocket);
                }
                else
                    socket = new Socket(hostDestination2, serversocket);
                outObject = new ObjectOutputStream(socket.getOutputStream());
                inObject = new ObjectInputStream(socket.getInputStream());
                try {
                    send((Client)inObject.readObject());
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                run();
                break;
            } catch (IOException e) {
                connection +=1;
            }
        }
    }
    private void begin() throws IOException {
        String text = "";
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);

        while (true) {
            displayMenu();
            System.out.println("Introduce your choice:");
            //System.out.println("begin");
            try {
                text = reader.readLine();
                // WRITE INTO THE SOCKET
                out.writeUTF(text);
                out.flush();

                if(text.isEmpty()||text.length()>1){
                    System.out.println("Invalid option\n");
                    //displayMenu();
                }
                else if(text.equals("1")){
                    availableProjects();
                }
                else if(text.equals("2")){
                    oldProjects();
                }
                else if(text.equals("3")){
                    projectDetails();
                }
                else if(text.equals("4")){
                    login();
                }
                else if(text.equals("5")){
                    register();
                }
                else if(text.equals("6")){
                    exit();
                    System.exit(0);
                    return;
                }
                else{
                    System.out.println("Invalid option\n");
                }
            }catch (UnknownHostException e) {
                System.out.println("Sock:" + e.getMessage());
            } catch (EOFException e) {
                System.out.println("EOF:" + e.getMessage());
            }catch (IOException e) {
                System.out.println("IO:" + e.getMessage());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
    private void displayMenu(){
        System.out.println("1- Show current available projects");
        System.out.println("2- Show older projects");
        System.out.println("3- Show project details");
        System.out.println("4- Login");
        System.out.println("5- Register");
        System.out.println("6- Exit");
    }

    private void availableProjects(){
        Response resp;
        ArrayList<String> aux = new ArrayList<>();
        try {
            inObject = new ObjectInputStream(socket.getInputStream());

            resp = (Response)inObject.readObject();
            aux = resp.getInfo();
            System.out.println(aux);
                int numProj = (aux.size() + 1) / 5;
                int res = 0;
                if (!aux.isEmpty()) {
                    for (int i = 0; i < numProj; i++) {
                        System.out.println("Project ID: " + aux.get(res) + " Name: " + aux.get(res + 1) + " Deadline: " + aux.get(res + 2) + " Money: " + aux.get(res + 3) + " Goal: " + aux.get(res + 4));
                        res += 5;
                    }
                }

            else{
                System.out.println("No projects");
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
    private void oldProjects(){
        Response resp;
        ArrayList<String> aux = new ArrayList<>();
        try {
            inObject = new ObjectInputStream(socket.getInputStream());
            resp = (Response)inObject.readObject();
            aux = resp.getInfo();
            System.out.println(resp.getInfo());
            int numProj = (aux.size()+1)/7;
            int res = 0;
            if(!aux.isEmpty()){
                for(int i = 0; i < numProj; i++){
                    System.out.println("Project ID: "+ aux.get(res) + " Name: " + aux.get(res+1) +" Deadline: " + aux.get(res+2) + " Money: " + aux.get(res + 3) + " Goal: " + aux.get(res +4));
                    res += 5;
                }
            }
            else{
                System.out.println("No projects");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
    private void projectDetails() {
        Project project;
        Response resp;
        Scanner info = new Scanner(System.in);
        String data;
        String canceled, finished;
        ArrayList<String> aux = new ArrayList<>();
        availableProjects();
        System.out.println("Project ID to check the details: ");
        data = info.nextLine();
        boolean number = false;
        while(!number){
            try{
                Integer.parseInt(data);
                number = true;
            }
            catch(NumberFormatException ex){
                System.out.println("Invalid input!");
                System.out.println("Project ID to check the details: ");
                data = info.nextLine();
            }

        }
        System.out.println("Project " + data);

        try {
            out.writeUTF(data);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            inObject = new ObjectInputStream(socket.getInputStream());
            resp = (Response) inObject.readObject();
            aux = resp.getInfo();
            System.out.println(resp.getInfo());
            if(aux.get(6).equals("0")){
                canceled = "Not canceled";
            }
            else{
                canceled = "Canceled";
            }
            if(aux.get(7).equals("0")){
                finished = "Not finished";
            }
            else{
                finished = "Finished";
            }
            System.out.println("Name: " + aux.get(0) + " Admin: " + aux.get(1) + " Description: " + aux.get(2) + " Money: " + aux.get(3) + " Goal: " + aux.get(4) + " Deadline: " + aux.get(5) + " " + canceled + " " + finished);
            //System.out.println("Rewards:");
            //System.out.println(aux.size());
            int numRew = ((aux.size() - 8)/4);
            System.out.println(numRew);
            int beg = 9;
            for(int i = 0; i < numRew; i++){
                System.out.println("RewardID: " + aux.get(beg) + " Description: " + aux.get(beg + 1) + " Money: " + aux.get(beg + 2) + " ProjectID: " + aux.get(beg + 3));
                beg = beg + 4;
            }
            System.out.println("Most voted choive: " +aux.get(beg+1));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
    private void register(){
        Client user;
        String name;
        String password1, password2;
        String encryptPW;
        int saldo = 0;
        Scanner sc = new Scanner(System.in);
        System.out.println("Introduce your username:");
        name = sc.nextLine();
        System.out.println("Enter your password:");
        password1 = sc.nextLine();
        while(true){
            System.out.println("Confirm your password:");
            password2 = sc.nextLine();
            if(password1.compareTo(password2) == 0){
                encryptPW = encryptPassword(password1);
                System.out.println(encryptPW);
                user = new Client(name, encryptPW);
                //System.out.println("user created");
                send(user);
                logged = true;
                break;
            }
            else{
                System.out.println("Passwords do not match");
            }
        }


    }

    public static String encryptPassword(String base) {
        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(base.getBytes("UTF-8"));
            StringBuffer hexString = new StringBuffer();

            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if(hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }
    private void send(Client user){
        System.out.println("Sending information to primary server...");

        try {
            outObject = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try{
            outObject.writeObject(user);
            outObject.flush();
            //outObject.reset();

            // READ FROM SOCKET
            String data = in.readUTF();

            // DISPLAY WHAT WAS READ
            System.out.println("Received: " + data);

        }catch(IOException e){
            System.out.println("ConnectionTCP is down");
        }
    }
    private void login() throws IOException, ClassNotFoundException {
        //login
        Client user;
        Scanner info = new Scanner(System.in);
        String name, password, encryptPW;
        System.out.println("Username: ");
        name = info.nextLine();
        System.out.println("Password: ");
        password = info.nextLine();
        encryptPW = encryptPassword(password);
        //check if client exists in DB

        user = new Client(name, encryptPW);
        String data = null;
        try {
            outObject = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            outObject.writeObject(user);
            outObject.flush();
            data = in.readUTF();
            System.out.println(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(data.compareTo("Welcome") == 0){
            currentUser = user;
            userOptions();

        }else{
            begin();
        }


    }
    private void userOptions() throws ClassNotFoundException, IOException {
        String text = "";
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);
        while (true) {
            userMenu();
            System.out.println("Introduce your choice:");
            //System.out.println("userOptions");
            // READ STRING FROM KEYBOARD
            try {
                text = reader.readLine();
            } catch (Exception e) {
            }
            // WRITE INTO THE SOCKET
            try{
                out.writeUTF(text);
                out.flush();
            }catch (IOException e){
                e.printStackTrace();
            }
            if (text.equals("1")) {
                availableProjects(); //DONNE
            } else if (text.equals("2")) {
                oldProjects(); //DONNE
            } else if (text.equals("3")) {
                projectDetails(); //DONNE
            } else if (text.equals("4")) {
                consultAccount(); //DONNE
            }else if (text.equals("5")) {
                consultRewards(); //DONNE
            }else if (text.equals("6")) {
                donateMoney(); //DONNE
            }else if (text.equals("7")) {
                sendMessage(); //DONNE
            }else if (text.equals("8")) {
                System.out.println("escolheu 8");
                createProject(); //DONNE
            }else if (text.equals("9")) {
                changeRewards(); //DONNE
            }else if (text.equals("10")) {
                cancelProject(); //DONNE
            }else if (text.equals("11")) {
                answerQuestion(); //DONNE
            }else if (text.equals("12")) {
                logout(); //DONNE
            }else{
                System.out.println("Invalid option\n");
            }

        }
    }
    private void userMenu(){
        System.out.println("1- Show current available projects");
        System.out.println("2- Show older projects");
        System.out.println("3- Show project details");
        System.out.println("4- Consult account");
        System.out.println("5- Consult rewards");
        System.out.println("6- Donate money");
        System.out.println("7- Send messages to project");
        System.out.println("8- Create project");
        System.out.println("9- Add/remove rewards of your own projects");
        System.out.println("10- Cancel project");
        System.out.println("11- Answer question");
        System.out.println("12- Logout");

    }
    private void consultAccount(){
        String data = null;
        try {
            outObject = new ObjectOutputStream(socket.getOutputStream());
            outObject.writeObject(currentUser);
            outObject.flush();
            data = in.readUTF();
            System.out.println("User: " +currentUser.getName());
            System.out.println("Money available: " +data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void consultRewards() throws ClassNotFoundException {
        String data = null;
        Response resp;
        ArrayList<String> aux = new ArrayList<>();
        try {
            outObject = new ObjectOutputStream(socket.getOutputStream());
            outObject.writeObject(currentUser);
            outObject.flush();
            inObject = new ObjectInputStream(socket.getInputStream());
            resp = (Response)inObject.readObject();
            aux = resp.getInfo();
            //System.out.println(aux);
            int availableRewards = ((aux.size()+1) - 2)/3;
            int res = 1;
            if(!aux.isEmpty()){
                if(availableRewards==0)
                {
                    System.out.println("No rewards available");
                }
                else {
                    System.out.println("Rewards available:");
                    for (int i = 0; i < availableRewards; i++) {
                        System.out.println("Description: " + aux.get(res) + " Value: " + aux.get(res + 1) + " ID: " + aux.get(res + 2));
                        res += 3;
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void donateMoney() throws IOException {
        Scanner info = new Scanner(System.in);
        String data, question, option, aux, choice;
        Response resp, newResp;
        availableProjects();
        System.out.println("Project ID to donate money: ");
        data = info.nextLine();
        boolean number = false;
        while(!number){
            try{
                Integer.parseInt(data);
                number = true;
            }
            catch(NumberFormatException ex){
                System.out.println("Invalid input!");
                System.out.println("Project ID  to donate money: ");
                data = info.nextLine();
            }

        }
        System.out.println("Project " + data);
        out.writeUTF(data);
        out.flush();

        displayRewards();
        System.out.println("Choose the reward:");
        choice = info.nextLine();
        boolean number2 = false;
        while(!number2){
            try{
                Integer.parseInt(choice);
                number2 = true;
            }
            catch(NumberFormatException ex){
                System.out.println("Invalid input!");
                System.out.println("Choose the reward: ");
                choice = info.nextLine();
            }

        }
        out.writeUTF(choice);
        out.flush();

        try {
            resp = (Response)inObject.readObject();
            if(resp.isSuccess()){
                System.out.println("Question: ");
                System.out.println(resp.getInfo().get(0));
            }
        } catch (ClassNotFoundException e) {

            e.printStackTrace();
        }

        try {
            newResp = (Response) inObject.readObject();
            if(newResp.isSuccess()){
                System.out.println("Options:");
                System.out.println(newResp.getInfo());
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println("Choose your option:");
        aux = info.nextLine();
        out.writeUTF(aux);
        out.flush();
        System.out.println(in.readUTF());



    }
    private void displayRewards(){
        Response resp;
        ArrayList<String> aux = new ArrayList<>();
        try {
            inObject = new ObjectInputStream(socket.getInputStream());
            resp = (Response)inObject.readObject();
            aux = resp.getInfo();
            int numRew = (aux.size()+1)/4;
            int res= 0;
            for(int i = 0; i < numRew; i++){
                System.out.println("Reward ID:" + aux.get(res)+ " Description: " +aux.get(res+1) + " Value: "+aux.get(res+2));
                res += 4;
            }



        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    private void sendMessage() throws IOException {
        Scanner info = new Scanner(System.in);
        String data, aux;
        Response resp;
        availableProjects();
        System.out.println("Project ID to send message: ");
        data = info.nextLine();
        boolean number = false;
        while(!number){
            try{
                Integer.parseInt(data);
                number = true;
            }
            catch(NumberFormatException ex){
                System.out.println("Invalid input!");
                System.out.println("Project ID to check the details: ");
                data = info.nextLine();
            }

        }
        out.writeUTF(data);
        out.flush();
        System.out.println("Write message:");
        out.writeUTF(info.nextLine());
        out.flush();

        System.out.println("Receiving information");
        inObject = new ObjectInputStream(socket.getInputStream());
        try {
            resp = (Response) inObject.readObject();
            if(resp.isSuccess()){
                System.out.println("criada");
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }



    }
    private void createProject(){
        System.out.println("createProject");
        Scanner info = new Scanner(System.in);
        String name, description ,data = null, question;
        String mainGoal, year, month,day, numRewards, moneyReward, stringChoices;
        int money, nRewards, numChoices;
        ArrayList<String> answers = new ArrayList<>();
        Project project;
        Answer newAnswer;
        ArrayList <Reward> rewards = new ArrayList<>();
        System.out.println("Project Name:");
        do{
            name = info.nextLine();
        }while (name.equals(""));
        System.out.println("Project Description:");
            description = info.nextLine();
        System.out.println("Money that you need:");
        mainGoal = info.nextLine();
        boolean number = false;
        while(!number){
            try{
                Integer.parseInt(mainGoal);
                number = true;
            }
            catch(NumberFormatException ex){
                System.out.println("Invalid input!");
                System.out.println("PMoney that you need: ");
                mainGoal = info.nextLine();
            }

        }
        System.out.println("Deadline(YYYY/MM//DD");
        System.out.println("Year: ");
            year = info.nextLine();
            boolean number2 = false;
            while(!number2){
                try{
                    Integer.parseInt(year);
                    number2 = true;
                }
                catch(NumberFormatException ex){
                    System.out.println("Invalid input!");
                    System.out.println("Year: ");
                    year = info.nextLine();
                }

            }
            System.out.println("Month: ");
            month = info.nextLine();
            boolean number3 = false;
            while(!number3){
                try{
                    Integer.parseInt(month);
                    number3 = true;
                }
                catch(NumberFormatException ex){
                    System.out.println("Invalid input!");
                    System.out.println("Month: ");
                    month = info.nextLine();
                }

            }
            System.out.println("Day: ");
            day = info.nextLine();
            boolean number4 = false;
            while(!number4){
                try{
                    Integer.parseInt(day);
                    number4 = true;
                }
                catch(NumberFormatException ex){
                    System.out.println("Invalid input!");
                    System.out.println("Day: ");
                    day = info.nextLine();
                }

            }

        System.out.println("Number of rewards:");

        numRewards = info.nextLine();
        boolean number5 = false;
        while(!number5){
            try{
                Integer.parseInt(numRewards);
                number5 = true;
            }
            catch(NumberFormatException ex){
                System.out.println("Invalid input!");
                System.out.println("Number of rewards: ");
                numRewards = info.nextLine();
            }

        }
        nRewards = Integer.parseInt(numRewards);
        //info.nextLine();
        Data date = new Data(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day));

        for(int i = 0; i < nRewards; i++){
            int auxInt;
            System.out.println("Type the money for reward number "+(i+1)+":");
            moneyReward = info.nextLine();
            boolean number6 = false;
            while(!number6){
                try{
                    Integer.parseInt(moneyReward);
                    number6 = true;
                }
                catch(NumberFormatException ex){
                    System.out.println("Invalid input!");
                    System.out.println("Type the money for reward number "+(i+1)+":");
                    moneyReward = info.nextLine();
                }

            }
            auxInt= Integer.parseInt(moneyReward);

            System.out.println("The type of reward for the money:");
            String auxString;

                auxString = info.nextLine();

            rewards.add(new Reward(auxString, auxInt));
        }
        System.out.println("Question:");

            question = info.nextLine();


        System.out.println("Number of choices:");
        stringChoices = info.nextLine();
        boolean number7 = false;
        while(!number7){
            try{
                Integer.parseInt(stringChoices);
                number7 = true;
            }
            catch(NumberFormatException ex){
                System.out.println("Invalid input!");
                System.out.println("Number of choices:");
                stringChoices = info.nextLine();
            }

        }
            numChoices = Integer.parseInt(stringChoices);


        for(int i = 0; i < numChoices; i++){
            System.out.println("Choice "+ (i+1)+":");
            answers.add(info.nextLine());
        }
        newAnswer = new Answer(question, answers);
        project = new Project(name, currentUser, description, 0, date, Integer.parseInt(mainGoal), rewards, newAnswer);
        //sendProject
        System.out.println("Creating project");
        try {
            outObject = new ObjectOutputStream(socket.getOutputStream());
            outObject.writeObject(project);
            data = in.readUTF();
            if(data.compareTo("Project created")==0){
                System.out.println(data);
            }
            else{
                userMenu();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private boolean validNumber(String number)
    {

        return false;

    }
    private void changeRewards() throws IOException {
        Scanner info = new Scanner(System.in);
        String idProj, money, reward;
        int choice;
        Response resp, newResp, auxResp;
        if(showClientProjects()){
            System.out.println("Choose the project you want to change:");
            idProj= info.nextLine();
            System.out.println("Add(1) or Remove(2) rewards:");
            choice = info.nextInt();
            info.nextLine();
            if(choice == 1){
                out.writeUTF("1");
                out.flush();
                System.out.println("Money for the new reward:");
                money = info.nextLine();
                System.out.println("Reward:");
                reward = info.nextLine();
                out.writeUTF(idProj);
                out.flush();
                out.writeUTF(reward);
                out.flush();
                out.writeUTF(money);
                out.flush();
                try {
                    inObject = new ObjectInputStream(socket.getInputStream());
                    resp = (Response) inObject.readObject();
                    if(resp.isSuccess()){
                        System.out.println("Added reward");
                    }
                    else{
                        System.out.println("Cannot add reward");
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }



            }
            else if(choice == 2){
                out.writeUTF("2");
                out.flush();
                out.writeUTF(idProj);
                out.flush();
                try {
                    inObject = new ObjectInputStream(socket.getInputStream());
                    newResp = (Response) inObject.readObject();
                    if(newResp.isSuccess()){
                        System.out.println(newResp.getInfo());
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                System.out.println("Reward ID to remove");
                reward = info.nextLine();
                out.writeUTF(reward);
                out.flush();
                System.out.println(in.readUTF());

            }
        }
        /*else{
            userMenu();
        }*/


    }
    private boolean showClientProjects(){
        Response resp;
        ArrayList<String> aux = new ArrayList<>();
        try {
            inObject = new ObjectInputStream(socket.getInputStream());
            resp = (Response)inObject.readObject();
            aux = resp.getInfo();
            int numProj = (aux.size()+1)/4;
            int res = 0;
            if(!aux.isEmpty()){
                for(int i = 0; i < numProj; i++){
                    System.out.println("Project ID: "+ aux.get(res) + " Name: " + aux.get(res+1) +" Deadline: " + aux.get(res+2) + " Money: " + aux.get(res + 3) + " Goal: " + aux.get(res +4));
                    res += 5;
                }
                return true;
            }
            else{
                System.out.println("No projects");
                return false;
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return true;

    }
    private void cancelProject() throws IOException {
        Scanner info = new Scanner(System.in);
        String data, aux;
        if(showClientProjects()){
            System.out.println("What project do you want to cancel:");
            data = info.nextLine();
            out.writeUTF(data);
            out.flush();

            Response resp;
            try {
                inObject = new ObjectInputStream(socket.getInputStream());
                resp = (Response)inObject.readObject();
                if(resp.isSuccess()){
                    System.out.println("Project canceled!");
                }
                else{
                    System.out.println("Cannot cancel the project");
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        out.flush();
        /*else{
            userMenu();
        }*/




    }
    private void answerQuestion() throws IOException {
        if(showMessages()){
            Scanner info = new Scanner(System.in);
            String data, message;
            Response resp;
            System.out.println("Select the message ID you want to answer:");
            data = info.nextLine();
            boolean number = false;
            while(!number){
                try{
                    Integer.parseInt(data);
                    number = true;
                }
                catch(NumberFormatException ex){
                    System.out.println("Invalid input!");
                    System.out.println("Project ID to check the details: ");
                    data = info.nextLine();
                }

            }
            System.out.println("Answer:");
            message = info.nextLine();
            out.writeUTF(data);
            out.flush();
            out.writeUTF(message);
            out.flush();

            System.out.println("Receiving information");
            inObject = new ObjectInputStream(socket.getInputStream());
            try {
                resp = (Response) inObject.readObject();
                if(resp.isSuccess()){
                    System.out.println("criadaaaa");
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        out.flush();


    }
    private boolean showMessages(){
        Response resp;
        ArrayList<String> aux = new ArrayList<>();
        try {
            inObject = new ObjectInputStream(socket.getInputStream());
            resp = (Response)inObject.readObject();
            aux = resp.getInfo();
            if(aux.size()>0){
                //System.out.println(resp.getInfo());
                int numMessages = (aux.size() + 1)/5;
                int res = 0;
                for(int i = 0; i < numMessages; i++){
                    System.out.println("MessageID: " + aux.get(res) + " Sender: " + aux.get(res+1) + " Message: " + aux.get(res + 4));
                    res +=  5;
                }
                return true;
            }
            else{
                System.out.println("No messages to read");
                return false;
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return true;
    }
    private void logout(){
        try{
            begin();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    private void exit(){
        System.out.println("Exiting");
        try{
            socket.close();
            out.close();
            in.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}