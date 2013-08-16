package ch.openech.mj.lanterna.toolkit;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Action;

import ch.openech.mj.toolkit.IComponent;

import com.googlecode.lanterna.gui.Component;
import com.googlecode.lanterna.gui.TextGraphics;
import com.googlecode.lanterna.gui.component.AbstractContainer;
import com.googlecode.lanterna.gui.component.Button;
import com.googlecode.lanterna.terminal.TerminalPosition;
import com.googlecode.lanterna.terminal.TerminalSize;

public class LanternaEditorLayout extends AbstractContainer implements IComponent {
	
	private final Component content;
	private final List<Button> buttons = new ArrayList<>();
	
	public LanternaEditorLayout(IComponent content, Action[] actions) {
		this.content = (Component) content;
		super.addComponent(this.content);
		
		for (final Action action : actions) {
			com.googlecode.lanterna.gui.Action lanternaAction = new com.googlecode.lanterna.gui.Action() {
				@Override
				public void doAction() {
					action.actionPerformed(new ActionEvent(LanternaEditorLayout.this, 0, null));
				}
			};
			Button button = new Button((String) action.getValue(Action.NAME), lanternaAction);
			buttons.add(button);
			super.addComponent(button);
		}
	}

	@Override
	public void repaint(TextGraphics graphics) {
		TerminalPosition position = new TerminalPosition(0, 0);
		TerminalSize size = new TerminalSize(graphics.getWidth(), graphics.getHeight() - 1);
		TextGraphics subSubGraphics = graphics.subAreaGraphics(position, size);
		content.repaint(subSubGraphics);
		
		int right = graphics.getWidth();
		for (int i = buttons.size() - 1; i>= 0; i--) {
			Button button = buttons.get(i);
			right = right - button.getPreferredSize().getColumns();
			TerminalPosition buttonPosition = new TerminalPosition(right, graphics.getHeight() - 1);
			TerminalSize buttonSize = new TerminalSize(button.getPreferredSize().getColumns(), 1);
			TextGraphics subSubGraphicsButton = graphics.subAreaGraphics(buttonPosition, buttonSize);
			button.repaint(subSubGraphicsButton);
			right = right - 1; // padding between buttons
		}
	}

	@Override
	protected TerminalSize calculatePreferredSize() {
		TerminalSize size = content.getPreferredSize();
		return new TerminalSize(size.getColumns(), size.getRows() + 1);
	}
	
}
