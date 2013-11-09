package ch.openech.mj.vaadin.toolkit;

import ch.openech.mj.toolkit.IFocusListener;
import ch.openech.mj.toolkit.TextField;
import ch.openech.mj.util.StringUtils;

import com.vaadin.ui.Label;

public class VaadinReadOnlyTextField extends Label implements TextField {
	private static final long serialVersionUID = 1L;

	public VaadinReadOnlyTextField() {
		addStyleName("v-html-readonly");
		setWidth("100%");
	}

	
	@Override
	public void setEditable(boolean editable) {
		// read only field cannot be enabled
	}

	@Override
	public void setCommitListener(Runnable listener) {
		// read only field cannot get commit command
	}

	@Override
	public void setInput(String text) {
		if (!StringUtils.isEmpty(getInput())) {
			setReadOnly(false);
		}
		if (!StringUtils.isEmpty(text)) {
			super.setValue(text);
			setContentMode(CONTENT_TEXT);
		} else {
			super.setValue("&nbsp;");
			setContentMode(CONTENT_XHTML);
		}
		setReadOnly(true);		
	}

	@Override
	public String getInput() {
		return (String) super.getValue();
	}

	@Override
	public void setFocusListener(IFocusListener focusListener) {
		// TODO Auto-generated method stub
		
	}
	
}