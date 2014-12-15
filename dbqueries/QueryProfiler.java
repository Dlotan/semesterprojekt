package dbqueries;

import java.sql.Connection;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

public class QueryProfiler {
	private final Connection conn;
	private Statement stmt = null;
	public QueryProfiler(Connection conn) {
		this.conn = conn;
	}
	private long profileQuery(Query query) {
		String statement = query.getQueryString();
		long querytime = 0;
		try{
			stmt = conn.createStatement();
			long before = System.currentTimeMillis();
			stmt.executeQuery(statement);
			querytime = System.currentTimeMillis() - before;
			stmt.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return querytime;
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
