package org.krzysio;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class OracleOperations extends DbOperations {

	@Override
	protected Connection getDbConnection() throws SQLException {
		return DriverManager.getConnection(
				"jdbc:oracle:thin:@//localhost:1521/xe", "hashcodes", "root");
	}

	@Override
	public String getDbName() {
		return "Oracle";
	}

}
