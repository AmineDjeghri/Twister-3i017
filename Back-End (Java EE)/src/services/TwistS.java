package services;

import java.sql.Connection;

import java.sql.SQLException;
import java.util.ArrayList;

import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mongodb.DBCollection;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.util.JSON;

import db.Database;
import tools.ErrorJSON;
import tools.ErrorTools;
import tools.FollowerTools;
import tools.MapReduceTools;
import tools.TwistTools;
import tools.SessionTools;
import tools.UserTools;

public class TwistS {

	public static JSONObject addTwist(String key_session, String message) {
		
		if(key_session==null || message==null  )
			return ErrorJSON.serviceRefused(ErrorTools.MESSAGE_MISSING_PARAMETERS,ErrorTools.CODE_MISSING_PARAMETERS);
		Connection connection=null;
		
		try {
			//connect to mysql
			 connection = Database.getMySQLConnection();
			
			if(!SessionTools.isConnectedByKey(key_session,connection)) {
				return ErrorJSON.serviceRefused(ErrorTools.MESSAGE_USER_NOT_CONNECTED,ErrorTools.CODE_USER_NOT_CONNECTED);	
			} 
			
			if(SessionTools.hasExceededTimeOut(key_session, connection)) {
				SessionTools.removeSession(key_session,connection);
				return ErrorJSON.serviceRefused(ErrorTools.MESSAGE_USER_DISCONNECTED_AUTOMATICALLY, ErrorTools.CODE_USER_DISCONNECTED_AUTOMATICALLY);
			}
			
			//ajout de 30min dans session_fin
			SessionTools.updateTimeOut(key_session, connection);
		
			String id_user = SessionTools.getIdUser(key_session, connection);
			String login = UserTools.getLogin(id_user, connection);
			
			String firstname = UserTools.getFirstName(id_user, connection);
			String familyname = UserTools.getFamilyName(id_user, connection);
			//connect to mongodb
			
			MongoDatabase mongoDatabase=Database.getMongoDBConnection();
			MongoCollection<Document> commentCollection = mongoDatabase.getCollection("message");
			
			TwistTools.addTwist(id_user,login, firstname, familyname, message,commentCollection);
			
		}catch(SQLException e) {
			return ErrorJSON.serviceRefused(ErrorTools.MESSAGE_ERROR_SQL, ErrorTools.CODE_ERROR_SQL);
			
		}
		finally {
			if(connection!=null)
				try {
					connection.close();
				} catch (SQLException e) {
					return ErrorJSON.serviceRefused(ErrorTools.MESSAGE_ERROR_SQL, ErrorTools.CODE_ERROR_SQL);
				}
		}
		return ErrorJSON.serviceAccepted("You have successfully added your message");
	}
	
	
	
