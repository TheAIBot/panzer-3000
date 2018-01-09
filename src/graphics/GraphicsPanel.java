package graphics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

import javax.swing.JPanel;

import com.sun.javafx.font.Metrics;
import com.sun.xml.internal.bind.v2.runtime.Name;

import engine.Bullet;
import engine.Tank;
import engine.Wall;

public class GraphicsPanel extends JPanel {
	private ArrayList<Tank>   tanks 	= new ArrayList<Tank>();
	private ArrayList<Bullet> bullets 	= new ArrayList<Bullet>();
	private ArrayList<Wall>   walls 	= new ArrayList<Wall>();
	boolean playerHasWon 				= false;
	
	public GraphicsPanel() {
		setBackground(Color.WHITE);
		
	}
	

	@Override
	protected void paintComponent(Graphics g) {
		((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                		   				  RenderingHints.VALUE_ANTIALIAS_ON);
		super.paintComponent(g);
		drawTanks(g);
		drawBullets(g);
		drawWalls(g);
		drawWinnerMessage(g);
	}
	


	@Override
	public Dimension getPreferredSize() {
		int min = Math.min(this.getParent().getWidth(), this.getParent().getHeight());
	    return new Dimension(min, min);
	}
	
	private void drawWinnerMessage(Graphics g) {
		int fontSize;
		if (playerHasWon) {
			String message;
			if (tanks.size() == 0) { //Initially zero
				message = "切腹";
				fontSize = 200;
			} else if (tanks.size() == 1) {
				message = "Player " + tanks.get(0).userName + " has won.";
				fontSize = 100;
			} else {
				throw new Error("The game has ended with a number of tanks alive, different from 0 or 1");
			}
			g.setColor(Color.BLACK);
			g.setFont(new Font(g.getFont().getFontName(), Font.PLAIN, fontSize)); 
		    FontMetrics metrics = g.getFontMetrics(g.getFont());
			int xCoordinate = this.getWidth()/2 - metrics.stringWidth(message)/2;
			int yCoordinate = this.getHeight()/2;
			g.drawString(message, xCoordinate, yCoordinate);
			
		}		
	}
	
	private void drawTanks(Graphics g) {
		g.setColor(Color.GREEN);
		for (Tank tank : tanks) {
			drawTankBody(tank, g);
		}
		
		g.setColor(Color.RED);
		for (Tank tank : tanks) {
			drawTankHealth(tank, g);
		}
		
		g.setColor(Color.BLACK);
		for (Tank tank : tanks) {
			drawTankGun(tank, g);
		}
		
		g.setColor(Color.BLACK);
		for (Tank tank : tanks) {
			drawUserName(tank, g);
		}
	}


	private void drawTankHealth(Tank tank, Graphics g) {
		Polygon healthBar = tank.getHealthBar(this.getWidth(), this.getHeight());
		g.fillPolygon(healthBar);
	}
	
	private void drawUserName(Tank tank, Graphics g) {
	    FontMetrics metrics = g.getFontMetrics(g.getFont());
		int xCoordinate = (int) (tank.x * this.getWidth() - metrics.stringWidth(tank.userName)/2);
		int yCoordinate = (int) ((tank.y - 0.7*tank.bodyHeight) * this.getHeight());
		g.drawString(tank.userName, xCoordinate, yCoordinate);
	}


	private void drawTankBody(Tank tank, Graphics g)
	{
		Polygon tankBody = tank.getTankRectangle(this.getWidth(), this.getHeight());
		g.fillPolygon(tankBody);
	}
	
	private void drawTankGun(Tank tank, Graphics g)
	{
		Polygon gunBody = tank.getGunRectangle(this.getWidth(), this.getHeight());
		g.fillPolygon(gunBody);
	}
	
	private void drawBullets(Graphics g)
	{
		g.setColor(Color.RED);
		for (Bullet bullet : bullets) {
			drawBullet(bullet, g);
		}
	}
	
	private void drawBullet(Bullet bullet, Graphics g)
	{
		final int x = (int)((bullet.x - bullet.size / 2) * this.getWidth());
		final int y = (int)((bullet.y - bullet.size / 2) * this.getHeight());
		final int width = (int)(bullet.size * this.getWidth());
		final int height = (int)(bullet.size * this.getHeight());
		
		g.fillOval(x, y, width, height);
	}
	
	private void drawWalls(Graphics g)
	{
		g.setColor(Color.GRAY);
		for (Wall wall : walls) {
			drawWall(wall, g);
		}
	}
	
	private void drawWall(Wall wall, Graphics g)
	{
		final int x = (int)((wall.x) * this.getWidth());
		final int y = (int)((wall.y) * this.getHeight());
		final int width = (int)(wall.width * this.getWidth());
		final int height = (int)(wall.height * this.getHeight());
		
		g.fillRect(x, y, width, height);
	}
	
	public void setTanks(ArrayList<Tank> tanks)
	{
		this.tanks = tanks;
	}
	
	public void setBullets(ArrayList<Bullet> bullets)
	{
		this.bullets = bullets;
	}
	
	public void setWalls(ArrayList<Wall> walls)
	{
		this.walls = walls;
	}


	public void setPlayerHasWon() {
		playerHasWon = true;
	}
	
}