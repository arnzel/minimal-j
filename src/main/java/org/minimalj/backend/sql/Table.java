package org.minimalj.backend.sql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import org.minimalj.model.Code;
import org.minimalj.model.Keys;
import org.minimalj.model.annotation.Searched;
import org.minimalj.model.properties.FlatProperties;
import org.minimalj.model.properties.PropertyInterface;
import org.minimalj.transaction.criteria.Criteria;
import org.minimalj.transaction.criteria.Criteria.AndCriteria;
import org.minimalj.transaction.criteria.Criteria.OrCriteria;
import org.minimalj.transaction.criteria.FieldCriteria;
import org.minimalj.transaction.criteria.SearchCriteria;
import org.minimalj.util.GenericUtils;
import org.minimalj.util.IdUtils;
import org.minimalj.util.LoggingRuntimeException;

@SuppressWarnings("rawtypes")
public class Table<T> extends AbstractTable<T> {

	protected final PropertyInterface idProperty;
	
	protected final String selectByIdQuery;
	protected final String selectAllQuery;
	protected final String updateQuery;
	protected final String deleteQuery;

	protected final LinkedHashMap<PropertyInterface, ListTable> listTables;
	protected final List<AbstractTable> subTables = new ArrayList<>();
	
	public Table(SqlPersistence sqlPersistence, Class<T> clazz) {
		super(sqlPersistence, null, clazz);
		
		this.idProperty = FlatProperties.getProperty(clazz, "id", true);
		Objects.nonNull(idProperty);

		List<PropertyInterface> lists = FlatProperties.getListProperties(clazz);
		this.listTables = createListTables(lists);
		
		this.selectByIdQuery = selectByIdQuery();
		this.selectAllQuery = selectAllQuery();
		this.updateQuery = updateQuery();
		this.deleteQuery = deleteQuery();
	}
	
	@Override
	public void createTable(SqlSyntax syntax) {
		super.createTable(syntax);
		for (AbstractTable subTable : subTables) {
			subTable.createTable(syntax);
		}
	}

	@Override
	public void createIndexes(SqlSyntax syntax) {
		super.createIndexes(syntax);
		for (AbstractTable subTable : subTables) {
			subTable.createIndexes(syntax);
		}
	}

	@Override
	public void createConstraints(SqlSyntax syntax) {
		super.createConstraints(syntax);
		for (AbstractTable subTable : subTables) {
			subTable.createConstraints(syntax);
		}
	}
	
	public Object insert(T object) {
		try (PreparedStatement insertStatement = createStatement(sqlPersistence.getConnection(), insertQuery, true)) {
			Object id;
			if (IdUtils.hasId(object.getClass())) {
				id = IdUtils.getId(object);
				if (id == null) {
					id = IdUtils.createId();
					IdUtils.setId(object, id);
				}
			} else {
				id = IdUtils.createId();
			}
			setParameters(insertStatement, object, false, ParameterMode.INSERT, id);
			insertStatement.execute();
			insertLists(object);
			if (object instanceof Code) {
				sqlPersistence.invalidateCodeCache(object.getClass());
			}
			return id;
		} catch (SQLException x) {
			throw new LoggingRuntimeException(x, sqlLogger, "Couldn't insert in " + getTableName() + " with " + object);
		}
	}

	protected void insertLists(T object) {
		for (Entry<PropertyInterface, ListTable> listEntry : listTables.entrySet()) {
			List list = (List) listEntry.getKey().getValue(object);
			if (list != null && !list.isEmpty()) {
				listEntry.getValue().addAll(object, list);
			}
		}
	}

	public void delete(Object id) {
		try (PreparedStatement updateStatement = createStatement(sqlPersistence.getConnection(), deleteQuery, false)) {
			updateStatement.setObject(1, id);
			updateStatement.execute();
		} catch (SQLException x) {
			throw new LoggingRuntimeException(x, sqlLogger, "Couldn't delete " + getTableName() + " with ID " + id);
		}
	}
	
	private LinkedHashMap<PropertyInterface, ListTable> createListTables(List<PropertyInterface> listProperties) {
		LinkedHashMap<PropertyInterface, ListTable> listTables = new LinkedHashMap<>();
		for (PropertyInterface listProperty : listProperties) {
			ListTable listTable = createListTable(listProperty);
			listTables.put(listProperty, listTable);
			if (listTable instanceof AbstractTable) {
				subTables.add((AbstractTable) listTable);
			}
		}
		return listTables;
	}

