package tools;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Random;

public class SessionTools {

	public static String getIdUser(String key_session, Connection connection) throws SQLException{

		String select = "SELECT id_user FROM session where key_session = ?";
		PreparedStatement preparedStatement=null;

		try {
			
			preparedStatement = connection.prepareStatement(select);

			preparedStatement.setString(1, key_session);

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

	public static void removeSession(String key_session, Connection connection) throws SQLException {
		
		String delete ="DELETE FROM Session WHERE key_session =  ?";
		
		PreparedStatement preparedStatement=null;
		try {
		preparedStatement = connection.prepareStatement(delete);
		
		preparedStatement.setString(1, key_session);

		preparedStatement.executeUpdate();

		
		}
		finally {
			if(preparedStatement!=null)
				preparedStatement.close();
		}

	}

	public static String insertNewSession(String id_user, Connection connection) throws SQLException{

		String key_session = generatekey();
		
		String insert="INSERT INTO Session"
						+ "(id_user, key_session, date_session) VALUES"
						+ "(?,?,?)";
		
		PreparedStatement preparedStatement=null;
		try {
		preparedStatement = connection.prepareStatement(insert);
		
		preparedStatement.setString(1, id_user);
		preparedStatement.setString(2, key_session);
		
		Timestamp dateDebutSession = new Timestamp(System.currentTimeMillis());
		preparedStatement.setTimestamp(3,dateDebutSession);
		
		preparedStatement.executeUpdate();

		return key_session;
		
		}
		finally {
			if(preparedStatement!=null)
				preparedStatement.close();
		}
		
	}

	public static boolean isConnected(String id_user, Connection connection) throws SQLException {
		String select = "SELECT * FROM session where id_user = ?";
		PreparedStatement preparedStatement=null;

		try {
			preparedStatement = connection.prepareStatement(select);

			preparedStatement.setString(1, id_user);

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

	public static boolean isConnectedByKey(String key_session, Connection connection) throws SQLException {
		
		String select = "SELECT * FROM session WHERE key_session = ?";
		PreparedStatement preparedStatement=null;

		try {
			preparedStatement = connection.prepareStatement(select);
			
			preparedStatement.setString(1, key_session);
			
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
	
	public static int updateTimeOut(String key_session,Connection connection) throws SQLException {
		String update = "UPDATE session SET date_fin = ? WHERE key_session = ?";

		PreparedStatement preparedStatement=null;

		try {
			preparedStatement = connection.prepareStatement(update);
			
			Timestamp dateFinSession = new Timestamp(System.currentTimeMillis()+60*1000*10);
			preparedStatement.setTimestamp(1, dateFinSession);
			preparedStatement.setString(2, key_session);
			
			int res = preparedStatement.executeUpdate();
			
			return res;
		}

		finally {
			if(preparedStatement!=null)
				preparedStatement.close();
		}
		
	}
	
	public static boolean hasExceededTimeOut(String key_session,Connection connection) throws SQLException {
		String select = "SELECT date_fin FROM session WHERE key_session = ?";

		PreparedStatement preparedStatement=null;

		try {
			preparedStatement = connection.prepareStatement(select);
			
			
			preparedStatement.setString(1, key_session);
			
			ResultSet res = preparedStatement.executeQuery();
			
	        if(res.next()){
				Timestamp date_fin=res.getTimestamp(1);
				return date_fin.before(new Timestamp(System.currentTimeMillis()));

	        }
	        
	        return false;
		}

		finally {
			if(preparedStatement!=null)
				preparedStatement.close();
		}
		
	}
	
	
	private static String generatekey() {
		String code = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
		String key = "";
		for (int i=0; i<32; i++) {
			// 32 longueur de la clé
			int j = new Random().nextInt(code.length());
			key += code.charAt(j);
		}
		return key;
	}

}
