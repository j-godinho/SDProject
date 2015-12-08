package common;

import java.io.Serializable;
import java.util.ArrayList;

public class Response implements Serializable{





    private ArrayList <String> info;
    private boolean value;
    private int money;
    private Project project;
    private boolean success;
    public Response(){}



    //GETTERS AND SETTERS

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public boolean isValue() {
        return value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }

    public ArrayList<String> getInfo() {
        return info;
    }

    public void setInfo(ArrayList<String> info) {
        this.info = info;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}