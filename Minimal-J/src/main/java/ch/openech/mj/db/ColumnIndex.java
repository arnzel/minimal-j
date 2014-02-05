package ch.openech.mj.db;//

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ch.openech.mj.model.PropertyInterface;
import ch.openech.mj.util.LoggingRuntimeException;


public class ColumnIndex<T> extends DbIndex<T> {

	private final ColumnIndex<?> innerIndex;
	
	ColumnIndex(DbPersistence dbPersistence, AbstractTable<T> table, PropertyInterface property, String column, ColumnIndex<?> innerIndex) {
		super(dbPersistence, table, property, column);
		this.innerIndex = innerIndex;
	}

	private List<Integer> findIds(List<Integer> ids) throws SQLException {
		PreparedStatement selectStatement = table.getStatement(dbPersistence.getConnection(), selectQuery, false);
		List<Integer> result = new ArrayList<>(ids.size());
		for (Integer i : ids) {
			helper.setParameter(selectStatement, 1, i, property);
			result.addAll(executeSelectIds(selectStatement));
		}
		return result;
	}
	
	@Override
	public List<Integer> search(Object query, int maxObjects) {
		Connection connection = dbPersistence.getConnection();
		try {
			if (innerIndex != null) {
				List<Integer> queryIds = innerIndex.search(query, maxObjects);
				return findIds(queryIds);
			}
			PreparedStatement selectStatement = table.getStatement(connection, selectQuery, false);
			helper.setParameter(selectStatement, 1, query, property);
			List<Integer> result = executeSelectIds(selectStatement);
			return result;
		} catch (SQLException x) {
			String message = "Couldn't use index of column " + column + " of table " + table.getTableName() + " with query " + query;
			throw new LoggingRuntimeException(x, sqlLogger, message);
		}
	}

	public List<T> findObjects(Object query) {
		List<Integer> ids = search(query, 0);
		List<T> result = new ArrayList<>(ids.size());
		for (Integer id : ids) {
			result.add(lookup(id));
		}
		return result;
	}
	
	private List<Integer> executeSelectIds(PreparedStatement preparedStatement) throws SQLException {
		List<Integer> result = new ArrayList<>();
		try (ResultSet resultSet = preparedStatement.executeQuery()) {
			while (resultSet.next()) {
				result.add(resultSet.getInt(1));
			}
		}
		return result;
	}

}
