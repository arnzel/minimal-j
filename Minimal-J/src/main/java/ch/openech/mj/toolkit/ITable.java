package ch.openech.mj.toolkit;

import java.util.List;


public interface ITable<T> extends IComponent {

	public void setClickListener(TableActionListener listener);
	public void setDeleteListener(TableActionListener listener);
	public void setInsertListener(TableActionListener listener);
	public void setFunctionListener(int function, TableActionListener listener);
	
	public T getSelectedObject();
	
	public List<T> getSelectedObjects();
	
	public static interface TableActionListener {
		public void action();
	}

}
