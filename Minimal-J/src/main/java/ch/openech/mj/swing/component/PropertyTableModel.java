package ch.openech.mj.swing.component;

import java.util.Collections;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import ch.openech.mj.model.PropertyInterface;
import ch.openech.mj.resources.Resources;

public class PropertyTableModel<T> extends AbstractTableModel {
		private final List<PropertyInterface> properties;
		private List<T> list = Collections.emptyList();

		public PropertyTableModel(List<PropertyInterface> properties) {
			this.properties = properties;
		}
		
		public void setObjects(List<T> bookList) {
			this.list = bookList;
			fireTableDataChanged();
		}
		
		public T getObject(int row) {
			return list.get(row);
		}

		@Override
		public boolean isCellEditable(int row, int column) {
			return false;
		}

		@Override
		public String getColumnName(int column) {
			PropertyInterface property = properties.get(column);
			return Resources.getObjectFieldName(Resources.getResourceBundle(), property);
		}

		@Override
		public Object getValueAt(int row, int column) {
			T object = list.get(row);
			PropertyInterface property = properties.get(column);
			return property.getValue(object);
		}

		@Override
		public int getRowCount() {
			return list.size();
		}

		@Override
		public int getColumnCount() {
			return properties.size();
		}

		@Override
		public Class<?> getColumnClass(int columnIndex) {
			return properties.get(columnIndex).getFieldClazz();
		}

		public T getRow(int row) {
			return list.get(row);
		}
	}