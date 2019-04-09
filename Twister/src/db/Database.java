package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;

public class Database {

	private DataSource dataSource;
	private static Database database =null;

	public Database(String jndiname) throws SQLException {
		try {
			dataSource = (DataSource) new InitialContext().lookup("java:comp/env/" + jndiname);
		} catch (NamingException e) {
			// Handle error that it’s not configured in JNDI.
			throw new SQLException(jndiname + " is missing in JNDI! : " + e.getMessage());
		}
	}

	public Connection getConnection() throws SQLException {
		return dataSource.getConnection();

	}

	public static Connection getMySQLConnection()throws SQLException {
		if(DBStatic.mysql_pooling==false) {
			
			//connect to local mysql database
			return (DriverManager.getConnection("jdbc:mysql://"+ DBStatic.mysql_host +"/"+DBStatic.mysql_db, DBStatic.mysql_username, DBStatic.mysql_password));}
			
			//connect to remote mysql database
			//return (DriverManager.getConnection("jdbc:mysql://"+ DBStatic.mysql_host_R +"/"+DBStatic.mysql_db_R, DBStatic.mysql_username_R, DBStatic.mysql_password_R));}
		
		else{
			if(database==null) 
				database=new Database("jdbc/twister_BD");
			
			return(database.getConnection());
			}
		}

	
	public static MongoDatabase getMongoDBConnection(){
		//local mongodb uri 
		MongoClientURI uri = new MongoClientURI(DBStatic.mongodb_uri);
		
		//remote mongodb uri
		//MongoClientURI uri = new MongoClientURI(DBStatic.mongodb_uri_R);
		
        MongoClient mongoClient = new MongoClient(uri);
        MongoDatabase database = mongoClient.getDatabase("twister_BD");
        return database;
	}
}