	ListTable createListTable(PropertyInterface property) {
		Class<?> elementClass = GenericUtils.getGenericClass(property.getType());
		if (IdUtils.hasId(elementClass)) {
			sqlPersistence.addClass(elementClass);
			return new LazyListAdapter(sqlPersistence, sqlPersistence.getTable(elementClass), property.getPath());
		} else {
			return new EagerListTable(sqlPersistence, buildSubTableName(property), elementClass, idProperty);
		}
	}
	
	protected String buildSubTableName(PropertyInterface property) {
		return getTableName() + "__" + property.getName();
	}
	
	public void update(T object) {
		try (PreparedStatement updateStatement = createStatement(sqlPersistence.getConnection(), updateQuery, false)) {
			setParameters(updateStatement, object, false, ParameterMode.UPDATE, IdUtils.getId(object));
			updateStatement.execute();
			
			for (Entry<PropertyInterface, ListTable> listTableEntry : listTables.entrySet()) {
				List list  = (List) listTableEntry.getKey().getValue(object);
				listTableEntry.getValue().replaceAll(object, list);
			}
			
			if (object instanceof Code) {
				sqlPersistence.invalidateCodeCache(object.getClass());
			}
		} catch (SQLException x) {
			throw new LoggingRuntimeException(x, sqlLogger, "Couldn't update in " + getTableName() + " with " + object);
		}
	}

	public ListTable getListTable(String discriminator) {
		for (Map.Entry<PropertyInterface, ListTable> entry : listTables.entrySet()) {
			if (entry.getKey().getPath().equals(discriminator)) {
				return entry.getValue();
			}
		}
		return null;
	}
	
	public T read(Object id) {
		return read(id, true);
	}
	
	public T read(Object id, boolean complete) {
		try (PreparedStatement selectByIdStatement = createStatement(sqlPersistence.getConnection(), selectByIdQuery, false)) {
			selectByIdStatement.setObject(1, id);
			T object = executeSelect(selectByIdStatement);
			if (object != null) {
				if (complete) {
					loadLists(object);
				} else {
					IdUtils.setId(object, new ReadOnlyId(id));
				}
			}
			return object;
		} catch (SQLException x) {
			throw new LoggingRuntimeException(x, sqlLogger, "Couldn't read " + getTableName() + " with ID " + id);
		}
	}

	private List<String> getColumns(Object[] keys) {
		List<String> result = new ArrayList<>();
		PropertyInterface[] properties = Keys.getProperties(keys);
		for (Map.Entry<String, PropertyInterface> entry : columns.entrySet()) {
			PropertyInterface property = entry.getValue();
			for (PropertyInterface p : properties) {
				if (p.getPath().equals(property.getPath())) {
					result.add(entry.getKey());
				}
			}
		}
		return result;
	}

	public List<Object> whereClause(Criteria criteria) {
		List<Object> result;
		if (criteria instanceof AndCriteria) {
			AndCriteria andCriteria = (AndCriteria) criteria;
			result = combine(andCriteria.getCriterias(), "AND");
		} else if (criteria instanceof OrCriteria) {
			OrCriteria orCriteria = (OrCriteria) criteria;
			result = combine(orCriteria.getCriterias(), "OR");
		} else if (criteria instanceof FieldCriteria) {
			FieldCriteria fieldCriteria = (FieldCriteria) criteria;
			result = new ArrayList<>();
			PropertyInterface propertyInterface = Keys.getProperty(fieldCriteria.getKey());
			Object value = fieldCriteria.getValue();
			String term = whereStatement(propertyInterface.getPath(), fieldCriteria.getOperator());
			if (value != null && IdUtils.hasId(value.getClass())) {
				value = IdUtils.getId(value);
			}
			result.add(term);
			result.add(value);
		} else if (criteria instanceof SearchCriteria) {
			SearchCriteria searchCriteria = (SearchCriteria) criteria;
			result = new ArrayList<>();
			String search = convertUserSearch(searchCriteria.getQuery());
			String clause = "(";
			List<String> searchColumns = searchCriteria.getKeys() != null ? getColumns(searchCriteria.getKeys()) : findSearchColumns(clazz);
			boolean first = true;
			for (String column : searchColumns) {
				if (!first) {
					clause += " OR ";
				} else {
					first = false;
				}
				clause += column + (searchCriteria.isNotEqual() ? " NOT" : "") + " LIKE ?";
				result.add(search);
			}
			if (this instanceof HistorizedTable) {
				clause += ") and version = 0";
			} else {
				clause += ")";
			}
			result.add(0, clause); // insert at beginning
		} else if (criteria == null || criteria.getClass() == Criteria.class) {
			result = Collections.singletonList("1=1");
		} else {
			throw new IllegalArgumentException("Unknown criteria: " + criteria);
		}
		return result;
	}
	
