package ch.openech.mj.example;

import static ch.openech.mj.example.model.Customer.*;
import ch.openech.mj.edit.form.Form;
import ch.openech.mj.example.model.Customer;

public class CustomerForm extends Form<Customer> {

	public CustomerForm(boolean editable) {
		super(editable, 4);
		
		line(CUSTOMER.customerIdentification.firstName);
		line(CUSTOMER.customerIdentification.name);
		line(CUSTOMER.customerIdentification.birthDay);
		line(CUSTOMER.remarks);
	
		line(CUSTOMER.address.mrMrs, CUSTOMER.address.title);
		line(CUSTOMER.address.firstName, CUSTOMER.address.lastName);
		line(CUSTOMER.address.addressLine1);
		line(CUSTOMER.address.addressLine2);
		line(CUSTOMER.address.street, CUSTOMER.address.houseNumber);
		line(CUSTOMER.address.country, CUSTOMER.address.zip, CUSTOMER.address.town);
		line(CUSTOMER.address.locality);
	}
	
}
