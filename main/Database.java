package main;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Vector;

public class Database {
	private String dbURL = null;
	public Connection conn = null;
	private Statement stmt = null;
	public static final String tableName = "T";
	public static final String attributeName = "A";
	public static final String indexName = "I";

	public Database(String databaseName) {
		try {
			Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
			// Get a connection.
			dbURL = "jdbc:derby:" + databaseName
					+ ";create=true;user=me;password=mine";
			conn = DriverManager.getConnection(dbURL);
			// Create Table if not already there.
			DatabaseMetaData dbm = conn.getMetaData();
			// check if "tableName" table is there
			ResultSet tables = dbm.getTables(null, null, tableName, null);
			if (tables.next() == false) {
				// Table doesn't exists.
				executeStatement("CREATE TABLE " + tableName + "("
						+ attributeName + " int)");
			}
		} catch (Exception except) {
			// except.printStackTrace();
		}
	}

	public void executeStatement(String statement) {
		System.out.println("Execute: " + statement);
		try {
			stmt = conn.createStatement();
			stmt.execute(statement);
			stmt.close();
		} catch (SQLException sqlExcept) {
			sqlExcept.printStackTrace();
		}
	}

	public void fill(List<Integer> numbers) {
		List<List<Integer>> subLists = new Vector<List<Integer>>();
		final int partitionSize = 2000;
		for (int i = 0; i < numbers.size(); i += partitionSize) {
			subLists.add(numbers.subList(i,
					i + Math.min(partitionSize, numbers.size() - i)));
		}
		String startQuery = "insert into " + tableName + "(" + attributeName
				+ ") Values(";
		for (List<Integer> subList : subLists) {
			Vector<String> stringList = new Vector<>();
			stringList.ensureCapacity(numbers.size());
			for (Integer number : subList) {
				stringList.add(Integer.toString(number));
			}
			String queries = String.join("),(", stringList);
			String finalQuery = startQuery + queries + ")";
			executeStatement(finalQuery);
		}
	}

	public void createIndex() {
		executeStatement("CREATE INDEX " + indexName + " on " + tableName
				+ " (" + attributeName + ")");
	}

	public void output() {
		try {
			stmt = conn.createStatement();
			ResultSet results = stmt.executeQuery("select * from " + tableName);
			ResultSetMetaData rsmd = results.getMetaData();
			int numberCols = rsmd.getColumnCount();
			for (int i = 1; i <= numberCols; i++) {
				// print Column Names
				System.out.print(rsmd.getColumnLabel(i) + "\t\t");
			}

			System.out.println("\n------------------");

			while (results.next()) {
				int num = results.getInt(1);
				System.out.println(num);
			}
			results.close();
			stmt.close();
		} catch (SQLException sqlExcept) {
			sqlExcept.printStackTrace();
		}
	}

	public void clear() {
		executeStatement("DELETE FROM " + tableName);
	}

	public void close() {
		try {
			if (stmt != null) {
				stmt.close();
			}
			if (conn != null) {
				DriverManager.getConnection(dbURL + ";shutdown=true");
				conn.close();
			}
		} catch (SQLException sqlExcept) {

		}
	}
}
