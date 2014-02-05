package ch.openech.mj.db;

import java.util.List;

public interface Index<T> {

//	public String getColumn();

	public List<Integer> search(Object query, int maxObjects);

	public T lookup(Integer id);

}