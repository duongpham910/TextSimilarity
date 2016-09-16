package DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by DuongPham on 28/08/2016.
 */
public class ConnectionDB {

    private static ConnectionDB instance;
    private Connection con;
    private String dbClass="com.mysql.cj.jdbc.Driver";
    private String dbUrl="jdbc:mysql://localhost/newspaper";

    private ConnectionDB(){
        try {
            Class.forName(dbClass);
            con=(Connection) DriverManager.getConnection(dbUrl, "root", "123456");
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static ConnectionDB getInstance(){
        if(instance==null){
            instance=new ConnectionDB();
        }
        return instance;
    }

    public Connection getCon() {
        return con;
    }
}