	private List<Object> combine(List<Criteria> criterias, String operator) {
		if (criterias.isEmpty()) {
			return null;
		} else if (criterias.size() == 1) {
			return whereClause(criterias.get(0));
		} else {
			List<Object> whereClause = whereClause(criterias.get(0));
			String clause = "(" + whereClause.get(0);
			for (int i = 1; i<criterias.size(); i++) {
				List<Object> whereClause2 = whereClause(criterias.get(i));
				clause += " " + operator + " " + whereClause2.get(0);
				if (whereClause2.size() > 1) {
					whereClause.addAll(whereClause2.subList(1, whereClause2.size()));
				}
			}
			clause += ")";
			whereClause.set(0, clause); // replace
			return whereClause;
		}
	}
	
	public List<T> read(Criteria criteria, int maxResults) {
		List<Object> whereClause = whereClause(criteria);
		String query = "SELECT * FROM " + getTableName() + " WHERE " + whereClause.get(0);
		try (PreparedStatement statement = createStatement(sqlPersistence.getConnection(), query, false)) {
			for (int i = 1; i<whereClause.size(); i++) {
				helper.setParameter(statement, i, whereClause.get(i), null); // TODO property is not known here anymore. Set<enum> will fail
			}
			return executeSelectAll(statement, maxResults);
		} catch (SQLException e) {
			throw new LoggingRuntimeException(e, sqlLogger, "read with SimpleCriteria failed");
		}
	}
	
	public int count(Criteria criteria) {
		List<Object> whereClause = whereClause(criteria);
		String query = "SELECT COUNT(*) FROM " + getTableName() + " WHERE " + whereClause.get(0);
		try (PreparedStatement statement = createStatement(sqlPersistence.getConnection(), query, false)) {
			for (int i = 1; i<whereClause.size(); i++) {
				helper.setParameter(statement, i, whereClause.get(i), null);
			}
			try (ResultSet resultSet = statement.executeQuery()) {
				resultSet.next();
				return resultSet.getInt(1);
			}
		} catch (SQLException e) {
			throw new LoggingRuntimeException(e, sqlLogger, "read with SimpleCriteria failed");
		}
	}

	public <S> List<S> readView(Class<S> resultClass, Criteria criteria, int maxResults) {
		List<Object> whereClause = whereClause(criteria);
		String query = select(resultClass) + " WHERE " + whereClause.get(0);
		try (PreparedStatement statement = createStatement(sqlPersistence.getConnection(), query, false)) {
			for (int i = 1; i<whereClause.size(); i++) {
				statement.setObject(i, whereClause.get(i));
			}
			return executeSelectViewAll(resultClass, statement, maxResults);
		} catch (SQLException e) {
			throw new LoggingRuntimeException(e, sqlLogger, "read with SimpleCriteria failed");
		}
	}
	
	private String select(Class<?> resultClass) {
		String querySql = "select ID";
		Map<String, PropertyInterface> propertiesByColumns = sqlPersistence.findColumns(resultClass);
		for (String column : propertiesByColumns.keySet()) {
			querySql += ", ";
			querySql += column;
		}
		querySql += " from " + getTableName();
		return querySql;
	}
	
