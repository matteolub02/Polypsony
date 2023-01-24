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
	
	public void changePos () { 
		/*
		 * getPos from player, 4 cases:
		 * low row : 
		 * 		y MUST BE defaultY
		 * 		if (pos == 0) x = defaultX
		 * 		if (pos > 0 && < 9) x = defaultX - (60 + 40 * (pos - 1))
		 * 		if (pos == 9) x = defaultX - (120 + 40 * (pos - 1))  
		 * left row :
		 * 		x MUST BE x = defaultX - (120 + 40 * 8)
		 * 		if (pos == 9) y = defaultY
		 * 		if (pos > 9 && pos < 19) y = defaultY - ( 60 + 40 * (pos - 10))
		 * 		if (pos == 19) y = defaultY - ( 120 + 40 * (pos - 10) )
		 * high row :
		 * 		y MUST BE y = defaultY - (120 + 40 * 8)
		 * 		if (pos == 19) x = defaultX - (120 + 40 * 8)
		 * 		if (pos > 19 && pos < 29) x = defaultX - (120 + 40 * 8) + (60 + 40 * (pos - 20))
		 * 		if (x == 29) x = defaultX
		 * right row : 
		 */
		
	}
	
	public void removePlayer () {

		//TODO: rivedere
	}

	public int getPlayerNumber() {
		return playerNumber;
	}

	public void setPlayerNumber(int playerNumber) {
		this.playerNumber = playerNumber;
	}
}
