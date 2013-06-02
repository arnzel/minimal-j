package ch.openech.mj.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import ch.openech.mj.db.model.ColumnProperties;

/**
 * Minimal-J internal<p>
 * 
 * HistorizedTable has one version columns, HistorizedSubTable hast two.
 * One for startVersion and one vor endVersion.<p>
 * 
 * For a new Entry there is startVersion and endVersion = 0.<p>
 * 
 * After an update endVersion contains the version from which the
 * entry is <i>not</i> valid anymore. The startVersion of the new
 * row contains the version from which the entry is valid.
 * 
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class HistorizedSubTable extends AbstractTable {

	private PreparedStatement selectByIdAndTimeStatement;
	private PreparedStatement endStatement;
	private PreparedStatement readVersionsStatement;
	
	public HistorizedSubTable(DbPersistence dbPersistence, String prefix, Class clazz) {
		super(dbPersistence, prefix, clazz);
	}
	
	@Override
	public void initialize() throws SQLException {
		super.initialize();
		selectByIdAndTimeStatement = prepare(selectByIdAndTimeQuery());
		readVersionsStatement = prepare(readVersionsQuery());
		endStatement = prepare(endQuery());
	}
	
	@Override
	public void closeStatements() throws SQLException {
		super.closeStatements();
		selectByIdAndTimeStatement.close();
		endStatement.close();
		readVersionsStatement.close();
	}
	
	public void insert(int parentId, List objects, Integer version) throws SQLException {
		for (int position = 0; position<objects.size(); position++) {
			Object object = objects.get(position);
			int parameterPos = setParameters(insertStatement, object, false, true);
			setParameterInt(insertStatement, parameterPos++, parentId);
			setParameterInt(insertStatement, parameterPos++, position);
			setParameterInt(insertStatement, parameterPos++, version);
			insertStatement.execute();
		}
	}

	public void update(int parentId, List objects, int version) throws SQLException {
		List objectsInDb = read(parentId, version);
		int position = 0;
		while (position < Math.max(objects.size(), objectsInDb.size())) {
			boolean end = false;
			boolean insert = false;
			if (position < objectsInDb.size() && position < objects.size()) {
				Object object = objects.get(position);
				Object objectInDb = objectsInDb.get(position);
				end = insert = !ColumnProperties.equals(object, objectInDb);
			} else if (position < objectsInDb.size()) {
				end = true;
			} else /* if (position < objects.size()) */ {
				insert = true;
			}
			
			if (end) {
				endStatement.setInt(1, version);
				endStatement.setInt(2, parentId);
				endStatement.setInt(3, position);
				endStatement.execute();	
			}
			
			if (insert) {
				int parameterPos = setParameters(insertStatement, objects.get(position), false, true);
				setParameterInt(insertStatement, parameterPos++, parentId);
				setParameterInt(insertStatement, parameterPos++, position);
				setParameterInt(insertStatement, parameterPos++, version);
				insertStatement.execute();
			}
			position++;
		}
	}

	public List read(int parentId, Integer time) throws SQLException {
		if (time == null) {
			return read(parentId);
		}
		selectByIdAndTimeStatement.setInt(1, parentId);
		selectByIdAndTimeStatement.setInt(2, time);
		selectByIdAndTimeStatement.setInt(3, time);
		return executeSelectAll(selectByIdAndTimeStatement);
	}

	private List read(int id) throws SQLException {
		selectByIdStatement.setInt(1, id);
		return executeSelectAll(selectByIdStatement);
	}
	
	public void readVersions(int parentId, List<Integer> result) throws SQLException {
		readVersionsStatement.setInt(1, parentId);
		try (ResultSet resultSet = readVersionsStatement.executeQuery()) {
			while (resultSet.next()) {
				int version = resultSet.getInt(1);
				if (!result.contains(version)) result.add(version);
				int endVersion = resultSet.getInt(2);
				if (!result.contains(version)) result.add(endVersion);
			}
		}
	}
	
	// Queries
	
	protected String selectByIdAndTimeQuery() {
		StringBuilder query = new StringBuilder();
		query.append("SELECT * FROM "); query.append(getTableName()); 
		query.append(" WHERE id = ? AND (startVersion = 0 OR startVersion < ?) AND (endVersion = 0 OR endVersion >= ?) ORDER BY position");
		return query.toString();
	}
	
	@Override
	protected String selectByIdQuery() {
		StringBuilder query = new StringBuilder();
		query.append("SELECT * FROM "); query.append(getTableName()); query.append(" WHERE id = ?");
		query.append(" AND endVersion = 0 ORDER BY position");
		return query.toString();
	}
	
	@Override
	protected String insertQuery() {
		StringBuilder s = new StringBuilder();
		
		s.append("INSERT INTO "); s.append(getTableName()); s.append(" (");
		for (Object columnNameObject : columnNames) {
			// myst, direkt auf columnNames zugreiffen funktionert hier nicht
			String columnName = (String) columnNameObject;
			s.append(columnName);
			s.append(", ");
		}
		s.append("id, position, startVersion, endVersion) VALUES (");
		for (int i = 0; i<columnNames.size(); i++) {
			s.append("?, ");
		}
		s.append("?, ?, ?, 0)");

		return s.toString();
	}
	
	private String endQuery() {
		StringBuilder s = new StringBuilder();
		s.append("UPDATE "); s.append(getTableName()); s.append(" SET endVersion = ? WHERE id = ? AND position = ? AND endVersion = 0");
		return s.toString();
	}
	
	private String readVersionsQuery() {
		StringBuilder s = new StringBuilder();
		
		s.append("SELECT startVersion, endVersion FROM "); s.append(getTableName()); 
		s.append(" WHERE id = ?");

		return s.toString();
	}
	

}