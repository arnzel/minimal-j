package ch.openech.mj.example;

import static ch.openech.mj.example.model.Customer.*;
import ch.openech.mj.edit.form.Form;
import ch.openech.mj.edit.form.IForm;
import ch.openech.mj.example.model.Customer;

public class CustomerForm extends Form<Customer> {

	public CustomerForm(IForm.FormChangeListener<Customer> formListener) {
		super(formListener);
		
		line(CUSTOMER.firstName);
		line(CUSTOMER.name);
		line(CUSTOMER.birthDay);
		
	}
	
}
