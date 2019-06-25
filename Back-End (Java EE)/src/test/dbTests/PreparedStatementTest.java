package test.dbTests;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;

import db.Database;

public class PreparedStatementTest {

	public static void main(String[] args) {
		Connection connection;
		try {
			connection = Database.getMySQLConnection();

		Statement insertion=connection.createStatement();
		
		String insert="INSERT INTO USER"
				+ "(id_user) VALUES"
				+ "(?)";
		
		PreparedStatement preparedStatement = connection.prepareStatement(insert);
		
		preparedStatement.setInt(1, 5);
	
		// execute insert SQL stetement
		preparedStatement .executeUpdate();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
