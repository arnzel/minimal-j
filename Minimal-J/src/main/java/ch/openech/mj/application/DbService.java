package ch.openech.mj.application;

import java.util.List;

import ch.openech.mj.model.Index;

public interface DbService {

//	public <T> T read(Class<T> clazz, int id);

	public <T> int insert(T object);

	public <T> void update(T object);

	public <T> void delete(T object);

	public <T> List<T> findList(Index<T> index, Object query);

	public <T> T find(Index<T> index, Object query);

	public <T> List<Integer> readVersions(T object);

	public <T> T read(T object, Integer time);
	
}
