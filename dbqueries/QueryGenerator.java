package dbqueries;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import main.Database;

public class QueryGenerator {
	//private List<Integer> numbers;
	private ArrayList<Integer> sortedNumbers;
	private final int min; // Should be 0.
	private final int max; // Should be 1 billion.
	private final Database database;
	public QueryGenerator(Database database, List<Integer> integerNumbers) {
		this.database = database;
		sortedNumbers = new ArrayList<Integer>();
		sortedNumbers.ensureCapacity(integerNumbers.size());
		for(int number : integerNumbers) {
			sortedNumbers.add(number);
		}
		Collections.sort(sortedNumbers);
		min = sortedNumbers.firstElement();
		max = sortedNumbers.lastElement();
	}
	private int closest(int of) {
	    int min = Integer.MAX_VALUE;
	    int closest = of;
	    for (int v : sortedNumbers) {
	        final int diff = Math.abs(v - of);

	        if (diff < min) {
	            min = diff;
	            closest = v;
	        }
	    }
	    return closest;
	}
	public List<SingleQuery> getSingleQueries() {
		List<SingleQuery> result = new LinkedList<>();
		// First Element.
		result.add(new SingleQuery(database, min, true));
		// Last Element.
		result.add(new SingleQuery(database, max, true));
		// Mid Element.
		result.add(new SingleQuery(database, sortedNumbers.elementAt(sortedNumbers.size() / 2), true));
		// Mid range element.
		final int midRange = (max - min) / 2;
		result.add(new SingleQuery(database, closest(midRange), true));
		// Miss first.
		int i = min + 1;
		while(Collections.binarySearch(sortedNumbers, i) >= 0) {
			i++;
		}
		result.add(new SingleQuery(database, i, false));
		// Miss last.
		i = max - 1;
		while(Collections.binarySearch(sortedNumbers, i) >= 0) {
			i--;
		}
		result.add(new SingleQuery(database, i, false));
		// Miss element near mid element.
		i = sortedNumbers.elementAt(sortedNumbers.size() / 2);
		while(Collections.binarySearch(sortedNumbers, i) >= 0) {
			i++;
		}
		result.add(new SingleQuery(database, i, false));
		// Miss element near mid range.
		i = midRange;
		while(Collections.binarySearch(sortedNumbers, i) >= 0) {
			i++;
		}
		result.add(new SingleQuery(database, i, false));
		return result;
	}
	private int getResultSize(int min, int max) {
		// Can be optimized.
		int count = 0;
		for(int number : sortedNumbers) {
			if(number > max) {
				break;
			}
			if(number >= min) {
				count++;
			}
		}
		return count;
	}
	public List<RangeQuery> getRangeQueries() {
		List<RangeQuery> result = new LinkedList<>();
		// Full Range.
		result.add(new RangeQuery(database, min, max, getResultSize(min, max)));
		// Range without upper and lower.
		int i,j;
		i = min + 1;
		j = max - 1;
		result.add(new RangeQuery(database, i, j, getResultSize(i, j)));
		// Min to mid range.
		i = min;
		j = (max - min) / 2;
		result.add(new RangeQuery(database, i, j, getResultSize(i, j)));
		// Mid range to max.
		i = (max - min) / 2;
		j = max;
		result.add(new RangeQuery(database, i, j, getResultSize(i, j)));
		// Min to mid element.
		i = min;
		j = sortedNumbers.elementAt(sortedNumbers.size() / 2);
		result.add(new RangeQuery(database, i, j, getResultSize(i, j)));
		// Mid to max element.
		i = sortedNumbers.elementAt(sortedNumbers.size() / 2);
		j = max;
		result.add(new RangeQuery(database, i, j, getResultSize(i, j)));
		// First 10 elements.
		i = min;
		j = sortedNumbers.elementAt(9);
		result.add(new RangeQuery(database, i, j, 10));
		// Mid 11 elements.
		i = sortedNumbers.elementAt((sortedNumbers.size() / 2) - 5);
		j = sortedNumbers.elementAt((sortedNumbers.size() / 2) + 5);
		result.add(new RangeQuery(database, i, j, 11));
		// Range without result.
		int hit = sortedNumbers.elementAt(sortedNumbers.size() / 4);
		j = hit - 1;
		// Don't hit element next to j
		while(Collections.binarySearch(sortedNumbers, j) >= 0) {
			j--;
		}
		hit = sortedNumbers.elementAt((sortedNumbers.size() / 4) - 1);
		i = hit + 1;
		// Search from lower hit.
		while(Collections.binarySearch(sortedNumbers, j) >= 0) {
			i++;
		}
		if(i > j) {
			result.add(new RangeQuery(database, i, i, 0));
		}
		else {
			result.add(new RangeQuery(database, i, j, 0));
		}
		// Only one element.
		i = sortedNumbers.elementAt(((sortedNumbers.size() / 4) * 3) + 1);
		j = sortedNumbers.elementAt(((sortedNumbers.size() / 4) * 3) + 1);
		result.add(new RangeQuery(database, i, j, 1));
		return result;
	}
}
