package org.minimalj.frontend.swing.toolkit;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.FocusTraversalPolicy;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.FocusManager;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.text.JTextComponent;

import org.minimalj.application.ApplicationContext;
import org.minimalj.frontend.swing.SwingFrontend;
import org.minimalj.frontend.swing.SwingTab;
import org.minimalj.frontend.toolkit.CheckBox;
import org.minimalj.frontend.toolkit.ClientToolkit;
import org.minimalj.frontend.toolkit.ClientToolkit.DialogListener.DialogResult;
import org.minimalj.frontend.toolkit.ComboBox;
import org.minimalj.frontend.toolkit.FlowField;
import org.minimalj.frontend.toolkit.FormContent;
import org.minimalj.frontend.toolkit.IAction;
import org.minimalj.frontend.toolkit.IAction.ActionChangeListener;
import org.minimalj.frontend.toolkit.IDialog;
import org.minimalj.frontend.toolkit.ILink;
import org.minimalj.frontend.toolkit.ITable;
import org.minimalj.frontend.toolkit.ITable.TableActionListener;
import org.minimalj.frontend.toolkit.ProgressListener;
import org.minimalj.frontend.toolkit.SwitchComponent;
import org.minimalj.frontend.toolkit.TextField;
import org.minimalj.util.StringUtils;

public class SwingClientToolkit extends ClientToolkit {

	private static ThreadLocal<SwingTab> tabByThread = new ThreadLocal<SwingTab>();
	
	public static SwingTab getTab() {
		return tabByThread.get();
	}
	
	public static void setTab(SwingTab tab) {
		tabByThread.set(tab);
	}
	
	@Override
	public IComponent createLabel(String string) {
		return new SwingLabel(string);
	}
	
	@Override
	public IComponent createLabel(IAction action) {
		return new SwingActionLabel(action);
	}

	private static class SwingActionLabel extends JLabel implements IComponent {
		private static final long serialVersionUID = 1L;

