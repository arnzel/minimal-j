package ch.openech.mj.edit;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import ch.openech.mj.resources.ResourceAction;
import ch.openech.mj.resources.ResourceHelper;
import ch.openech.mj.resources.Resources;
import ch.openech.mj.toolkit.ClientToolkit;
import ch.openech.mj.toolkit.IAction;
import ch.openech.mj.toolkit.IComponent;
import ch.openech.mj.toolkit.IDialog;
import ch.openech.mj.toolkit.IDialog.CloseListener;
import ch.openech.mj.toolkit.ITable;
import ch.openech.mj.toolkit.TextField;
import ch.openech.mj.util.GenericUtils;

public abstract class SearchDialogAction<T> implements IAction {
	private final Object[] keys;
	private IDialog dialog;
	private ITable<T> table;
	private TextField textFieldSearch;
	
	public SearchDialogAction(Object... keys) {
		this.keys = keys;
		String actionName = getClass().getSimpleName();
		ResourceHelper.initProperties(this, Resources.getResourceBundle(), actionName);
	}

	@Override
	public void action(IComponent source) {
		try {
			showPageOn(source);
		} catch (Exception x) {
			// TODO show dialog
			x.printStackTrace();
		}
	}
	
	private void showPageOn(IComponent source) {
		textFieldSearch = ClientToolkit.getToolkit().createTextField(new SearchChangeListener(), 100);
		
		@SuppressWarnings("unchecked")
		Class<T> clazz = (Class<T>) GenericUtils.getGenericClass(getClass());		
		table = ClientToolkit.getToolkit().createTable(clazz, keys);
				
		IComponent layout = ClientToolkit.getToolkit().createSearchLayout(textFieldSearch, new SearchAction(), table, new OkAction());
		
		dialog = ClientToolkit.getToolkit().openDialog(source, layout, "Suche");
		
		dialog.setCloseListener(new CloseListener() {
			@Override
			public boolean close() {
				return true;
			}
		});
		
		table.setClickListener(new SearchClickListener());
		dialog.openDialog();
		ClientToolkit.getToolkit().focusFirstComponent(textFieldSearch);
	}
	
	protected abstract List<T> search(String text);
	
	protected abstract void save(T object);
	
	private class SearchChangeListener implements ChangeListener {

		@Override
		public void stateChanged(ChangeEvent e) {
			// At the moment no continous - search
		}
		
	}
	
	private class SearchAction extends ResourceAction {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			List<T> objects = search(textFieldSearch.getText());
			table.setObjects(objects);
		}
	}

	private class OkAction extends ResourceAction {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			saveAndClose();
		}
	}
	
	private class SearchClickListener implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			saveAndClose();
		}
	}
	
	private void saveAndClose() {
		T selected = table.getSelectedObject();
		save(selected);
		dialog.closeDialog();
	}
	
}
