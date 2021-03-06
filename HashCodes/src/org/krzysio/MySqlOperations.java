package org.krzysio;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author kbarczynski
 */
public class MySqlOperations extends DbOperations {

	@Override
	protected Connection getDbConnection() throws SQLException {
		return DriverManager.getConnection("jdbc:mysql://localhost/hashcodes", "root", "");
	}

	@Override
	public String getDbName() {
		return "MySql";
	}

}
