package org.minimalj.frontend.lanterna.toolkit;

import java.io.InputStream;
import java.io.OutputStream;

import org.minimalj.frontend.lanterna.component.LanternaForm;
import org.minimalj.frontend.toolkit.Caption;
import org.minimalj.frontend.toolkit.CheckBox;
import org.minimalj.frontend.toolkit.ClientToolkit;
import org.minimalj.frontend.toolkit.ComboBox;
import org.minimalj.frontend.toolkit.FlowField;
import org.minimalj.frontend.toolkit.GridFormLayout;
import org.minimalj.frontend.toolkit.HorizontalLayout;
import org.minimalj.frontend.toolkit.IAction;
import org.minimalj.frontend.toolkit.IComponent;
import org.minimalj.frontend.toolkit.IDialog;
import org.minimalj.frontend.toolkit.ILink;
import org.minimalj.frontend.toolkit.ITable;
import org.minimalj.frontend.toolkit.SwitchLayout;
import org.minimalj.frontend.toolkit.TextField;
import org.minimalj.frontend.toolkit.ITable.TableActionListener;

import com.googlecode.lanterna.gui.Action;
import com.googlecode.lanterna.gui.Component;
import com.googlecode.lanterna.gui.GUIScreen;
import com.googlecode.lanterna.gui.component.Button;
import com.googlecode.lanterna.gui.dialog.MessageBox;

public class LanternaClientToolkit extends ClientToolkit {
	private final GUIScreen gui;
	
	public LanternaClientToolkit(GUIScreen gui) {
		this.gui = gui;
	}
	
	@Override
	public CheckBox createCheckBox(InputComponentListener changeListener, String text) {
		return new LanternaCheckBox(changeListener, text);
	}

	@Override
	public <T> ComboBox<T> createComboBox(InputComponentListener changeListener) {
		return new LanternaComboBox<T>(changeListener);
	}

	@Override
	public IDialog createDialog(IComponent parent, String title, IComponent content, IAction... actions) {
		return new LanternaDialog(gui, content, title, actions);
	}

	@Override
	public FlowField createFlowField() {
		return new LanternaFlowField();
	}

	@Override
	public IComponent createFormAlignLayout(IComponent content) {
		return content;
	}

	@Override
	public GridFormLayout createGridLayout(int columns, int columnWidth) {
		return new LanternaForm(columns);
	}

	@Override
	public HorizontalLayout createHorizontalLayout(IComponent... components) {
		return new LanternaHorizontalLayout(components);
	}

	@Override
	public IComponent createLabel(final IAction action) {
		LanternaActionAdapter lanternaAction = new LanternaActionAdapter(action);
		LanternaActionLabel button = new LanternaActionLabel(action.getName(), lanternaAction);
		lanternaAction.setComponent(button);
		return button;
	}

	private static class LanternaActionLabel extends Button implements IComponent {
		public LanternaActionLabel(String name, Action action) {
			super(name, action);
		}
	}

	/*
	 * Cannot be done as inner class because lanterna action has to be provided
	 * to the button constructor. And the minimal-j action needs the component for
	 * the action method.
	 */
	private static class LanternaActionAdapter implements Action {
		private final IAction action;
		private IComponent component;
		
		public LanternaActionAdapter(IAction action) {
			this.action = action;
		}
		
		public void setComponent(IComponent component) {
			this.component = component;
		}
		
		public void doAction() {
			action.action(component);
		}
	}

	
	@Override
	public IComponent createLabel(String text) {
		return new LanternaLabel(text);
	}

	@Override
	public IComponent createLink(String text, String address) {
		LanternaLinkAction action = new LanternaLinkAction(address);
		LanternaLink link = new LanternaLink(text, address, action);
		return link;
	}
	
	public static class LanternaLink extends Button implements ILink {
		private final String address;
		private LanternaLinkAction action;
		
		public LanternaLink(String text, String address, LanternaLinkAction action) {
			super(text, action);
			this.address = address;
			this.action = action;
		}

		@Override
		public String getAddress() {
			return address;
		}
		
		public void setListener(LanternaLinkListener listener) {
			action.setListener(listener);
		}
	}

	public class LanternaLinkAction implements Action {
		private final String address;
		private LanternaLinkListener listener;
		
		public LanternaLinkAction(String address) {
			this.address = address;
		}

		public void setListener(LanternaLinkListener listener) {
			this.listener = listener;
		}
		
		@Override
		public void doAction() {
			listener.action(address);
		}
	}

	public static interface LanternaLinkListener {
		public void action(String address);
	}

	
	@Override
	public TextField createReadOnlyTextField() {
		return new LanternaReadOnlyTextField();
	}

	@Override
	public SwitchLayout createSwitchLayout() {
		return new LanternaSwitchLayout();
	}

	@Override
	public <T> ITable<T> createTable(Object[] fields) {
		return new LanternaTable<T>(fields);
	}

	@Override
	public TextField createTextField(InputComponentListener changeListener, int maxLength) {
		return new LanternaTextField(changeListener);
	}

	@Override
	public TextField createTextField(InputComponentListener changeListener, int maxLength, String allowedCharacters) {
		return new LanternaTextField(changeListener);
	}

	@Override
	public IComponent createTitle(String text) {
		return new LanternaLabel(text);
	}

	@Override
	public Caption decorateWithCaption(IComponent component, String caption) {
		return new LanternaCaption((Component) component, caption);
	}

	@Override
	public OutputStream store(IComponent parent, String buttonText) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InputStream load(IComponent parent, String buttonText) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void showConfirmDialog(IComponent component, String message,
			String title, ConfirmDialogType type, DialogListener listener) {
		// TODO Auto-generated method stub
	}

	@Override
	public void showError(Object parent, String text) {
		MessageBox.showMessageBox(gui, "Error", text);
	}

	@Override
	public void showMessage(Object parent, String text) {
		MessageBox.showMessageBox(gui, "Message", text);

	}

	@Override
	public <T> IDialog createSearchDialog(IComponent parent, Search<T> index, Object[] keys, TableActionListener<T> listener) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> ILookup<T> createLookup(InputComponentListener changeListener, Search<T> index, Object[] keys) {
		// TODO Auto-generated method stub
		return null;
	}

	
	
}