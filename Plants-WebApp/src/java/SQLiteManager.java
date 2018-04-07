
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


public class SQLiteManager {
    private final String dbPath;
    private Connection conn = null;
    private Statement stmt = null;

    public SQLiteManager() {
        dbPath = "measurments.db";
    }
    
    public SQLiteManager(String databasePath) {
        dbPath = databasePath;
    }
    
    public void connect() {
        try {
         Class.forName("org.sqlite.JDBC");
         conn = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
         System.out.println("Opened database successfully.");
      } catch ( ClassNotFoundException | SQLException e ) {
         System.err.println( e.getClass().getName() + ": " + e.getMessage() );
      }
    }
    
    public ResultSet select(String query) {
        try {
            stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery(query);
            return result;
            
        } catch (SQLException ex) {
            Logger.getLogger(SQLiteManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public void close() {
        try {
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(SQLiteManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Database closed.");
    }
}
