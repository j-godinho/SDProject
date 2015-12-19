package rmiserver;

public final class DataBaseConstants {

    public DataBaseConstants(){}


    public String usersTableCreation= 	"CREATE TABLE IF NOT EXISTS USERS "+
            "(NAME 		TEXT	PRIMARY KEY	NOT NULL, "+
            "PASS   	TEXT					NULL, "+
            "MONEY		INT						NOT NULL, "+
            "TUMBLR 	INT						NULL);";

    String projectsTableCreation="CREATE TABLE IF NOT EXISTS PROJECTS "+
            "(ID 		SERIAL	            PRIMARY KEY," +
            "NAME 		TEXT				NOT NULL, "+
            "ADMIN      TEXT                REFERENCES USERS(NAME) ON DELETE CASCADE,"+
            "DESCR 		TEXT				NOT NULL, "+
            "MAINGOAL   INT                 NOT NULL, "+
            "MONEY      INT                 NOT NULL, "+
            "DEADLINE	DATE			    NOT NULL, "+
            "CANCELED   INT                 NOT NULL," +
            "FINISHED   INT                 NOT NULL," +
    		"REBLOG		TEXT                 NULL," +
            "POSTID		TEXT					NULL);";	

    String rewardsTableCreation="CREATE TABLE IF NOT EXISTS REWARDS "+
            "(ID 		SERIAL	            PRIMARY KEY," +
            "DESCR 		TEXT				NOT NULL, "+
            "VALUE      INT                 NOT NULL, "+
            "PROJECTID	INT					REFERENCES PROJECTS(ID) ON DELETE CASCADE) ; ";


    String choicesTableCreation="CREATE TABLE IF NOT EXISTS CHOICES"+
            "(ID 		SERIAL		        PRIMARY KEY," +
            "QUESTION   TEXT                NOT NULL," +
            "CHOICE 	TEXT                NOT NULL, "+
            "COUNT      INT                 NOT NULL, "+
            "PROJECTID	INT					REFERENCES PROJECTS(ID) ON DELETE CASCADE) ;";

    String messagesTableCreation="CREATE TABLE IF NOT EXISTS MESSAGES"+
            "(ID 		SERIAL		        PRIMARY KEY,"  +
            "SENDER       TEXT              REFERENCES USERS(NAME) ON DELETE CASCADE," +
            "RECEIVER   TEXT                REFERENCES USERS(NAME) ON DELETE CASCADE," +
            "PROJECTID  INT                 REFERENCES PROJECTS(ID) ON DELETE CASCADE, "+
            "TEXT   	TEXT                NOT NULL);";

    String clientRewardsTableCreation="CREATE TABLE IF NOT EXISTS CLIENTREWARDS "+
            "(ID 		SERIAL	            PRIMARY KEY," +
            "NAME 		TEXT				REFERENCES USERS(NAME) ON DELETE CASCADE , "+
            "DESCR 		TEXT				NOT NULL, "+
            "VALUE      INT                 NOT NULL, "+
            "PROJECTID	INT					REFERENCES PROJECTS(ID) ON DELETE CASCADE, " +
            "RECEIVED   INT                 NOT NULL); ";

    String onlineUsersTableCreation="CREATE TABLE IF NOT EXISTS ONLINEUSERS "+
    		 "(ID 		SERIAL		        PRIMARY KEY,"  +
    		 "NAME 		TEXT				NOT NULL);";

    
    //CHOICES FUNCTONS
    String insertNewChoice = "INSERT INTO CHOICES (QUESTION, CHOICE, COUNT, PROJECTID) VALUES (?,?,0,?);";
    String incrementCount = "UPDATE CHOICES SET COUNT=COUNT+1 WHERE ID=?;";
    String getMostVoted   = "SELECT QUESTION, CHOICE from choices where count=(SELECT MAX(COUNT) FROM CHOICES where PROJECTID=?) AND PROJECTID=? LIMIT 1;";

    String getChoices = "SELECT ID, CHOICE, COUNT from CHOICES where PROJECTID=?;";


    

    String dropTables = "DROP TABLE  PROJECTS;";

    //choice 1
    String checkIfExists        = "SELECT NAME FROM USERS WHERE (USERS.NAME = ? );";
    String registerNewAccount   = "INSERT INTO USERS (NAME,PASS,MONEY) VALUES (?, ?, 100);";
    /////////////////////////////////////

    //choice 2
    String checkLogin           = "SELECT NAME FROM USERS WHERE (USERS.NAME= ? AND USERS.PASS = ?);";
    /////////////////////////////////////

