package ch.openech.mj.swing;

import javax.swing.AbstractAction;

import ch.openech.mj.resources.Resources;
import ch.openech.mj.swing.resources.SwingResourceHelper;

public abstract class SwingResourceAction extends AbstractAction {
	private static final long serialVersionUID = 1L;

	protected SwingResourceAction() {
		String actionName = this.getClass().getSimpleName();
		SwingResourceHelper.initProperties(this, Resources.getResourceBundle(), actionName);
	}

	protected SwingResourceAction(String actionName) {
		SwingResourceHelper.initProperties(this, Resources.getResourceBundle(), actionName);
	}
}
