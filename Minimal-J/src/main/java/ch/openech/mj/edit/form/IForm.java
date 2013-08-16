package ch.openech.mj.edit.form;

import java.util.List;

import ch.openech.mj.edit.validation.ValidationMessage;
import ch.openech.mj.toolkit.IComponent;

public interface IForm<T> {

	public IComponent getComponent();
	
	public void setObject(T value);

	public interface FormChangeListener<S> {

		public void validate(S object, List<ValidationMessage> validationResult);

		public void indicate(List<ValidationMessage> validationMessages, boolean allUsedFieldsValid);

		public void changed();

		public void commit();

	}
}