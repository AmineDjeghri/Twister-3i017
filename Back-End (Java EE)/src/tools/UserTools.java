package tools;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.json.JSONException;
import org.json.JSONObject;

import com.mongodb.client.MongoCollection;

import sun.text.normalizer.CharTrie.FriendAgent;


public class UserTools {

	public static boolean userExists(String login, Connection connection) throws SQLException {


		String select = "SELECT * FROM user where login_user = ?";


		PreparedStatement preparedStatement =null;
		try{
			preparedStatement=	connection.prepareStatement(select);
			preparedStatement.setString(1, login);

			ResultSet res = preparedStatement.executeQuery();

			if (res.next()) 
				return true;
			else 
				return false;	

		}finally {
			if(preparedStatement!=null)
				preparedStatement.close();
		}		

	}

	public static void addUser(String prenom, String nom, String login, String password,String email, Connection connection) throws SQLException {

		String insert="INSERT INTO USER"
				+ "(login_user, first_name_user, family_name_user,password_user,mail_user) VALUES"
				+ "(?,?,?,?,?)";

		PreparedStatement preparedStatement =null;
		try{
			preparedStatement=	connection.prepareStatement(insert);


			preparedStatement.setString(1, login);
			preparedStatement.setString(2, prenom);
			preparedStatement.setString(3, nom);
			preparedStatement.setString(4, password);
			preparedStatement.setString(5, email);		

			preparedStatement.executeUpdate();
		}

		finally {
			if(preparedStatement!=null)
				preparedStatement.close();
		}

	}

	public static boolean checkPassword(String login, String password, Connection connection)  throws SQLException{

		String query = "SELECT login_user, password_user FROM user where (login_user,password_user) = (?,?)";

		PreparedStatement preparedStatement =null;
		try{
			preparedStatement=	connection.prepareStatement(query);
			preparedStatement.setString(1, login);
			preparedStatement.setString(2, password);


			ResultSet res=preparedStatement.executeQuery();

			if (res.next()) 
				return true;
			else
				return false;

		}finally {
			if(preparedStatement!=null)
				preparedStatement.close();
		}	


	}


	public static String getIdUser(String login, Connection connection) throws SQLException {

		String query = "SELECT id_user FROM user where login_user = ?";

		PreparedStatement preparedStatement =null;
		try{
			preparedStatement=	connection.prepareStatement(query);
			preparedStatement.setString(1, login);



			ResultSet res = preparedStatement.executeQuery();

			if (res.next()) 
				return res.getString("id_user");

			else 
				return null;
		}

		finally {
			if(preparedStatement!=null)
				preparedStatement.close();
		}

	}


	public static String getLogin(String id_friend,Connection connection) throws SQLException{
		String query = "SELECT login_user FROM user where id_user = ?";

		PreparedStatement preparedStatement =null;
		try{
			preparedStatement=	connection.prepareStatement(query);
			preparedStatement.setString(1, id_friend);

			ResultSet	res = preparedStatement.executeQuery();

			if (res.next()) 
				return res.getString("login_user");
			else
				return null;

		}finally {
			if(preparedStatement!=null)
				preparedStatement.close();
		}
	}
	
	public static JSONObject getUser(String key_session ,Connection connection) throws SQLException, JSONException{
		String query = "SELECT id_user, login_user, first_name_user, family_name_user, password_user, mail_user"
				+ " FROM user, session "
				+ "WHERE session.key_session = ? AND user.id_user = session.id_user";

		PreparedStatement preparedStatement =null;
		preparedStatement=	connection.prepareStatement(query);
		preparedStatement.setString(1, key_session);
		
		try{

			ResultSet res = preparedStatement.executeQuery();
			JSONObject user = new JSONObject();
			
			if (res.next()) {
				user.put("id_user", res.getString("id_user"));
				user.put("login_user", res.getString("login_user"));
				user.put("first_name_user", res.getString("first_name_user"));
				user.put("family_name_user", res.getString("family_name_user"));
				user.put("password_user", res.getString("password_user"));
				user.put("mail_user", res.getString("mail_user"));
				return user;
			
			}else {
				return null;
			}
		}finally {
			if(preparedStatement!=null)
				preparedStatement.close();
		}
	}

