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
import tools.CommentTools;
import tools.ErrorJSON;
import tools.ErrorTools;
import tools.SessionTools;
import tools.TwistTools;
import tools.UserTools;

public class LikeS {

	public static JSONObject addLike(String key_session,String id_message) {
		
		if(key_session==null || id_message==null )
			return ErrorJSON.serviceRefused(ErrorTools.MESSAGE_MISSING_PARAMETERS,ErrorTools.CODE_MISSING_PARAMETERS);
		Connection connection = null;
		try {
			//connect to mysql
			connection = Database.getMySQLConnection();
			
			if(!SessionTools.isConnectedByKey(key_session,connection)) 
				return ErrorJSON.serviceRefused(ErrorTools.MESSAGE_USER_NOT_CONNECTED,ErrorTools.CODE_USER_NOT_CONNECTED);	
			
			if(SessionTools.hasExceededTimeOut(key_session, connection)) {
				SessionTools.removeSession(key_session,connection);
				return ErrorJSON.serviceRefused(ErrorTools.MESSAGE_USER_DISCONNECTED_AUTOMATICALLY, ErrorTools.CODE_USER_DISCONNECTED_AUTOMATICALLY);
			}
			
			//ajout de 30min dans session_fin
			SessionTools.updateTimeOut(key_session, connection);
		
			String id_user = SessionTools.getIdUser(key_session, connection);
			String login = UserTools.getLogin(id_user, connection);
			
			MongoDatabase mongoDatabase=Database.getMongoDBConnection();
			MongoCollection<Document> commentCollection = mongoDatabase.getCollection("message");
			
			if(!TwistTools.twistExists(id_message,commentCollection))
				return ErrorJSON.serviceRefused(ErrorTools.MESSAGE_TWIST_DOES_NOT_EXIST, ErrorTools.CODE_TWIST_DOES_NOT_EXIST);
			
			if(LikeTools.checkAuthor(id_message,id_user, commentCollection))
				return  ErrorJSON.serviceRefused(ErrorTools.MESSAGE_ALREADY_LIKED, ErrorTools.CODE_ALREADY_LIKED);
			
			String firstname = UserTools.getFirstName(id_user, connection);
			String familyname = UserTools.getFamilyName(id_user, connection);
			
			LikeTools.addLike(id_message, id_user, firstname, familyname, login, commentCollection);
			
		} catch(JSONException e) {
			return ErrorJSON.serviceRefused(ErrorTools.MESSAGE_ERROR_JSON, ErrorTools.CODE_ERROR_JSON);
		}catch(SQLException e) {
			return ErrorJSON.serviceRefused(ErrorTools.MESSAGE_ERROR_SQL, ErrorTools.CODE_ERROR_SQL);
			
		}finally {
			if(connection!=null)
				try {
					connection.close();
				} catch (SQLException e) {
					return ErrorJSON.serviceRefused(ErrorTools.MESSAGE_ERROR_SQL, ErrorTools.CODE_ERROR_SQL);
				}
		}
		return ErrorJSON.serviceAccepted("You have successfully added your like");
	}

	
	public static JSONObject removeLike(String key_session,String id_message, String id_like) {
		
		if(key_session==null || id_like==null || id_message==null  )
			return ErrorJSON.serviceRefused(ErrorTools.MESSAGE_MISSING_PARAMETERS,ErrorTools.CODE_MISSING_PARAMETERS);
		
		Connection connection = null;
		try {
			//connect to mysql
			connection = Database.getMySQLConnection();
			
			if(!SessionTools.isConnectedByKey(key_session,connection)) 
				return ErrorJSON.serviceRefused(ErrorTools.MESSAGE_USER_NOT_CONNECTED,ErrorTools.CODE_USER_NOT_CONNECTED);	
			
			if(SessionTools.hasExceededTimeOut(key_session, connection)) {
				SessionTools.removeSession(key_session,connection);
				return ErrorJSON.serviceRefused(ErrorTools.MESSAGE_USER_DISCONNECTED_AUTOMATICALLY, ErrorTools.CODE_USER_DISCONNECTED_AUTOMATICALLY);
			}
			
			//ajout de 30min dans session_fin
			SessionTools.updateTimeOut(key_session, connection);
		
			MongoDatabase mongoDatabase=Database.getMongoDBConnection();
			MongoCollection<Document> commentCollection = mongoDatabase.getCollection("message");
			
			
			if(!TwistTools.twistExists(id_message,commentCollection))
				return ErrorJSON.serviceRefused(ErrorTools.MESSAGE_TWIST_DOES_NOT_EXIST, ErrorTools.CODE_TWIST_DOES_NOT_EXIST);
			
			if(!LikeTools.likeExists(id_message,id_like,commentCollection))
				return ErrorJSON.serviceRefused(ErrorTools.MESSAGE_LIKE_DOES_NOT_EXIST, ErrorTools.CODE_LIKE_DOES_NOT_EXIST);
			
			
			String id_user = SessionTools.getIdUser(key_session, connection);
			
			if(!LikeTools.checkAuthor(id_message,id_user, commentCollection))
				return ErrorJSON.serviceRefused(ErrorTools.MESSAGE_CAN_NOT_EDIT, ErrorTools.CODE_CAN_NOT_EDIT);
			
			LikeTools.removeLike(id_message,id_like,commentCollection);
			
		}catch(SQLException e) {
			return ErrorJSON.serviceRefused(ErrorTools.MESSAGE_ERROR_SQL, ErrorTools.CODE_ERROR_SQL);
		}finally {
			if(connection!=null)
				try {
					connection.close();
				} catch (SQLException e) {
					return ErrorJSON.serviceRefused(ErrorTools.MESSAGE_ERROR_SQL, ErrorTools.CODE_ERROR_SQL);
				}
		}
		return ErrorJSON.serviceAccepted("You have successfully removed your like");
	}

	
	public static JSONObject listLike(String key_session, String id_message) {
		
		if(key_session==null || id_message==null  )
			return ErrorJSON.serviceRefused(ErrorTools.MESSAGE_MISSING_PARAMETERS,ErrorTools.CODE_MISSING_PARAMETERS);
		
		Connection connection = null;
		try {
			//connect to mysql
			connection = Database.getMySQLConnection();
			
			if(!SessionTools.isConnectedByKey(key_session,connection)) 
				return ErrorJSON.serviceRefused(ErrorTools.MESSAGE_USER_NOT_CONNECTED,ErrorTools.CODE_USER_NOT_CONNECTED);
			
			if(SessionTools.hasExceededTimeOut(key_session, connection)) {
				SessionTools.removeSession(key_session,connection);
				return ErrorJSON.serviceRefused(ErrorTools.MESSAGE_USER_DISCONNECTED_AUTOMATICALLY, ErrorTools.CODE_USER_DISCONNECTED_AUTOMATICALLY);
			}
			
			//ajout de 30min dans session_fin
			SessionTools.updateTimeOut(key_session, connection);
		
			MongoDatabase mongoDatabase=Database.getMongoDBConnection();
			MongoCollection<Document> commentCollection = mongoDatabase.getCollection("message");
			
			if(!TwistTools.twistExists(id_message,commentCollection))
				return ErrorJSON.serviceRefused(ErrorTools.MESSAGE_TWIST_DOES_NOT_EXIST, ErrorTools.CODE_TWIST_DOES_NOT_EXIST);
			
			return LikeTools.listLike(id_message,commentCollection);
		
		}catch(JSONException e) {
			return ErrorJSON.serviceRefused(ErrorTools.MESSAGE_ERROR_JSON, ErrorTools.CODE_ERROR_JSON);
			
		}catch(SQLException e) {
			return ErrorJSON.serviceRefused(ErrorTools.MESSAGE_ERROR_SQL, ErrorTools.CODE_ERROR_SQL);
			
		}finally {
			if(connection!=null)
				try {
					connection.close();
				} catch (SQLException e) {
					return ErrorJSON.serviceRefused(ErrorTools.MESSAGE_ERROR_SQL, ErrorTools.CODE_ERROR_SQL);
				}
		}
	}

}
