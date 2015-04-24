package org.minimalj.frontend.page;

import java.util.ArrayList;
import java.util.List;

import org.minimalj.frontend.editor.Editor;
import org.minimalj.frontend.editor.EditorAction;
import org.minimalj.frontend.toolkit.Action;

public class ActionGroup extends Action {

	private final List<Action> items = new ArrayList<>();

	public ActionGroup(String resourceName) {
		super(resourceName);
	}

	@Override
	public void action() {
		// n/a
	}

	public void add(Action item) {
		items.add(item);
	}

	public void add(Editor<?> editor) {
		items.add(new EditorAction(editor));
	}
	
	public void add(Page page) {
		items.add(new PageAction(page));
	}

	public void add(Page page, String name) {
		items.add(new PageAction(page, name));
	}

	public List<Action> getItems() {
		return items;
	}
	
	public ActionGroup addGroup(String name) {
		ActionGroup group = new ActionGroup(name);
		add(group);
		return group;
	}

	public void addSeparator() {
		add(new Separator());
	}

}