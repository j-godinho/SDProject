package common;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Configs {


    private String server1;
    private String server2;

    private String db_address;
    private int db_port;
    private int tcp1;
    private int tcp2;
    private int rmi_port;
    private String data_base;
    private String admin;
    private String pass;


    public Configs(){
        Properties prop = new Properties();
        InputStream config = null;
        try{
            config = new FileInputStream("config.properties");
            prop.load(config);


        }catch(IOException ex)
        {
            ex.printStackTrace();;
        }
        finally {
            if(config!=null)
            {
                try{
                    config.close();
                } catch(IOException e){
                    e.printStackTrace();
                }
            }
        }

        //SETTERS
        setServer1(prop.getProperty("SERVER1"));
        setServer2(prop.getProperty("SERVER2"));
        setDb_address(prop.getProperty("DB_ADDRESS"));
        setDb_port(Integer.parseInt(prop.getProperty("DB_PORT").trim()));
        setTcp1(Integer.parseInt(prop.getProperty("TCP1").trim()));
        setTcp2(Integer.parseInt(prop.getProperty("TCP2").trim()));
        setRmi_port(Integer.parseInt(prop.getProperty("RMI_PORT").trim()));

        setData_base(prop.getProperty("DATA_BASE"));
        setAdmin(prop.getProperty("ADMIN"));
        setPass(prop.getProperty("ADMIN_PASS"));




    }


    public String getServer1() {
        return server1;
    }

    public void setServer1(String server1) {
        this.server1 = server1;
    }

    public String getServer2() {
        return server2;
    }

    public void setServer2(String server2) {
        this.server2 = server2;
    }

    public String getDb_address() {
        return db_address;
    }

    public void setDb_address(String db_address) {
        this.db_address = db_address;
    }

    public int getDb_port() {
        return db_port;
    }

    public void setDb_port(int db_port) {
        this.db_port = db_port;
    }

    public int getTcp1() {
        return tcp1;
    }

    public void setTcp1(int tcp1) {
        this.tcp1 = tcp1;
    }

    public int getTcp2() {
        return tcp2;
    }

    public void setTcp2(int tcp2) {
        this.tcp2 = tcp2;
    }

    public int getRmi_port() {
        return rmi_port;
    }

    public void setRmi_port(int rmi_port) {
        this.rmi_port = rmi_port;
    }

    public String getData_base() {
        return data_base;
    }

    public void setData_base(String data_base) {
        this.data_base = data_base;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}
