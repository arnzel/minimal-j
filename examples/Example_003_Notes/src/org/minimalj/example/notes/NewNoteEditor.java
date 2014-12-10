package org.minimalj.example.notes;

import org.minimalj.backend.Backend;
import org.minimalj.frontend.edit.Editor;
import org.minimalj.frontend.edit.form.Form;

public class NewNoteEditor extends Editor<Note> {

	@Override
	protected Form<Note> createForm() {
		return new NoteForm();
	}

	@Override
	protected Object save(Note object) throws Exception {
		Backend.getInstance().insert(object);
		return null;
	}

	private static class NoteForm extends Form<Note> {
		
		public NoteForm() {
			line(Note.$.date);
			line(Note.$.text);
		}
	}
}
