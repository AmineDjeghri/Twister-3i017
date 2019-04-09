package db;

public class DBStatic {

	public static boolean mysql_pooling=true;
	//local mysql database
	public static String mysql_username="root";
	public static String mysql_host="localhost:3306";
	public static String mysql_password="root";
	public static String mysql_db="twister_BD";
	
	//remote mysql database
	public static String mysql_username_R="n72953X5Sw";
	public static String mysql_host_R="remotemysql.com:3306";
	public static String mysql_password_R="ZnjfZyj7Tn";
	public static String mysql_db_R="n72953X5Sw";	
	
	//local mongodb database:
	public static String mongodb_uri="mongodb://localhost";
	
	//remote mongodb database (use standard string uri):
	public static String mongodb_uri_R="mongodb://root:root@cluster0-shard-00-00-loipi.mongodb.net:27017,cluster0-shard-00-01-loipi.mongodb.net:27017,cluster0-shard-00-02-loipi.mongodb.net:27017/test?ssl=true&replicaSet=Cluster0-shard-0&authSource=admin&retryWrites=true";
	public static String mongo_db = "twister_BD"; //same database name for both
	
		

}
