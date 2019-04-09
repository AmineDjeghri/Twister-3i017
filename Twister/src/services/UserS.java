package services;

import java.sql.Connection;

import java.sql.SQLException;

import org.json.JSONException;
import org.json.JSONObject;

import db.Database;
import tools.ErrorJSON;
import tools.SessionTools;
import tools.UserTools;

public class UserS {

	//Create user web service
	public static JSONObject createUser(String prenom, String nom, String login, String password,String email) {

		if(prenom==null || nom==null || login == null || password ==null || email==null)
			return ErrorJSON.serviceRefused("A missing parameter",-1);

		Connection  connection=null;

		try {
			connection=Database.getMySQLConnection();
			
			if(UserTools.userExists(login,connection))
				return ErrorJSON.serviceRefused("User already exists",1);

			UserTools.addUser(prenom,nom,login,password,email,connection);
			
			return ErrorJSON.serviceAccepted("You have successfully created your account");

		} catch (SQLException e) {
			e.printStackTrace();

		} finally {
			if(connection !=null)
				try {
					connection.close();

				} catch (SQLException e) {
					e.printStackTrace();
				}

		}
		return ErrorJSON.serviceRefused("Problem occured in createUser Service", -1);

	}

	//login web service
	public static JSONObject login(String login, String password)  {
		if( login == null || password ==null )
			return ErrorJSON.serviceRefused("A missing parameter",-1);
		
		Connection connection=null;
		JSONObject json= new JSONObject();

		try {
			connection =Database.getMySQLConnection();
			
			if(!UserTools.userExists(login,connection))
				return ErrorJSON.serviceRefused("User don't exist",1);

			if(!UserTools.checkPassword(login,password,connection))
				return ErrorJSON.serviceRefused("Wrong password",1);

			String id_user=UserTools.getIdUser(login,connection);

			if(SessionTools.isConnected(id_user,connection))
				return ErrorJSON.serviceRefused("User already connected", 1);

			String key_session= SessionTools.insertNewSession(id_user,connection);
			
			//ajout de 30 minutes � la session pour le timeout
			SessionTools.updateTimeOut(key_session, connection);

			json.put("id_user",id_user);
			json.put("login", login);
			json.put("key_session", key_session);

			return json;

		} catch (JSONException e) {
			e.printStackTrace();

		} catch (SQLException e) {
			e.printStackTrace();
			
		} finally {

			if(connection!=null)
				try {
					connection.close();

				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
		
		return ErrorJSON.serviceRefused("Problem occured in login Service", -1);


	}


	//logout web service
	public static JSONObject logout(String key_session) {

		if( key_session == null)
			return ErrorJSON.serviceRefused("A missing parameter",-1);

		Connection connection=null;
		try {
			
			connection=Database.getMySQLConnection();

			if(!SessionTools.isConnectedByKey(key_session,connection))
				return ErrorJSON.serviceRefused("Not connected", -3);
			
			if(SessionTools.hasExceededTimeOut(key_session, connection)) {
				SessionTools.removeSession(key_session,connection);
				System.out.println("aaaa");
				return ErrorJSON.serviceRefused("TimeOut exceeded, you have already been disconnected automatically", 1);
			}

			SessionTools.removeSession(key_session,connection);
			
			return ErrorJSON.serviceAccepted("You have successfully logged out");
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
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
		return ErrorJSON.serviceRefused("Problem occured in logout Service", -1);

	}

	public static JSONObject EditUser(String login, String prenom, String nom, String email, String key_session) {
		
		if(key_session==null || login ==null )
			return ErrorJSON.serviceRefused("A missing parameter",-1);

		Connection  connection=null;

		try {
			connection=Database.getMySQLConnection();
			
			if(!SessionTools.isConnectedByKey(key_session,connection)) 
				return ErrorJSON.serviceRefused("Not connected",-3);	
			
			if(SessionTools.hasExceededTimeOut(key_session, connection)) {
				SessionTools.removeSession(key_session,connection);
				return ErrorJSON.serviceRefused("TimeOut exceeded, disconnected automatically", 1);
			}
			
			//ajout de 30min dans session_fin
			SessionTools.updateTimeOut(key_session, connection);
			
			if(!UserTools.userExists(login,connection))
				return ErrorJSON.serviceRefused("User don't exists",1);

			String id_user = SessionTools.getIdUser(key_session, connection);
			String login_DB = UserTools.getLogin(id_user, connection);
			
			if(!(login_DB.compareTo(login) == 0))
				return ErrorJSON.serviceRefused("you can't edit this",-1);
	
			if(!(prenom.compareTo("") == 0))
				UserTools.changeFirstName(id_user, prenom, connection);
			
			if(!(nom.compareTo("") == 0))
				UserTools.changeFamilyName(id_user, nom, connection);
			
			if(!(email.compareTo("") == 0))
				UserTools.changeEmail(id_user, email, connection);
		
			return ErrorJSON.serviceAccepted("You have successfully edited your account");

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(connection !=null)
				try {
					connection.close();

				} catch (SQLException e) {
					e.printStackTrace();
				}

		}
		return ErrorJSON.serviceRefused("Problem occured in EditUser Service", -1);
	}

	public static JSONObject changePassword(String login, String password, String key_session) {
		if(key_session==null || password ==null )
			return ErrorJSON.serviceRefused("A missing parameter",-1);

		Connection  connection=null;

		try {
			connection=Database.getMySQLConnection();
			
			if(!SessionTools.isConnectedByKey(key_session,connection)) 
				return ErrorJSON.serviceRefused("Not connected",-3);	
			
			if(SessionTools.hasExceededTimeOut(key_session, connection)) {
				SessionTools.removeSession(key_session,connection);
				return ErrorJSON.serviceRefused("TimeOut exceeded, disconnected automatically", 1);
			}
			
			//ajout de 30min dans session_fin
			SessionTools.updateTimeOut(key_session, connection);
			
			if(!UserTools.userExists(login,connection))
				return ErrorJSON.serviceRefused("User don't exists",1);

			String id_user = SessionTools.getIdUser(key_session, connection);
			String login_DB = UserTools.getLogin(id_user, connection);
			
			if(!(login_DB.compareTo(login) == 0))
				return ErrorJSON.serviceRefused("you can't change this",-1);
	
			String password_DB = UserTools.getPassword(id_user,connection);
			
			// À revoir, il ne faut pas qu'il mette le même mot de passe
			if(password_DB.compareTo(password) == 0)
				ErrorJSON.serviceRefused("It is the same password", -1);
			
			UserTools.changePassword(id_user, password, connection);
			
			return ErrorJSON.serviceAccepted("You have successfully edited your password");

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(connection !=null)
				try {
					connection.close();

				} catch (SQLException e) {
					e.printStackTrace();
				}

		}
		return ErrorJSON.serviceRefused("Problem occured in EditUser Service", -1);
	}



}
