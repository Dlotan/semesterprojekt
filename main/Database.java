package main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class Database {
	private String dbURL = null;
	public Connection conn = null;
	private Statement stmt = null;
	public static String databaseName = "D";
	public static final String attributeName = "A";
	private final String tableName;
	private final String indexName;
	private boolean connected = false;
	public Database(String databaseName, String tableName, String indexName) {
		this.tableName = tableName;
		this.indexName = indexName;
	}
	public Database(String databaseName, String generatorName) {
		tableName = "T" + generatorName;
		indexName = "I" + generatorName;
	}
	
	public boolean isConnected() {
		return connected;
	}
	
	public String getTableName() {
		return tableName;
	}

	public void executeStatement(String statement) throws DatabaseNotConnectedException{
		if(connected == false) {
			throw new DatabaseNotConnectedException();
		}
		System.out.println("Execute: " + statement);
		try {
			stmt = conn.createStatement();
			stmt.execute(statement);
			stmt.close();
		} catch (SQLException sqlExcept) {
			sqlExcept.printStackTrace();
		}
	}

	public void fill(List<Integer> numbers) throws DatabaseNotConnectedException{
		if(connected == false) {
			throw new DatabaseNotConnectedException();
		}
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

	public void createIndex() throws DatabaseNotConnectedException{
		if(connected == false) {
			throw new DatabaseNotConnectedException();
		}
		executeStatement("CREATE INDEX " + indexName + " on " + tableName
				+ " (" + attributeName + ")");
	}

	public void output() throws DatabaseNotConnectedException{
		if(connected == false) {
			throw new DatabaseNotConnectedException();
		}
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
	
	public List<Integer> getNumbers() throws DatabaseNotConnectedException{
		if(connected == false) {
			throw new DatabaseNotConnectedException();
		}
		List<Integer> result = new ArrayList<>();
		try {
			stmt = conn.createStatement();
			ResultSet results = stmt.executeQuery("select * from " + tableName);
			while (results.next()) {
				int num = results.getInt(1);
				result.add(num);
			}
			results.close();
			stmt.close();
		} catch (SQLException sqlExcept) {
			sqlExcept.printStackTrace();
		}
		return result;
	}

	public void clear() throws DatabaseNotConnectedException{
		if(connected == false) {
			throw new DatabaseNotConnectedException();
		}
		try{
			executeStatement("DROP TABLE " + tableName);
			executeStatement("CREATE TABLE " + tableName + "("
					+ attributeName + " int)");
		} catch(Exception e) {
			
		}
	}
	
	public void connect() {
		try {
			Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
			// Get a connection.
			dbURL = "jdbc:derby:" + databaseName
					+ ";create=true;user=me;password=mine";
			conn = DriverManager.getConnection(dbURL);
		} catch (Exception e) {
			e.printStackTrace();
		}
		connected = true;
	}

	public void disconnect() throws DatabaseNotConnectedException{
		if(connected == false) {
			throw new DatabaseNotConnectedException();
		}
		try {
			if (stmt != null) {
				stmt.close();
				stmt = null;
			}
			if (conn != null) {
				DriverManager.getConnection(dbURL + ";shutdown=true");
				conn.close();
				conn = null;
			}
		} catch (SQLException sqlExcept) {

		}
		connected = false;
	}
}
