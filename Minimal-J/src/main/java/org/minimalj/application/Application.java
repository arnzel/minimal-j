package org.minimalj.application;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import org.minimalj.backend.SocketBackendServer;
import org.minimalj.backend.db.DbBackend;
import org.minimalj.frontend.page.EmptyPage;
import org.minimalj.frontend.page.Page;
import org.minimalj.frontend.swing.SwingFrontend;
import org.minimalj.frontend.toolkit.IAction;
import org.minimalj.frontend.vaadin.VaadinFrontend;
import org.minimalj.util.resources.Resources;

/**
 * Extend this class as the start point for your Application.
 * Both frontend and backend get their application
 * specification from this class.<p>
 * 
 * Set the first argument of the JVM if the application
 * is started with SwingFrontend as main class or set the <code>init-param</code> in the servlet
 * element in the <code>web.xml</code> if the VaadinFrontend is used.<p>
 *
 * All non static methods can be overridden to define the behavior
 * of the application.
 * 
 * @see SwingFrontend
 * @see VaadinFrontend
 * @see SocketBackendServer
 * @see DbBackend
 */
public abstract class Application {
	private static Application application;
	
	public static Application getApplication() {
		if (application == null) {
			throw new IllegalStateException("Application has to be initialized");
		}
		return application;
	}
	
	/**
	 * Sets the application of this vm. Can only be called once.
	 * This method should only be called by a frontend or a backend main class.
	 * 
	 * @param application normally the created by createApplication method
	 */
	public static synchronized void setApplication(Application application) {
		if (Application.application != null) {
			throw new IllegalStateException("Application cannot be changed");
		}		
		if (application == null) {
			throw new IllegalArgumentException("Application cannot be null");
		}
		Application.application = application;
		
		ResourceBundle resourceBundle = application.getResourceBundle();
		if (resourceBundle != null) Resources.addResourceBundle(resourceBundle);
	}
	
	/**
	 * This is just a shortcut for creating the application from jvm arguments.
	 * Most frontend or backend main classes use this method
	 * 
	 * @param args the arguments provided to the jvm
	 */
	public static void initApplication(String[] args) {
		if (args.length < 1) {
			throw new IllegalArgumentException("Please specify an Application as first argument");
		}
		
		String applicationClassName = args[0];
		Application application = createApplication(applicationClassName);
		Application.setApplication(application);
	}
	
	/**
	 * Creates the Application from a class name. This method should normally only
	 * be called by a frontend or a backend main class.
	 * 
	 * @param applicationClassName qualified class name
	 * @return the created application. Different exceptions are thrown if the
	 * creation failed.
	 */
	public static Application createApplication(String applicationClassName) {
		Class<?> applicationClass;
		try {
			applicationClass = Class.forName(applicationClassName);
		} catch (ClassNotFoundException e) {
			throw new IllegalArgumentException("Could not found Application class: " + applicationClassName);
		}
		Object application;
		try {
			application = applicationClass.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			throw new IllegalArgumentException("Could not instantiate Application class: " + applicationClassName, e);
		}
		
		if (!(application instanceof Application)) {
			throw new IllegalArgumentException("Class " + applicationClassName + " doesn't extend Application");
		}
		
		return (Application) application;
	}
	
	/**
	 * 
	 * @param simplePackageName for example "editor".
	 * @return the package name of this type of package for this application. For example "org.minimalj.example.frontend.editor"
	 */
	public static String getCompletePackageName(String simplePackageName) {
		Application application = Application.getApplication();
		String applicationClassName = application.getClass().getName();
		int pos = applicationClassName.lastIndexOf(".");
		if (pos  >= 0) {
			return applicationClassName.substring(0, pos + 1) + simplePackageName;
		} else {
			return applicationClassName + "." + simplePackageName;
		}
	}
	
	/**
	 * note: Use MultiResourceBundle if more than one ResourceBundle
	 * should be loaded.
	 * @return The application specific ResourceBundle.
	 */
	protected ResourceBundle getResourceBundle() {
		try {
			return ResourceBundle.getBundle(this.getClass().getName());
		} catch (MissingResourceException x) {
			Logger logger = Logger.getLogger(Application.class.getName());
			logger.warning("Missing the default ResourceBundle for " + this.getClass().getName());
			logger.fine("The default ResourceBundle has the same name as the Application that is launched.");
			logger.fine("See the MjExampleApplication.java and MjExampleApplication.properties");
			return null;
		}
	}

	public Map<String, String> getQueries() {
		try {
			ResourceBundle queriesResourceBundle = ResourceBundle.getBundle(getCompletePackageName("backend") + ".queries");
			if (queriesResourceBundle != null) {
				HashMap<String, String> queries = new HashMap<>();
				Enumeration<String> keyEnumeration = queriesResourceBundle.getKeys();
				while (keyEnumeration.hasMoreElements()) {
					String key = keyEnumeration.nextElement();
					queries.put(key, queriesResourceBundle.getString(key));
				}
				return queries;
			} else {
				return Collections.emptyMap();
			}
		} catch (MissingResourceException x) {
			Logger logger = Logger.getLogger(Application.class.getName());
			logger.info("No queries available");
			return null;
		}
	}
	
	public Class<?>[] getEntityClasses() {
		return new Class<?>[0];
	}

	public String getName() {
		if (Resources.getResourceBundle().containsKey("Application.title")) {
			return Resources.getResourceBundle().getString("Application.title");
		} else {
			return getClass().getSimpleName();
		}
	}
	
	public Class<?> getPreferencesClass() {
		return null;
	}
	
	public Class<?>[] getSearchClasses() {
		return new Class<?>[0];
	}
	
	public Page createDefaultPage() {
		return new EmptyPage();
	}
	
	public List<IAction> getActionsNew() {
		return Collections.emptyList();
	}

	public List<IAction> getActionsImport() {
		return Collections.emptyList();
	}

	public List<IAction> getActionsExport() {
		return Collections.emptyList();
	}

	public List<IAction> getActionsView() {
		return Collections.emptyList();
	}
	
	public static void main(String[] args) throws Exception {
		Logger logger = Logger.getLogger(Application.class.getName());
		logger.warning("Starting the Application class is not the intended way to start a Minimal-J application");
		String mainClass = System.getProperty("sun.java.command");
		if (mainClass == null) {
			logger.severe("and the started application could not be retrieved. Nothing started.");
		} else if (Application.class.getName().equals(mainClass)) {
			logger.severe("and starting the Application class doesn't work at all. Nothing started.");
		} else {
			SwingFrontend.main(new String[]{mainClass});
		}
	}
	
}