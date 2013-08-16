package ch.openech.mj.lanterna.toolkit;

import java.awt.event.ActionListener;
import java.awt.event.FocusListener;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import ch.openech.mj.toolkit.TextField;

import com.googlecode.lanterna.gui.component.InteractableComponent;
import com.googlecode.lanterna.gui.component.TextBox;
import com.googlecode.lanterna.gui.listener.ComponentAdapter;
import com.googlecode.lanterna.input.Key;

public class LanternaTextField extends TextBox implements TextField {

	private final ChangeListener changeListener;
	private FocusListener focusListener;
	
	public LanternaTextField(ChangeListener changeListener) {
		this.changeListener = changeListener;
		addComponentListener(new TextFieldComponentListener());
	}

	@Override
	public void setEnabled(boolean editable) {
		super.setVisible(editable);
	}

	@Override
	public void setFocusListener(FocusListener focusListener) {
		this.focusListener = focusListener;
	}

	@Override
	public void setCommitListener(ActionListener listener) {
		// ignored at the moment
	}

	@Override
	public String getText() {
		String text = super.getText();
		if (text.length() == 0) return null;
		return text;
	}

	@Override
	public void setText(String text) {
		if (text == null) {
			text = "";
		}
		super.setText(text);
	}
	
	private void fireChangeEvent() {
		changeListener.stateChanged(new ChangeEvent(LanternaTextField.this));
	}
	
	@Override
	public Result keyboardInteraction(Key key) {
		Result result = super.keyboardInteraction(key);
		if (result != Result.EVENT_NOT_HANDLED) {
			fireChangeEvent();
		}
		return result;
	}

	private class TextFieldComponentListener extends ComponentAdapter {

		@Override
		public void onComponentReceivedFocus(InteractableComponent interactableComponent) {
			if (focusListener != null) {
				focusListener.focusGained(null);
			}
		}
		
		@Override
		public void onComponentLostFocus(InteractableComponent interactableComponent) {
			if (focusListener != null) {
				focusListener.focusLost(null);
			}
		}
		
	}
	
}
