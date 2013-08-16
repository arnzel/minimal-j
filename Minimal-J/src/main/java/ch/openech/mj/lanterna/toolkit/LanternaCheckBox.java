package ch.openech.mj.lanterna.toolkit;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.googlecode.lanterna.gui.component.CheckBox;
import com.googlecode.lanterna.input.Key;

public class LanternaCheckBox extends CheckBox implements ch.openech.mj.toolkit.CheckBox {

	private final ChangeListener changeListener;
	
	public LanternaCheckBox(ChangeListener changeListener, String label) {
		super(label, false);
		this.changeListener = changeListener;
	}

	@Override
	public void setSelected(boolean selected) {
		if (isScrollable() != selected) {
			select();
		}
	}

	private void fireChangeEvent() {
		changeListener.stateChanged(new ChangeEvent(LanternaCheckBox.this));
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
	public void setEnabled(boolean enabled) {
		// not supported
	}

}