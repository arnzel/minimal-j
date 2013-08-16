package ch.openech.mj.edit;

import ch.openech.mj.edit.form.IForm;
import ch.openech.mj.toolkit.ClientToolkit;
import ch.openech.mj.toolkit.IAction;
import ch.openech.mj.toolkit.IComponent;
import ch.openech.mj.toolkit.IDialog;
import ch.openech.mj.toolkit.IDialog.CloseListener;

/**
 * An Action that shows a given Editor in a dialog if executed.
 * Dialog means the editor covers only the needed part of the screen.
 * If the Editor should cover all of the screen use EditorPageAction.
 *
 */
public class EditDialogAction implements IAction {
	private final Edit<?> editor;

	public EditDialogAction(Edit<?> editor) {
		this.editor = editor;
	}

	@Override
	public void action(IComponent source) {
		try {
			showDialogOn(editor, source);
		} catch (Exception x) {
			// TODO show dialog
			x.printStackTrace();
		}
	}

	public static <T> void showDialogOn(final Edit<T> editor, IComponent source) {
		IForm<?> form = editor.startEditor();
		IComponent layout = ClientToolkit.getToolkit().createEditorLayout(form.getComponent(), editor.getActions());
		final IDialog dialog = ClientToolkit.getToolkit().openDialog(source, layout, editor.getTitle());
		
		dialog.setCloseListener(new CloseListener() {
			@Override
			public boolean close() {
				editor.checkedClose();
				return editor.isFinished();
			}
		});
		
		editor.setEditorFinishedListener(new Edit.EditorFinishedListener() {
			@Override
			public void finished() {
				dialog.closeDialog();
			}

			@Override
			public void canceled() {
				dialog.closeDialog();
			}
		});
		dialog.openDialog();
		ClientToolkit.getToolkit().focusFirstComponent(form.getComponent());
	}
	
}
