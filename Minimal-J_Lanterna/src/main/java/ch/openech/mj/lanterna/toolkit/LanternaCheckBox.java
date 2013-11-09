package ch.openech.mj.lanterna.toolkit;

import ch.openech.mj.toolkit.ClientToolkit.InputComponentListener;

import com.googlecode.lanterna.gui.component.CheckBox;
import com.googlecode.lanterna.input.Key;

public class LanternaCheckBox extends CheckBox implements ch.openech.mj.toolkit.CheckBox {

	private final InputComponentListener changeListener;
	
	public LanternaCheckBox(InputComponentListener changeListener, String label) {
		super(label, false);
		this.changeListener = changeListener;
	}

	private void fireChangeEvent() {
		changeListener.changed(LanternaCheckBox.this);
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
	public void setEditable(boolean enabled) {
		// not supported
	}

	@Override
	public void setChecked(boolean selected) {
		if (isScrollable() != selected) {
			select();
		}
	}

	@Override
	public boolean isChecked() {
		return isSelected();
	}

}