	public static void changeFirstName(String id_user, String prenom,Connection connection) throws SQLException {
		String query = "UPDATE user "
					+ "SET first_name_user = ?"
					+ " WHERE id_user = ?" ;
			
		PreparedStatement preparedStatement =null;
		preparedStatement=	connection.prepareStatement(query);
		preparedStatement.setString(1, prenom);
		preparedStatement.setString(2, id_user);
		preparedStatement.executeUpdate();
		
	}

	public static void changeFamilyName(String id_user, String nom ,Connection connection) throws SQLException {
		String query = "UPDATE user "
					+ "SET family_name_user = ?"
					+ " WHERE id_user = ?" ;
			
		PreparedStatement preparedStatement =null;
		preparedStatement=	connection.prepareStatement(query);
		preparedStatement.setString(1, nom);
		preparedStatement.setString(2, id_user);
		preparedStatement.executeUpdate();
	
	}

	public static void changeEmail(String id_user, String email,Connection connection) throws SQLException {
		String query = "UPDATE user "
					+ "SET mail_user = ?"
					+ " WHERE id_user = ?" ;
			
		PreparedStatement preparedStatement =null;
		preparedStatement=	connection.prepareStatement(query);
		preparedStatement.setString(1, email);
		preparedStatement.setString(2, id_user);
		preparedStatement.executeUpdate();
	
	}

	public static String getPassword(String id_user, Connection connection) throws SQLException {
		String query = "SELECT password_user FROM user where id_user = ?";

		PreparedStatement preparedStatement =null;
		try{
			preparedStatement=	connection.prepareStatement(query);
			preparedStatement.setString(1,id_user);

			ResultSet res = preparedStatement.executeQuery();

			if (res.next()) 
				return res.getString("password_user");
			else
				return null;

		}finally {
			if(preparedStatement!=null)
				preparedStatement.close();
		}
	}
	
	public static String getFirstName(String id_user, Connection connection) throws SQLException {
		String query = "SELECT first_name_user FROM user where id_user = ?";

		PreparedStatement preparedStatement =null;
		try{
			preparedStatement=	connection.prepareStatement(query);
			preparedStatement.setString(1,id_user);

			ResultSet res = preparedStatement.executeQuery();

			if (res.next()) 
				return res.getString("first_name_user");
			else
				return null;

		}finally {
			if(preparedStatement!=null)
				preparedStatement.close();
		}
	}
	
	public static String getFamilyName(String id_user, Connection connection) throws SQLException {
		String query = "SELECT family_name_user  FROM user where id_user = ?";

		PreparedStatement preparedStatement =null;
		try{
			preparedStatement=	connection.prepareStatement(query);
			preparedStatement.setString(1,id_user);

			ResultSet res = preparedStatement.executeQuery();

			if (res.next()) 
				return res.getString("family_name_user");
			else
				return null;

		}finally {
			if(preparedStatement!=null)
				preparedStatement.close();
		}
	}

	public static void changePassword(String id_user, String password, Connection connection) throws SQLException {
		String query = "UPDATE user "
				+ "SET password_user = ?"
				+ " WHERE id_user = ?" ;
		
		PreparedStatement preparedStatement =null;
		try{
		preparedStatement=	connection.prepareStatement(query);
		preparedStatement.setString(1, password);
		preparedStatement.setString(2, id_user);
		preparedStatement.executeUpdate();
		}finally {
			if(preparedStatement!=null)
				preparedStatement.close();
		}
	}

	public static JSONObject SearchUser(String recherche, Connection connection, MongoCollection<Document> collection) throws SQLException, JSONException {
		String query = "SELECT *"
				+ " FROM user"
				+ " WHERE first_name_user LIKE ?"
				+ " OR family_name_user LIKE ?"
				+ " OR login_user LIKE ?";

		PreparedStatement preparedStatement =null;
		preparedStatement =	connection.prepareStatement(query);
		
		preparedStatement.setString(1, "%"+recherche+"%");
		preparedStatement.setString(2,"%"+recherche+"%");
		preparedStatement.setString(3, "%"+recherche+"%");
		
		JSONObject json = new JSONObject();
		ArrayList<JSONObject> users = new ArrayList<JSONObject>();
		try{
			ResultSet res = preparedStatement.executeQuery();
			
			while (res.next()) {
				String id_user = res.getString("id_user");
				JSONObject profile = getProfilebyLogin(id_user, connection, collection);
				users.add(profile);
			}
			
			return json.put("users", users);
		}finally {
			if(preparedStatement!=null)
				preparedStatement.close();
		}
	}

