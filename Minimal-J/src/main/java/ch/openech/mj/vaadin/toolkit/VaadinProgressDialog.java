package ch.openech.mj.vaadin.toolkit;

import ch.openech.mj.toolkit.ProgressListener;

import com.vaadin.ui.ProgressIndicator;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class VaadinProgressDialog extends Window implements ProgressListener {

	private final ProgressIndicator progressIndicator;
	private final UI ui;
	
	public VaadinProgressDialog(UI ui, String title) {
		super(title);
		this.ui = ui;
		progressIndicator = new ProgressIndicator();
		
		VerticalLayout layout = new VerticalLayout();
		layout.addComponent(progressIndicator);
		setContent(layout);
		
		setModal(true);
		ui.addWindow(this);
	}

	
	@Override
	public void showProgress(int value, int maximum) {
		if (value == maximum) {
			setVisible(false);
			ui.requestRepaint();
		} else {
			progressIndicator.setIndeterminate(false);
			progressIndicator.setValue(((float) value) / ((float) maximum));
		}
	}

}
