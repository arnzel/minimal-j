package org.minimalj.frontend.form.element;

import org.minimalj.frontend.editor.Editor;
import org.minimalj.frontend.editor.EditorAction;
import org.minimalj.frontend.form.Form;
import org.minimalj.frontend.page.Page;
import org.minimalj.frontend.page.PageAction;
import org.minimalj.frontend.toolkit.Action;
import org.minimalj.frontend.toolkit.ClientToolkit;
import org.minimalj.frontend.toolkit.ClientToolkit.IComponent;
import org.minimalj.frontend.toolkit.IList;
import org.minimalj.model.properties.PropertyInterface;
import org.minimalj.util.CloneHelper;

/**
 * The state of an ObjectField is saved in the object variable.<p>
 * 
 * You have to implement for an ObjectField:
 * <ul>
 * <li>display: The widgets have to be updated according to the object</li>
 * <li>fireChange: The object has to be updated according the widgets</li>
 * </ul>
 *
 * @param <T>
 */
public abstract class AbstractObjectFormElement<T> extends AbstractFormElement<T> implements Enable {
	// private static final Logger logger = Logger.getLogger(ObjectField.class.getName());
	
	private final boolean editable;

	private final IList list;

	private T object;
	
	public AbstractObjectFormElement(PropertyInterface property, boolean editable) {
		super(property);
		this.editable = editable;
		list = editable ? ClientToolkit.getToolkit().createList(getActions()) : ClientToolkit.getToolkit().createList();
	}

	protected final boolean isEditable() {
		return editable;
	}
	
	@Override
	public T getValue() {
		return object;
	}

	@Override
	public void setValue(T object) {
		this.object = object;
		handleChange();
	}

	@Override
	public IComponent getComponent() {
		return list;
	}
	
	protected abstract Form<T> createFormPanel();

	public class ObjectFormElementEditor extends Editor<T> {
		public ObjectFormElementEditor() {
		}

		@Override
		public Form<T> createForm() {
			return AbstractObjectFormElement.this.createFormPanel();
		}

		@Override
		public T load() {
			return AbstractObjectFormElement.this.getValue();
		}
		
		@Override
		public T newInstance() {
			// Delegate ObjectField class. Its not possible to override here
			// (because of some strange erasure thing)
			return AbstractObjectFormElement.this.newInstance();
		}

		@Override
		public Object save(T edited) {
			AbstractObjectFormElement.this.setValue(edited);
			handleChange();
			return SAVE_SUCCESSFUL;
		}
	}
	
	protected Action getEditorAction() {
		return new EditorAction(new ObjectFormElementEditor());
	}
	
	/*
	 * Only to be used in ObjectFieldEditor
	 */
	protected T newInstance() {
		@SuppressWarnings("unchecked")
		Class<T> clazz = (Class<T>) org.minimalj.util.GenericUtils.getGenericClass(AbstractObjectFormElement.this.getClass());
		T newInstance = CloneHelper.newInstance(clazz);
		return newInstance;
	}
	
	protected void handleChange() {
		display();
		super.fireChange();
	}

	protected void display() {
		list.clear();
		if (object != null) {
			show(object);
		}
	}

	protected abstract void show(T object);
	
	protected void add(Object object, Action... actions) {
		if (isEditable()) {
			list.add(object, actions);
		} else {
			list.add(object);
		}
	}

	protected void add(String text, Page linkedPage) {
		list.add(new PageAction(linkedPage, text));
	}
	
	/**
	 * Null as return is ok, but mostly an 'Add'-Action is returned.
	 * The actions are only shown if the Form(Element) is editable
	 * 
	 * @return the actions that apply for all (and the 'empty') entries
	 */
	protected Action[] getActions() {
		return null;
	}
	
	public void setEnabled(boolean enabled) {
		list.setEnabled(enabled);
	}

}
