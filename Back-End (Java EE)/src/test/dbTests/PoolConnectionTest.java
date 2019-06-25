package test.dbTests;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import db.Database;

public class PoolConnectionTest {



	public static void main(String[] args) {
		
		Connection connection ;
		 
		Statement statement ;
		
	try {
	    connection = Database.getMySQLConnection(); 
	    statement = connection.createStatement();
	     
	    System.out.println("Connection Established");
	    
	    
	    System.out.println(statement.executeUpdate("insert into user (id_user) values('1')"));
	    ResultSet res=statement.executeQuery("SELECT * from user");
	    printResultSet(res);
	    System.out.println(statement.executeUpdate("delete from user where id_user='1'"));
	    
	    statement.close();		
	    connection.close();

		}catch( SQLException e){
		    System.err.println(e);
		}

		}
	
	final private static void printResultSet(ResultSet rs) throws SQLException {

	    // Prepare metadata object and get the number of columns.
	    ResultSetMetaData rsmd = rs.getMetaData();
	    int columnsNumber = rsmd.getColumnCount();

	    // Print column names (a header).
	    for (int i = 1; i <= columnsNumber; i++) {
	        if (i > 1) System.out.print(" | ");
	        System.out.print(rsmd.getColumnName(i));
	    }
	    System.out.println("");

	    while (rs.next()) {
	        for (int i = 1; i <= columnsNumber; i++) {
	            if (i > 1) System.out.print(" | ");
	            System.out.print(rs.getString(i));
	        }
	        System.out.println("");
	    }
	}
	
}
