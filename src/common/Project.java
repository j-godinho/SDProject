package common;

import java.io.Serializable;
import java.util.ArrayList;

public class Project implements Serializable {

    private int id;
    private String name;
    private Client admin;
    private String description;
    private int money;
    private int mainGoal;
    private Data deadline;
    private Answer choices;
    private ArrayList <Reward> rewards = new ArrayList<>();
    //private ArrayList <String> goals;
    private ArrayList <Message> messages = new ArrayList<>();

    Project(){}


    public Project(String name, Client admin, String description,int money, Data deadline, int mainGoal, ArrayList rewards)
    {
        setName(name);
        setDescription(description);
        setMoney(money);
        setRewards(rewards);
        setDeadline(deadline);
        setAdmin(admin);
        setMainGoal(mainGoal);

    }
    Project( String name, Client admin, String description,int money,  Data date, int mainGoal, ArrayList rewards,   Answer choices)
    {
        setName(name);
        setDescription(description);
        setMoney(money);
        setRewards(rewards);
        setDeadline(date);
        setMainGoal(mainGoal);
        setAdmin(admin);
        setChoices(choices);
    }



    //GETTERS AND SETTERS



    public int getMainGoal() {
        return mainGoal;
    }

    public void setMainGoal(int mainGoal) {
        this.mainGoal = mainGoal;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<Reward> getRewards() {
        return rewards;
    }

    public void setRewards(ArrayList<Reward> rewards) {
        this.rewards = rewards;
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<Message> messages) {
        this.messages = messages;
    }

    public Data getDeadline() {
        return deadline;
    }

    public void setDeadline(Data deadline) {
        this.deadline = deadline;
    }

    public Client getAdmin() {
        return admin;
    }

    public void setAdmin(Client admin) {
        this.admin = admin;
    }

    public Answer getChoices() {
        return choices;
    }

    public void setChoices(Answer choices) {
        this.choices = choices;
    }

	
}