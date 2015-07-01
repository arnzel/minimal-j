package org.minimalj.example.notes2;

import java.util.List;

import org.minimalj.application.Application;
import org.minimalj.frontend.page.ActionGroup;
import org.minimalj.frontend.page.Page;
import org.minimalj.frontend.toolkit.Action;

public class NotesApplication extends Application {

	@Override
	public List<Action> getMenu() {
		ActionGroup menu = new ActionGroup(null);
		menu.add(new NewNoteEditor());
		return menu.getItems();
	}

	@Override
	public Page createDefaultPage() {
		return new NoteTablePage();
	}

	@Override
	public Class<?>[] getEntityClasses() {
		return new Class[]{Note.class};
	}
	
	
}
