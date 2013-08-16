package ch.openech.mj.lanterna;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.prefs.Preferences;

import javax.swing.Action;

import ch.openech.mj.application.ApplicationContext;
import ch.openech.mj.application.MjApplication;
import ch.openech.mj.lanterna.component.HighContrastLanternaTheme;
import ch.openech.mj.lanterna.component.Select;
import ch.openech.mj.lanterna.toolkit.LanternaActionAdapater;
import ch.openech.mj.lanterna.toolkit.LanternaClientToolkit;
import ch.openech.mj.lanterna.toolkit.LanternaSwitchLayout;
import ch.openech.mj.page.ActionGroup;
import ch.openech.mj.page.Page;
import ch.openech.mj.page.PageContext;
import ch.openech.mj.resources.ResourceAction;
import ch.openech.mj.resources.Resources;
import ch.openech.mj.swing.PreferencesHelper;
import ch.openech.mj.swing.component.History;
import ch.openech.mj.swing.component.History.HistoryListener;
import ch.openech.mj.toolkit.ClientToolkit;
import ch.openech.mj.toolkit.IComponent;
import ch.openech.mj.util.StringUtils;

import com.googlecode.lanterna.gui.Border;
import com.googlecode.lanterna.gui.Component;
import com.googlecode.lanterna.gui.GUIScreen;
import com.googlecode.lanterna.gui.Window;
import com.googlecode.lanterna.gui.component.Button;
import com.googlecode.lanterna.gui.component.Panel;
import com.googlecode.lanterna.gui.component.TextBox;
import com.googlecode.lanterna.gui.dialog.ActionListDialog;
import com.googlecode.lanterna.gui.layout.BorderLayout;
import com.googlecode.lanterna.gui.layout.HorisontalLayout;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.swing.SwingTerminal;

public class LanternaLauncher {

	private static String applicationName;
	private static ApplicationContext applicationContext;

	private LanternaLauncher() {
		// private
	}
	
	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	public void run() {
		try {
			SwingTerminal terminal = new SwingTerminal();
			Screen screen = new Screen(terminal);

			LanternaGUIScreen gui = new LanternaGUIScreen(screen);
			gui.setTheme(new HighContrastLanternaTheme());
			ClientToolkit.setToolkit(new LanternaClientToolkit(gui));

			Class<? extends MjApplication> applicationClass = (Class<? extends MjApplication>) Class
					.forName(applicationName);
			MjApplication application = applicationClass.newInstance();
			application.init();
			applicationContext = new LanternaApplicationContext();

			screen.startScreen();
			gui.init();
		} catch (Exception x) {
			x.printStackTrace();
		}
	}

	public class LanternaApplicationContext extends ApplicationContext {
		private String user;

		public LanternaApplicationContext() {
		}

		@Override
		public void setUser(String user) {
			this.user = user;
		}

		@Override
		public String getUser() {
			return user;
		}

		@Override
		public void savePreferences(Object preferences) {
			PreferencesHelper.save(Preferences
					.userNodeForPackage(LanternaLauncher.this.getClass()),
					preferences);
		}

		@Override
		public void loadPreferences(Object preferences) {
			PreferencesHelper.load(Preferences
					.userNodeForPackage(LanternaLauncher.this.getClass()),
					preferences);
		}
	}

	public class LanternaMenuPanel extends Panel {

		private final PageContext pageContext;
		private Panel bar;
		
		public LanternaMenuPanel(PageContext pageContext) {
			this.pageContext = pageContext;
			
			setBorder(new Border.Invisible());
			setLayoutManager(new BorderLayout());
			
			Panel panel = new Panel();
			panel.setLayoutManager(new BorderLayout());
			
			bar = new Panel();
			bar.setLayoutManager(new HorisontalLayout());
			updateMenu(null);
			
			panel.addComponent(bar, BorderLayout.LEFT);
			panel.addComponent(createSearchField(), BorderLayout.RIGHT);
			
			addComponent(panel, BorderLayout.TOP);
		}
		
		public void updateMenu(Page page) {
			bar.removeAllComponents();
			
			ActionGroup actionGroup = new ActionGroup();
			MjApplication.getApplication().fillActionGroup(pageContext, actionGroup);
			if (page != null) {
				page.fillActionGroup(actionGroup.getOrCreateActionGroup(ActionGroup.OBJECT));
			}
			
			ActionGroup fileGroup = actionGroup.getOrCreateActionGroup(ActionGroup.FILE);
			createMenu(bar, fileGroup.getOrCreateActionGroup(ActionGroup.NEW));
			
			createMenu(bar, actionGroup.getOrCreateActionGroup(ActionGroup.OBJECT));

			createMenu(bar, fileGroup.getOrCreateActionGroup(ActionGroup.IMPORT));
			createMenu(bar, fileGroup.getOrCreateActionGroup(ActionGroup.EXPORT));
		}

		protected void createMenu(Panel bar, ActionGroup newGroup) {
			com.googlecode.lanterna.gui.Action newAction = actionGroup(newGroup);
			if (newAction != null) {
				Button button = new Button(newAction.toString(), newAction);
				bar.addComponent(button);
			}
		}