		public SwingActionLabel(final IAction action) {
			setText(action.getName());
//			label.setToolTipText(Resources.getResourceBundle().getString(runnable.getClass().getSimpleName() + ".description"));
			
			setForeground(Color.BLUE);
			setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					updateEventTab((Component) e.getSource());
					action.action();
				}
			});
		}
	}
	
	public static SwingTab findSwingTab(Component c) {
		while (c != null && !(c instanceof SwingTab)) {
			if (c instanceof JPopupMenu) {
				c = ((JPopupMenu) c).getInvoker();
			} else {
				c = c.getParent();
			}
		}
		return (SwingTab) c;
	}
	
	public static void updateEventTab(Component c) {
		SwingTab swingTab = findSwingTab(c);
		tabByThread.set(swingTab);
	}

	@Override
	public IComponent createTitle(String string) {
		return new SwingTitle(string);

	}

	@Override
	public TextField createReadOnlyTextField() {
		return new SwingReadOnlyTextField();
	}

	@Override
	public TextField createTextField(InputComponentListener changeListener, int maxLength) {
		return new SwingTextField(changeListener, maxLength);
	}

	@Override
	public TextField createTextField(InputComponentListener changeListener, int maxLength, String allowedCharacters) {
		return new SwingTextField(changeListener, maxLength, allowedCharacters);
	}

	@Override
	public FlowField createFlowField() {
		return new SwingFlowField();
	}

	@Override
	public <T> ComboBox<T> createComboBox(InputComponentListener changeListener) {
		return new SwingComboBox<T>(changeListener);
	}

	@Override
	public CheckBox createCheckBox(InputComponentListener changeListener, String text) {
		return new SwingCheckBox(changeListener, text);
	}

	@Override
	public IComponent createHorizontalLayout(IComponent... components) {
		return new SwingHorizontalLayout(components);
	}

	@Override
	public WizardContent createWizardContent() {
		return new SwingSwitchContent();
	}

	@Override
	public SwitchComponent createSwitchComponent(IComponent... components) {
		return new SwingSwitchComponent(components);
	}

	@Override
	public FormContent createFormContent(int columns, int columnWidthPercentage) {
		return new SwingGridFormLayout(columns, columnWidthPercentage);
	}

	public static void focusFirstComponent(JComponent jComponent) {
		if (jComponent.isShowing()) {
			focusFirstComponentNow(jComponent);
		} else {
			focusFirstComponentLater(jComponent);
		}
	}

	private static void focusFirstComponentNow(JComponent component) {
		FocusTraversalPolicy focusPolicy = component.getFocusTraversalPolicy();
		if (component instanceof JTextComponent || component instanceof JComboBox || component instanceof JCheckBox) {
			component.requestFocus();
		} else if (focusPolicy != null && focusPolicy.getFirstComponent(component) != null) {
			focusPolicy.getFirstComponent(component).requestFocus();
		} else {
			FocusManager.getCurrentManager().focusNextComponent(component);
		}
	}

	private static void focusFirstComponentLater(final JComponent component) {
		component.addHierarchyListener(new HierarchyListener() {
			@Override
			public void hierarchyChanged(HierarchyEvent e) {
				component.removeHierarchyListener(this);
				focusFirstComponent(component);
			}
		});
	}

	@Override
	public void showMessage(String text) {
		Window window = findWindow();
		JOptionPane.showMessageDialog(window, text, "Information", JOptionPane.INFORMATION_MESSAGE);
	}

	@Override
	public void showError(String text) {
		Window window = findWindow();
		JOptionPane.showMessageDialog(window, text, "Fehler", JOptionPane.ERROR_MESSAGE);
	}

	@Override
	public void showConfirmDialog(String message, String title, ConfirmDialogType type,
			DialogListener listener) {
		int optionType = type.ordinal();
		int result = JOptionPane.showConfirmDialog(getTab(), message, title, optionType);
		listener.close(DialogResult.values()[result]);
	}

	@Override
	public <T> ITable<T> createTable(Object[] fields) {
		return new SwingTable<T>(fields);
	}

	public ProgressListener showProgress(String text) {
		SwingProgressInternalFrame frame = new SwingProgressInternalFrame(text);
		getTab().openModalDialog(frame);
		return frame;
	}

	@Override
	public IDialog createDialog(String title, IContent content, IAction... actions) {
		JComponent contentComponent = new SwingEditorLayout(content, actions);
		// TODO check for OS or move this to UI
		contentComponent.setBorder(BorderFactory.createLineBorder(Color.BLACK));

		return createDialog(title, contentComponent);
	}

	private IDialog createDialog(String title, JComponent content) {
		return new SwingInternalFrame(getTab(), content, title);
	}

	public static Window findWindow() {
		Component parentComponent = getTab();
		while (parentComponent != null && !(parentComponent instanceof Window)) {
			if (parentComponent instanceof JPopupMenu) {
				parentComponent = ((JPopupMenu) parentComponent).getInvoker();
			} else {
				parentComponent = parentComponent.getParent();
			}
		}
		return (Window) parentComponent;
	}
		
	@Override
	public <T> ILookup<T> createLookup(InputComponentListener changeListener, Search<T> index, Object[] keys) {
		return new SwingLookup<T>(changeListener, index, keys);
	}
	
	private static class SwingLookup<T> extends JPanel implements ILookup<T> {
		private static final long serialVersionUID = 1L;
		
		private final InputComponentListener changeListener;
		private final Search<T> search;
		private final Object[] keys;
		private final SwingLookupLabel actionLabel;
		private IDialog dialog;
		private T selectedObject;
		
		public SwingLookup(InputComponentListener changeListener, Search<T> search, Object[] keys) {
			super(new BorderLayout());
			
			this.changeListener = changeListener;
			this.search = search;
			this.keys = keys;
			
			this.actionLabel = new SwingLookupLabel();
			add(actionLabel, BorderLayout.CENTER);
			add(new SwingRemoveLabel(), BorderLayout.LINE_END);
		}

		@Override
		public void setText(String text) {
			if (!StringUtils.isBlank(text)) {
				actionLabel.setText(text);
			} else {
				actionLabel.setText("[+]");
			}
		}

		@Override
		public T getSelectedObject() {
			return selectedObject;
		}
		
		private class SwingLookupLabel extends JLabel {
			private static final long serialVersionUID = 1L;

			public SwingLookupLabel() {
				setForeground(Color.BLUE);
				setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						dialog = ((SwingClientToolkit) ClientToolkit.getToolkit()).createSearchDialog(search, keys, new LookupClickListener());
						dialog.openDialog();
					}
				});
			}
		}
		
		private class SwingRemoveLabel extends JLabel {
			private static final long serialVersionUID = 1L;

			public SwingRemoveLabel() {
				super("[x]");
				setForeground(Color.BLUE);
				setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						SwingLookup.this.selectedObject = null;
						changeListener.changed(SwingLookup.this);
					}
				});
			}
		}
		
		private class LookupClickListener implements TableActionListener<T> {
			@Override
			public void action(T selectedObject, List<T> selectedObjects) {
				SwingLookup.this.selectedObject = selectedObject;
				dialog.closeDialog();
				changeListener.changed(SwingLookup.this);
			}
		}

	}

	public <T> IDialog createSearchDialog(Search<T> index, Object[] keys, TableActionListener<T> listener) {
		SwingSearchPanel<T> panel = new SwingSearchPanel<T>(index, keys, listener);
		return createDialog(null, panel);
	}

	@Override
	public OutputStream store(String buttonText) {
		JFileChooser chooser = new JFileChooser();
		chooser.setMultiSelectionEnabled(false);
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		if (JFileChooser.APPROVE_OPTION == chooser.showDialog(getTab(), buttonText)) {
			File outputFile = chooser.getSelectedFile();
			try {
				return new FileOutputStream(outputFile);
			} catch (FileNotFoundException e) {
				throw new RuntimeException(e);
			}
		} else {
			return null;
		}
	}

	@Override
	public InputStream load(String buttonText) {
		JFileChooser chooser = new JFileChooser();
		chooser.setMultiSelectionEnabled(false);
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		if (JFileChooser.APPROVE_OPTION == chooser.showDialog(getTab(), buttonText)) {
			File inputFile = chooser.getSelectedFile();
			try {
				return new FileInputStream(inputFile);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				return null;
			}
		} else {
			return null;
		}
	}

	public static boolean verticallyGrowing(Component component) {
		if (component instanceof SwingFlowField || component instanceof JTable) {
			return true;
		}
		if (component instanceof Container) {
			Container container = (Container) component;
			for (Component c : container.getComponents()) {
				if (verticallyGrowing(c)) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public ILink createLink(String text, String address) {
        return new SwingLink(text, address);
	}
	
	public static class SwingLink extends JLabel implements ILink {
		private static final long serialVersionUID = 1L;
		private final String address;
		private MouseListener mouseListener;
		
		public SwingLink(String text, String address) {
			super(text);
			this.address = address;
			setForeground(Color.BLUE);
			setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

			addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					mouseListener.mouseClicked(e);
				}
			});
		}

		public String getAddress() {
			return address;
		}

		public void setMouseListener(MouseListener mouseListener) {
			this.mouseListener = mouseListener;
		}
	}

	public static Action[] adaptActions(IAction[] actions) {
		Action[] swingActions = new Action[actions.length];
		for (int i = 0; i<actions.length; i++) {
			swingActions[i] = adaptAction(actions[i]);
		}
		return swingActions;
	}

	public static Action adaptAction(final IAction action) {
		final Action swingAction = new AbstractAction(action.getName()) {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				if (SwingUtilities.isEventDispatchThread()) {
					SwingClientToolkit.updateEventTab((Component) e.getSource());
				}
				action.action();
			}
		};
		swingAction.putValue(Action.SHORT_DESCRIPTION, action.getDescription());
		action.setChangeListener(new ActionChangeListener() {
			{
				update();
			}
			
			@Override
			public void change() {
				update();
			}

			protected void update() {
				swingAction.setEnabled(action.isEnabled());
			}
		});
		return swingAction;
	}

	@Override
	public void show(String pageLink) {
		getTab().show(pageLink);
	}

	@Override
	public void show(List<String> pageLinks, int index) {
		getTab().show(pageLinks, index);
	}

	@Override
	public void refresh() {
		getTab().refresh();
	}

	@Override
	public ApplicationContext getApplicationContext() {
		return SwingFrontend.getApplicationContext();
	}
	
}