	protected <S> List<S> executeSelectViewAll(Class<S> resultClass, PreparedStatement preparedStatement, long maxResults) throws SQLException {
		List<S> result = new ArrayList<>();
		try (ResultSet resultSet = preparedStatement.executeQuery()) {
			Map<Class<?>, Map<Object, Object>> loadedReferences = new HashMap<>();
			while (resultSet.next() && result.size() < maxResults) {
				S resultObject = sqlPersistence.readResultSetRow(resultClass, resultSet, loadedReferences);
				result.add(resultObject);

				// TODO: load lists of view if executeSelectViewAll
				
//				Object id = IdUtils.getId(resultObject);
//				List<PropertyInterface> viewLists = findLists(resultClass);
//				for (PropertyInterface viewListProperty : viewLists) {
//					List list = (List) viewLists.get(listProperty).getValue(resultObject);
//					if (subTables.get(listProperty) instanceof EagerListTable) {
//						EagerListTable subTable = (EagerListTable) subTables.get(listProperty);
//						list.addAll(subTable.read(id));
//					} else if (subTables.get(listProperty) instanceof HistorizedEagerListTable) {
//						HistorizedEagerListTable subTable = (HistorizedEagerListTable) subTables.get(listProperty);
//						list.addAll(subTable.read(id, 0));
//					}
//				}
			}
		}
		return result;
	}
	
	public String convertUserSearch(String s) {
		s = s.replace('*', '%');
		return s;
	}
	
	private List<String> findSearchColumns(Class<?> clazz) {
		List<String> searchColumns = new ArrayList<>();
		for (Map.Entry<String, PropertyInterface> entry : columns.entrySet()) {
			PropertyInterface property = entry.getValue();
			Searched searchable = property.getAnnotation(Searched.class);
			if (searchable != null) {
				searchColumns.add(entry.getKey());
			}
		}
		if (searchColumns.isEmpty()) {
			throw new IllegalArgumentException("No fields are annotated as 'Searched' in " + clazz.getName());
		}
		return searchColumns;
	}
	
	@SuppressWarnings("unchecked")
	protected void loadLists(T object) throws SQLException {
		for (Entry<PropertyInterface, ListTable> listTableEntry : listTables.entrySet()) {
			List values = listTableEntry.getValue().read(object);
			PropertyInterface listProperty = listTableEntry.getKey();
			if (listProperty.isFinal()) {
				List list = (List) listProperty.getValue(object);
				list.clear();
				list.addAll(values);
			} else {
				listProperty.setValue(object, values);
			}
		}
	}
	
	// Statements

	@Override
	protected String selectByIdQuery() {
		StringBuilder query = new StringBuilder();
		query.append("SELECT * FROM ").append(getTableName()).append(" WHERE id = ?");
		return query.toString();
	}
	
	protected String selectAllQuery() {
		StringBuilder query = new StringBuilder();
		query.append("SELECT * FROM ").append(getTableName()); 
		return query.toString();
	}
	
	@Override
	protected String insertQuery() {
		StringBuilder s = new StringBuilder();
		
		s.append("INSERT INTO ").append(getTableName()).append(" (");
		for (String columnName : getColumns().keySet()) {
			s.append(columnName).append(", ");
		}
		s.append("id) VALUES (");
		for (int i = 0; i<getColumns().size(); i++) {
			s.append("?, ");
		}
		s.append("?)");

		return s.toString();
	}
	
	protected String updateQuery() {
		StringBuilder s = new StringBuilder();
		
		s.append("UPDATE ").append(getTableName()).append(" SET ");
		for (Object columnNameObject : getColumns().keySet()) {
			s.append((String) columnNameObject).append("= ?, ");
		}
		s.delete(s.length()-2, s.length());
		s.append(" WHERE id = ?");

		return s.toString();
	}
	
	protected String deleteQuery() {
		StringBuilder s = new StringBuilder();
		s.append("DELETE FROM ").append(getTableName()).append(" WHERE id = ?");
		return s.toString();
	}
	
	@Override
	protected void addSpecialColumns(SqlSyntax syntax, StringBuilder s) {
		if (idProperty != null) {
			syntax.addIdColumn(s, idProperty);
		} else {
			syntax.addIdColumn(s, Object.class, 36);
		}
	}
	
	@Override
	protected void addPrimaryKey(SqlSyntax syntax, StringBuilder s) {
		syntax.addPrimaryKey(s, "id");
	}	
}