		protected com.googlecode.lanterna.gui.Action actionGroup(ActionGroup group) {
			if (group.getActions().isEmpty()) return null;
			String item = (String) group.getValue(Action.NAME);
			ActionSelection as = new ActionSelection(item);
			for (Action action : group.getActions()) {
				if (action instanceof ActionGroup) {
					ActionGroup subGroup = (ActionGroup) action;
					as.addAction(actionGroup(subGroup));
				} else {
					LanternaActionAdapater actionAdapater = new LanternaActionAdapater(action, this);
					as.addAction(actionAdapater);
				}
			}
			return as;
		}
		
		private class ActionSelection implements com.googlecode.lanterna.gui.Action {

			private final String item;
			private final List<com.googlecode.lanterna.gui.Action> actions = new ArrayList<>();
			
			public ActionSelection(String item) {
				this.item = item;
			}

			public void addAction(com.googlecode.lanterna.gui.Action action) {
				actions.add(action);
			}
			
			@Override
			public void doAction() {
				com.googlecode.lanterna.gui.Action[] actionArray = actions.toArray(new com.googlecode.lanterna.gui.Action[actions.size()]);
				ActionListDialog.showActionListDialog(getGUIScreen(), "Action", "", 0, true, actionArray);
			}

			@Override
			public String toString() {
				return item;
			}
		}
		
		private Select<String> comboBoxSearchObject;
		private TextBox textFieldSearch;
		private Map<String, Class<?>> searchClassesByObjectName = new HashMap<String, Class<?>>();
		
		protected Panel createSearchField() {
			Panel panel = new Panel();
			panel.setLayoutManager(new HorisontalLayout());
			
			Class<?>[] searchClasses = MjApplication.getApplication().getSearchClasses();
			List<String> objectNameList = new ArrayList<>();
			for (Class<?> searchClass : searchClasses) {
				String objectName = Resources.getString("Search." + searchClass.getSimpleName());
				objectNameList.add(objectName);
				searchClassesByObjectName.put(objectName, searchClass);
			}

			comboBoxSearchObject = new Select<>();
			comboBoxSearchObject.setObjects(objectNameList);
			panel.addComponent(comboBoxSearchObject);
			
			textFieldSearch = new TextBox();
			panel.addComponent(textFieldSearch);
			
			final Button button = new Button("Search", new LanternaActionAdapater(new SearchAction(), panel));
			panel.addComponent(button);

			return panel;
		}
		
//		private static class SearchCellRenderer extends DefaultListCellRenderer {
//			@Override
//			public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
//				Class<?> searchClass = (Class<?>) value;
//				value = Resources.getString("Search." + searchClass.getSimpleName());
//				return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
//			}
//		}
		
		protected class SearchAction extends ResourceAction {
			@Override
			public void actionPerformed(ActionEvent e) {
				Class<?> searchObject = searchClassesByObjectName.get(comboBoxSearchObject.getSelectedObject());
				String text = textFieldSearch.getText();
				search((Class<? extends Page>) searchObject, text);
			}
		}
		
		public void search(Class<? extends Page> searchClass, String text) {
			pageContext.show(Page.link(searchClass, text));
		}
		
	}
	
	public class LanternaGUIScreen extends GUIScreen implements PageContext {

		private LanternaMenuPanel menuPanel;
		private LanternaSwitchLayout switchLayout;
		
		private final Window window;
		private final History<String> history;
		private final LanternaPageContextHistoryListener historyListener;
		
		public LanternaGUIScreen(Screen screen) {
			super(screen);
			historyListener = new LanternaPageContextHistoryListener();
			history = new History<String>(historyListener);
			
			window = new Window("");
			window.setBorder(new Border.Invisible());
		}
		
		public void init() {
			menuPanel = new LanternaMenuPanel(this);
			
			window.addComponent((Component) menuPanel);

			switchLayout = new LanternaSwitchLayout();
			menuPanel.addComponent((Component) switchLayout, BorderLayout.CENTER);

			showWindow(window, Position.FULL_SCREEN);
		}
		
		@Override
		public void closeTab() {
			// not implemented
		}

		@Override
		public void show(String pageLink) {
			history.add(pageLink);
			
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
		
		private class LanternaPageContextHistoryListener implements HistoryListener {
			@Override
			public void onHistoryChanged() {
				Page page = Page.createPage(LanternaGUIScreen.this, history.getPresent());
				show(page);
				menuPanel.updateMenu(page);
			}

			private void show(Page page) {
				switchLayout.show((IComponent) page.getComponent());
				ClientToolkit.getToolkit().focusFirstComponent(page.getComponent());
			}
		}
	}
	
	public static void main(final String[] args) throws Exception {
		applicationName = System.getProperty("MjApplication");
		if (StringUtils.isBlank(applicationName)) {
			System.err.println("Missing MjApplication parameter");
			System.exit(-1);
		}

		new LanternaLauncher().run();
	}
}
