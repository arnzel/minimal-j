package ch.openech.mj.edit.fields;

import ch.openech.mj.edit.Edit;
import ch.openech.mj.edit.EditDialogAction;
import ch.openech.mj.model.PropertyInterface;
import ch.openech.mj.resources.Resources;
import ch.openech.mj.toolkit.ClientToolkit;
import ch.openech.mj.toolkit.IAction;
import ch.openech.mj.toolkit.IComponent;
import ch.openech.mj.toolkit.TextField;

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
public abstract class ObjectFlowField<T> extends ObjectField<T> {
	// private static final Logger logger = Logger.getLogger(ObjectField.class.getName());
	
	public ObjectFlowField(PropertyInterface property) {
		this(property, true);
	}

	public ObjectFlowField(PropertyInterface property, boolean editable) {
		super(property, editable);
		
	}

	public abstract class ObjectFieldPartEditor<P> extends Edit<P> {

		@Override
		public P load() {
			return getPart(ObjectFlowField.this.getObject());
		}
		
		@Override
		public boolean save(P part) {
			setPart(ObjectFlowField.this.getObject(), part);
			fireObjectChange();
			return true;
		}

		protected abstract P getPart(T object);

		protected abstract void setPart(T object, P p);
		
	}
	
	// why public
	public class RemoveObjectAction implements IAction {
		@Override
		public void action(IComponent source) {
			ObjectFlowField.this.setObject(null);
		}
	}
	
	protected void addObject(Object object) {
		if (object != null) {
			TextField textField = ClientToolkit.getToolkit().createReadOnlyTextField();
			textField.setText(object.toString());
			visual.add(textField);
		}
	}

	protected void addHtml(String html) {
		visual.addHtml(html);
	}

	protected void addGap() {
		visual.addGap();
	}

	public void addAction(IAction action) {
		addAction(Resources.getString(action.getClass().getSimpleName() + ".text"), action);
	}

	public void addLink(String text, String link) {
		visual.add(ClientToolkit.getToolkit().createLink(text, link));
	}

	@Deprecated
	public void addAction(Edit edit) {
		addAction(edit, edit.getClass().getSimpleName());
	}

	@Deprecated
	public void addAction(Edit edit, String text) {
		text = Resources.getString(text + ".text");
		EditDialogAction editDialogAction = new EditDialogAction(edit);
		visual.add(ClientToolkit.getToolkit().createLink(text, editDialogAction));
	}
	
}
