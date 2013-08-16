package ch.openech.mj.application;

import java.util.ResourceBundle;

import ch.openech.mj.page.ActionGroup;
import ch.openech.mj.page.EmptyPage;
import ch.openech.mj.page.Page;
import ch.openech.mj.page.PageContext;
import ch.openech.mj.resources.Resources;

/**
 * Extend this class as main entry for your Application.<p>
 * 
 * Set the argument <code>MjApplication</code> as VM argument if the application
 * is started with SwingLauncher or set the <code>init-param</code> in the servlet
 * element in the <code>web.xml</code> if the VaadinLauncher is used.
 *
 */
public abstract class MjApplication {

	private static MjApplication application;
	
	public static MjApplication getApplication() {
		if (application == null) {
			throw new IllegalStateException("CientApplicationConfig has to be initialized");
		}
		return application;
	}
	
	private static synchronized void setApplication(MjApplication application) {
		if (MjApplication.application != null) {
			throw new IllegalStateException("Application cannot be changed");
		}		
		if (application == null) {
			throw new IllegalArgumentException("Application cannot be null");
		}
		MjApplication.application = application;
	}
	
	/**
	 * 
	 * @param simplePackageName for example "editor".
	 * @return the package name of this type of package for this application. For example "ch.openech.mj.example.editor"
	 */
	public static String getCompletePackageName(String simplePackageName) {
		MjApplication application = MjApplication.getApplication();
		String applicationClassName = application.getClass().getName();
		int pos = applicationClassName.lastIndexOf(".");
		if (pos  >= 0) {
			return applicationClassName.substring(0, pos + 1) + simplePackageName;
		} else {
			return applicationClassName + "." + simplePackageName;
		}
	}
	
	protected MjApplication() {
		setApplication(this);
		Resources.addResourceBundle(getResourceBundle());
	}
	
	public abstract ResourceBundle getResourceBundle();

	public abstract String getWindowTitle(PageContext pageContext);
	
	public abstract Class<?> getPreferencesClass();
	
	public abstract Class<?>[] getSearchClasses();
	
	public Page createDefaultPage() {
		return new EmptyPage(context);
	}
	
	public void fillActionGroup(PageContext pageContext, ActionGroup actionGroup) {
		// should be done in subclass
	}
	
	public void init() {
		// should be done in subclass
	}
	
}
