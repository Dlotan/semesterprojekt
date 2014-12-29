package dbqueries;

import main.Database;

public class RangeQuery implements Query {
	private final int resultSize;
	private final int lowerBound;
	private final int upperBound;
	private final Database database;
	public RangeQuery(Database database, int lowerBound, int upperBound, int resultSize) {
		assert(lowerBound <= upperBound);
		this.database = database;
		this.resultSize = resultSize;
		this.lowerBound = lowerBound;
		this.upperBound = upperBound;
	}

	@Override
	public String getQueryString() {
		return "SELECT * FROM " + database.getTableName() + " WHERE " + Database.attributeName + " BETWEEN " + lowerBound 
			+ " AND " + upperBound;
	}

	@Override
	public int getResultSize() {
		return resultSize;
	}

}
