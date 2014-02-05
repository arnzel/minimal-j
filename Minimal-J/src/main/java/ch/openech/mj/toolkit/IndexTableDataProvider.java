package ch.openech.mj.toolkit;

import ch.openech.mj.model.Index;
import ch.openech.mj.toolkit.ClientToolkit.TableDataConsumer;
import ch.openech.mj.toolkit.ClientToolkit.TableDataProvider;

public class IndexTableDataProvider<T> implements TableDataProvider<T> {

	private final Index<T> index;
	private TableDataConsumer<T> consumer;
	
	public IndexTableDataProvider(Index<T> index, String text) {
		this.index = index;
		find(text);
	}

	private void find(String text) {
	}

	@Override
	public void setConsumer(TableDataConsumer<T> consumer) {
		this.consumer = consumer;
	}

	@Override
	public int getRowCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void requestRows(int firstRow, int size) {
	}

}
