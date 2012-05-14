package ch.openech.mj.vaadin.toolkit;

import ch.openech.mj.toolkit.IComponent;

import com.vaadin.ui.Label;

public class VaadinTitle extends Label implements IComponent {

	public VaadinTitle(String content) {
//		content = Util.escapeHTML(content);
		setContentMode(CONTENT_XHTML);
		setValue("<h1>" + content + "</h1><hr />");
	}

}