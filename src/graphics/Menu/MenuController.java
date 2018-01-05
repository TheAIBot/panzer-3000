package graphics.Menu;


import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Stack;

import javax.swing.JFrame;

import Logger.Log;
import engine.Input;
import graphics.Menu.Pages.GamePage;
import graphics.Menu.Pages.PageRequestsListener;
import graphics.Menu.Pages.SuperPage;

public class MenuController implements PageRequestsListener, KeyListener {
	private final JFrame mainMenu;
	private final GamePage MAIN_PAGE = new GamePage(this);
	private SuperPage currentPage;
	private final Stack<SuperPage> previousPages = new Stack<SuperPage>();
	private final Input input = new Input();
	
	public MenuController(String windowName, int startWidth, int startHeight)
	{
		mainMenu = new JFrame(windowName);
		mainMenu.setSize(startWidth, startHeight);
		mainMenu.setLocationRelativeTo(null);
		mainMenu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainMenu.addWindowListener(new WindowAdapter() {
			@Override
	        public void windowClosing(WindowEvent e) {
	            super.windowClosing(e);
	            currentPage.closePage();
	            //AudioManager.closeBackgroundMusic();
	        }
		});
		mainMenu.addKeyListener(this);
	}
	
	public void showWindow()
	{
		currentPage = MAIN_PAGE;
		mainMenu.add(MAIN_PAGE.getPage());
		currentPage = MAIN_PAGE;
		currentPage.startPage();
		mainMenu.setVisible(true);
	}

	@Override
	public void back() {
		if (!previousPages.isEmpty()) {
			switchPage(previousPages.pop(), false);
		}
		else {
			Log.message("Tried to go back a page when there is no previous page to go back to");
		}
	}
	
	@Override
	public void switchPage(SuperPage toSwitchTo)
	{
		switchPage(toSwitchTo, true);
	}
	
	private void switchPage(SuperPage toSwitchTo, boolean addPreviousPage)
	{
		if (toSwitchTo.canShowPage()) {
			if (addPreviousPage) {
				previousPages.add(currentPage);
			}
			currentPage.closePage();
			currentPage = toSwitchTo;
			
			mainMenu.getContentPane().removeAll();
			mainMenu.add(toSwitchTo.getPage());
			mainMenu.repaint();
			mainMenu.setVisible(true);
			
			currentPage.startPage();
		}
	}

	@Override
	public void resize(Dimension dim) {
		mainMenu.setSize(dim.width, dim.height);
	}

	@Override
	public void canResize(boolean canResize) {
		mainMenu.setResizable(canResize);
		
	}

	@Override
	public void setFullScreen() {
		mainMenu.setExtendedState(JFrame.MAXIMIZED_BOTH); 		
	}

	@Override
	public void hideScreen() {
		mainMenu.setVisible(false);
	}

	@Override
	public void showScreen() {
		mainMenu.setVisible(true);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyChar()) {
		case 'w':
			input.w = true;
			break;
		case 'a':
			input.a = true;
			break;
		case 's':
			input.s = true;
			break;
		case 'd':
			input.d = true;
		default:
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		switch (e.getKeyChar()) {
		case 'w':
			input.w = false;
			break;
		case 'a':
			input.a = false;
			break;
		case 's':
			input.s = false;
			break;
		case 'd':
			input.d = false;
		default:
			break;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}
	
	public Input getInput()
	{
		return input;
	}
}
