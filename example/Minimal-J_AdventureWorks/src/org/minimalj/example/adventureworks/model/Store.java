package org.minimalj.example.adventureworks.model;

import org.minimalj.model.Keys;
import org.minimalj.model.annotation.Required;
import org.minimalj.model.annotation.Size;

public class Store {
	public static final Store $ = Keys.of(Store.class);
	
	public Object id;
	
	public final BusinessEntity businessEntity = new BusinessEntity();
	
	@Required @Size(AdventureWorksFormats.Name)
	public String name;

	public String demographics;
}
