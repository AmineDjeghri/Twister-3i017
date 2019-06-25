package services;

import java.sql.Connection;

import java.sql.SQLException;
import java.util.List;

import javax.mail.MessagingException;

import org.bson.Document;
import org.json.JSONException;
import org.json.JSONObject;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import db.Database;
import tools.ErrorJSON;
import tools.ErrorTools;
import tools.MailTools;
import tools.SessionTools;
import tools.UserTools;

public class UserS {

	//Create user web service
	public static JSONObject createUser(String prenom, String nom, String login, String password,String email) {

		if(prenom==null || nom==null || login == null || password ==null || email==null)
			return ErrorJSON.serviceRefused(ErrorTools.MESSAGE_MISSING_PARAMETERS, ErrorTools.CODE_MISSING_PARAMETERS);

		Connection  connection=null;

		try {
			connection=Database.getMySQLConnection();
			
			if(UserTools.userExists(login,connection))
				return ErrorJSON.serviceRefused(ErrorTools.MESSAGE_USER_ALREADY_EXISTS,ErrorTools.CODE_USER_ALREADY_EXISTS);

			UserTools.addUser(prenom,nom,login,password,email,connection);

		} catch (SQLException e) {
			return ErrorJSON.serviceRefused(ErrorTools.MESSAGE_ERROR_SQL, ErrorTools.CODE_ERROR_SQL);
			
		} finally {
			if(connection !=null)
				try {
					connection.close();

				} catch (SQLException e) {
					return ErrorJSON.serviceRefused(ErrorTools.MESSAGE_ERROR_SQL, ErrorTools.CODE_ERROR_SQL);
				}

		}
		return ErrorJSON.serviceAccepted("You have successfully created your account");
	}

	//login web service
	public static JSONObject login(String login, String password)  {
		if( login == null || password ==null )
			return ErrorJSON.serviceRefused(ErrorTools.MESSAGE_MISSING_PARAMETERS,ErrorTools.CODE_MISSING_PARAMETERS);
		
		Connection connection=null;
		JSONObject json= new JSONObject();

		try {
			connection =Database.getMySQLConnection();
			
			if(!UserTools.userExists(login,connection))
				return ErrorJSON.serviceRefused(ErrorTools.MESSAGE_USER_DOES_NOT_EXIST,ErrorTools.CODE_USER_DOES_NOT_EXIST);

			if(!UserTools.checkPassword(login,password,connection))
				return ErrorJSON.serviceRefused(ErrorTools.MESSAGE_INCORRECT_PASSWORD,ErrorTools.CODE_INCORRECT_PASSWORD);

			String id_user=UserTools.getIdUser(login,connection);
			String old_key_session = SessionTools.getKey_Session(id_user, connection);
			
			//if the user is already connected elsewhere, delete that session, and insert a new session here
			if(SessionTools.isConnected(id_user,connection)) {
				SessionTools.removeSession(old_key_session, connection);
			}
			
			String new_key_session = SessionTools.insertNewSession(id_user,connection);
			
			//ajout de 30 minutes � la session pour le timeout
			SessionTools.updateTimeOut(new_key_session, connection);

			json.put("id_user",id_user);
			json.put("login", login);
			json.put("key_session", new_key_session);

			return json;

		} catch (JSONException e) {
			return ErrorJSON.serviceRefused(ErrorTools.MESSAGE_ERROR_JSON, ErrorTools.CODE_ERROR_JSON);

		} catch (SQLException e) {
			return ErrorJSON.serviceRefused(ErrorTools.MESSAGE_ERROR_SQL, ErrorTools.CODE_ERROR_SQL);
			
		} finally {

			if(connection!=null)
				try {
					connection.close();

				} catch (SQLException e) {
					return ErrorJSON.serviceRefused(ErrorTools.MESSAGE_ERROR_SQL, ErrorTools.CODE_ERROR_SQL);
				}
		}

	}


