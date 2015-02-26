package org.minimalj.frontend.vaadin.toolkit;

import org.minimalj.frontend.toolkit.ProgressListener;

import com.vaadin.ui.ProgressIndicator;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class VaadinProgressDialog extends Window implements ProgressListener {
	private static final long serialVersionUID = 1L;

	private final ProgressIndicator progressIndicator;
	
	public VaadinProgressDialog(String title) {
		super(title);
		progressIndicator = new ProgressIndicator();
		
		VerticalLayout layout = new VerticalLayout();
		layout.addComponent(progressIndicator);
		setContent(layout);
		
		setModal(true);
		UI.getCurrent().addWindow(this);
	}

	
	@Override
	public void showProgress(int value, int maximum) {
		if (value == maximum) {
			setVisible(false);
		} else {
			progressIndicator.setIndeterminate(false);
			progressIndicator.setValue(((float) value) / ((float) maximum));
		}
	}

}
