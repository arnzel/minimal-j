package ch.openech.mj.swing.toolkit;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.ChangeListener;

import ch.openech.mj.db.model.Constants;
import ch.openech.mj.edit.fields.EditField;
import ch.openech.mj.toolkit.IComponent;
import ch.openech.mj.util.StringUtils;

public class SwingDirectoryField extends JPanel implements EditField<String>, IComponent {

	private final String name;
	private final JTextField textField;
	private final JButton button;
	
	public SwingDirectoryField(Object key) {
		super(new BorderLayout());
		name = Constants.getConstant(key);
		
		textField = new JTextField();
		button = new JButton("...");
		
		add(textField, BorderLayout.CENTER);
		add(button, BorderLayout.LINE_END);

		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser(getObject());
				chooser.setMultiSelectionEnabled(false);
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				if (JFileChooser.APPROVE_OPTION == chooser.showDialog(textField, "Verzeichnis wählen")) {
					setObject(chooser.getSelectedFile().getPath());
				}
			}
		});
	}

	@Override
	public String getObject() {
		return textField.getText();
	}

	@Override
	public void setObject(String value) {
		textField.setText(value);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setChangeListener(ChangeListener changeListener) {
		// TODO
	}

	@Override
	public boolean isEmpty() {
		return StringUtils.isEmpty(textField.getText());
	}

	@Override
	public IComponent getComponent() {
		return this;
	}

}