	public static JSONObject removeTwist(String key_session, String id_message) {
		
		if(key_session==null || id_message==null  )
			return ErrorJSON.serviceRefused(ErrorTools.MESSAGE_MISSING_PARAMETERS,ErrorTools.CODE_MISSING_PARAMETERS);
		Connection connection=null;
		
		try {
			//connect to mysql
			 connection = Database.getMySQLConnection();
			
			String id_user=SessionTools.getIdUser(key_session,connection);
			
			if(!SessionTools.isConnectedByKey(key_session,connection)) {
				return ErrorJSON.serviceRefused(ErrorTools.MESSAGE_USER_NOT_CONNECTED,ErrorTools.CODE_USER_NOT_CONNECTED);	
			} 
			
			if(SessionTools.hasExceededTimeOut(key_session, connection)) {
				SessionTools.removeSession(key_session,connection);
				return ErrorJSON.serviceRefused(ErrorTools.MESSAGE_USER_DISCONNECTED_AUTOMATICALLY, ErrorTools.CODE_USER_DISCONNECTED_AUTOMATICALLY);
			}
			
			//ajout de 30min dans session_fin
			SessionTools.updateTimeOut(key_session, connection);
		
			MongoDatabase mongoDatabase=Database.getMongoDBConnection();
			MongoCollection<Document> commentCollection = mongoDatabase.getCollection("message");
		
			if(!TwistTools.twistExists(id_message, commentCollection)) {
				return ErrorJSON.serviceRefused(ErrorTools.MESSAGE_TWIST_DOES_NOT_EXIST,ErrorTools.CODE_TWIST_DOES_NOT_EXIST);
			}
			
			if(!TwistTools.checkAuthor(id_user, id_message, commentCollection)) {
				return ErrorJSON.serviceRefused(ErrorTools.MESSAGE_NOT_AUTHOR,ErrorTools.CODE_NOT_AUTHOR);	
			} 
			TwistTools.removeTwist(id_user, id_message,commentCollection);
			
		}catch(SQLException e) {
			return ErrorJSON.serviceRefused(ErrorTools.MESSAGE_ERROR_SQL, ErrorTools.CODE_ERROR_SQL);
		}
		finally {
			if(connection!=null)
				try {
					connection.close();
				} catch (SQLException e) {
					return ErrorJSON.serviceRefused(ErrorTools.MESSAGE_ERROR_SQL, ErrorTools.CODE_ERROR_SQL);
				}
		}
		return ErrorJSON.serviceAccepted("You have successfully removed your message");
	}



	public static JSONObject listTwists(String key_session, String id_user) {
		
		if(key_session==null)
			return ErrorJSON.serviceRefused(ErrorTools.MESSAGE_MISSING_PARAMETERS,ErrorTools.CODE_MISSING_PARAMETERS);
		Connection connection =null;
		
		try {
			//connect to mysql
			 connection = Database.getMySQLConnection();

			if(!SessionTools.isConnectedByKey(key_session,connection)) {
				return ErrorJSON.serviceRefused(ErrorTools.MESSAGE_USER_NOT_CONNECTED,ErrorTools.CODE_USER_NOT_CONNECTED);	
			} 
			
			if(SessionTools.hasExceededTimeOut(key_session, connection)) {
				SessionTools.removeSession(key_session,connection);
				return ErrorJSON.serviceRefused(ErrorTools.MESSAGE_USER_DISCONNECTED_AUTOMATICALLY, ErrorTools.CODE_USER_DISCONNECTED_AUTOMATICALLY);
			}
			//ajout de 30min dans session_fin
			SessionTools.updateTimeOut(key_session, connection);
			
			MongoDatabase mongoDatabase=Database.getMongoDBConnection();
			MongoCollection<Document> meessageCollection = mongoDatabase.getCollection("message");

			if(id_user == null)
				return TwistTools.listTwists(meessageCollection);
			
			String login = UserTools.getLogin(id_user, connection);
			
			if(!UserTools.userExists(login, connection)) {
				return ErrorJSON.serviceRefused(ErrorTools.MESSAGE_USER_DOES_NOT_EXIST,ErrorTools.CODE_USER_DOES_NOT_EXIST);
			} 
			
			return TwistTools.listTwists(id_user, meessageCollection);	
			
		}catch(JSONException e) {
			return ErrorJSON.serviceRefused(ErrorTools.MESSAGE_ERROR_JSON, ErrorTools.CODE_ERROR_JSON);
			
		}catch(SQLException e) {
			return ErrorJSON.serviceRefused(ErrorTools.MESSAGE_ERROR_SQL, ErrorTools.CODE_ERROR_SQL);
			
		}	finally {
				if(connection!=null)
					try {
						connection.close();
					} catch (SQLException e) {
						return ErrorJSON.serviceRefused(ErrorTools.MESSAGE_ERROR_SQL, ErrorTools.CODE_ERROR_SQL);
					}
			}	
	}

	
public static JSONObject wallTwists(String key_session) {
		
	if(key_session==null)
		return ErrorJSON.serviceRefused(ErrorTools.MESSAGE_MISSING_PARAMETERS,ErrorTools.CODE_MISSING_PARAMETERS);
	
	Connection connection=null;
	try {
		//connect to mysql
		 connection = Database.getMySQLConnection();

		if(!SessionTools.isConnectedByKey(key_session,connection)) {
			return ErrorJSON.serviceRefused(ErrorTools.MESSAGE_USER_NOT_CONNECTED,ErrorTools.CODE_USER_NOT_CONNECTED);	
		} 
		
		if(SessionTools.hasExceededTimeOut(key_session, connection)) {
			SessionTools.removeSession(key_session,connection);
			return ErrorJSON.serviceRefused(ErrorTools.MESSAGE_USER_DISCONNECTED_AUTOMATICALLY, ErrorTools.CODE_USER_DISCONNECTED_AUTOMATICALLY);
		}
		//ajout de 30min dans session_fin
		SessionTools.updateTimeOut(key_session, connection);
		
		MongoDatabase mongoDatabase=Database.getMongoDBConnection();
		MongoCollection<Document> meessageCollection = mongoDatabase.getCollection("message");


		//user id + friends ids
		String id_user=SessionTools.getIdUser(key_session,connection);
		System.out.println(id_user);
		
		ArrayList<String> ids = new ArrayList<String>();
		ids.add(id_user);
		
		JSONArray friends = FollowerTools.listFollowing(id_user, connection).getJSONArray("following");
		
		JSONObject friend=new JSONObject();
		for(int i=0;i<friends.length();i++) {
			friend=friends.getJSONObject(i);
			ids.add(friend.getString("id_following"));
		}
		
		ArrayList<JSONObject> twists = new ArrayList<>();
		for(String id:ids) {	
		JSONArray user_twists =TwistTools.listTwists(id, meessageCollection).getJSONArray("twists");
				for(int i=0;i<user_twists.length();i++) {
					twists.add(user_twists.getJSONObject(i));
				}
		}
		return new JSONObject().put("twists", twists);
		
	}catch(JSONException e) {
		e.printStackTrace();
		return ErrorJSON.serviceRefused(ErrorTools.MESSAGE_ERROR_JSON, ErrorTools.CODE_ERROR_JSON);
	}catch(SQLException e) {
		return ErrorJSON.serviceRefused(ErrorTools.MESSAGE_ERROR_SQL, ErrorTools.CODE_ERROR_SQL);
	}
	finally {
		if(connection!=null)
			try {
				connection.close();
			} catch (SQLException e) {
				return ErrorJSON.serviceRefused(ErrorTools.MESSAGE_ERROR_SQL, ErrorTools.CODE_ERROR_SQL);
			}
	}

}


