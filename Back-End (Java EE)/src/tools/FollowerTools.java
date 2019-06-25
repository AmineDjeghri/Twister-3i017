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

		String select = "SELECT id_user2 "
				+ " FROM friendShip "
				+ " WHERE id_user1 = ?";

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
				String familyname_friend = UserTools.getFamilyName(id_friend, connection);
				String firstname_friend = UserTools.getFirstName(id_friend, connection);
				
				friend.put("id_following", id_friend);
				friend.put("login_following", login_friend);
				friend.put("firstname_following", firstname_friend);
				friend.put("familyname_following", familyname_friend);
				// Pour chaque followers, on regarde si id_user follow id_friend (always true normelement)
				friend.put("isfollowed", FollowerTools.followerExists(id_user, id_friend, connection));
				System.out.println(FollowerTools.followerExists(id_user, id_friend, connection));
				
				friends.add(friend);
			}
			
			json.put("following", friends);
			
			return json;
		}
		finally {
			if(preparedStatement!=null)
				preparedStatement.close();	
		}

	}
	public static JSONObject listFollowers(String id_user, Connection connection) throws SQLException, JSONException {

		String select = "SELECT id_user1 "
				+ " FROM friendShip "
				+ " WHERE id_user2 = ?";

		PreparedStatement preparedStatement=null;
		JSONObject json = new JSONObject();
		
		ArrayList<JSONObject> friends = new ArrayList<JSONObject>();
		
		try {
			preparedStatement = connection.prepareStatement(select);
			preparedStatement.setString(1, id_user);

			ResultSet res = preparedStatement.executeQuery();

			while (res.next()) {
				JSONObject friend = new JSONObject();
				
				String id_friend = res.getString("id_user1");
				String login_friend = UserTools.getLogin(id_friend, connection);
				String familyname_follower = UserTools.getFamilyName(id_friend, connection);
				String firstname_follower = UserTools.getFirstName(id_friend, connection);
		
				friend.put("id_follower", id_friend);
				friend.put("login_follower", login_friend);
				friend.put("firstname_follower", firstname_follower);
				friend.put("familyname_follower", familyname_follower);
				// Pour chaque followers, on regarde si id_user follow id_friend 
				friend.put("isfollowed", FollowerTools.followerExists(id_user, id_friend, connection));
				
				friends.add(friend);
			}
			
			json.put("followers", friends);
			
			return json;
		}
		finally {
			if(preparedStatement!=null)
				preparedStatement.close();	
		}

	}

	public static String getnbFollowing(String id_user, Connection connection) throws SQLException {
		String select = "SELECT count(id_user2) AS nbFollowing"
				+ " FROM friendShip"
				+ " where id_user1 = ?";
		

		PreparedStatement preparedStatement=null;
		
		try {
			preparedStatement = connection.prepareStatement(select);
			preparedStatement.setString(1, id_user);

			ResultSet res = preparedStatement.executeQuery();

			if (res.next()) {
				//System.out.println(res.getInt("nbFollowing"));
				return Integer.toString(res.getInt("nbFollowing"));
			}
			return null;
		}
		finally {
			if(preparedStatement!=null)
				preparedStatement.close();	
		}
	}
	public static String getnbFollowers(String id_user, Connection connection) throws SQLException {
		String select = "SELECT count(id_user1) AS nbFollowers"
				+ " FROM friendShip"
				+ " where id_user2 = ?";
	

		PreparedStatement preparedStatement=null;
		
		try {
			preparedStatement = connection.prepareStatement(select);
			preparedStatement.setString(1, id_user);

			ResultSet res = preparedStatement.executeQuery();

			if (res.next()) {
				//System.out.println(res.getInt("nbFollowing"));
				return Integer.toString(res.getInt("nbFollowers"));
			}
			return null;
		}
		finally {
			if(preparedStatement!=null)
				preparedStatement.close();	
		}
	}

		
}



