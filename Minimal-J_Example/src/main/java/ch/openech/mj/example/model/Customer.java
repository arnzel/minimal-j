package ch.openech.mj.example.model;

import ch.openech.mj.model.Keys;
import ch.openech.mj.model.annotation.Size;

public class Customer {

	public static final Customer CUSTOMER = Keys.of(Customer.class);
	public static final Object[] INDEX = new Object[] {Customer.CUSTOMER.customerIdentification.firstName, Customer.CUSTOMER.customerIdentification.name};

	public CustomerIdentification customerIdentification = new CustomerIdentification();
	public final Address address = new Address();
	
	@Size(2000)
	public String remarks;
	
}
