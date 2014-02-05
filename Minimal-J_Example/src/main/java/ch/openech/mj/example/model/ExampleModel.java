package ch.openech.mj.example.model;

import ch.openech.mj.db.DbPersistence;
import ch.openech.mj.db.ImmutableTable;
import ch.openech.mj.db.Table;
import ch.openech.mj.model.Index;
import ch.openech.mj.model.Model;

public class ExampleModel implements Model {

	public static final Index FULLTEXT = Index.fulltext(BOOK.bookIdentification.title, BOOK.bookIdentification.author);

	
	public static final Index BY_BOOK = Index.by(LEND.book);
	public static final Index BY_CUSTOMER = Index.by(LEND.customer);
	
	public final ImmutableTable<BookIdentification> bookIdentification;
	public final Table<Book> book;
	
	public final ImmutableTable<CustomerIdentification> customerIdentification;
	public final Table<Customer> customer;
	
	public final Table<Lend> lend;
	
	public ExampleModel() {
		super(DbPersistence.embeddedDataSource());
		
		bookIdentification = addImmutableClass(BookIdentification.class);
		book = addClass(Book.class);

		customerIdentification = addImmutableClass(CustomerIdentification.class);
		customer = addClass(Customer.class);

		lend = addClass(Lend.class);
	}
	
}
