package dbqueries;

import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import main.Database;

public class QueryProfiler {
	private Statement stmt = null;
	public static int meanIterations = 10;
	public static int warmupIterations = 10;
	final Database database;
	public QueryProfiler(Database database) {
		this.database = database;
	}
	private double profileQuery(Query query) {
		String statement = query.getQueryString();
		long querytime = 0;
		try{
			if(database.isConnected()) {
				database.disconnect();
			}
			// Warm up
			for(int i = 0; i < warmupIterations; i++) {
				database.connect();
				stmt = database.conn.createStatement();
				stmt.executeQuery(statement);
				stmt.close();
				stmt = null;
				database.disconnect();
			} 
			// Profile
			for(int i = 0; i < meanIterations; i++)
			{
				database.connect();
				stmt = database.conn.createStatement();
				long before = System.currentTimeMillis();
				stmt.executeQuery(statement);
				querytime += System.currentTimeMillis() - before;
				stmt.close();
				stmt = null;
				database.disconnect();
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return querytime / (meanIterations * 1.);
	}
	public List<Double> profileSingleQueries(List<SingleQuery> singleQueries) {
		System.gc();
		List<Double> result = new LinkedList<>();
		for(Query query : singleQueries) {
			result.add(profileQuery(query));
		}
		return result;
	}
	public List<Double> profileRangeQueries(List<RangeQuery> rangeQueries) {
		System.gc();
		List<Double> result = new LinkedList<>();
		for(Query query : rangeQueries) {
			result.add(profileQuery(query));
		}
		return result;
	}
}
