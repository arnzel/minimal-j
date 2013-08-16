package ch.openech.mj.example;

import ch.openech.mj.edit.DialogFormAction;
import ch.openech.mj.edit.form.IForm;
import ch.openech.mj.example.model.Book;
import ch.openech.mj.example.page.BookViewPage;
import ch.openech.mj.page.Page;

public class AddBookAction extends DialogFormAction<Book> {

	@Override
	public IForm<Book> createForm(IForm.FormChangeListener<Book> formListener) {
		return new BookForm(formListener);
	}
	
	@Override
	public boolean save(Book book) throws Exception {
		int id = ExamplePersistence.getInstance().book().insert(book);
		setFollowLink(Page.link(BookViewPage.class, Integer.toString(id)));
		return true;
	}

	@Override
	public String getTitle() {
		return "Buch hinzufügen";
	}

}
