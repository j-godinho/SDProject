package rmiserver;

import java.sql.*;
import java.util.TimerTask;

public class DateController  extends TimerTask{
    Connection c;
    DataBaseConstants consts;
    public DateController(Connection c, DataBaseConstants consts)
    {
        this.c=c;
        this.consts=consts;
    }
    public void run(){

        try{
            System.out.println("RUN function - thread");
            Statement stmt = c.createStatement();
            ResultSet result = null;

            //getValue
            c.setAutoCommit(false);

            result = stmt.executeQuery(consts.selectProjects);
            System.out.println("select projects");
            while(result.next())
            {
                if(result.getInt("money")>=result.getInt("maingoal"))
                {
                    System.out.println("Sucesso");
                    //SUCESSO
                    PreparedStatement ps =c.prepareStatement(consts.giveRewards);
                    ps.setInt(1, result.getInt("ID"));
                    ps.execute();

                    ps = c.prepareStatement(consts.transferMoneyAdmin);
                    ps.setInt(1, result.getInt("ID"));
                    ps.execute();
                }
                else
                {
                    System.out.println("Fracasso");
                    //fracasso
                    PreparedStatement ps = c.prepareStatement(consts.returnClientMoney);
                    ps.setInt(1, result.getInt("id"));
                    ps.execute();
                }

            }
            System.out.println("verify projects");
            stmt.executeUpdate(consts.verifyProjects);

            c.commit();
        }catch(SQLException e)
        {
            e.printStackTrace();
        }
    }
}
