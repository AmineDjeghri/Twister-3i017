package services;

import java.sql.Connection;
import java.sql.SQLException;

import org.json.JSONException;
import org.json.JSONObject;

import db.Database;
import tools.ErrorJSON;
import tools.FollowerTools;
import tools.SessionTools;
import tools.UserTools;

public class FollowerS {

	public static JSONObject follow(String key_session, String id_friend) {

		if(key_session==null || id_friend==null  )
			return ErrorJSON.serviceRefused("missing parameter", -1);

		Connection connection=null;
		try {

			connection =Database.getMySQLConnection();
			
			if(!SessionTools.isConnectedByKey(key_session, connection))
				return ErrorJSON.serviceRefused("Not connected", -3);
			
			if(SessionTools.hasExceededTimeOut(key_session, connection)) {
				SessionTools.removeSession(key_session,connection);
				return ErrorJSON.serviceRefused("TimeOut exceeded, disconnected automatically", 1);
			}
			
			//ajout de 30min dans session_fin
			SessionTools.updateTimeOut(key_session, connection);
			
			String friend_login=UserTools.getLogin(id_friend,connection);
			String id_user = SessionTools.getIdUser(key_session, connection);
	
			//Un utilisateur peut s'ajouter comme friend
			if(id_friend.compareTo(id_user) == 0)
				return ErrorJSON.serviceRefused("You can't add yourself as a friend", 1);
			
			
			if(!UserTools.userExists(friend_login,connection))
				return ErrorJSON.serviceRefused("User don't exist ",1);


			if(FollowerTools.followerExists(id_user,id_friend,connection))
				return ErrorJSON.serviceRefused("Already friends",1);	

			//verify session expire

			FollowerTools.follow(id_user,id_friend,connection);
			
			return ErrorJSON.serviceAccepted("You have successfully added your friend");

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
		
		return ErrorJSON.serviceRefused("Problem occured in addFriend Service", -1);
	}

	public static JSONObject unfollow(String key_session, String id_friend) {

		if(key_session==null || id_friend==null  )
			return ErrorJSON.serviceRefused("missing parameter",-1);
		
		Connection connection=null;
		try {
			
			connection = Database.getMySQLConnection();

			if(!SessionTools.isConnectedByKey(key_session, connection))
				return ErrorJSON.serviceRefused("Not connected", -3);
			
			if(SessionTools.hasExceededTimeOut(key_session, connection)) {
				SessionTools.removeSession(key_session,connection);
				return ErrorJSON.serviceRefused("TimeOut exceeded, disconnected automatically", 1);
			}
			
			//ajout de 30min dans session_fin
			SessionTools.updateTimeOut(key_session, connection);
			
			String friend_login=UserTools.getLogin(id_friend,connection);

			if(!UserTools.userExists(friend_login,connection))
				return ErrorJSON.serviceRefused("User don't exists to delete it",1);

			String id_user=SessionTools.getIdUser(key_session,connection);

			if(!FollowerTools.followerExists(id_user,id_friend,connection))
				return ErrorJSON.serviceRefused("Not friends",1);	

			FollowerTools.unfollow(id_user,id_friend,connection);
			
			return ErrorJSON.serviceAccepted("You have successfully deleted your friend");

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
		return ErrorJSON.serviceRefused("Problem occured in removeFriend Service", -1);
	}

	
	public static JSONObject listFollowing(String key_session) {

		if(key_session==null)
			return ErrorJSON.serviceRefused("missing parameter",-1);
		
		Connection connection=null;
		
		try {
			
			connection = Database.getMySQLConnection();

			if(!SessionTools.isConnectedByKey(key_session, connection))
				return ErrorJSON.serviceRefused("Not connected", -3);
			
			if(SessionTools.hasExceededTimeOut(key_session, connection)) {
				SessionTools.removeSession(key_session,connection);
				return ErrorJSON.serviceRefused("TimeOut exceeded, disconnected automatically", 1);
			}
			
			//ajout de 30min dans session_fin
			SessionTools.updateTimeOut(key_session, connection);
			
			String id_user=SessionTools.getIdUser(key_session,connection);
			
			JSONObject json = FollowerTools.listFollowing(id_user, connection);
			
			return json;
		} catch (JSONException e) {
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
		return ErrorJSON.serviceRefused("Problem occured in listFriends Service", -1);
	}

}
