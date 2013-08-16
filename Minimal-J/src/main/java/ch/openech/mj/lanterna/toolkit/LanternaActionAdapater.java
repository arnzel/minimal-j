package ch.openech.mj.lanterna.toolkit;

import java.awt.event.ActionEvent;

import com.googlecode.lanterna.gui.Action;

public class LanternaActionAdapater implements Action {

	private final javax.swing.Action swingAction;
	private final Object source;
	
	public LanternaActionAdapater(javax.swing.Action swingAction, Object source) {
		this.swingAction = swingAction;
		this.source = source;
	}

	@Override
	public void doAction() {
		swingAction.actionPerformed(new ActionEvent(source, 0, null));
	}
	
	public String getText() {
		return (String) swingAction.getValue(javax.swing.Action.NAME);
	}

	@Override
	public String toString() {
		String text = getText();
		return text != null ? text : "";
	}
	
}
