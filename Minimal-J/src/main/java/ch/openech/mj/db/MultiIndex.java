package ch.openech.mj.db;

import java.util.ArrayList;
import java.util.List;

public class MultiIndex<T> implements Index<T> {

	private final ColumnIndex<T>[] indexes;
	
	public MultiIndex(ColumnIndex<T>[] indexes) {
		this.indexes = indexes;
	}

	public List<T> findObjects(Object query) {
		List<T> result = new ArrayList<>(50);
		for (ColumnIndex<T> index : indexes) {
			List<T> objects = index.findObjects(query);
			for (T object : objects) {
				if (!result.contains(object)) {
					result.add(object);
				}
			}
		}
		return result;
	}
	
	@Override
	public List<Integer> search(Object query, int maxObjects) {
		List<Integer> result = new ArrayList<>(50);
		for (ColumnIndex<T> index : indexes) {
			List<Integer> ids = index.search(query, 0);
			for (Integer id : ids) {
				if (!result.contains(id)) {
					result.add(id);
				}
			}
		}
		return result;
	}
	
	@Override
	public T lookup(Integer id) {
		return indexes[0].lookup(id);
	}
	
}
