package org.minimalj.example.library.frontend.page;

import static org.minimalj.example.library.model.Customer.*;

import java.util.List;

import org.minimalj.backend.Backend;
import org.minimalj.example.library.model.Customer;
import org.minimalj.frontend.page.TablePage;
import org.minimalj.transaction.criteria.Criteria;
import org.minimalj.util.IdUtils;


public class CustomerTablePage extends TablePage<Customer> {

	private final String text;
	
	public static final Object[] FIELDS = {
		$.firstName, //
		$.name, //
		$.dateOfBirth, //
	};
	
	public CustomerTablePage(String text) {
		super(FIELDS, text);
		this.text = text;
	}
	
	@Override
	protected List<Customer> load(String searchText) {
		return Backend.getInstance().read(Customer.class, Criteria.search(searchText), 100);
	}

	@Override
	protected void clicked(Customer selectedObject, List<Customer> selectedObjects) {
		show(CustomerPage.class, IdUtils.getIdString(selectedObject));
	}
	
	@Override
	public String getTitle() {
		return "Treffer für " + text;
	}
}
