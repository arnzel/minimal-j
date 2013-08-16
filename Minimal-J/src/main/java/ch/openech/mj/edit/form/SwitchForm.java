package ch.openech.mj.edit.form;

import ch.openech.mj.toolkit.ClientToolkit;
import ch.openech.mj.toolkit.IComponent;
import ch.openech.mj.toolkit.SwitchLayout;

public class SwitchForm<T> implements IForm<T> {

	private SwitchLayout switchLayout;
	
	public SwitchForm() {
		switchLayout = ClientToolkit.getToolkit().createSwitchLayout();
	}

	@Override
	public IComponent getComponent() {
		return switchLayout;
	}

	public void setForm(IForm<?> form) {
		switchLayout.show(form.getComponent());
	}

	@Override
	public void setObject(T object) {
		//
	}

}
