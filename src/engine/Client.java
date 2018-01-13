package engine;

import java.util.ArrayList;

import graphics.GraphicsPanel;
import Menu.GUIControl;
import Menu.InputHandler;
import logger.Log;
import network.spaces.ClientConnector;
import network.spaces.ClientInfo;

public class Client {
	boolean hasPlayerWon = false;

	public void startGame(String ipaddress, int port, ClientInfo clientInfo, InputHandler inputHandler, GUIControl guiControl, GraphicsPanel panel) {
		try {
			Log.message("Starting client");
			ClientConnector connection = new ClientConnector();
			connection.connectToServer(ipaddress, port, clientInfo);
			guiControl.gameStarted();
			Log.message("Client connected");
			
			
			Object[] wallObjects = connection.receiveWalls();
			ArrayList<Wall> walls = DeSerializer.toList((byte[])wallObjects[0], Wall.class);
			panel.setWalls(walls);
			
			while (!hasPlayerWon) {
				
				//The call is blocking, so it won't continue before the update is given
				Object[] updatedObjects 	= connection.recieveUpdates(); 
				ArrayList<Tank>   tanks		= DeSerializer.toList((byte[])updatedObjects[0], Tank.class);
				ArrayList<Bullet> bullets 	= DeSerializer.toList((byte[])updatedObjects[1], Bullet.class);
				ArrayList<Powerup> powerups = DeSerializer.toList((byte[])updatedObjects[2], Powerup.class);
				
				/*
				if (GameEngine.hasTankWonGame(tanks, connection.numberOfClients)) {
					System.out.println("The game has been won!!!");
					hasPlayerWon = true;
					panel.setPlayerHasWon();
					
					Thread.sleep(2000);
					guiControl.gameEnded();
					return;
				}
				*/
				//Log.message("Received tanks and bullet updates");
				
				//Here the graphics needs to render the things seen above
				panel.setTanks(tanks);
				panel.setBullets(bullets);
				panel.setPowerups(powerups);
				panel.repaint();

				//Create a new Input
				Input userInput = inputHandler.getInput();
				//Log.message(userInput.toString());
				
				//finally send the inputs to the server.			
				connection.sendUserInput(userInput);
				//Log.message("Sent user input");
			}	
		} catch (Exception e) {
			Log.exception(e);
			guiControl.gameEnded();
		}
	}
}
