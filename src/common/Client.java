package common;

import java.io.Serializable;
import java.util.ArrayList;

public class Client implements Serializable{

    private String name;
    private String password;
    private int money;
    private ArrayList <Project> projects;
    private ArrayList <String> rewardsReceived;

    private int initialMoney = 100;

    Client(){}

    //New Client
    public Client(String name, String password)
    {
        setName(name);
        setPassword(password);
        setMoney(initialMoney);
        setProjects(null);
        setRewardsReceived(null);
    }

    //Already existed Client - get from database
    Client(String name, String password, int money, ArrayList <Project> projects, ArrayList <String> rewardsReceived)
    {
        setName(name);
        setPassword(password);
        setMoney(money);
        setProjects(projects);
        setRewardsReceived(rewardsReceived);
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public ArrayList<Project> getProjects() {
        return projects;
    }

    public void setProjects(ArrayList<Project> projects) {
        this.projects = projects;
    }

    public ArrayList<String> getRewardsReceived() {
        return rewardsReceived;
    }

    public void setRewardsReceived(ArrayList<String> rewardsReceived) {
        this.rewardsReceived = rewardsReceived;
    }
}