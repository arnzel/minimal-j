package ch.openech.mj.example;

import ch.openech.mj.db.Table;
import ch.openech.mj.edit.Editor;
import ch.openech.mj.edit.form.IForm;
import ch.openech.mj.example.model.Lend;

public class AddLendEditor extends Editor<Lend> {

	@Override
	public IForm<Lend> createForm(IForm.FormChangeListener<Lend> formListener) {
		return new LendForm(formListener);
	}
	
	@Override
	public boolean save(Lend lend) throws Exception {
		int id = ((Table<Lend>) ExamplePersistence.getInstance().getTable(Lend.class)).insert(lend);
		return true;
	}

	@Override
	public String getTitle() {
		return "Ausleihe hinzufügen";
	}

}
