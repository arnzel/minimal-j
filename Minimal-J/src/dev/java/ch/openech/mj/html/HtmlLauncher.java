package ch.openech.mj.html;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ch.openech.mj.application.ApplicationContext;
import ch.openech.mj.application.MjApplication;
import ch.openech.mj.lanterna.component.TextMenu;
import ch.openech.mj.page.ActionGroup;
import ch.openech.mj.page.PageContext;
import ch.openech.mj.resources.Resources;
import ch.openech.mj.toolkit.ClientToolkit;
import ch.openech.mj.util.StringUtils;

import com.googlecode.lanterna.gui.component.Panel;

public class HtmlLauncher extends HttpServlet {

	private final ApplicationContext applicationContext = new HtmlApplicationContext();
	private static boolean applicationInitialized;
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		initializeApplication();
		
		System.out.println(req.getRequestURI());
		if (req.getRequestURI().endsWith(".js") || req.getRequestURI().endsWith(".css")) {
			serveStaticResourcesInVAADIN(req.getRequestURI().substring(req.getContextPath().length()), req, resp);
			return;
		}
		
		PrintWriter writer = resp.getWriter();
		writer.write("<html>");
		writer.write("<head>");
		writer.write("<link rel=\"stylesheet\" href=\"jquery_custom/development-bundle/themes/base/jquery.ui.all.css\" />");
//		writer.write("<script src=\"jquery_custom/development-bundle/jquery-1.9.1.js\"></script>");
		writer.write("<script src=\"//ajax.googleapis.com/ajax/libs/jquery/1.10.1/jquery.min.js\"></script>");
		
//		writer.write("<script src=\"jquery_custom/development-bundle/ui/jquery.ui.core.js\"></script>");
//		writer.write("<script src=\"jquery_custom/development-bundle/ui/jquery.ui.widget.js\"></script>");
//		writer.write("<script src=\"jquery_custom/development-bundle/ui/jquery.ui.position.js\"></script>");
//		writer.write("<script src=\"jquery_custom/development-bundle/ui/jquery.ui.button.js\"></script>");
//		writer.write("<script src=\"jquery_custom/development-bundle/ui/jquery.ui.menu.js\"></script>");
//		writer.write("<script src=\"jquery_custom/development-bundle/ui/jquery.ui.menubar.js\"></script>");
		
//		writer.write("<script>$( document ).ready(function() { $(\"#bar\").menubar(); alert(\"Hallo\"); });</script>");
		writer.write("<script>$('#id').get(0).type = 'date'</script>");
//		writer.write("<script>alert('hallo')</script>");
//		writer.write("<script>$( document ).ready(function() { alert(\"Hallo\"); $('#id').get(0).type = 'date'; });</script>");
		writer.write("</head><body>");
//		createMenu(writer);
		writer.write("Hallo<br>");
//		writer.write(getInitParameter("MjApplication"));
		writer.write("<form><input id=\"in\" /></form>");
		writer.write("</body>");
		writer.write("</html>");

		
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}
	

	private synchronized void initializeApplication() {
		if (!applicationInitialized) {
			String applicationClassName = getInitParameter("MjApplication");
			if (StringUtils.isBlank(applicationClassName)) {
				throw new IllegalArgumentException("Missing MjApplication parameter");
			}
			try {
				@SuppressWarnings("unchecked")
				Class<? extends MjApplication> applicationClass = (Class<? extends MjApplication>) Class.forName(applicationClassName);
				MjApplication application = applicationClass.newInstance();
				application.init();
			} catch (IllegalAccessException | InstantiationException | ClassNotFoundException x) {
				throw new RuntimeException(x);
			}
			applicationInitialized = true;
		}
	}

	protected ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	static {
		Locale.setDefault(Locale.GERMAN); // TODO correct setting of Locale
		ClientToolkit.setToolkit(new HtmlClientToolkit());
		Resources.addResourceBundle(ResourceBundle.getBundle("ch.openech.mj.resources.MinimalJ"));
	}
	
	private Panel createMenu(PrintWriter writer) {
		TextMenu menu = new TextMenu();

		ActionGroup actionGroup = new ActionGroup();
		MjApplication.getApplication().fillActionGroup(new HtmlPageContext(), actionGroup);

		ActionGroup fileGroup = actionGroup.getOrCreateActionGroup(ActionGroup.FILE);
		ActionGroup newGroup = fileGroup.getOrCreateActionGroup(ActionGroup.NEW);
		ActionGroup importGroup = fileGroup.getOrCreateActionGroup(ActionGroup.IMPORT);
		ActionGroup exportGroup = fileGroup.getOrCreateActionGroup(ActionGroup.EXPORT);

		writer.write("<ul id=\"bar\" class=\"menubar\">");
		writer.write("<li><a href=\"#\">New</a></li>");
		writer.write("</ul>");
		//		addActionGroup(menu, importGroup);
//		addActionGroup(menu, exportGroup);

		return menu;
	}

	public class HtmlApplicationContext extends ApplicationContext {
		private Object preferences;
		private String user;
		
		@Override
		public void setUser(String user) {
			this.user = user;
		}

		@Override
		public String getUser() {
			return user;
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
	
	public class HtmlPageContext implements PageContext {

		@Override
		public void closeTab() {
			// not implemented
			
		}

		@Override
		public void show(String pageLink) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void show(List<String> pageLinks, int index) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public PageContext addTab() {
			// not implemented
			return null;
		}

		@Override
		public ApplicationContext getApplicationContext() {
			return applicationContext;
		}
		
	}
	

    static final int DEFAULT_BUFFER_SIZE = 32 * 1024;
    private int resourceCacheTime = 3600;
    
    private void serveStaticResourcesInVAADIN(String filename,
            HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        final ServletContext sc = getServletContext();
        URL resourceUrl = sc.getResource(filename);
        if (resourceUrl == null) {
            // try if requested file is found from classloader

            // strip leading "/" otherwise stream from JAR wont work
            filename = filename.substring(1);
            resourceUrl = getClass().getClassLoader().getResource(filename);

            if (resourceUrl == null) {
                // cannot serve requested file
//                logger.info("Requested resource ["
//                        + filename
//                        + "] not found from filesystem or through class loader."
//                        + " Add widgetset and/or theme JAR to your classpath or add files to WebContent/VAADIN folder.");
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            // security check: do not permit navigation out of the VAADIN
            // directory
//            if (!isAllowedVAADINResourceUrl(request, resourceUrl)) {
//                logger.info("Requested resource ["
//                        + filename
//                        + "] not accessible in the VAADIN directory or access to it is forbidden.");
//                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
//                return;
//            }
        }

        // Find the modification timestamp
        long lastModifiedTime = 0;
        try {
            lastModifiedTime = resourceUrl.openConnection().getLastModified();
            // Remove milliseconds to avoid comparison problems (milliseconds
            // are not returned by the browser in the "If-Modified-Since"
            // header).
            lastModifiedTime = lastModifiedTime - lastModifiedTime % 1000;

            if (browserHasNewestVersion(request, lastModifiedTime)) {
                response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
                return;
            }
        } catch (Exception e) {
            // Failed to find out last modified timestamp. Continue without it.
//            logger.log(
//                    Level.FINEST,
//                    "Failed to find out last modified timestamp. Continuing without it.",
//                    e);
        }

        // Set type mime type if we can determine it based on the filename
        final String mimetype = sc.getMimeType(filename);
        if (mimetype != null) {
            response.setContentType(mimetype);
        }

        // Provide modification timestamp to the browser if it is known.
        if (lastModifiedTime > 0) {
            response.setDateHeader("Last-Modified", lastModifiedTime);
            /*
             * The browser is allowed to cache for 1 hour without checking if
             * the file has changed. This forces browsers to fetch a new version
             * when the Vaadin version is updated. This will cause more requests
             * to the servlet than without this but for high volume sites the
             * static files should never be served through the servlet. The
             * cache timeout can be configured by setting the resourceCacheTime
             * parameter in web.xml
             */
            response.setHeader("Cache-Control",
                    "max-age: " + String.valueOf(resourceCacheTime));
        }

        // Write the resource to the client.
        final OutputStream os = response.getOutputStream();
        final byte buffer[] = new byte[DEFAULT_BUFFER_SIZE];
        int bytes;
        InputStream is = resourceUrl.openStream();
        while ((bytes = is.read(buffer)) >= 0) {
            os.write(buffer, 0, bytes);
        }
        is.close();
    }
    
    /**
     * Checks if the browser has an up to date cached version of requested
     * resource. Currently the check is performed using the "If-Modified-Since"
     * header. Could be expanded if needed.
     * 
     * @param request
     *            The HttpServletRequest from the browser.
     * @param resourceLastModifiedTimestamp
     *            The timestamp when the resource was last modified. 0 if the
     *            last modification time is unknown.
     * @return true if the If-Modified-Since header tells the cached version in
     *         the browser is up to date, false otherwise
     */
    private boolean browserHasNewestVersion(HttpServletRequest request,
            long resourceLastModifiedTimestamp) {
        if (resourceLastModifiedTimestamp < 1) {
            // We do not know when it was modified so the browser cannot have an
            // up-to-date version
            return false;
        }
        /*
         * The browser can request the resource conditionally using an
         * If-Modified-Since header. Check this against the last modification
         * time.
         */
        try {
            // If-Modified-Since represents the timestamp of the version cached
            // in the browser
            long headerIfModifiedSince = request
                    .getDateHeader("If-Modified-Since");

            if (headerIfModifiedSince >= resourceLastModifiedTimestamp) {
                // Browser has this an up-to-date version of the resource
                return true;
            }
        } catch (Exception e) {
            // Failed to parse header. Fail silently - the browser does not have
            // an up-to-date version in its cache.
        }
        return false;
    }
    
}
