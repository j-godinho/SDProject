package common;

import java.io.Serializable;

public class Reward implements Serializable{

    private String descr;
    private int value;

    Reward(){}

    public Reward(String descr, int value)
    {
        setDescr(descr);
        setValue(value);

    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}