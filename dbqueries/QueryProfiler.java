package dbqueries;

import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import main.Database;

public class QueryProfiler {
	private Statement stmt = null;
	final int iterations = 5;
	final int warmupIterations = 3;
	public QueryProfiler() {
		
	}
	private long profileQuery(Query query) {
		String statement = query.getQueryString();
		long querytime = 0;
		try{
			// Warmup
			for(int i = 0; i < warmupIterations; i++) {
				Database database = new Database(Database.databaseName);
				stmt = database.conn.createStatement();
				stmt.executeQuery(statement);
				stmt.close();
				database.close();
			} 
			for(int i = 0; i < iterations; i++)
			{
				Database database = new Database(Database.databaseName);
				stmt = database.conn.createStatement();
				long before = System.nanoTime();
				stmt.executeQuery(statement);
				querytime += System.nanoTime() - before;
				stmt.close();
				database.close();
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return querytime / iterations;
	}
	public List<Long> profileSingleQueries(List<SingleQuery> singleQueries) {
		List<Long> result = new LinkedList<>();
		for(Query query : singleQueries) {
			result.add(profileQuery(query));
		}
		return result;
	}
	public List<Long> profileRangeQueries(List<RangeQuery> rangeQueries) {
		List<Long> result = new LinkedList<>();
		for(Query query : rangeQueries) {
			result.add(profileQuery(query));
		}
		return result;
	}
}
