package org.minimalj.frontend.swing.toolkit;

import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.minimalj.frontend.toolkit.ClientToolkit.InputComponentListener;
import org.minimalj.frontend.toolkit.TextField;

public class SwingTextAreaField extends JTextArea implements TextField {
	
	private final InputComponentListener changeListener;
	private Runnable commitListener;
	
	public SwingTextAreaField(InputComponentListener changeListener, int maxLength, String pattern) {
		super(new SwingTextField.FilteredDocument(maxLength, pattern));
		
		this.changeListener = changeListener;
		getDocument().addDocumentListener(new TextFieldChangeListener());
		
		// not yet supported
//		textArea.addActionListener(new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				if (commitListener != null) {
//					commitListener.run();
//				}
//			}
//		});
	}

	public class TextFieldChangeListener implements DocumentListener {

		@Override
		public void changedUpdate(DocumentEvent arg0) {
			fireChangeEvent();
		}

		@Override
		public void insertUpdate(DocumentEvent arg0) {
			fireChangeEvent();
		}

		@Override
		public void removeUpdate(DocumentEvent arg0) {
			fireChangeEvent();
		}
		
		private void fireChangeEvent() {
			changeListener.changed(SwingTextAreaField.this);
		}
	}

	@Override
	public void setCommitListener(Runnable commitListener) {
		this.commitListener = commitListener;
	}

	@Override
	public void setValue(String value) {
		super.setText(value);
	}

	@Override
	public String getValue() {
		return super.getText();
	}
}

