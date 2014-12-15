package dbqueries;

import main.Database;

public class RangeQuery implements Query {
	final int resultSize;
	final int lowerBound;
	final int upperBound;
	public RangeQuery(int lowerBound, int upperBound, int resultSize) {
		assert(lowerBound <= upperBound);
		this.resultSize = resultSize;
		this.lowerBound = lowerBound;
		this.upperBound = upperBound;
	}

	@Override
	public String getQueryString() {
		return "SELECT * FROM " + Database.tableName + " WHERE a BETWEEN " + lowerBound 
			+ " AND " + upperBound;
	}

	@Override
	public int getResultSize() {
		return resultSize;
	}

}
