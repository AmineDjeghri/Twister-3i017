package test.dbTests;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class MysqlConnectionTest {

	public static void main(String[] args) {
		
		testMysqlLocalDB();
		//testMysqlCloudDB();
		}
	
	
	private static void testMysqlCloudDB() {
		Connection connection ;
		 
		Statement statement ;
		
	try {
		Class.forName("com.mysql.jdbc.Driver");
		System.out.println("test");
		// db parameters
	    String url       = "jdbc:mysql://remotemysql.com:3306/n72953X5Sw";
	    String user      = "n72953X5Sw";
	    String password  = "ZnjfZyj7Tn";
	    
	    connection = DriverManager.getConnection(url,user,password); 
	    statement = connection.createStatement();
	     
	    System.out.println("Connection Established");
	    
	    statement.close();		
	    connection.close();
	    
		}catch(Exception e) {
	    	e.printStackTrace();
	    }
	}
	
	
	
	private static void testMysqlLocalDB() {
		System.out.println("test");
		Connection connection ;
		 
		Statement statement ;
		
	try {
		Class.forName("com.mysql.jdbc.Driver");
		System.out.println("test");
		// db parameters
	    String url       = "jdbc:mysql://localhost:3306/twister_BD";
	    String user      = "root";
	    String password  = "root";
	    
	    connection = DriverManager.getConnection(url,user,password); 
	    statement = connection.createStatement();
	     
	    System.out.println("Connection Established");
	    
	    
	    statement.close();		
	    connection.close();

		}catch(ClassNotFoundException | SQLException e){
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