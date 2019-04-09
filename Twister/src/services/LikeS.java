package services;

import java.sql.Connection;
import java.sql.SQLException;

import org.bson.Document;
import org.json.JSONException;
import org.json.JSONObject;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import db.Database;
import tools.LikeTools;
import tools.ErrorJSON;
import tools.SessionTools;
import tools.TwistTools;
import tools.UserTools;

public class LikeS {

	public static JSONObject addLike(String key_session,String id_message) {
		
		if(key_session==null || id_message==null )
			return ErrorJSON.serviceRefused("Missing parameter",-2);
		
		try {
			//connect to mysql
			Connection connection = Database.getMySQLConnection();
			
			if(!SessionTools.isConnectedByKey(key_session,connection)) 
				return ErrorJSON.serviceRefused("Not connected",-3);	
			
			if(SessionTools.hasExceededTimeOut(key_session, connection)) {
				SessionTools.removeSession(key_session,connection);
				return ErrorJSON.serviceRefused("TimeOut exceeded, disconnected automatically", 1);
			}
			
			//ajout de 30min dans session_fin
			SessionTools.updateTimeOut(key_session, connection);
		
			String id_user = SessionTools.getIdUser(key_session, connection);
			String login = UserTools.getLogin(id_user, connection);
			
			MongoDatabase mongoDatabase=Database.getMongoDBConnection();
			MongoCollection<Document> commentCollection = mongoDatabase.getCollection("message");
			
			if(!TwistTools.twistExists(id_message,commentCollection))
				return ErrorJSON.serviceRefused("No message to add like", 2);
			
			if(LikeTools.checkAuthor(id_message,id_user, commentCollection))
				return  ErrorJSON.serviceRefused("You have already liked this", 2);
			
			LikeTools.addLike(id_message, id_user, login, commentCollection);
			
			//if(id_message.compareTo("") != 0)
			//	LikeTools.addLikeComment(id_message,id_user,login,commentCollection);
		
			return ErrorJSON.serviceAccepted("You have successfully added your like");
			
		} catch(JSONException e) {
			e.printStackTrace();
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return ErrorJSON.serviceRefused("Problem occured in addLike Service", -1);
	}

	
	public static JSONObject removeLike(String key_session,String id_message, String id_like) {
		
		if(key_session==null || id_like==null || id_message==null  )
			return ErrorJSON.serviceRefused("Missing parameter",-2);
		
		try {
			//connect to mysql
			Connection connection = Database.getMySQLConnection();
			
			if(!SessionTools.isConnectedByKey(key_session,connection)) 
				return ErrorJSON.serviceRefused("Not connected",-3);	
			
			if(SessionTools.hasExceededTimeOut(key_session, connection)) {
				SessionTools.removeSession(key_session,connection);
				return ErrorJSON.serviceRefused("TimeOut exceeded, disconnected automatically", 1);
			}
			
			//ajout de 30min dans session_fin
			SessionTools.updateTimeOut(key_session, connection);
		
			MongoDatabase mongoDatabase=Database.getMongoDBConnection();
			MongoCollection<Document> commentCollection = mongoDatabase.getCollection("message");
			
			
			if(!TwistTools.twistExists(id_message,commentCollection))
				return ErrorJSON.serviceRefused("No message to remove like", 2);
			
			
			String id_user = SessionTools.getIdUser(key_session, connection);
			
			if(!LikeTools.checkAuthor(id_message,id_user, commentCollection))
				return ErrorJSON.serviceRefused("No like to delete it", 2);
			
			LikeTools.removeLike(id_message,id_like,commentCollection);
			
			return ErrorJSON.serviceAccepted("You have successfully removed your like");
			
		}catch(SQLException e) {
			e.printStackTrace();
			
		}
		return ErrorJSON.serviceRefused("Problem occured in RemoveLike Service", -1);
	}

	
	public static JSONObject listLike(String key_session, String id_message) {
		
		if(key_session==null || id_message==null  )
			return ErrorJSON.serviceRefused("Missing parameter",-2);
		
		try {
			//connect to mysql
			Connection connection = Database.getMySQLConnection();
			
			if(!SessionTools.isConnectedByKey(key_session,connection)) 
				return ErrorJSON.serviceRefused("Not connected",-3);
			
			if(SessionTools.hasExceededTimeOut(key_session, connection)) {
				SessionTools.removeSession(key_session,connection);
				return ErrorJSON.serviceRefused("TimeOut exceeded, disconnected automatically", 1);
			}
			
			//ajout de 30min dans session_fin
			SessionTools.updateTimeOut(key_session, connection);
		
			MongoDatabase mongoDatabase=Database.getMongoDBConnection();
			MongoCollection<Document> commentCollection = mongoDatabase.getCollection("message");
			
			if(!TwistTools.twistExists(id_message,commentCollection))
				return ErrorJSON.serviceRefused("No message", 2);
			
			
			return LikeTools.listLike(id_message,commentCollection);
		
		}catch(JSONException e) {
			e.printStackTrace();
			
		}catch(SQLException e) {
			e.printStackTrace();
			
		}
		return ErrorJSON.serviceRefused("Problem occured in listLike Service", -1);
	}

}
