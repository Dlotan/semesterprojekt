package dbqueries;

import main.Database;

public class SingleQuery implements Query {
	private int element;
	private boolean hit;
	private final Database database;
	public SingleQuery(Database database, int element, boolean hit) {
		this.database = database;
		this.element = element;
		this.hit = hit;
	}

	@Override
	public String getQueryString() {
		return "SELECT * FROM " + database.getTableName() + " WHERE " + Database.attributeName + " = " + element;
	}

	@Override
	public int getResultSize() {
		if(hit) {
			return 1;
		}
		return 0;
	}

}
