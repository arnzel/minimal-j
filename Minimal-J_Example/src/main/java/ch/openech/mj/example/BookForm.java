package ch.openech.mj.example;

import static ch.openech.mj.example.model.Book.*;
import ch.openech.mj.edit.form.Form;
import ch.openech.mj.edit.form.IForm;
import ch.openech.mj.example.model.Book;

public class BookForm extends Form<Book> {

	public BookForm(IForm.FormChangeListener<Book> formListener) {
		super(formListener, 2);
		
		line(BOOK.title);
		line(BOOK.author, BOOK.date);
		line(BOOK.media, BOOK.pages);
		line(BOOK.available, BOOK.price);
	}
	
}
