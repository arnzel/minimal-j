package org.minimalj.backend;

import java.util.List;

import org.minimalj.transaction.criteria.Criteria;

/**
 * The common interface of all types of persistences. Note that specific implementations
 * can have more methods. See for example the <code>execute</code> methods in SqlPersistence
 *
 */
public interface Persistence {

	public <T> T read(Class<T> clazz, Object id);

	public <T> List<T> read(Class<T> clazz, Criteria criteria, int maxResults);

	public <T> Object insert(T object);

	public <T> void update(T object);

	public <T> void delete(Class<T> clazz, Object id);
	
}
