package ch.openech.mj.example;

import java.util.ResourceBundle;

import ch.openech.mj.application.MjApplication;
import ch.openech.mj.example.page.BookTablePage;
import ch.openech.mj.page.ActionGroup;
import ch.openech.mj.page.PageContext;

public class MjExampleApplication extends MjApplication {

	@Override
	public ResourceBundle getResourceBundle() {
		return ResourceBundle.getBundle("ch.openech.mj.example.Application");
	}

	@Override
	public void fillActionGroup(PageContext pageContext, ActionGroup actionGroup) {
		ActionGroup file = actionGroup.getOrCreateActionGroup(ActionGroup.FILE);
		ActionGroup niu = file.getOrCreateActionGroup(ActionGroup.NEW);
//		niu.add(new EditorDialogAction(pageContext, new AddBookEditor()));
//		niu.add(new EditorDialogAction(pageContext, new AddCustomerEditor()));
//		niu.add(new EditorDialogAction(pageContext, new AddLendEditor()));
		niu.add(new AddBookAction(pageContext));
//		niu.add(new EditorDialogAction(pageContext, new AddCustomerEditor()));
//		niu.add(new EditorDialogAction(pageContext, new AddLendEditor()));
	}

	@Override
	public String getWindowTitle(PageContext pageContext) {
		return "Minimal-J Example Application";
	}

	@Override
	public Class<?>[] getSearchClasses() {
		return new Class<?>[]{BookTablePage.class, };
	}

	@Override
	public Class<?> getPreferencesClass() {
		return null;
	}
	
}
