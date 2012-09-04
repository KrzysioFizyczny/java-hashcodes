package org.krzysio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class DbOperations {

	void close(Statement statement, ResultSet resultSet, Connection connection) {
		if (statement != null) {
			try {
				if (!statement.isClosed()) {
					statement.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		if (resultSet != null) {
			try {
				if (!resultSet.isClosed()) {
					resultSet.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		if (connection != null) {
			try {
				if (!connection.isClosed()) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	protected abstract Connection getDbConnection() throws SQLException;
	
	public abstract String getDbName();

	public String getLastString() {
		PreparedStatement preparedStatement = null;
		Connection connection = null;
		ResultSet resultSet = null;
		String lastString = "";

		try {
			connection = getDbConnection();
			String getLastStringQuery = "select s.string from hashcodes.strings s where s.id = (select max(id) from hashcodes.strings)";

			preparedStatement = connection.prepareStatement(getLastStringQuery);
			resultSet = preparedStatement.executeQuery();
			
			if (resultSet.next()) {
				lastString = resultSet.getString(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			close(preparedStatement, resultSet, connection);
		}

		return lastString;
	}
	
	public boolean saveNextStrings(String[] strings) {
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		replaceToInsertStatements(strings);
		
		try {
			connection = getDbConnection();
			connection.setAutoCommit(false);
			connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
			
			statement = connection.createStatement();
			
			for (String string : strings) {
				statement.addBatch(string);
			}

			statement.executeBatch();
			connection.commit();
			return true;
		} catch (SQLException e) {
			try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
			return false;
		} finally {
			close(statement, resultSet, connection);
		}
	}
	
	private void replaceToInsertStatements(String[] strings) {
		for (int i = 0; i < strings.length; i++) {
			strings[i] = String.format("insert into strings(string, hash_code) values ('%s', %d)", strings[i], strings[i].hashCode());
		}
	}
	
	
}