	public static JSONObject getProfilebyLogin(String id_user, Connection connection, MongoCollection<Document> collection) throws SQLException, JSONException {
		String query = "SELECT *"
				+ " FROM user"
				+ " WHERE id_user = ? ";

		PreparedStatement preparedStatement = null;
		preparedStatement =	connection.prepareStatement(query);
		preparedStatement.setString(1, id_user);

		String nb_following = FollowerTools.getnbFollowing(id_user, connection);
		String nb_followers = FollowerTools.getnbFollowers(id_user, connection);
		String nb_twists = TwistTools.getnbTwists(id_user, collection);
		//System.out.println(nb_twists);
		
		ResultSet res = preparedStatement.executeQuery();
		try{

			//System.out.println(res.next());
			JSONObject profile = new JSONObject();
			if (res.next()) {
	
				//System.out.println("aaaaa");
				
				profile.put("id_user", res.getString("id_user"));
				profile.put("login_user", res.getString("login_user"));
				profile.put("first_name_user", res.getString("first_name_user"));
				profile.put("family_name_user", res.getString("family_name_user"));
				profile.put("nb_followers", nb_followers);
				profile.put("nb_following", nb_following);
				profile.put("nb_twists", nb_twists);
				profile.put("isfollowed", FollowerTools.followerExists(id_user, res.getString("id_user"), connection));
			}
			//System.out.println(users.toString());
			return profile;
		}finally {
			if(preparedStatement!=null)
				preparedStatement.close();
		}
	}
	

	public static JSONObject getProfilebyLogin(String id_user,String id_user_KeySession, Connection connection, MongoCollection<Document> collection) throws SQLException, JSONException {
		String query = "SELECT *"
				+ " FROM user"
				+ " WHERE id_user = ? ";

		PreparedStatement preparedStatement = null;
		preparedStatement =	connection.prepareStatement(query);
		preparedStatement.setString(1, id_user);

		String nb_following = FollowerTools.getnbFollowing(id_user, connection);
		String nb_followers = FollowerTools.getnbFollowers(id_user, connection);
		String nb_twists = TwistTools.getnbTwists(id_user, collection);
		//System.out.println(nb_twists);
		
		ResultSet res = preparedStatement.executeQuery();
		try{

			//System.out.println(res.next());
			JSONObject profile = new JSONObject();
			if (res.next()) {
	
				//System.out.println("aaaaa");
				
				profile.put("id_user", res.getString("id_user"));
				profile.put("login_user", res.getString("login_user"));
				profile.put("first_name_user", res.getString("first_name_user"));
				profile.put("family_name_user", res.getString("family_name_user"));
				profile.put("nb_followers", nb_followers);
				profile.put("nb_following", nb_following);
				profile.put("nb_twists", nb_twists);
				profile.put("isfollowed", FollowerTools.followerExists(id_user_KeySession, id_user, connection));
			}
			//System.out.println(users.toString());
			return profile;
		}finally {
			if(preparedStatement!=null)
				preparedStatement.close();
		}
	}


	public static boolean CheckEmail(String email , String login, Connection connection) throws SQLException {
		String select = "SELECT * "
				+ "FROM user "
				+ "where mail_user = ? "
				+ "AND login_user= ?";


		PreparedStatement preparedStatement =null;
		try{
			preparedStatement=	connection.prepareStatement(select);
			preparedStatement.setString(1, email);
			preparedStatement.setString(2, login);

			ResultSet res = preparedStatement.executeQuery();

			if (res.next()) 
				return true;
			else 
				return false;	

		}finally {
			if(preparedStatement!=null)
				preparedStatement.close();
		}	
	}

}



















