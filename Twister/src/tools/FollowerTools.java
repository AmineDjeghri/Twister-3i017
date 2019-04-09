package tools;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

public class FollowerTools {

	public static boolean followerExists(String id_user, String id_friend, Connection connection) throws SQLException {

		String select = "SELECT * FROM friendShip where (id_user1,id_user2) = (?,?)";

		PreparedStatement preparedStatement=null;
		try {
			preparedStatement = connection.prepareStatement(select);
			preparedStatement.setString(1, id_user);
			preparedStatement.setString(2, id_friend);

			ResultSet res = preparedStatement.executeQuery();

			if (res.next()) 
				return true;
			else 
				return false;
		}
		finally {
			if(preparedStatement!=null)
				preparedStatement.close();	
		}

	}

	public static void follow(String id_user, String id_friend, Connection connection) throws SQLException {

		String insert = "INSERT INTO friendShip"
				+ "(id_user1, id_user2) VALUES"
				+ "(?,?)";
		PreparedStatement preparedStatement =null;

		try {
			preparedStatement = connection.prepareStatement(insert);

			preparedStatement.setString(1, id_user);
			preparedStatement.setString(2, id_friend);

			preparedStatement.executeUpdate();
		}finally {
			if(preparedStatement!=null)
				preparedStatement.close();
		}

	}

	public static void unfollow(String id_user, String id_friend, Connection connection) throws SQLException {

		String insert = "DELETE FROM friendShip WHERE (id_user1, id_user2) = (?,?)";

		PreparedStatement preparedStatement =null;
		
		try{
			
			preparedStatement=connection.prepareStatement(insert);
			preparedStatement.setString(1, id_user);
			preparedStatement.setString(2, id_friend);

			preparedStatement.executeUpdate();
			
		}finally {
			
			if(preparedStatement!=null)
				preparedStatement.close();
			
		}

	}

	public static JSONObject listFollowing(String id_user, Connection connection) throws SQLException, JSONException {

		String select = "SELECT id_user2 FROM friendShip where id_user1 = ?";

		PreparedStatement preparedStatement=null;
		JSONObject json = new JSONObject();
		
		ArrayList<JSONObject> friends = new ArrayList<JSONObject>();
		
		try {
			preparedStatement = connection.prepareStatement(select);
			preparedStatement.setString(1, id_user);

			ResultSet res = preparedStatement.executeQuery();

			while (res.next()) {
				JSONObject friend = new JSONObject();
				
				String id_friend = res.getString("id_user2");
				String login_friend = UserTools.getLogin(id_friend, connection);
		
				friend.put("id_friend", id_friend);
				friend.put("login_friend", login_friend);
				
				friends.add(friend);
			}
			
			json.put("friends", friends);
			
			return json;
		}
		finally {
			if(preparedStatement!=null)
				preparedStatement.close();	
		}

	}

		
}



