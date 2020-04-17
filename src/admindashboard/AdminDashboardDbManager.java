/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package admindashboard;

import databaseconnection.DbConnection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author kynda
 */
public class AdminDashboardDbManager
{
    
    public ArrayList retrieveUserInfo() {
        DbConnection connection = new DbConnection();
        ArrayList<ArrayList<String>> data = new ArrayList<>();        
        Statement stmt;
        // SELECT username, MAX(logInDate) FROM logFile GROUP BY username;
        try {
            stmt = connection.getConnectionLoginDB().createStatement(); 
            String sql = "SELECT Login_Credentials.u_id, Login_Credentials.u_fname, Login_Credentials.u_sname, Login_Credentials.u_email, Login_Credentials.u_username, Login_Credentials.u_type, Login_Credentials.u_regDate AS logInDate"+
                    " FROM Login_Credentials";
                    
            stmt.execute(sql);
            ResultSet rs = stmt.getResultSet();
            
            try {
                
                while(rs.next()) {
                    ArrayList<String> user = new ArrayList<>();
                    user.add(String.valueOf(rs.getInt("u_id")));
                    user.add(rs.getString("u_fname"));
                    user.add(rs.getString("u_sname"));
                    user.add(rs.getString("u_email"));
                    user.add(rs.getString("u_username"));
                    user.add(rs.getString("u_type"));
                    user.add(rs.getString("logInDate"));
                    data.add(user);
                }
            } finally {
                rs.close();
            }
            
            stmt.close(); 
            connection.getConnectionDataDB().close();
        } catch (SQLException e) {
            System.out.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return data;        
    }
    
    public ArrayList retrieveLogInfo() {
        ArrayList<ArrayList<String>> data = new ArrayList<>();        
        Statement stmt;
        DbConnection connection = new DbConnection();
        // SELECT username, MAX(logInDate) FROM logFile GROUP BY username;
        try {
            
            
            stmt = connection.getConnectionLoginDB().createStatement(); 
            String sql = "SELECT Login_Credentials.u_id, Login_Credentials.u_fname, Login_Credentials.u_sname, Login_Credentials.u_email, Login_Credentials.u_username, logFile.logInDate,  IFNULL(logFile.logOutDateTime, 'N/A') AS logOutDateTime "+ 
                "FROM Login_Credentials JOIN logFile ON Login_Credentials.u_username=logFile.username;";
            stmt.execute(sql);
            ResultSet rs = stmt.getResultSet();
            
            try {
                
                while(rs.next()) {
                    ArrayList<String> user = new ArrayList<>();
                    user.add(String.valueOf(rs.getInt("u_id")));
                    user.add(rs.getString("u_fname"));
                    user.add(rs.getString("u_sname"));
                    user.add(rs.getString("u_email"));
                    user.add(rs.getString("u_username"));
                    user.add(rs.getString("logInDate"));
                    user.add(rs.getString("logOutDateTime"));
                    data.add(user);
                }
            } finally {
                rs.close();
            }
            
            stmt.close(); 
            connection.getConnectionDataDB().close();
        } catch (SQLException e) {
            System.out.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return data;        
    }
    
    public String setUserOnlineValue() {       
        Statement stmt;
        int counter = 0;
        DbConnection connection = new DbConnection();
        try {
            stmt = connection.getConnectionLoginDB().createStatement(); 
            String sql = "SELECT COUNT(*) "+
                "FROM logFile "+
                "WHERE logOutDateTime IS NULL "+
                "GROUP BY username;";
            stmt.execute(sql);
            ResultSet rs = stmt.getResultSet();
            while(rs.next()) {
                counter++;
            }
            
            stmt.close(); 
            connection.getConnectionDataDB().close();
        } catch (SQLException e) {
            System.out.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return String.valueOf(counter);
    }
    
    public String setNumberOfRegistration() {       
        Statement stmt;
        String counter="";
        DbConnection connection = new DbConnection();
        try {
            stmt = connection.getConnectionLoginDB().createStatement(); 
            String sql = "SELECT COUNT(*) FROM Login_Credentials WHERE strftime('%m', u_regDate) IN (strftime('%m', 'now'));";
            stmt.execute(sql);
           
            ResultSet rs = stmt.getResultSet();
            while(rs.next()) {
                counter = rs.getString(1);
            }
            
            stmt.close(); 
            connection.getConnectionDataDB().close();
        } catch (SQLException e) {
            System.out.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return counter;
    }
    
    
    public boolean searchUser(String values,String selection,ArrayList<ArrayList<String>> data){
         String table = getRowLoginCred(selection);
         Statement statement;
         DbConnection connection = new DbConnection();
    try
    {
         statement =connection.getConnectionLoginDB().createStatement();
         String sql = "SELECT * FROM Login_Credentials WHERE "+table+"='"+values+"';"; 
         String sqls ="SELECT * FROM Login_Credentials WHERE "+table+"="+values+";";
         if(selection.equals("ID")){
              statement.execute(sqls);
         }else{
             statement.execute(sql);
         }
       
         ResultSet rs = statement.getResultSet();
        try{
         while(rs.next()){
          
             
            ArrayList<String> user = new ArrayList<>();
            user.add(String.valueOf(rs.getInt("u_id")));
            user.add(rs.getString("u_fname"));
            user.add(rs.getString("u_sname"));
            user.add(rs.getString("u_email"));
            user.add(rs.getString("u_username"));
            user.add(rs.getString("u_type"));
            user.add(rs.getString("u_regDate"));
            data.add(user);
             
         }
        }finally{
                  statement.close();
             }
        
         connection.getConnectionLoginDB().close();
         return true;
    } catch (SQLException ex)
    {
        Logger.getLogger(AdminDashboardDbManager.class.getName()).log(Level.SEVERE, null, ex);
        return false;
    }
     
    }
    
    public String getRowLoginCred(String selection){
        
        String row ;
        if(selection.equals("ID")){
            row = "u_id";
        }else if(selection.equals("Email")){
            row="u_email";
        }else if(selection.equals("Username")){
            row="u_username";
        }else if(selection.equals("Type")){
            row="u_type";
        }else{
            row= null;
        }
        
        return row;
    }

    public String getRowLog(String selection){
        
        String row = null;
        
        if(selection == "Username"){
            row = "username";
        }else if(selection =="Password"){
            row="password";
        }else if(selection =="LogInDate"){
            row="logInDate";
        }else if(selection=="LogOutDate"){
            row="logOutDateTime";
        }else{
            row= null;
        }
        
        return row;
    }

    public boolean searchLog(String values,String selection,ArrayList<ArrayList<String>> data){
         //String table = getRowLog(selection);
         Statement statement;
         DbConnection connection = new DbConnection();
    try
    {
         statement =connection.getConnectionLoginDB().createStatement();
        // String sql = "SELECT * FROM logFile WHERE "+table+"='"+values+"';"; 
         String sqlId ="SELECT Login_Credentials.u_id, Login_Credentials.u_fname, Login_Credentials.u_sname, Login_Credentials.u_email, Login_Credentials.u_username, logFile.logInDate,  IFNULL(logFile.logOutDateTime, 'N/A') AS logOutDateTime "+ 
                "FROM Login_Credentials JOIN logFile ON Login_Credentials.u_username=logFile.username WHERE Login_Credentials.u_id ="+values+";";
         String sqlEmail ="SELECT Login_Credentials.u_id, Login_Credentials.u_fname, Login_Credentials.u_sname, Login_Credentials.u_email, Login_Credentials.u_username, logFile.logInDate,  IFNULL(logFile.logOutDateTime, 'N/A') AS logOutDateTime "+ 
                "FROM Login_Credentials JOIN logFile ON Login_Credentials.u_username=logFile.username WHERE Login_Credentials.u_email ='"+values+"';";
         String sqlUsername ="SELECT Login_Credentials.u_id, Login_Credentials.u_fname, Login_Credentials.u_sname, Login_Credentials.u_email, Login_Credentials.u_username, logFile.logInDate,  IFNULL(logFile.logOutDateTime, 'N/A') AS logOutDateTime "+ 
                "FROM Login_Credentials JOIN logFile ON Login_Credentials.u_username=logFile.username WHERE Login_Credentials.u_username ='"+values+"';";
         
         if(selection.equals("ID")){
              statement.execute(sqlId);
         }else if(selection.equals("Email")){
             statement.execute(sqlEmail);
         }else if (selection.equals("Username")){
              statement.execute(sqlUsername);
         }
       
         ResultSet rs = statement.getResultSet();
        try{
         while(rs.next()){
          
             
             ArrayList<String> user = new ArrayList<>();
                    user.add(String.valueOf(rs.getInt("u_id")));
                    user.add(rs.getString("u_fname"));
                    user.add(rs.getString("u_sname"));
                    user.add(rs.getString("u_email"));
                    user.add(rs.getString("u_username"));
                    user.add(rs.getString("logInDate"));
                    user.add(rs.getString("logOutDateTime"));
                    data.add(user);
             
         }
        }finally{
                  statement.close();
             }
        
         connection.getConnectionLoginDB().close();
         return true;
    } catch (SQLException ex)
    {
        Logger.getLogger(AdminDashboardDbManager.class.getName()).log(Level.SEVERE, null, ex);
        return false;
    }
          
      }
    
    public boolean deleteUser(String id){
        Statement statement;
        DbConnection connection = new DbConnection();
        try{
            statement =connection.getConnectionLoginDB().createStatement();
            String sql="DELETE FROM Login_Credentials WHERE u_id="+id+";";
            statement.execute(sql);
            connection.getConnectionLoginDB().close();
            return true;
            
        }catch (SQLException ex){
        Logger.getLogger(AdminDashboardDbManager.class.getName()).log(Level.SEVERE, null, ex);
        return false;
        }
        
    }
    
    public boolean checkIfUserExists(String id){
        Statement statement;
        DbConnection connection = new DbConnection();
        int count= 0;
        
        try{
            
            statement =connection.getConnectionLoginDB().createStatement();
            
            
            String sql="SELECT * FROM Login_Credentials WHERE u_id ='"+ id +"';";
            statement.execute(sql);
            ResultSet rs = statement.getResultSet();
            while(rs.next()){
                count = rs.getRow();
               
            }
            statement.close();
            connection.getConnectionLoginDB().close();
            
            if(count == 0){
                return false;
                
            }else{
                
                return true;
            }
           
            
            
            
            
        }catch (SQLException ex){
        Logger.getLogger(AdminDashboardDbManager.class.getName()).log(Level.SEVERE, null, ex);
        return false;
        }
    }
    
    public boolean addUser(String fname,String lname,String email,String username,String password,String type){
        Statement statement;
        DbConnection connection = new DbConnection();
        String sql="INSERT INTO Login_Credentials(u_fname,u_sname,u_email,u_username,u_password,u_type) VALUES('"+ fname +"','"+ lname +"','"+ email +"','"+ username +"','"+ password +"','"+ type +"');";
        
    try{
            
          statement =connection.getConnectionLoginDB().createStatement(); 
          statement.execute(sql);
          connection.getConnectionLoginDB().close();
          return true;
        
    }catch(SQLException ex){
        Logger.getLogger(AdminDashboardDbManager.class.getName()).log(Level.SEVERE, null, ex);
        return false;
    }
    
}
    
}
