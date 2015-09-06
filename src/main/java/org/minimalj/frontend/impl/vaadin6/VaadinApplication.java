package org.minimalj.frontend.impl.vaadin6;

import java.util.Enumeration;
import java.util.Locale;

import org.minimalj.application.Application;
import org.minimalj.frontend.Frontend;
import org.minimalj.frontend.impl.vaadin6.toolkit.VaadinFrontend;
import org.minimalj.frontend.page.EmptyPage;
import org.minimalj.util.StringUtils;

/**
 * TODO VaadinApplication should make Preferences persistent
 * 
 * Note: this class extends Vaadin - Application not the
 * Minimal-J Application
 */
public class VaadinApplication extends com.vaadin.Application {
	private static final long serialVersionUID = 1L;
	
	private static boolean applicationInitialized;
	
	@Override
	public void init() {
		initializeApplication();
		
		setTheme("openech");
		VaadinWindow mainWindow = new VaadinWindow();
		setMainWindow(mainWindow);
		mainWindow.show(new EmptyPage());
	}

	private synchronized void initializeApplication() {
		if (!applicationInitialized) {
			String applicationName = getProperty("Application");
			if (StringUtils.isBlank(applicationName)) {
				throw new IllegalArgumentException("Missing Application parameter");
			}
			Application application = Application.createApplication(applicationName);
			Application.setApplication(application);
			copyPropertiesFromWebContextToSystem();
			applicationInitialized = true;
		}
	}
	
	private void copyPropertiesFromWebContextToSystem() {
		Enumeration<?> propertyNames = getPropertyNames();
		while (propertyNames.hasMoreElements()) {
			String propertyName = (String) propertyNames.nextElement();
			System.setProperty(propertyName, getProperty(propertyName));
		}
	}	

	static {
		Locale.setDefault(Locale.GERMAN); // TODO correct setting of Locale
		Frontend.setInstance(new VaadinFrontend());
	}
}