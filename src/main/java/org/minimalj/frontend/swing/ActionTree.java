package org.minimalj.frontend.swing;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.util.List;

import javax.swing.JPanel;

import org.minimalj.frontend.page.ActionGroup;
import org.minimalj.frontend.swing.component.SwingPageBar;
import org.minimalj.frontend.swing.toolkit.SwingClientToolkit.SwingActionLabel;
import org.minimalj.frontend.swing.toolkit.SwingLabel;
import org.minimalj.frontend.toolkit.Action;


public class ActionTree extends JPanel {
	private static final long serialVersionUID = 1L;

	public ActionTree(List<Action> actions, String title) {
		super(new BorderLayout());
		add(new SwingPageBar(title), BorderLayout.NORTH);
		add(new ActionTree(actions), BorderLayout.CENTER);
	}
	
	public ActionTree(List<Action> actions) {
		super(new VerticalLayoutManager());
		
		for (Action action : actions) {
			if (action instanceof ActionGroup) {
				add(new SwingLabel(action.getName()));
				ActionGroup actionGroup = (ActionGroup) action;
				add(new ActionTree(actionGroup.getItems()));
			} else {
				add(new SwingActionLabel(action));
			}
		}
	}		

	public static class VerticalLayoutManager implements LayoutManager {

		private Dimension preferredSize = new Dimension(100, 0);
		
		public VerticalLayoutManager() {
		}

		@Override
		public Dimension preferredLayoutSize(Container parent) {
			layoutContainer(parent);
			return preferredSize;
		}

		@Override
		public Dimension minimumLayoutSize(Container parent) {
			layoutContainer(parent);
			return preferredSize;
		}

		@Override
		public void layoutContainer(Container parent) {
			int y = 0;
			int x = 10;
			int width = parent.getWidth();
			for (Component component : parent.getComponents()) {
				int height = component.getPreferredSize().height;
				component.setBounds(x, y, width, height);
				y += 3 + height;
			}
			preferredSize = new Dimension(100, y);
		}

		@Override
		public void addLayoutComponent(String name, Component comp) {
		}

		@Override
		public void removeLayoutComponent(Component comp) {
		}
	}
}