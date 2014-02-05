package ch.openech.mj.search;

import java.util.List;

public interface Search<T> {

	public T lookup(int id);

	public List<Integer> search(String text, int maxObjects);
	
}
