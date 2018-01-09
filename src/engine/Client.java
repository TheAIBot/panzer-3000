package engine;

import java.util.ArrayList;

import Logger.Log;
import connector.ClientConnector;
import graphics.GraphicsPanel;
import graphics.Menu.MenuController;
import graphics.Menu.Pages.GamePage;

public class Client {
	
	public void startGame(String ipaddress, String username, MenuController menu, GraphicsPanel panel) {
		try {
			Log.message("Starting client");
			ClientConnector connection = new ClientConnector();
			connection.connectToServer(ipaddress, username);
			Log.message("Client connected");
			
			//MenuController menu = new MenuController("Panzer", 500, 500);
			//menu.showWindow();
			//GraphicsPanel panel = GamePage.GetGraphicsPanel();
			//Log.message("Created gui");
			
			while (true) {
				
				//The call is blocking, so it won't continue before the update is given
				Object[] updatedObjects 	= connection.recieveUpdates(); 
				ArrayList<Tank> tanks 		= DeSerializer.toList((byte[])updatedObjects[1], Tank.class);
				ArrayList<Bullet> bullets 	= DeSerializer.toList((byte[])updatedObjects[2], Bullet.class);
				ArrayList<Wall> walls       = DeSerializer.toList((byte[])updatedObjects[3], Wall.class);
				
				//Log.message("Received tanks and bullet updates");
				
				//Here the graphics needs to render the things seen above
				panel.setTanks(tanks);
				panel.setBullets(bullets);
				panel.setWalls(walls);
				panel.repaint();

				//Create a new Input
				Input userInput = menu.getInput();
				//Log.message(userInput.toString());
				
				//finally send the inputs to the server.			
				connection.sendUserInput(userInput);
				//Log.message("Sent user input");
			}	
		} catch (Exception e) {
			Log.exception(e);
		}
	}
}
