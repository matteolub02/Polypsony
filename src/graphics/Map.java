package graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class Map extends JPanel {
	
	
	/*
	 * TODO: rivedere con lambda functions
	 */
	class Position {
		int x, y;
		final int defaultX, defaultY;
		
		Position (int x, int y) {
			this.x = x; defaultX = x;
			this.y = y; defaultY = y;
		}
		
		void changePos(int x, int y) {
			this.x = x;
			this.y = y;
		}
		
		int getX () {
			return x;
		}
		
		int getY() {
			return y;
		}
		
		int getDefaultX () {
			return defaultX;
		}
		
		int getDefaultY () {
			return defaultY;
		}
		
	}
	
	private int HEIGHT = 500, WIDTH = 500;
	private Image map = (new ImageIcon("src/graphics/map.png")).getImage();
	private static final long serialVersionUID = 1L;
	private int playerNumber = 0;
	private final Position[] positions = {this.new Position(460, 460), this.new Position(470, 460),
											this.new Position(460, 470), this.new Position(470, 470)};
	private static final Color[] colors = {Color.green, Color.black, Color.yellow, Color.blue};
	
	public Map(int playerNumber) {
		this.setPlayerNumber(playerNumber);
		this.setBorder(BorderFactory.createLineBorder(Color.black));
		this.setSize(HEIGHT, WIDTH);
	}
	
	public void paint (Graphics g) {
		Graphics2D g2D = (Graphics2D) g;
		g2D.drawImage(map, 0, 0, null);
		
		/*
		 * TODO: l'oggetto Player, quando viene rimosso deve far sparire anche la pedina,
		 * oltre a tutte le sue proprietà
		 */
		
		for (int i = 0; i < playerNumber; i++) {
			g2D.setColor(colors[i]);
			g2D.fillOval(positions[i].getX(), positions[i].getY(), 10, 10);
		}
	}
	
	/*
	 * FIRST: 60px
	 * CASES: 40px
	 */
	
	public void movePiece (int player, int pos) { 
		if (pos >= 0 && pos <= 9) {
			positions[player].y = positions[player].defaultY;
			if (pos == 0) positions[player].x = positions[player].defaultX;
			if (pos > 0 && pos < 10) positions[player].x = positions[player].defaultX - (60 + 40 * (pos - 1));
		}
		else if (pos >= 10 && pos <= 19) {
			positions[player].x = positions[player].defaultX - (120 + 40 * (8));
			if (pos == 10) positions[player].y = positions[player].defaultY;
			if (pos > 10 && pos <= 19) positions[player].y = positions[player].defaultY - ( 60 + 40 * (pos - 11));
		}
		else if (pos >= 20 && pos <= 29) {
			positions[player].y = positions[player].defaultY - (120 + 40 * (8));
			if (pos == 20) positions[player].x = positions[player].defaultX - (120 + 40 * (8));
			if (pos > 20 && pos <= 29) positions[player].x = positions[player].defaultX - (120 + 40 * (8)) + (60 + 40 * (pos - 21));
		}
		else if (pos >= 30 && pos <= 39) {
			positions[player].x = positions[player].defaultX;
			if (pos == 30) positions[player].y = positions[player].defaultY - (120 + 40 * (8));
			if (pos > 30 && pos <= 39) positions[player].y = positions[player].defaultY - (120 + 40 * (8)) + (60 + 40 * (pos - 31));
		}
		else System.out.println("ERR - NO SUCH POS");
	}
	
	public void removePlayer (int i) {
		
		//TODO: rivedere
	}

	public int getPlayerNumber() {
		return playerNumber;
	}

	public void setPlayerNumber(int playerNumber) {
		this.playerNumber = playerNumber;
	}
}