    //choice 3
    String getAvailableProjects = "SELECT ID, NAME, DEADLINE, MONEY, MAINGOAL FROM PROJECTS WHERE FINISHED=0 AND CANCELED=0;";

    //choice 4
    String getOlderProjects     = "SELECT ID, NAME, DEADLINE, MONEY, MAINGOAL, CANCELED, FINISHED FROM PROJECTS WHERE FINISHED=1 OR CANCELED=1;";
    //choice 5 (not tested)
    String getProjectDetails    = "SELECT NAME, ADMIN, DESCR, MONEY, MAINGOAL, DEADLINE, CANCELED, FINISHED FROM PROJECTS WHERE PROJECTS.ID=?;";
    String getProjectRewards    = "SELECT ID, DESCR, VALUE, PROJECTID FROM REWARDS WHERE PROJECTID=?;";


    /////////////////////////////////////

    //choice 6
    String getUserMoney         = "SELECT MONEY FROM USERS WHERE(USERS.NAME=?);";
    /////////////////////////////////////

    //choice 7
    String insertClientReward   = "INSERT INTO CLIENTREWARDS(NAME, DESCR,VALUE, PROJECTID, RECEIVED) VALUES (?,?,?,?,0);";


    String getRewardsAvailable  = "SELECT DESCR, VALUE, PROJECTID FROM CLIENTREWARDS WHERE NAME=? AND RECEIVED=0;";
    String getRewardsReceived   = "SELECT DESCR, VALUE, PROJECTID FROM CLIENTREWARDS WHERE NAME=? AND RECEIVED=1;";
    /////////////////////////////////////



    //choice 8
    String getQuestion          ="SELECT QUESTION FROM CHOICES WHERE PROJECTID=? LIMIT 1;";
    String getOptions           ="SELECT ID, CHOICE, COUNT FROM CHOICES WHERE PROJECTID=?;";

    String incrementProjectMoney= "UPDATE PROJECTS SET MONEY=MONEY+? WHERE ID=?;";
    String takeClientMoney      = "UPDATE USERS SET MONEY=MONEY-? WHERE NAME=?;";
    String addDonation          = "INSERT INTO CLIENTREWARDS (NAME, DESCR,VALUE, PROJECTID, RECEIVED) VALUES (?,?,?,?,0);";
    /////////////////////////////////////


    //choice 9
    String sendMessage          = "INSERT INTO MESSAGES (SENDER, RECEIVER, PROJECTID , TEXT) VALUES (?,(SELECT ADMIN FROM PROJECTS WHERE ID=?),?,?);";
    //////////////////////////////////////

    //choice 10
    String projectExists        = "SELECT NAME FROM PROJECTS WHERE NAME = ?;";
    String insertProject        = "INSERT INTO PROJECTS ( NAME,ADMIN, DESCR, MONEY, MAINGOAL, DEADLINE, FINISHED, CANCELED) VALUES(?,?,?,0,?,?,0,0);";
    String inProjectRewards     = "INSERT INTO REWARDS (DESCR, VALUE, PROJECTID) VALUES (?,?, ?);";
    String inProjectChoices     = "INSERT INTO CHOICES(QUESTION, ANSWER, PROJECTID) VALUES (?,?,?);";
    //////////////////////////////////////

    //choice 11
    String addRewards           = "INSERT INTO REWARDS(DESCR, VALUE, PROJECTID) VALUES (?,?, ?);";

    String takeProjectRewardMoney = "UPDATE PROJECTS SET MONEY=PROJECTS.MONEY-CLIENTREWARDS.VALUE FROM CLIENTREWARDS WHERE PROJECTS.ID=? AND CLIENTREWARDS.PROJECTID=? AND CLIENTREWARDS.DESCR=(SELECT DESCR FROM REWARDS WHERE ID=?);";
    String returnRewardMoney    = "UPDATE USERS SET MONEY=MONEY+CLIENTREWARDS.VALUE FROM CLIENTREWARDS WHERE (CLIENTREWARDS.PROJECTID=? AND CLIENTREWARDS.DESCR=(SELECT DESCR FROM REWARDS WHERE ID=?) AND USERS.NAME=CLIENTREWARDS.NAME);";
    String removeRewards        = "DELETE FROM REWARDS WHERE ID=?;";
    String removeClientReward  = "DELETE FROM CLIENTREWARDS WHERE PROJECTID=? AND DESCR=(SELECT DESCR FROM REWARDS WHERE ID=?);";//////////////////////////////////////