	/** À completer */
	public static JSONObject search(String key_session, String query, String friends) {
		if(key_session==null || query==null  || friends==null )
			return ErrorJSON.serviceRefused(ErrorTools.MESSAGE_MISSING_PARAMETERS,ErrorTools.CODE_MISSING_PARAMETERS);
		
		Connection connection = null;
		try {
			//connect to mysql
			connection = Database.getMySQLConnection();

			if(!SessionTools.isConnectedByKey(key_session,connection)) {
				return ErrorJSON.serviceRefused(ErrorTools.MESSAGE_USER_NOT_CONNECTED,ErrorTools.CODE_USER_NOT_CONNECTED);	
			} 
			
			if(SessionTools.hasExceededTimeOut(key_session, connection)) {
				SessionTools.removeSession(key_session,connection);
				return ErrorJSON.serviceRefused(ErrorTools.MESSAGE_USER_DISCONNECTED_AUTOMATICALLY, ErrorTools.CODE_USER_DISCONNECTED_AUTOMATICALLY);
			}
			//ajout de 30min dans session_fin
			SessionTools.updateTimeOut(key_session, connection);
			
			MongoDatabase mongoDatabase=Database.getMongoDBConnection();
			MongoCollection<Document> meessageCollection = mongoDatabase.getCollection("message");
			//return MapReduceTools.search(query,messageCollection);
			return null;
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



	public static JSONObject ReTwist(String key_session, String id_message, String message) {
		
		if(key_session==null || id_message==null  )
			return ErrorJSON.serviceRefused(ErrorTools.MESSAGE_MISSING_PARAMETERS,ErrorTools.CODE_MISSING_PARAMETERS);
		
		Connection connection = null;
		try {
			//connect to mysql
			connection = Database.getMySQLConnection();

			String id_user=SessionTools.getIdUser(key_session,connection);
			
			if(!SessionTools.isConnectedByKey(key_session,connection)) {
				return ErrorJSON.serviceRefused(ErrorTools.MESSAGE_USER_NOT_CONNECTED,ErrorTools.CODE_USER_NOT_CONNECTED);	
			} 
			
			if(SessionTools.hasExceededTimeOut(key_session, connection)) {
				SessionTools.removeSession(key_session,connection);
				return ErrorJSON.serviceRefused(ErrorTools.MESSAGE_USER_DISCONNECTED_AUTOMATICALLY, ErrorTools.CODE_USER_DISCONNECTED_AUTOMATICALLY);
			}
			
			//ajout de 30min dans session_fin
			SessionTools.updateTimeOut(key_session, connection);
		
			//connect to mongodb
			MongoDatabase mongoDatabase=Database.getMongoDBConnection();
			MongoCollection<Document> commentCollection = mongoDatabase.getCollection("message");
			
			if(!TwistTools.twistExists(id_message,commentCollection))
				return ErrorJSON.serviceRefused(ErrorTools.MESSAGE_TWIST_DOES_NOT_EXIST, ErrorTools.CODE_TWIST_DOES_NOT_EXIST);
			
			TwistTools.reTwist(id_message,id_user,message,commentCollection);
		

			
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
		return ErrorJSON.serviceAccepted("You have successfully added your message");
	}



	public static JSONObject editTwist(String key_session, String id_message, String message) {
		if(key_session==null || message==null || id_message==null  )
			return ErrorJSON.serviceRefused(ErrorTools.MESSAGE_MISSING_PARAMETERS,ErrorTools.CODE_MISSING_PARAMETERS);
		
		Connection connection = null;
		try {
			//connect to mysql
			connection = Database.getMySQLConnection();
			String id_user=SessionTools.getIdUser(key_session,connection);
			
			if(!SessionTools.isConnectedByKey(key_session,connection)) {
				return ErrorJSON.serviceRefused(ErrorTools.MESSAGE_USER_NOT_CONNECTED,ErrorTools.CODE_USER_NOT_CONNECTED);	
			} 
			
			if(SessionTools.hasExceededTimeOut(key_session, connection)) {
				SessionTools.removeSession(key_session,connection);
				return ErrorJSON.serviceRefused(ErrorTools.MESSAGE_USER_DISCONNECTED_AUTOMATICALLY, ErrorTools.CODE_USER_DISCONNECTED_AUTOMATICALLY);
			}
			
			//ajout de 30min dans session_fin
			SessionTools.updateTimeOut(key_session, connection);
		
			//connect to mongodb
			
			MongoDatabase mongoDatabase=Database.getMongoDBConnection();
			MongoCollection<Document> commentCollection = mongoDatabase.getCollection("message");
			
			
			if(!TwistTools.twistExists(id_message, commentCollection)) {
				return ErrorJSON.serviceRefused(ErrorTools.MESSAGE_TWIST_DOES_NOT_EXIST,ErrorTools.CODE_TWIST_DOES_NOT_EXIST);
			}
			
			if(!TwistTools.checkAuthor(id_user, id_message, commentCollection)) {
				return ErrorJSON.serviceRefused(ErrorTools.MESSAGE_NOT_AUTHOR,ErrorTools.CODE_NOT_AUTHOR);	
			} 
			
			TwistTools.editTwist(id_message,message,commentCollection);
	
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
		return ErrorJSON.serviceAccepted("You have successfully edited your message");
	}
	
}
