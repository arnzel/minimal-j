package org.minimalj.frontend.lanterna.toolkit;

import java.util.List;

import org.minimalj.frontend.lanterna.component.Select;
import org.minimalj.frontend.toolkit.ClientToolkit.Input;
import org.minimalj.frontend.toolkit.ClientToolkit.InputComponentListener;

import com.googlecode.lanterna.input.Key;

public class LanternaComboBox<T> extends Select<T> implements Input<T> {

	private final InputComponentListener changeListener;
	
	public LanternaComboBox(List<T> objects, InputComponentListener changeListener) {
		this.changeListener = changeListener;
		setObjects(objects);
	}

	private void fireChangeEvent() {
		changeListener.changed(LanternaComboBox.this);
	}
	
	@Override
	public Result keyboardInteraction(Key key) {
		Result result = super.keyboardInteraction(key);
		if (result != Result.EVENT_NOT_HANDLED) {
			fireChangeEvent();
		}
		return result;
	}

	@Override
	public void setEditable(boolean editable) {
		// TODO Auto-generated method stub
	}

	@Override
	public void setValue(T value) {
		setSelectedObject(value);
	}

	@Override
	public T getValue() {
		return getSelectedObject();
	}
}