    //choice 12
    String cancelProjectViewProjects = "SELECT ID, NAME, DEADLINE, MONEY, MAINGOAL FROM PROJECTS WHERE ADMIN=? AND CANCELED=0 AND FINISHED=0;";
    String returnClientMoney =  "UPDATE USERS  SET MONEY=MONEY+CLIENTREWARDS.VALUE FROM CLIENTREWARDS WHERE (PROJECTID=? AND USERS.NAME=CLIENTREWARDS.NAME) ;";
    String takeProjectMoney =  "UPDATE PROJECTS SET MONEY=0 WHERE ID=?;";
    String cancelProject       ="UPDATE PROJECTS SET CANCELED=1 WHERE PROJECTS.ID=?;";
    String removeClientRewards="DELETE FROM CLIENTREWARDS WHERE PROJECTID=?;";

    //////////////////////////////////////

    //choice 13
    String answerQuestion          = "INSERT INTO MESSAGES (SENDER, RECEIVER, PROJECTID , TEXT) VALUES (?,(SELECT SENDER FROM MESSAGES WHERE MESSAGES.ID=?),(SELECT PROJECTID FROM MESSAGES WHERE MESSAGES.ID=?),?);";
    String getProjectMessages      = "SELECT MESSAGES.ID, PROJECTS.NAME, SENDER, TEXT FROM MESSAGES, PROJECTS WHERE PROJECTID=? AND RECEIVER = (SELECT ADMIN FROM PROJECTS WHERE PROJECTS.ID=?);";
    String getClientMessages       = "SELECT * FROM MESSAGES WHERE MESSAGES.RECEIVER=?;";
    //////////////////////////////////////
    String getClientProjects        ="SELECT ID, NAME, DEADLINE, MONEY, MAINGOAL FROM PROJECTS WHERE ADMIN=?;";


    //Fim do Projecto

    String selectProjects          ="SELECT ID, MONEY, MAINGOAL FROM PROJECTS WHERE now()>=DEADLINE AND FINISHED=0";
    String verifyProjects          ="UPDATE PROJECTS SET FINISHED=1 WHERE (now()>=PROJECTS.DEADLINE AND PROJECTS.FINISHED=0);";

    //give rewards to users - SUCESSO
    String giveRewards              ="UPDATE CLIENTREWARDS SET RECEIVED=1 WHERE PROJECTID=?;";
    String transferMoneyAdmin       ="UPDATE USERS SET MONEY = USERS.MONEY + PROJECTS.MONEY FROM PROJECTS WHERE (PROJECTS.ID=? AND PROJECTS.ADMIN=USERS.NAME);";

    //devolver dinheiro- FRACASSO
    //String returnClientMoney =  "UPDATE USERS SET MONEY=MONEY+CLIENTREWARDS.VALUE FROM CLIENTREWARDS WHERE (PROJECTID=? AND USERS.NAME=CLIENTREWARDS.NAME) ;";

    //TODO ----------------------------------------SEGURANÇA---------------------------------------
    String projectIDExists = "SELECT ID FROM PROJECTS WHERE ID=?;";
    String messageIDExists = "SELECT ID FROM MESSAGES WHERE ID=?;";
    String rewardIDExists = "SELECT ID FROM REWARDS WHERE ID=?;";
    String projectIDExistsAvailable="SELECT ID FROM PROJECTS WHERE ID=?;";

    //TODO ----------------------------------------------------------------------------------------
    
    //TUMBLR STRINGS
    String registerTumblr = "INSERT INTO USERS (NAME,MONEY,TUMBLR) VALUES (?,100, 1);";
    String insertTumblrProject = "INSERT INTO PROJECTS ( NAME,ADMIN, DESCR, MONEY, MAINGOAL, DEADLINE, FINISHED, CANCELED, REBLOG, POSTID) VALUES(?,?,?,0,?,?,0,0);";
    
    String isTumblrAccount = "SELECT TUMBLR FROM USERS WHERE NAME=?;";
    String insertPost = "UPDATE PROJECTS SET POSTID = ? , REBLOG = ? WHERE NAME = ?;";
    String getPostInfo = "SELECT REBLOG, POSTID FROM PROJECTS WHERE ID=?;";
    
    //hugo
	String getAdmin = "SELECT ADMIN FROM PROJECTS WHERE ID=?;";
    String getValue = "SELECT VALUE FROM REWARDS WHERE ID=?;";
    String getProjectName = "SELECT NAME FROM PROJECTS WHERE ID=?;";
    String getSender= "SELECT SENDER FROM MESSAGES WHERE ID=?;";
    
	
    
    //online users
    String addOnlineUser = "INSERT INTO ONLINEUSERS (NAME) VALUES (?);";
    String removeOnlineUser= "DELETE FROM ONLINEUSERS WHERE NAME=?;";
    
	
    
}