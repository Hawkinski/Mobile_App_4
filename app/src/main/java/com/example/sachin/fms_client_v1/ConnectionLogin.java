package com.example.sachin.fms_client_v1;

import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by sachin on 4/9/2016.
 */
public class ConnectionLogin {

    String classs= "net.sourceforge.jtds.jdbc.Driver";

    public Connection CONN(){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
        Connection con = null;
        String url=null;


        try{
            Class.forName(classs);

            url="jdbc:jtds:sqlserver://appsql.dyndns.org;databaseName=HRZNET;user=sa;Password=KL";
            con= DriverManager.getConnection(url);

        } catch (SQLException se) {
            Log.e("ERRO", se.getMessage());
        } catch (ClassNotFoundException e) {
            Log.e("ERRO", e.getMessage());
        } catch (Exception e) {
            Log.e("ERRO", e.getMessage());
        }
        return con;

    }
}
