package org.minimalj.frontend.vaadin.toolkit;


import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.util.Iterator;
import java.util.List;

import org.minimalj.application.ApplicationContext;
import org.minimalj.frontend.toolkit.CheckBox;
import org.minimalj.frontend.toolkit.ClientToolkit;
import org.minimalj.frontend.toolkit.ComboBox;
import org.minimalj.frontend.toolkit.FlowField;
import org.minimalj.frontend.toolkit.FormContent;
import org.minimalj.frontend.toolkit.IAction;
import org.minimalj.frontend.toolkit.IDialog;
import org.minimalj.frontend.toolkit.ILink;
import org.minimalj.frontend.toolkit.ITable;
import org.minimalj.frontend.toolkit.ITable.TableActionListener;
import org.minimalj.frontend.toolkit.SwitchComponent;
import org.minimalj.frontend.toolkit.TextField;
import org.minimalj.frontend.vaadin.VaadinWindow;
import org.minimalj.util.StringUtils;

import com.vaadin.server.ExternalResource;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Link;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.BaseTheme;

public class VaadinClientToolkit extends ClientToolkit {

	@Override
	public IComponent createLabel(String string) {
		return new VaadinLabel(string);
	}
	
	@Override
	public IComponent createLabel(IAction action) {
		return new VaadinActionLabel(action);
	}

	private static class VaadinActionLabel extends Button implements IComponent {

		private static final long serialVersionUID = 1L;

		public VaadinActionLabel(final IAction action) {
			super(action.getName());
//			button.setDescription((String) action.getValue(Action.LONG_DESCRIPTION));
			setStyleName(BaseTheme.BUTTON_LINK);
			addListener(new ClickListener() {
				private static final long serialVersionUID = 1L;

				@Override
				public void buttonClick(ClickEvent event) {
					action.action();
				}
			});
		}
	}
	
	@Override
	public IComponent createTitle(String string) {
		return new VaadinTitle(string);
	}

	@Override
	public TextField createReadOnlyTextField() {
		return new VaadinReadOnlyTextField();
	}

	@Override
	public TextField createTextField(InputComponentListener changeListener, int maxLength) {
		return new VaadinTextField(changeListener, maxLength);
	}
	
	@Override
	public TextField createTextField(Boolean multiLine, InputComponentListener changeListener, int maxLength, String allowedCharacters,
			InputType inputType) {
		if (multiLine == null && maxLength >= 256 || multiLine) {
			return new VaadinTextAreaField(changeListener, maxLength, allowedCharacters);
		} else {
			return new VaadinTextField(changeListener, maxLength, allowedCharacters);
		}
	}

	@Override
	public FlowField createFlowField() {
		return new VaadinVerticalFlowField();
	}

	@Override
	public <T> ComboBox<T> createComboBox(InputComponentListener listener) {
		return new VaadinComboBox<T>(listener);
	}

	@Override
	public CheckBox createCheckBox(InputComponentListener listener, String text) {
		return new VaadinCheckBox(listener, text);
	}

	@Override
	public IComponent createHorizontalLayout(IComponent... components) {
		return new VaadinHorizontalLayout(components);
	}

	@Override
	public FormContent createFormContent(int columns, int columnWidthPercentage) {
		return new VaadinGridFormLayout(columns, columnWidthPercentage);
	}

	@Override
	public SwitchContent createSwitchContent() {
		return new VaadinSwitchContent();
	}
	
	@Override
	public SwitchComponent createSwitchComponent(IComponent... components) {
		return new VaadinSwitchComponent(components);
	}

	public static void focusFirstComponent(Component component) {
		AbstractField field = findAbstractField(component);
		if (field != null) {
			field.focus();
		}
	}
	
	private static AbstractField findAbstractField(Component c) {
		if (c instanceof AbstractField) {
			return ((AbstractField) c);
		} else if (c instanceof ComponentContainer) {
			ComponentContainer container = (ComponentContainer) c;
			Iterator<Component> components = container.getComponentIterator();
			while (components.hasNext()) {
				AbstractField field = findAbstractField(components.next());
				if (field != null) {
					return field;
				}
			}
		}
		return null;
	}
	
	@Override
	public void showMessage(String text) {
		com.vaadin.ui.Notification.show(text,
                com.vaadin.ui.Notification.Type.HUMANIZED_MESSAGE);
	}
	
	@Override
	public void showError(String text) {
		com.vaadin.ui.Notification.show(text,
                com.vaadin.ui.Notification.Type.ERROR_MESSAGE);
	}

	@Override
	public void showConfirmDialog(String message, String title, ConfirmDialogType type, DialogListener listener) {
		VaadinConfirmDialog dialog = VaadinConfirmDialog.getFactory().create(title, message, "ok", "cancel", null);
		VaadinConfirmDialog.Listener confirmDialogListener = new VaadinConfirmDialog.Listener() {
			@Override
			public void onClose(VaadinConfirmDialog dialog) {
				if (dialog.isConfirmed()) {
					listener.close(true);
				}
				// TODO...
			}
		};
		dialog.show(UI.getCurrent(), confirmDialogListener, true);
	}

