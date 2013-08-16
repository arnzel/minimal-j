package ch.openech.mj.edit.fields;

import java.awt.event.ActionListener;
import java.awt.event.FocusListener;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import ch.openech.mj.toolkit.TextField;

import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.ui.HorizontalLayout;

/**
 * A Vaadin Widget must not implement strange interfaces (meaning our TextField) because
 * it would not be recognized by the gwt compiler. So this VaadinTextField cannot extend
 * the vaadin.TextField and implement TextField directly. Everything is delegated.
 * 
 * @author Bruno
 *
 */
public class NativeVaadinTextField extends HorizontalLayout implements TextField {

	private final com.vaadin.ui.TextField textField;
	private final ChangeListener changeListener;
	private final String allowedCharacters;

	public NativeVaadinTextField(ChangeListener changeListener, int maxLength) {
		this(changeListener, maxLength, null);
	}
	
	public NativeVaadinTextField(ChangeListener changeListener, int maxLength, String allowedCharacters) {
		this.textField = new com.vaadin.ui.TextField();
		this.allowedCharacters = allowedCharacters;
		textField.setMaxLength(maxLength);
		textField.setNullRepresentation("");
		textField.setImmediate(true);
		textField.addListener(new TextFieldChangeListener());
		this.changeListener = changeListener;
		addComponent(textField);
		textField.setSizeFull();
	}

	@Override
	public void setText(String text) {
		textField.setValue(text);
	}

	@Override
	public String getText() {
		return (String) textField.getValue();
	}

	@Override
	public void setFocusListener(FocusListener focusListener) {
		// textField.setFocusListener(focusListener);
	}

	@Override
	public void setCommitListener(ActionListener listener) {
		// TODO listening to Enter Key at Vaadin TextField
	}
	
	private String filter(String s) {
		if (allowedCharacters == null) return s;
		
		String result = "";
		for (int i = 0; i<s.length(); i++) {
			char c = s.charAt(i);
			if (allowedCharacters.indexOf(c) < 0) {
				if (Character.isLowerCase(c)) c = Character.toUpperCase(c);
				else if (Character.isUpperCase(c)) c = Character.toLowerCase(c);
				if (allowedCharacters.indexOf(c) < 0) {
					continue;
				}
			}
			result += c;
		}
		return result;
	}
	
	public class TextFieldChangeListener implements TextChangeListener {

		@Override
		public void textChange(TextChangeEvent event) {
			String unfilteredString = event.getText();
			String filteredString = filter(unfilteredString);
			if (!unfilteredString.equals(filteredString)) {
				setText(filteredString);
			}
			changeListener.stateChanged(new ChangeEvent(NativeVaadinTextField.this));			
		}
	}
	
}
