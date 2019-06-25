package services;

import java.sql.Connection;
import java.sql.SQLException;

import org.json.JSONException;
import org.json.JSONObject;

import db.Database;
import tools.ErrorJSON;
import tools.ErrorTools;
import tools.FollowerTools;
import tools.SessionTools;
import tools.UserTools;

public class FollowerS {

	public static JSONObject follow(String key_session, String id_friend) {

		if(key_session==null || id_friend==null  )
			return ErrorJSON.serviceRefused(ErrorTools.MESSAGE_MISSING_PARAMETERS, ErrorTools.CODE_MISSING_PARAMETERS);

		Connection connection=null;
		try {

			connection =Database.getMySQLConnection();
			
			if(!SessionTools.isConnectedByKey(key_session, connection))
				return ErrorJSON.serviceRefused(ErrorTools.MESSAGE_USER_NOT_CONNECTED, ErrorTools.CODE_USER_NOT_CONNECTED);
			
			if(SessionTools.hasExceededTimeOut(key_session, connection)) {
				SessionTools.removeSession(key_session,connection);
				return ErrorJSON.serviceRefused(ErrorTools.MESSAGE_USER_DISCONNECTED_AUTOMATICALLY, ErrorTools.CODE_USER_DISCONNECTED_AUTOMATICALLY);
			}
			
			//ajout de 30min dans session_fin
			SessionTools.updateTimeOut(key_session, connection);
			
			String friend_login=UserTools.getLogin(id_friend,connection);
			String id_user = SessionTools.getIdUser(key_session, connection);
	
			//Un utilisateur peut s'ajouter comme friend
			if(id_friend.compareTo(id_user) == 0)
				return ErrorJSON.serviceRefused(ErrorTools.MESSAGE_USER_AS_FRIEND, ErrorTools.CODE_USER_AS_FRIEND);
			
			
			if(!UserTools.userExists(friend_login,connection))
				return ErrorJSON.serviceRefused(ErrorTools.MESSAGE_USER_DOES_NOT_EXIST,ErrorTools.CODE_USER_DOES_NOT_EXIST);


			if(FollowerTools.followerExists(id_user,id_friend,connection))
				return ErrorJSON.serviceRefused(ErrorTools.MESSAGE_USER_ALREADY_FRIEND,ErrorTools.CODE_USER_ALREADY_FRIEND);	

			FollowerTools.follow(id_user,id_friend,connection);

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
		
		return ErrorJSON.serviceAccepted("You have successfully added your friend");
	}

	public static JSONObject unfollow(String key_session, String id_friend) {

		if(key_session==null || id_friend==null  )
			return ErrorJSON.serviceRefused(ErrorTools.MESSAGE_MISSING_PARAMETERS,ErrorTools.CODE_MISSING_PARAMETERS);
		
		Connection connection=null;
		try {
			
			connection = Database.getMySQLConnection();

			if(!SessionTools.isConnectedByKey(key_session, connection))
				return ErrorJSON.serviceRefused(ErrorTools.MESSAGE_USER_NOT_CONNECTED, ErrorTools.CODE_USER_NOT_CONNECTED);
			
			if(SessionTools.hasExceededTimeOut(key_session, connection)) {
				SessionTools.removeSession(key_session,connection);
				return ErrorJSON.serviceRefused(ErrorTools.MESSAGE_USER_DISCONNECTED_AUTOMATICALLY, ErrorTools.CODE_USER_DISCONNECTED_AUTOMATICALLY);
			}
			
			//ajout de 30min dans session_fin
			SessionTools.updateTimeOut(key_session, connection);
			
			
			String friend_login=UserTools.getLogin(id_friend,connection);

			if(!UserTools.userExists(friend_login,connection))
				return ErrorJSON.serviceRefused(ErrorTools.MESSAGE_USER_DOES_NOT_EXIST,ErrorTools.CODE_USER_DOES_NOT_EXIST);

			String id_user=SessionTools.getIdUser(key_session,connection);

			if(!FollowerTools.followerExists(id_user,id_friend,connection))
				return ErrorJSON.serviceRefused(ErrorTools.MESSAGE_FRIEND_DOES_NOT_EXIST,ErrorTools.CODE_FRIEND_DOES_NOT_EXIST);	

			FollowerTools.unfollow(id_user,id_friend,connection);
			


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
		return ErrorJSON.serviceAccepted("You have successfully deleted your friend");
	}

	
	public static JSONObject listFollowing(String login, String key_session) {

		if(key_session==null || login==null)
			return ErrorJSON.serviceRefused(ErrorTools.MESSAGE_MISSING_PARAMETERS,ErrorTools.CODE_MISSING_PARAMETERS);
		
		Connection connection=null;
		
		try {
			connection =Database.getMySQLConnection();
			
			if(!SessionTools.isConnectedByKey(key_session, connection))
				return ErrorJSON.serviceRefused(ErrorTools.MESSAGE_USER_NOT_CONNECTED, ErrorTools.CODE_USER_NOT_CONNECTED);
			
			if(SessionTools.hasExceededTimeOut(key_session, connection)) {
				SessionTools.removeSession(key_session,connection);
				return ErrorJSON.serviceRefused(ErrorTools.MESSAGE_USER_DISCONNECTED_AUTOMATICALLY, ErrorTools.CODE_USER_DISCONNECTED_AUTOMATICALLY);
			}
			
			//ajout de 30min dans session_fin
			SessionTools.updateTimeOut(key_session, connection);
			
			if(!UserTools.userExists(login,connection))
				return ErrorJSON.serviceRefused(ErrorTools.MESSAGE_USER_DOES_NOT_EXIST,ErrorTools.CODE_USER_DOES_NOT_EXIST);
			
			//verifie user exists
			String id_user=UserTools.getIdUser(login, connection);
			
			JSONObject json = FollowerTools.listFollowing(id_user, connection);
			return json;
			
		} catch (JSONException e) {
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

	public static JSONObject listFollowers(String login, String key_session) {
		if(key_session==null || login==null)
			return ErrorJSON.serviceRefused(ErrorTools.MESSAGE_MISSING_PARAMETERS,ErrorTools.CODE_MISSING_PARAMETERS);
		
		Connection connection=null;
		
		
		try {
			connection =Database.getMySQLConnection();
			
			if(!SessionTools.isConnectedByKey(key_session, connection))
				return ErrorJSON.serviceRefused(ErrorTools.MESSAGE_USER_NOT_CONNECTED, ErrorTools.CODE_USER_NOT_CONNECTED);
			
			if(SessionTools.hasExceededTimeOut(key_session, connection)) {
				SessionTools.removeSession(key_session,connection);
				return ErrorJSON.serviceRefused(ErrorTools.MESSAGE_USER_DISCONNECTED_AUTOMATICALLY, ErrorTools.CODE_USER_DISCONNECTED_AUTOMATICALLY);
			}
			
			//ajout de 30min dans session_fin
			SessionTools.updateTimeOut(key_session, connection);
			
			if(!UserTools.userExists(login,connection))
				return ErrorJSON.serviceRefused(ErrorTools.MESSAGE_USER_DOES_NOT_EXIST,ErrorTools.CODE_USER_DOES_NOT_EXIST);
			
			String id_user = UserTools.getIdUser(login, connection);
			JSONObject json = FollowerTools.listFollowers(id_user, connection);
			return json;
			
		} catch (JSONException e) {
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

}
