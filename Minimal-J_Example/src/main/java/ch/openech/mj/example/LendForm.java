package ch.openech.mj.example;

import static ch.openech.mj.example.model.Lend.*;
import ch.openech.mj.edit.form.Form;
import ch.openech.mj.edit.form.IForm;
import ch.openech.mj.example.model.Lend;

public class LendForm extends Form<Lend> {

	public LendForm(IForm.FormChangeListener<Lend> formListener) {
		super(formListener);
		
		line(new BookField(LEND.book));
		line(new CustomerField(LEND.customer));
		line(LEND.till);
	}
	
}
