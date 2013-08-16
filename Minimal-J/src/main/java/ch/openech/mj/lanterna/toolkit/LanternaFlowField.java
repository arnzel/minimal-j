package ch.openech.mj.lanterna.toolkit;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Action;

import ch.openech.mj.toolkit.FlowField;

import com.googlecode.lanterna.gui.Component;
import com.googlecode.lanterna.gui.component.Button;
import com.googlecode.lanterna.gui.component.Label;
import com.googlecode.lanterna.gui.component.Panel;
import com.googlecode.lanterna.gui.layout.VerticalLayout;

public class LanternaFlowField extends Panel implements FlowField {
	private List<Component> components = new ArrayList<>();
	
	public LanternaFlowField() {
		setLayoutManager(new VerticalLayout());
	}
	
	@Override
	public void removeAll() {
		for (Component c : components) {
			removeComponent(c);
		}
		components.clear();
	}

	@Override
	public void addString(Object object) {
		Label label = new Label("" + object);
		components.add(0, label);
		addComponent(label);
	}

	@Override
	public void addHtml(String html) {
		Label label = new Label(html);
		components.add(0, label);
		addComponent(label);
	}

	@Override
	public void addAction(final Action action) {
		final Button button = new Button((String) action.getValue(Action.NAME), new com.googlecode.lanterna.gui.Action() {
			@Override
			public void doAction() {
				action.actionPerformed(new ActionEvent(LanternaFlowField.this, 0, ""));
			}
		});
		components.add(0, button);
		addComponent(button);
	}

	@Override
	public void addGap() {
		addString(" ");
	}

	@Override
	public void setEnabled(boolean enabled) {
		// ignored
	}

}
