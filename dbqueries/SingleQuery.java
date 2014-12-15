package dbqueries;

import main.Database;

public class SingleQuery implements Query {
	private int element;
	private boolean hit;
	public SingleQuery(int element, boolean hit) {
		this.element = element;
		this.hit = hit;
	}

	@Override
	public String getQueryString() {
		return "SELECT * FROM " + Database.tableName + " WHERE a = " + element;
	}

	@Override
	public int getResultSize() {
		if(hit) {
			return 1;
		}
		return 0;
	}

}
