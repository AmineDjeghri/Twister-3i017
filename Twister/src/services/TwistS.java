package services;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.util.JSON;

import db.Database;
import tools.ErrorJSON;
import tools.FollowerTools;
import tools.TwistTools;
import tools.SessionTools;
import tools.UserTools;

public class TwistS {

	public static JSONObject addTwist(String key_session, String message) {
		
		if(key_session==null || message==null  )
			return ErrorJSON.serviceRefused("Missing parameter",-1);
		Connection connection=null;
		
		try {
			//connect to mysql
			 connection = Database.getMySQLConnection();

			
			if(!SessionTools.isConnectedByKey(key_session,connection)) {
				return ErrorJSON.serviceRefused("Not connected",-3);	
			} 
			
			if(SessionTools.hasExceededTimeOut(key_session, connection)) {
				SessionTools.removeSession(key_session,connection);
				return ErrorJSON.serviceRefused("TimeOut exceeded, disconnected automatically", 1);
			}
			
			//ajout de 30min dans session_fin
			SessionTools.updateTimeOut(key_session, connection);
		
			String id_user = SessionTools.getIdUser(key_session, connection);
			String login = UserTools.getLogin(id_user, connection);
			
			//connect to mongodb
			
			MongoDatabase mongoDatabase=Database.getMongoDBConnection();
			MongoCollection<Document> commentCollection = mongoDatabase.getCollection("message");
			TwistTools.addTwist(id_user,login,message,commentCollection);
		
			return ErrorJSON.serviceAccepted("You have successfully added your message");
			
		}catch(SQLException e) {
			e.printStackTrace();
			
		}
		finally {
			if(connection!=null)
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
		return ErrorJSON.serviceRefused("Problem occured in addMessage Service", -1);
	}
	
	
	
	public static JSONObject removeTwist(String key_session, String id_message) {
		
		if(key_session==null || id_message==null  )
			return ErrorJSON.serviceRefused("missing parameter",-1);
		Connection connection=null;
		
		try {
			//connect to mysql
			 connection = Database.getMySQLConnection();
			
			String id_user=SessionTools.getIdUser(key_session,connection);
			
			if(!SessionTools.isConnectedByKey(key_session,connection)) {
				return ErrorJSON.serviceRefused("Not connected",-3);	
			} 
			
			if(SessionTools.hasExceededTimeOut(key_session, connection)) {
				SessionTools.removeSession(key_session,connection);
				return ErrorJSON.serviceRefused("TimeOut exceeded, disconnected automatically", 1);
			}
			
			//ajout de 30min dans session_fin
			SessionTools.updateTimeOut(key_session, connection);
		
			MongoDatabase mongoDatabase=Database.getMongoDBConnection();
			MongoCollection<Document> commentCollection = mongoDatabase.getCollection("message");
		
			if(!TwistTools.twistExists(id_message, commentCollection)) {
				return ErrorJSON.serviceRefused("Message doesn't exist",1);
			}
			
			if(!TwistTools.checkAuthor(id_user, id_message, commentCollection)) {
				return ErrorJSON.serviceRefused("you are not the author of this message",1);	
			} 
		
			TwistTools.removeTwist(id_user, id_message,commentCollection);
		
			return ErrorJSON.serviceAccepted("You have successfully removed your message");
			
		}catch(SQLException e) {
			e.printStackTrace();
			
		}
		finally {
			if(connection!=null)
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
		return ErrorJSON.serviceRefused("Problem occured in removeMessage Service", -1);

	}



	public static JSONObject listTwists(String key_session, String id_user) {
		
		if(key_session==null)
			return ErrorJSON.serviceRefused("missing parameter",-1);
		Connection connection =null;
		
		try {
			//connect to mysql
			 connection = Database.getMySQLConnection();

			if(!SessionTools.isConnectedByKey(key_session,connection)) {
				return ErrorJSON.serviceRefused("Not connected",-3);	
			} 
			
			if(SessionTools.hasExceededTimeOut(key_session, connection)) {
				SessionTools.removeSession(key_session,connection);
				return ErrorJSON.serviceRefused("TimeOut exceeded, disconnected automatically", 1);
			}
			//ajout de 30min dans session_fin
			SessionTools.updateTimeOut(key_session, connection);
			
			MongoDatabase mongoDatabase=Database.getMongoDBConnection();
			MongoCollection<Document> meessageCollection = mongoDatabase.getCollection("message");

			if(id_user == null)
				return TwistTools.listTwists(meessageCollection);
			
			String login = UserTools.getLogin(id_user, connection);
			
			if(!UserTools.userExists(login, connection)) {
				return ErrorJSON.serviceRefused("User don't exists",1);
			} 
			
			return TwistTools.listMessages(id_user, meessageCollection);	
			
		}catch(JSONException e) {
			e.printStackTrace();
		}catch(SQLException e) {
			e.printStackTrace();
		}	finally {
				if(connection!=null)
					try {
						connection.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
			}	
		
		return ErrorJSON.serviceRefused("Problem occured in listMessages Service", -1);
	}

	
public static JSONObject wallTwists(String key_session) {
		
	if(key_session==null)
		return ErrorJSON.serviceRefused("missing parameter",-1);
	
	Connection connection=null;
	try {
		//connect to mysql
		 connection = Database.getMySQLConnection();

		if(!SessionTools.isConnectedByKey(key_session,connection)) {
			return ErrorJSON.serviceRefused("Not connected",-3);	
		} 
		
		if(SessionTools.hasExceededTimeOut(key_session, connection)) {
			SessionTools.removeSession(key_session,connection);
			return ErrorJSON.serviceRefused("TimeOut exceeded, disconnected automatically", 1);
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
		
		JSONArray friends = FollowerTools.listFollowing(id_user, connection).getJSONArray("friends");
		
		JSONObject friend=new JSONObject();
		for(int i=0;i<friends.length();i++) {
			friend=friends.getJSONObject(i);
			ids.add(friend.getString("id_friend"));
		}
		
		ArrayList<JSONObject> twists = new ArrayList<>();
		for(String id:ids) {	
		JSONArray user_twists =TwistTools.listMessages(id, meessageCollection).getJSONArray("messages");
				for(int i=0;i<user_twists.length();i++) {
					twists.add(user_twists.getJSONObject(i));
				}
		}
		return new JSONObject().put("twists", twists);
		
	}catch(JSONException e) {
		e.printStackTrace();
	}catch(SQLException e) {
		e.printStackTrace();
		
	}
	finally {
		if(connection!=null)
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
	}
	return ErrorJSON.serviceRefused("Problem occured in Wall Twists Service", -1);

}


	/** À completer */
	public static JSONObject search(String key_session, String query, String friends) {
		if(key_session==null || query==null  || friends==null )
			return ErrorJSON.serviceRefused("missing parameter",-1);
		
		try {
			//connect to mysql
			Connection connection = Database.getMySQLConnection();

			if(!SessionTools.isConnectedByKey(key_session,connection)) {
				return ErrorJSON.serviceRefused("Not connected",-3);	
			} 
			
			if(SessionTools.hasExceededTimeOut(key_session, connection)) {
				SessionTools.removeSession(key_session,connection);
				return ErrorJSON.serviceRefused("TimeOut exceeded, disconnected automatically", 1);
			}
			//ajout de 30min dans session_fin
			SessionTools.updateTimeOut(key_session, connection);
			
			MongoDatabase mongoDatabase=Database.getMongoDBConnection();
			MongoCollection<Document> commentCollection = mongoDatabase.getCollection("message");

			
			return TwistTools.search(key_session,query,friends, commentCollection);	
			

		}catch(SQLException e) {
			e.printStackTrace();
			
		}
		return ErrorJSON.serviceRefused("Problem occured in listMessages Service", -1);
	}



	public static JSONObject ReTwist(String key_session, String id_message, String message) {
		
		if(key_session==null || id_message==null  )
			return ErrorJSON.serviceRefused("Missing parameter",-1);
		
		try {
			//connect to mysql
			Connection connection = Database.getMySQLConnection();

			String id_user=SessionTools.getIdUser(key_session,connection);
			
			if(!SessionTools.isConnectedByKey(key_session,connection)) {
				return ErrorJSON.serviceRefused("Not connected",-3);	
			} 
			
			if(SessionTools.hasExceededTimeOut(key_session, connection)) {
				SessionTools.removeSession(key_session,connection);
				return ErrorJSON.serviceRefused("TimeOut exceeded, disconnected automatically", 1);
			}
			
			//ajout de 30min dans session_fin
			SessionTools.updateTimeOut(key_session, connection);
		
			//connect to mongodb
			MongoDatabase mongoDatabase=Database.getMongoDBConnection();
			MongoCollection<Document> commentCollection = mongoDatabase.getCollection("message");
			
			if(!TwistTools.twistExists(id_message,commentCollection))
				return ErrorJSON.serviceRefused("No message to add comment", 2);
			
			TwistTools.reTwist(id_message,id_user,message,commentCollection);
		
			return ErrorJSON.serviceAccepted("You have successfully added your message");
			
		}catch(SQLException e) {
			e.printStackTrace();
			
		}
		return ErrorJSON.serviceRefused("Problem occured in addMessage Service", -1);
	}



	public static JSONObject editTwist(String key_session, String id_message, String message) {
		if(key_session==null || message==null || id_message==null  )
			return ErrorJSON.serviceRefused("Missing parameter",-1);
		
		try {
			//connect to mysql
			Connection connection = Database.getMySQLConnection();
			String id_user=SessionTools.getIdUser(key_session,connection);
			
			if(!SessionTools.isConnectedByKey(key_session,connection)) {
				return ErrorJSON.serviceRefused("Not connected",-3);	
			} 
			
			if(SessionTools.hasExceededTimeOut(key_session, connection)) {
				SessionTools.removeSession(key_session,connection);
				return ErrorJSON.serviceRefused("TimeOut exceeded, disconnected automatically", 1);
			}
			
			//ajout de 30min dans session_fin
			SessionTools.updateTimeOut(key_session, connection);
		
			//connect to mongodb
			
			MongoDatabase mongoDatabase=Database.getMongoDBConnection();
			MongoCollection<Document> commentCollection = mongoDatabase.getCollection("message");
			
			
			if(!TwistTools.twistExists(id_message, commentCollection)) {
				return ErrorJSON.serviceRefused("Message don't exists",1);
			}
			
			if(!TwistTools.checkAuthor(id_user, id_message, commentCollection)) {
				return ErrorJSON.serviceRefused("you are not the author of this message",1);	
			} 
			
			TwistTools.editTwist(id_message,message,commentCollection);
		
			return ErrorJSON.serviceAccepted("You have successfully edited your message");
			
		}catch(SQLException e) {
			e.printStackTrace();
			
		}
		return ErrorJSON.serviceRefused("Problem occured in editTwist Service", -1);
	}
	
	
	
	
}
