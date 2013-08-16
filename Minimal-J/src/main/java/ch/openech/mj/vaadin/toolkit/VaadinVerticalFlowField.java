package ch.openech.mj.vaadin.toolkit;

import ch.openech.mj.toolkit.FlowField;
import ch.openech.mj.toolkit.IComponent;

import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class VaadinVerticalFlowField extends VerticalLayout implements FlowField {

	public VaadinVerticalFlowField() {
	}
	
	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
	}

	@Override
	public void removeAll() {
		removeAllComponents();
	}

	@Override
	public void addGap() {
		addComponent(new Label("."));
	}

	@Override
	public void add(IComponent component) {
		super.addComponent((Component) component);
	}
	
}
