package engine;

import java.util.ArrayList;
import java.util.List;

import Logger.Log;
import connector.ClientConnector;
import graphics.GraphicsPanel;
import graphics.Menu.MenuController;
import graphics.Menu.Pages.GamePage;

public class Client {
	
	public void startGame() {
		Log.message("Starting client");
		ClientConnector connection = new ClientConnector();
		connection.connectToServer();
		Log.message("Client connected");
		
		MenuController menu = new MenuController("Panzer", 500, 500);
		menu.showWindow();
		GraphicsPanel panel = GamePage.GetGraphicsPanel();
		Log.message("Created gui");
		
		while (true) {
			
			//The call is blocking, so it won't continue before the update is given
			Object[] updatedObjects = connection.recieveUpdates(); 
			ArrayList<Tank> tanks 		= connection.unpackTanks(updatedObjects);
			ArrayList<Bullet> bullets 	= connection.unpackBullets(updatedObjects);
			Log.message("Received tanks and bullet updates");
			
			//Here the graphics needs to render the things seen above
			panel.setTanks(tanks);
			panel.setBullets(bullets);
			panel.repaint();

			//Create a new Input
			Input userInput = menu.getInput();
			
			//finally send the inputs to the server.			
			connection.sendUserInput(userInput);
			Log.message("Sent uder input");
		}
	}
}