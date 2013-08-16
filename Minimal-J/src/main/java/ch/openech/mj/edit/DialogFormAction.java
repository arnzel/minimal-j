package ch.openech.mj.edit;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import ch.openech.mj.edit.form.IForm;
import ch.openech.mj.page.PageContext;
import ch.openech.mj.resources.ResourceHelper;
import ch.openech.mj.resources.Resources;
import ch.openech.mj.toolkit.ClientToolkit;
import ch.openech.mj.toolkit.IComponent;
import ch.openech.mj.toolkit.IDialog;
import ch.openech.mj.toolkit.IDialog.CloseListener;
import ch.openech.mj.toolkit.ProgressListener;

/**
 * An Action that shows a given Editor in a dialog if executed.
 * Dialog means the editor covers only the needed part of the screen.
 * If the Editor should cover all of the screen use EditorPageAction.
 *
 */
public class DialogFormAction<T> extends AbstractAction {
	private final PageContext context;

	public DialogFormAction(PageContext context) {
		this.context = context;
		ResourceHelper.initProperties(this, Resources.getResourceBundle(), getClass().getSimpleName());
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		try {
			showDialog();
		} catch (Exception x) {
			// TODO show dialog
			x.printStackTrace();
		}
	}

	public <T> void showDialog() {
		IForm<?> form = startEditor();
		IComponent layout = ClientToolkit.getToolkit().createEditorLayout(form.getComponent(), editor.getActions());
		final IDialog dialog = ClientToolkit.getToolkit().openDialog(parent, layout, editor.getTitle());
		
		dialog.setCloseListener(new CloseListener() {
			@Override
			public boolean close() {
				editor.checkedClose();
				return editor.isFinished();
			}
		});
		
		editor.setEditorFinishedListener(new Edit.EditorFinishedListener() {
			private ProgressListener progressListener;
			
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
