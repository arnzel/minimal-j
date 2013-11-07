package ch.openech.mj.edit.fields;

import ch.openech.mj.model.PropertyInterface;
import ch.openech.mj.toolkit.CheckBox;
import ch.openech.mj.toolkit.ClientToolkit;
import ch.openech.mj.toolkit.IComponent;

public class CheckBoxField extends AbstractEditField<Boolean> {
	
	private final CheckBox checkBox;
	
	public CheckBoxField(PropertyInterface property, String text) {
		super(property, true);
		checkBox = ClientToolkit.getToolkit().createCheckBox(listener(), text);
	}
	
	@Override
	public IComponent getComponent() {
		return checkBox;
	}
	
	@Override
	public Boolean getObject() {
		return checkBox.isChecked();
	}		
	
	@Override
	public void setObject(Boolean value) {
		checkBox.setChecked(Boolean.TRUE.equals(value));
	}

}
