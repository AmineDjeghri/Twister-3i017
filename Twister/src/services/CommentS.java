package services;

import java.sql.Connection;
import java.sql.SQLException;

import org.bson.Document;
import org.json.JSONException;
import org.json.JSONObject;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import db.Database;
import tools.CommentTools;
import tools.ErrorJSON;
import tools.TwistTools;
import tools.SessionTools;
import tools.UserTools;

public class CommentS {

	public static JSONObject addComment(String key_session,String id_message, String comment) {
		
		if(key_session==null || comment==null || id_message==null )
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
				return ErrorJSON.serviceRefused("No message to add comment", 2);
			
			
			CommentTools.addComment(id_message,comment,id_user,login,commentCollection);
		
			return ErrorJSON.serviceAccepted("You have successfully added your comment");
			
		} catch(JSONException e) {
			e.printStackTrace();
		}catch(SQLException e) {
			e.printStackTrace();
			
		}
		return ErrorJSON.serviceRefused("Problem occured in addComment Service", -1);
	}

	
	public static JSONObject removeComment(String key_session,String id_message, String id_comment) {
		
		if(key_session==null || id_comment==null || id_message==null  )
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
				return ErrorJSON.serviceRefused("No message to remove comment", 2);
			
			if(!CommentTools.commentExists(id_message, id_comment,commentCollection))
				return ErrorJSON.serviceRefused("No comment", 2);
			
			String id_user = SessionTools.getIdUser(key_session, connection);
			
			if(!CommentTools.checkAuthor(id_user,id_message, id_comment, commentCollection)) {
				return ErrorJSON.serviceRefused("you are not the author of this comment",1);	
			} 
		
			CommentTools.removeComment(id_message,id_comment,commentCollection);
		
			return ErrorJSON.serviceAccepted("You have successfully removed a comment");
			
		}catch(SQLException e) {
			e.printStackTrace();
			
		}catch(JSONException e) {
			e.printStackTrace();
		}
		return ErrorJSON.serviceRefused("Problem occured in RemoveComment Service", -1);
	}

	
	public static JSONObject listComment(String key_session, String id_message) {
		
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
			
			//connect to mongodb
			
			return CommentTools.listComment(id_message,commentCollection);
		
		}catch(JSONException e) {
			e.printStackTrace();
			
		}catch(SQLException e) {
			e.printStackTrace();
			
		}
		return ErrorJSON.serviceRefused("Problem occured in listComment Service", -1);
	}


	public static JSONObject EditComment(String key_session, String id_message, String id_comment, String comment) {
		if(key_session==null || comment==null || id_message==null || id_comment==null)
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
				return ErrorJSON.serviceRefused("No message to edit comment", 2);
			
			if(!CommentTools.commentExists(id_message,id_comment,commentCollection))
				return ErrorJSON.serviceRefused("No comment to edit it", 2);
			
			if(!CommentTools.checkAuthor(id_user,id_message, id_comment, commentCollection)) {
				return ErrorJSON.serviceRefused("you are not the author of this comment",1);	
			} 
			
			CommentTools.editComment(id_message,id_comment,comment,commentCollection);
		
			return ErrorJSON.serviceAccepted("You have successfully edited your comment");
			
		} catch(JSONException e) {
			e.printStackTrace();
		}catch(SQLException e) {
			e.printStackTrace();
			
		}
		return ErrorJSON.serviceRefused("Problem occured in addComment Service", -1);
	}

}
