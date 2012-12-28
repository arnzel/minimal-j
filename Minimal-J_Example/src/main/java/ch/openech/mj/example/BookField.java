package ch.openech.mj.example;

import java.util.List;

import ch.openech.mj.db.model.Constants;
import ch.openech.mj.db.model.PropertyInterface;
import ch.openech.mj.edit.SearchDialogAction;
import ch.openech.mj.edit.fields.ObjectFlowField;
import ch.openech.mj.edit.form.IForm;
import ch.openech.mj.example.model.Book;

public class BookField extends ObjectFlowField<Book> {

	public BookField(PropertyInterface property) {
		super(property);
	}
	
	public BookField(Book key) {
		this(Constants.getProperty(key));
	}
	
	@Override
	public IForm<Book> createFormPanel() {
		// not used
		return null;
	}

	@Override
	protected void show(Book book) {
		addHtml(book.title);
	}

	@Override
	protected void showActions() {
        addAction(new BookSearchAction());
        addAction(new RemoveObjectAction());
	}
	
	public class BookSearchAction extends SearchDialogAction<Book> {
		
		public BookSearchAction() {
			super(Book.BOOK.title, Book.BOOK.author);
		}

		@Override
		protected List<Book> search(String text) {		
			List<Book> resultList = ExamplePersistence.getInstance().book().find(text);
			return resultList;
		}

		@Override
		protected void save(Book object) {
			setObject(object);
		}

	}
}