	@Override
	public <T> ITable<T> createTable(Object[] fields) {
		return new VaadinTable<T>(fields);
	}
	
	@Override
	public IDialog createDialog(String title, IContent content, IAction... actions) {
		Component component = new VaadinEditorLayout(content, actions);
		component.setSizeFull();

		return createDialog(title, component);
	}

	private IDialog createDialog(String title, Component component) {
		return new VaadinDialog((ComponentContainer) component, title);
	}
	
	@Override
	public <T> IDialog createSearchDialog(Search<T> index, Object[] keys, TableActionListener<T> listener) {
		VaadinSearchPanel<T> panel = new VaadinSearchPanel<>(index, keys, listener);
		return createDialog(null, panel);
	}

	@Override
	public <T> ILookup<T> createLookup(InputComponentListener changeListener, Search<T> index, Object[] keys) {
		return new VaadinLookup<T>(changeListener, index, keys);
	}
	
	private static class VaadinLookup<T> extends GridLayout implements ILookup<T> {
		private static final long serialVersionUID = 1L;
		
		private final InputComponentListener changeListener;
		private final Search<T> search;
		private final Object[] keys;
		private final VaadinLookupLabel actionLabel;
		private IDialog dialog;
		private T selectedObject;
		
		public VaadinLookup(InputComponentListener changeListener, Search<T> search, Object[] keys) {
			super(2, 1);
			
			this.changeListener = changeListener;
			this.search = search;
			this.keys = keys;
			
			this.actionLabel = new VaadinLookupLabel();
			addComponent(actionLabel);
			addComponent(new VaadinRemoveLabel());
			setColumnExpandRatio(0, 1.0f);
			setColumnExpandRatio(1, 0.0f);
		}

		@Override
		public void setText(String text) {
			if (!StringUtils.isBlank(text)) {
				actionLabel.setCaption(text);
			} else {
				actionLabel.setCaption("[+]");
			}
		}

		@Override
		public T getSelectedObject() {
			return selectedObject;
		}
		
		private class VaadinLookupLabel extends Button {
			private static final long serialVersionUID = 1L;

			public VaadinLookupLabel() {
				setStyleName(BaseTheme.BUTTON_LINK);
				addListener(new ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(ClickEvent event) {
						dialog = ((VaadinClientToolkit) ClientToolkit.getToolkit()).createSearchDialog(search, keys, new LookupClickListener());
						dialog.openDialog();
					}
				});
			}
		}
		
		private class VaadinRemoveLabel extends Button {
			private static final long serialVersionUID = 1L;

			public VaadinRemoveLabel() {
				super("[x]");
				setStyleName(BaseTheme.BUTTON_LINK);
				addListener(new ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(ClickEvent event) {
						VaadinLookup.this.selectedObject = null;
						changeListener.changed(VaadinLookup.this);
					}
				});
			}
		}
		
		private class LookupClickListener implements TableActionListener<T> {
			@Override
			public void action(T selectedObject, List<T> selectedObjects) {
				VaadinLookup.this.selectedObject = selectedObject;
				dialog.closeDialog();
				changeListener.changed(VaadinLookup.this);
			}
		}

	}
	
	@Override
	public OutputStream store(String buttonText) {
		return new VaadinExportDialog("Export").getOutputStream();
	}

	@Override
	public InputStream load(String buttonText) {
		VaadinImportDialog importDialog = new VaadinImportDialog("Import");
		PipedInputStream inputStream = importDialog.getInputStream();
		return inputStream;
	}

	@Override
	public ILink createLink(String text, String address) {
		final VaadinActionLink button = new VaadinActionLink(text, address);
		return button;
	}
	
	public static class VaadinActionLink extends Link implements ILink {
		private static final long serialVersionUID = 1L;
		private final String address;
		
		public VaadinActionLink(String text, String address) {
			super(text, new ExternalResource("#" + address));
			this.address = address;
		}

		public String getAddress() {
			return address;
		}
		
	}

	@Override
	public void show(String pageLink) {
		VaadinWindow window = (VaadinWindow) UI.getCurrent();
		window.show(pageLink);
	}

	@Override
	public void refresh() {
		UI.getCurrent().getPage().reload();
	}

	
	@Override
	public void show(List<String> pageLinks, int index) {
		VaadinWindow window = (VaadinWindow) UI.getCurrent();
		window.show(pageLinks, index);
	}

	@Override
	public ApplicationContext getApplicationContext() {
		VaadinWindow window = (VaadinWindow) UI.getCurrent();
		return window.getApplicationContext();
	}
	
}