	//logout web service
	public static JSONObject logout(String key_session) {

		if( key_session == null)
			return ErrorJSON.serviceRefused(ErrorTools.MESSAGE_MISSING_PARAMETERS,ErrorTools.CODE_MISSING_PARAMETERS);

		Connection connection=null;
		try {
			
			connection=Database.getMySQLConnection();

			if(!SessionTools.isConnectedByKey(key_session,connection))
				return ErrorJSON.serviceRefused(ErrorTools.MESSAGE_USER_NOT_CONNECTED, ErrorTools.CODE_USER_NOT_CONNECTED);
			
			if(SessionTools.hasExceededTimeOut(key_session, connection)) {
				SessionTools.removeSession(key_session,connection);
				return ErrorJSON.serviceRefused(ErrorTools.MESSAGE_USER_DISCONNECTED_AUTOMATICALLY, ErrorTools.CODE_USER_DISCONNECTED_AUTOMATICALLY);
			}

			SessionTools.removeSession(key_session,connection);
			
		} catch (SQLException e) {
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
		return ErrorJSON.serviceAccepted("You have successfully logged out");
	}
	

	public static JSONObject EditUser(String login, String prenom, String nom, String email, String key_session) {
		
		if(key_session==null || login ==null )
			return ErrorJSON.serviceRefused(ErrorTools.MESSAGE_MISSING_PARAMETERS,ErrorTools.CODE_MISSING_PARAMETERS);

		Connection  connection=null;

		try {
			connection=Database.getMySQLConnection();
			
			if(!SessionTools.isConnectedByKey(key_session,connection)) 
				return ErrorJSON.serviceRefused(ErrorTools.MESSAGE_USER_NOT_CONNECTED,ErrorTools.CODE_USER_NOT_CONNECTED);	
			
			if(SessionTools.hasExceededTimeOut(key_session, connection)) {
				SessionTools.removeSession(key_session,connection);
				return ErrorJSON.serviceRefused(ErrorTools.MESSAGE_USER_DISCONNECTED_AUTOMATICALLY, ErrorTools.CODE_USER_DISCONNECTED_AUTOMATICALLY);
			}
			
			//ajout de 30min dans session_fin
			SessionTools.updateTimeOut(key_session, connection);
			
			if(!UserTools.userExists(login,connection))
				return ErrorJSON.serviceRefused(ErrorTools.MESSAGE_USER_DOES_NOT_EXIST,ErrorTools.CODE_USER_DOES_NOT_EXIST);

			String id_user = SessionTools.getIdUser(key_session, connection);
			String login_DB = UserTools.getLogin(id_user, connection);
			
			if(!(login_DB.compareTo(login) == 0))
				return ErrorJSON.serviceRefused(ErrorTools.MESSAGE_CAN_NOT_EDIT,ErrorTools.CODE_CAN_NOT_EDIT);
	
			if(!(prenom.compareTo("") == 0))
				UserTools.changeFirstName(id_user, prenom, connection);
			
			if(!(nom.compareTo("") == 0))
				UserTools.changeFamilyName(id_user, nom, connection);
			
			if(!(email.compareTo("") == 0))
				UserTools.changeEmail(id_user, email, connection);
		
		} catch (SQLException e) {
			return ErrorJSON.serviceRefused(ErrorTools.MESSAGE_ERROR_SQL, ErrorTools.CODE_ERROR_SQL);
			
		} finally {
			if(connection !=null)
				try {
					connection.close();
				} catch (SQLException e) {
					return ErrorJSON.serviceRefused(ErrorTools.MESSAGE_ERROR_SQL, ErrorTools.CODE_ERROR_SQL);
				}

		}
		return ErrorJSON.serviceAccepted("You have successfully edited your account");
	}

	public static JSONObject changePassword(String old_password, String new_password, String key_session) {
		if(key_session==null || old_password ==null || new_password ==null )
			return ErrorJSON.serviceRefused(ErrorTools.MESSAGE_MISSING_PARAMETERS,ErrorTools.CODE_MISSING_PARAMETERS);

		Connection  connection=null;

		try {
			connection=Database.getMySQLConnection();
			
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
			
			if(!UserTools.checkPassword(login, old_password, connection))
				return ErrorJSON.serviceRefused(ErrorTools.MESSAGE_INCORRECT_PASSWORD,ErrorTools.CODE_INCORRECT_PASSWORD);

			if(new_password.compareTo(old_password)== 0)
				return ErrorJSON.serviceRefused(ErrorTools.MESSAGE_SAME_PASSWORD, ErrorTools.CODE_SAME_PASSWORD);
			
			UserTools.changePassword(id_user, new_password, connection);

		} catch (SQLException e) {
			return ErrorJSON.serviceRefused(ErrorTools.MESSAGE_ERROR_SQL, ErrorTools.CODE_ERROR_SQL);
			
		} finally {
			if(connection !=null)
				try {
					connection.close();

				} catch (SQLException e) {
					return ErrorJSON.serviceRefused(ErrorTools.MESSAGE_ERROR_SQL, ErrorTools.CODE_ERROR_SQL);
				}

		}
		return ErrorJSON.serviceAccepted("You have successfully edited your password");
	}

	public static JSONObject profileUser(String login, String key_session) {

		if( key_session == null)
			return ErrorJSON.serviceRefused(ErrorTools.MESSAGE_MISSING_PARAMETERS,ErrorTools.CODE_MISSING_PARAMETERS);

		Connection connection=null;
		try {
			
			connection=Database.getMySQLConnection();
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
			
			// Si le login est null, on suppose que c'est pour l'user connecté.
			String idUserbyKeySession = SessionTools.getIdUser(key_session, connection);
			if(login == null) {
				return UserTools.getProfilebyLogin(idUserbyKeySession, connection, commentCollection);
			}
			
			if(login != null)
				if(!UserTools.userExists(login, connection))
					return ErrorJSON.serviceRefused(ErrorTools.MESSAGE_USER_DOES_NOT_EXIST,ErrorTools.CODE_USER_DOES_NOT_EXIST);
						
			// On récupére le profile
			String id_user = UserTools.getIdUser(login, connection);
			return UserTools.getProfilebyLogin(id_user,idUserbyKeySession, connection, commentCollection);
		
		} catch (SQLException e) {
			e.printStackTrace();
			return ErrorJSON.serviceRefused(ErrorTools.MESSAGE_ERROR_SQL, ErrorTools.CODE_ERROR_SQL);
			
		} catch (JSONException e) {
			return ErrorJSON.serviceRefused(ErrorTools.MESSAGE_ERROR_JSON, ErrorTools.CODE_ERROR_JSON);
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

	public static JSONObject SearchUser(String recherche, String key_session) {

		if( key_session == null || recherche ==null)
			return ErrorJSON.serviceRefused(ErrorTools.MESSAGE_MISSING_PARAMETERS,ErrorTools.CODE_MISSING_PARAMETERS);

		Connection connection=null;
		try {
			
			connection=Database.getMySQLConnection();
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
			
			return UserTools.SearchUser(recherche, connection, commentCollection);
		
		} catch (SQLException e) {
			return ErrorJSON.serviceRefused(ErrorTools.MESSAGE_ERROR_SQL, ErrorTools.CODE_ERROR_SQL);
		} catch (JSONException e) {
			return ErrorJSON.serviceRefused(ErrorTools.MESSAGE_ERROR_JSON, ErrorTools.CODE_ERROR_JSON);
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

	public static JSONObject recoverMail(String email, String login) {
		if( email == null || login ==null)
			return ErrorJSON.serviceRefused(ErrorTools.MESSAGE_MISSING_PARAMETERS,ErrorTools.CODE_MISSING_PARAMETERS);
		
		Connection connection=null;
		
		try {
		connection=Database.getMySQLConnection();
		
		if(!UserTools.CheckEmail(email,login, connection))
			return ErrorJSON.serviceRefused(ErrorTools.MESSAGE_MAIL_DOES_NOT_EXIST,ErrorTools.CODE_MAIL_DOES_NOT_EXIST);
		
		String id_user = UserTools.getIdUser(login, connection);
		String password = UserTools.getPassword(id_user, connection);
		
		MailTools.sendemail(email, password);
				
		} catch (SQLException e) {
			e.printStackTrace();
			return ErrorJSON.serviceRefused(ErrorTools.MESSAGE_ERROR_SQL, ErrorTools.CODE_ERROR_SQL);
			
		} catch (MessagingException e) {
			return ErrorJSON.serviceRefused(ErrorTools.MESSAGE_ERROR_EMAIL, ErrorTools.CODE_ERROR_EMAIL);
			
		} finally {

			if(connection!=null)
				try {
					connection.close();
				} catch (SQLException e) {
					return ErrorJSON.serviceRefused(ErrorTools.MESSAGE_ERROR_SQL, ErrorTools.CODE_ERROR_SQL);
				}
		}
		return ErrorJSON.serviceAccepted("You will receive a mail");

	}



}
