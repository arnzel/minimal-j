package org.minimalj.frontend.json;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.minimalj.application.ApplicationContext;
import org.minimalj.frontend.page.Page;
import org.minimalj.frontend.toolkit.Action;
import org.minimalj.frontend.toolkit.CheckBox;
import org.minimalj.frontend.toolkit.ClientToolkit;
import org.minimalj.frontend.toolkit.ComboBox;
import org.minimalj.frontend.toolkit.FlowField;
import org.minimalj.frontend.toolkit.FormContent;
import org.minimalj.frontend.toolkit.IDialog;
import org.minimalj.frontend.toolkit.TextField;

public class JsonClientToolkit extends ClientToolkit {

	private final ThreadLocal<JsonClientSession> session = new ThreadLocal<>();
	
	@Override
	public IComponent createLabel(String string) {
		JsonTextField component = new JsonTextField("Label");
		component.setText(string);
		return component;
	}

	public static class JsonActionLabel extends JsonTextField {
		private final Action action;
		
		public JsonActionLabel(Action action) {
			super("Action");
			this.action = action;
			setText(action.getName());
		}
		
		public void action() {
			action.action();
		}
	}
	
	@Override
	public IComponent createLabel(Action action) {
		return new JsonActionLabel(action);
	}

	@Override
	public IComponent createTitle(String string) {
		JsonComponent component = new JsonComponent("Title");
		component.put(JsonValueComponent.VALUE, string);
		return component;
	}

	@Override
	public TextField createReadOnlyTextField() {
		return new JsonTextField("ReadOnlyTextField");
	}

	@Override
	public TextField createTextField(int maxLength, String allowedCharacters, InputType inputType, Search<String> autocomplete,
			InputComponentListener changeListener) {
		return new JsonTextField("TextField", maxLength, allowedCharacters, inputType, autocomplete, changeListener);
	}

	@Override
	public TextField createAreaField(int maxLength, String allowedCharacters, InputComponentListener changeListener) {
		return new JsonTextField("AreaField", maxLength, allowedCharacters, null, null, changeListener);
	}

	@Override
	public FlowField createFlowField() {
		return new JsonFlowField();
	}

	@Override
	public <T> ComboBox<T> createComboBox(List<T> objects, InputComponentListener changeListener) {
		return new JsonCombobox<T>(objects, changeListener);
	}

	@Override
	public CheckBox createCheckBox(InputComponentListener changeListener, String text) {
		return new JsonCheckBox(text, changeListener);
	}

	@Override
	public <T> ITable<T> createTable(Object[] keys, TableActionListener<T> listener) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void show(Page page) {
		JsonComponent jsonComponent = (JsonComponent) page.getContent();

		System.out.println(jsonComponent.toString());
//		
//		message.put("id", page.hashCode());
//		message.put("title", page.getTitle());
//		message.put("content", jsonComponent);
//		
//		// jsonComponent.setListener(...);
	}

	@Override
	public void show(List<Page> pages, int startIndex) {
		// TODO Auto-generated method stub
	}

	@Override
	public void refresh() {
		// TODO Auto-generated method stub
	}

	@Override
	public <T> ILookup<T> createLookup(InputComponentListener changeListener, Search<T> index, Object[] keys) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IComponent createComponentGroup(IComponent... components) {
		JsonComponent group = new JsonComponent("group");
		group.put("components", Arrays.asList(components));
		return group;
	}

	@Override
	public FormContent createFormContent(int columns, int columnWidth) {
		return new JsonFormContent(columns, columnWidth);
	}

	@Override
	public SwitchContent createSwitchContent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ApplicationContext getApplicationContext() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IDialog createDialog(String title, IContent content, Action... actions) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> IDialog createSearchDialog(Search<T> index, Object[] keys, TableActionListener<T> listener) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void showMessage(String text) {
		// TODO Auto-generated method stub

	}

	@Override
	public void showError(String text) {
		// TODO Auto-generated method stub

	}

	@Override
	public void showConfirmDialog(String message, String title, ConfirmDialogType type, DialogListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public OutputStream store(String buttonText) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InputStream load(String buttonText) {
		// TODO Auto-generated method stub
		return null;
	}

	
	protected void handle(String jsonString) {
		Map<String, Object> json = (Map<String, Object>) new JsonReader().read(jsonString);
		String sessionId = (String) json.get("session");
		JsonClientSession session = JsonClientSession.getSession(sessionId);
		this.session.set(session);
	}
	
}
