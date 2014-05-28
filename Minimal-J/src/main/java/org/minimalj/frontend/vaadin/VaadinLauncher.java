package org.minimalj.frontend.vaadin;

import java.util.Locale;
import java.util.ResourceBundle;

import org.minimalj.application.ApplicationContext;
import org.minimalj.application.Launcher;
import org.minimalj.application.MjApplication;
import org.minimalj.frontend.page.EmptyPage;
import org.minimalj.frontend.page.PageLink;
import org.minimalj.frontend.toolkit.ClientToolkit;
import org.minimalj.frontend.vaadin.toolkit.VaadinClientToolkit;
import org.minimalj.util.StringUtils;
import org.minimalj.util.resources.Resources;

import com.vaadin.Application;

/**
 * TODO VaadinApplication should make Preferences persistent
 * 
 * Note: this class extends Vaadin - Application not the
 * MjApplication
 */
public class VaadinLauncher extends Application {
	private static final long serialVersionUID = 1L;
	
	private final ApplicationContext applicationContext = new VaadinApplicationContext();
	private static boolean applicationInitialized;
	
	@Override
	public void init() {
		initializeApplication();
		
		setTheme("openech");
		VaadinWindow mainWindow = new VaadinWindow(applicationContext);
		setMainWindow(mainWindow);
		mainWindow.show(PageLink.link(EmptyPage.class));
	}

	private synchronized void initializeApplication() {
		if (!applicationInitialized) {
			String applicationName = getProperty("MjApplication");
			if (StringUtils.isBlank(applicationName)) {
				throw new IllegalArgumentException("Missing MjApplication parameter");
			}
			MjApplication application = Launcher.createApplication(applicationName);
			MjApplication.setApplication(application);
			applicationInitialized = true;
		}
	}

	protected ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	static {
		Locale.setDefault(Locale.GERMAN); // TODO correct setting of Locale
		ClientToolkit.setToolkit(new VaadinClientToolkit());
		Resources.addResourceBundle(ResourceBundle.getBundle("org.minimalj.resources.MinimalJ"));
	}

	public class VaadinApplicationContext extends ApplicationContext {
		private Object preferences;
		
		@Override
		public void setUser(String user) {
			VaadinLauncher.this.setUser(user);
		}

		@Override
		public String getUser() {
			return (String) VaadinLauncher.this.getUser();
		}

		@Override
		public void loadPreferences(Object preferences) {
			// nothing done (yet)
		}

		@Override
		public void savePreferences(Object preferences) {
			this.preferences = preferences;
		}
	}
}
