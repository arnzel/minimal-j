package ch.openech.mj.lanterna.toolkit;

import java.io.InputStream;

import javax.swing.Action;
import javax.swing.event.ChangeListener;

import ch.openech.mj.lanterna.component.LanternaForm;
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

import com.googlecode.lanterna.gui.Component;
import com.googlecode.lanterna.gui.GUIScreen;
import com.googlecode.lanterna.gui.dialog.MessageBox;

public class LanternaClientToolkit extends ClientToolkit {
	private final GUIScreen gui;
	
	public LanternaClientToolkit(GUIScreen gui) {
		this.gui = gui;
	}
	
	@Override
	public IComponent createLabel(String text) {
		return new LanternaLabel(text);
	}

	@Override
	public IComponent createTitle(String text) {
		return new LanternaLabel(text);
	}

	@Override
	public TextField createReadOnlyTextField() {
		return new LanternaReadOnlyTextField();
	}

	@Override
	public TextField createTextField(ChangeListener changeListener, int maxLength) {
		return new LanternaTextField(changeListener);
	}

	@Override
	public TextField createTextField(ChangeListener changeListener, int maxLength, String allowedCharacters) {
		return new LanternaTextField(changeListener);
	}

	@Override
	public FlowField createFlowField() {
		return new LanternaFlowField();
	}

	@Override
	public <T> ComboBox<T> createComboBox(ChangeListener changeListener) {
		return new LanternaComboBox<T>(changeListener);
	}

	@Override
	public CheckBox createCheckBox(ChangeListener changeListener, String text) {
		return new LanternaCheckBox(changeListener, text);
	}

	@Override
	public <T> ITable<T> createTable(Class<T> clazz, Object[] fields) {
		return new LanternaTable<T>(clazz, fields);
	}

	@Override
	public Caption decorateWithCaption(IComponent component, String caption) {
		return new LanternaCaption((Component) component, caption);
	}

	@Override
	public HorizontalLayout createHorizontalLayout(IComponent... components) {
		return new LanternaHorizontalLayout(components);
	}

	@Override
	public SwitchLayout createSwitchLayout() {
		return new LanternaSwitchLayout();
	}

	@Override
	public GridFormLayout createGridLayout(int columns, int columnWidth) {
		return new LanternaForm(columns);
	}

	@Override
	public IComponent createFormAlignLayout(IComponent content) {
		return content;
	}

	@Override
	public IComponent createEditorLayout(IComponent content, Action[] actions) {
		return new LanternaEditorLayout(content, actions);
	}

	@Override
	public IComponent createSearchLayout(TextField text, Action searchAction,
			IComponent content, Action... actions) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void showMessage(Object parent, String text) {
		MessageBox.showMessageBox(gui, "Message", text);

	}

	@Override
	public void showError(Object parent, String text) {
		MessageBox.showMessageBox(gui, "Error", text);
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
	public IDialog openDialog(IComponent parent, IComponent content, String title) {
		return new LanternaDialog(gui, (Component) content, title);
	}

	@Override
	public void focusFirstComponent(IComponent component) {
		// TODO Auto-generated method stub

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
