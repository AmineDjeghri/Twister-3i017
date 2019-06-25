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
import tools.ErrorTools;
import tools.TwistTools;
import tools.SessionTools;
import tools.UserTools;

public class CommentS {

	public static JSONObject addComment(String key_session,String id_message, String comment) {
		
		if(key_session==null || comment==null || id_message==null )
			return ErrorJSON.serviceRefused(ErrorTools.MESSAGE_MISSING_PARAMETERS,ErrorTools.CODE_MISSING_PARAMETERS);
		
		Connection connection=null;
		
		try {
			//connect to mysql
			connection = Database.getMySQLConnection();
			
			if(!SessionTools.isConnectedByKey(key_session,connection)) 
				return ErrorJSON.serviceRefused(ErrorTools.MESSAGE_USER_NOT_CONNECTED, ErrorTools.CODE_USER_NOT_CONNECTED);	
			
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
			
			String firstname = UserTools.getFirstName(id_user, connection);
			String familyname = UserTools.getFamilyName(id_user, connection);
			
			
			CommentTools.addComment(id_message,firstname, familyname, comment,id_user,login,commentCollection);
			
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
		return ErrorJSON.serviceAccepted("You have successfully added your comment");
	}

	
	public static JSONObject removeComment(String key_session,String id_message, String id_comment) {
		
		if(key_session==null || id_comment==null || id_message==null  )
			return ErrorJSON.serviceRefused(ErrorTools.MESSAGE_MISSING_PARAMETERS,ErrorTools.CODE_MISSING_PARAMETERS);
		Connection connection=null;
		
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
			
			if(!CommentTools.commentExists(id_message, id_comment,commentCollection))
				return ErrorJSON.serviceRefused(ErrorTools.MESSAGE_COMMENT_DOES_NOT_EXIST, ErrorTools.CODE_COMMENT_DOES_NOT_EXIST);
			
			String id_user = SessionTools.getIdUser(key_session, connection);
			
			if(!CommentTools.checkAuthor(id_user,id_message, id_comment, commentCollection)) {
				return ErrorJSON.serviceRefused(ErrorTools.MESSAGE_NOT_AUTHOR,ErrorTools.CODE_NOT_AUTHOR);	
			} 
		
			CommentTools.removeComment(id_message,id_comment,commentCollection);
		
			return ErrorJSON.serviceAccepted("You have successfully removed a comment");
			
		}catch(SQLException e) {
			return ErrorJSON.serviceRefused(ErrorTools.MESSAGE_ERROR_SQL, ErrorTools.CODE_ERROR_SQL);
			
		}catch(JSONException e) {
			return ErrorJSON.serviceRefused(ErrorTools.MESSAGE_ERROR_JSON, ErrorTools.CODE_ERROR_JSON);
		}finally {
			if(connection!=null)
				try {
					connection.close();
				} catch (SQLException e) {
					return ErrorJSON.serviceRefused(ErrorTools.MESSAGE_ERROR_SQL, ErrorTools.CODE_ERROR_SQL);
				}
		}
	}

	
	public static JSONObject listComment(String key_session, String id_message) {
		
		if(key_session==null || id_message==null  )
			return ErrorJSON.serviceRefused(ErrorTools.MESSAGE_MISSING_PARAMETERS,ErrorTools.CODE_MISSING_PARAMETERS);
		
		
		Connection connection=null;
		
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
			
			//connect to mongodb
			
			return CommentTools.listComment(id_message,commentCollection);
		
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


	public static JSONObject EditComment(String key_session, String id_message, String id_comment, String comment) {
		
		if(key_session==null || comment==null || id_message==null || id_comment==null)
			return ErrorJSON.serviceRefused(ErrorTools.MESSAGE_MISSING_PARAMETERS,ErrorTools.CODE_MISSING_PARAMETERS);
		
		Connection connection=null;
		try {
			//connect to mysql
			 connection = Database.getMySQLConnection();
			
			if(!SessionTools.isConnectedByKey(key_session,connection)) 
				return ErrorJSON.serviceRefused(ErrorTools.MESSAGE_USER_NOT_CONNECTED, ErrorTools.CODE_USER_NOT_CONNECTED);	
			
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
			
			if(!CommentTools.commentExists(id_message,id_comment,commentCollection))
				return ErrorJSON.serviceRefused(ErrorTools.MESSAGE_COMMENT_DOES_NOT_EXIST, ErrorTools.CODE_MAIL_DOES_NOT_EXIST);
			
			if(!CommentTools.checkAuthor(id_user,id_message, id_comment, commentCollection)) {
				return ErrorJSON.serviceRefused(ErrorTools.MESSAGE_NOT_AUTHOR,ErrorTools.CODE_NOT_AUTHOR);	
			} 
			
			CommentTools.editComment(id_message,id_comment,comment,commentCollection);
		

			
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
		return ErrorJSON.serviceAccepted("You have successfully edited your comment");
	}

}
