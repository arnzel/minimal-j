package ch.openech.mj.page;

import ch.openech.mj.model.Index;
import ch.openech.mj.toolkit.ClientToolkit;
import ch.openech.mj.toolkit.IComponent;
import ch.openech.mj.toolkit.ITable;
import ch.openech.mj.toolkit.ITable.TableActionListener;
import ch.openech.mj.toolkit.IndexTableDataProvider;

/**
 * Shows a table of objects of one class. 
 *
 * @param <T> Class of objects in this overview
 */
public abstract class TablePage<T> extends AbstractPage implements TableActionListener, RefreshablePage {

	private final IndexTableDataProvider<T> tableDataProvider;
	private final ITable<T> table;

	public TablePage(PageContext context, Index index, Object[] keys, String text) {
		super(context);
		tableDataProvider = new IndexTableDataProvider(index, text);
		table = ClientToolkit.getToolkit().createTable(tableDataProvider, keys);
		table.setClickListener(this);
		refresh();
	}

	protected ITable<T> getTable() {
		return table;
	}

	@Override
	public abstract void action();

	@Override
	public IComponent getComponent() {
		return table;
	}
	
	@Override
	public void refresh() {
		tableDataProvider.refresh();
	}
}
