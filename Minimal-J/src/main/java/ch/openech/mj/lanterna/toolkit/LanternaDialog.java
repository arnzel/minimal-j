package ch.openech.mj.lanterna.toolkit;

import ch.openech.mj.toolkit.IDialog;

import com.googlecode.lanterna.gui.Component;
import com.googlecode.lanterna.gui.GUIScreen;
import com.googlecode.lanterna.gui.Window;
import com.googlecode.lanterna.gui.listener.WindowAdapter;

public class LanternaDialog implements IDialog {

	private final GUIScreen screen;
	private final Component content;
	private Window window;
	
	private String title;
	private CloseListener closeListener;
	
	public LanternaDialog(GUIScreen screen, Component content, String title) {
		this.screen = screen;
		this.content = content;
		this.title = title;
	}
	
	@Override
	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public void setCloseListener(CloseListener closeListener) {
		this.closeListener = closeListener;
	}

	@Override
	public void openDialog() {
		window = new Window(title);
		window.addComponent(content);
		window.addWindowListener(new WindowAdapter() {
			@Override
			public void onWindowClosed(Window window) {
				if (closeListener != null) {
					closeListener.close();
				}
			}
		});
		screen.showWindow(window);
	}

	@Override
	public void closeDialog() {
		window.close();
	}

}
