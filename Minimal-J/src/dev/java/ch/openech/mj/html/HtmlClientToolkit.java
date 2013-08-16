package ch.openech.mj.html;

import java.io.InputStream;

import javax.swing.Action;
import javax.swing.event.ChangeListener;

import ch.openech.mj.toolkit.Caption;
import ch.openech.mj.toolkit.CheckBox;
import ch.openech.mj.toolkit.ClientToolkit;
import ch.openech.mj.toolkit.ComboBox;
import ch.openech.mj.toolkit.ConfirmDialogListener;
import ch.openech.mj.toolkit.ExportHandler;
import ch.openech.mj.toolkit.FlowField;
import ch.openech.mj.toolkit.GridFormLayout;
import ch.openech.mj.toolkit.HorizontalLayout;
import ch.openech.mj.toolkit.IComponent;
import ch.openech.mj.toolkit.IDialog;
import ch.openech.mj.toolkit.ITable;
import ch.openech.mj.toolkit.ImportHandler;
import ch.openech.mj.toolkit.ProgressListener;
import ch.openech.mj.toolkit.SwitchLayout;
import ch.openech.mj.toolkit.TextField;

public class HtmlClientToolkit extends ClientToolkit {

	@Override
	public IComponent createLabel(String string) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IComponent createTitle(String string) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TextField createReadOnlyTextField() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TextField createTextField(ChangeListener changeListener,
			int maxLength) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TextField createTextField(ChangeListener changeListener,
			int maxLength, String allowedCharacters) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FlowField createFlowField() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> ComboBox<T> createComboBox(ChangeListener changeListener) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CheckBox createCheckBox(ChangeListener changeListener, String text) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> ITable<T> createTable(Class<T> clazz, Object[] fields) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Caption decorateWithCaption(IComponent component, String caption) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HorizontalLayout createHorizontalLayout(IComponent... components) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SwitchLayout createSwitchLayout() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GridFormLayout createGridLayout(int columns, int columnWidth) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IComponent createFormAlignLayout(IComponent content) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IComponent createEditorLayout(IComponent content, Action[] actions) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IComponent createSearchLayout(TextField text, Action searchAction,
			IComponent content, Action... actions) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void showMessage(Object parent, String text) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void showError(Object parent, String text) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void showConfirmDialog(IComponent component, String message,
			String title, int type, ConfirmDialogListener listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ProgressListener showProgress(Object parent, String text) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IDialog openDialog(IComponent parent, IComponent content,
			String title) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void focusFirstComponent(IComponent component) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object getParent(Object component) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void export(Object parent, String buttonText,
			ExportHandler exportHandler) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public InputStream imprt(Object parent, String buttonText) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Deprecated
	public IComponent exportLabel(ExportHandler exportHandler, String label) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Deprecated
	public IComponent importField(ImportHandler importHandler, String buttonText) {
		// TODO Auto-generated method stub
		return null;
	}

}
