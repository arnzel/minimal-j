package org.minimalj.frontend.form.element;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import org.minimalj.model.properties.PropertyInterface;
import org.minimalj.model.validation.InvalidValues;
import org.minimalj.util.DateUtils;
import org.minimalj.util.StringUtils;
import org.minimalj.util.mock.MockDate;

public class LocalDateFormElement extends FormatFormElement<LocalDate> {

	public LocalDateFormElement(PropertyInterface property, boolean editable) {
		super(property, editable);
	}
	
	@Override
	protected String getAllowedCharacters(PropertyInterface property) {
		return "01234567890.-";
	} 

	@Override
	protected int getAllowedSize(PropertyInterface property) {
		return 10;
	}

	@Override
	public LocalDate getValue() {
		String fieldText = textField.getValue();
		try {
			return DateUtils.parse(fieldText);
		} catch (DateTimeParseException x) {
			return InvalidValues.createInvalidLocalDate(fieldText);
		}
	}
	
	@Override
	public void setValue(LocalDate value) {
		if (InvalidValues.isInvalid(value)) {
			String text = InvalidValues.getInvalidValue(value);
			textField.setValue(text);
		} else if (value != null) {
			String text = DateUtils.format(value);
			if (!StringUtils.equals(textField.getValue(), text)) {
				textField.setValue(text);
			}
		} else {
			textField.setValue(null);
		}
	}
	
	@Override
	public void mock() {
		setValue(MockDate.generateRandomDate());
	}
}