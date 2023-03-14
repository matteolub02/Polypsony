package view;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class GameMap extends JPanel {
	
	
	private HashMap<Integer, Point> pixelPositions = new HashMap<Integer, Point>();
	private JLabel label = new JLabel();
	private static final long serialVersionUID = 1L;
	private static final ImageIcon mapImg = new ImageIcon("src/view/map.png");
	private static final int FIRST_POS_X = 555, FIRST_POS_Y = 555;
	
	//606 603 TODO: cambiare con height e width
	public GameMap(int height, int width) {
		setPixelLocations();
		setSize(606, 603);
		Image map = (mapImg.getImage()).getScaledInstance(603, 606, Image.SCALE_SMOOTH);
		setLayout(null);
		label.setBounds(0, 0, 606, 603);
		label.setIcon(new ImageIcon(map));
		add(label);

	}
	
	protected void paintChildren(Graphics g) {
		  super.paintChildren(g);
		  //g.fillRect(545, 545, 40, 40);
		  
		  /*
		   * TODO: questo ciclo deve prendere in considerazione solo gli id dei giocatori presenti (da 0 a 4)
		   * Ogni volta che viene chiamato repaint, si prendono i valori e si ristampano con relative posizioni
		   * Giocatore 2 ha 10 pixel in più verso il basso (y + 10)
		   * Giocatore 3 10 pixel in più verso sinistra (x  + 10)
		   * Giocatore 4 10 sinistra e basso (y+10 e x+10)
		   * Bastano coordinate di default di x
		   */
		  

	}

	private void setPixelLocations () {
		for (int i = 0; i < 4; i ++) {
			if (i == 0) {
				for (int j = 0; j < 10; j++) {
					if (j == 0) {
						pixelPositions.put(j, new Point(FIRST_POS_X, FIRST_POS_Y));
					}
					else if (j == 1) {
						pixelPositions.put(j, new Point(FIRST_POS_X - 80, FIRST_POS_Y));
					}
					else {
						pixelPositions.put(j, new Point(pixelPositions.get(1).x - (48 * (j - 1)), FIRST_POS_Y));
					}
				}
			}
			else if (i == 1) {
				for (int j = 10; j < 20; j++) {
					if (j == 10) {
						pixelPositions.put(j, new Point(pixelPositions.get(9).x - 75, FIRST_POS_Y));
					}
					else if (j == 11) {
						pixelPositions.put(j, new Point (pixelPositions.get(10).x, FIRST_POS_Y - 75));
					}
					else {
						pixelPositions.put(j, new Point (pixelPositions.get(10).x, pixelPositions.get(11).y - (48 * ((j%10) - 1))));
					}
				}
			}
			else if (i == 2) {
				for (int j = 20; j < 30; j++) {
					if (j  == 20) {
						pixelPositions.put(j, new Point(pixelPositions.get(10).x, pixelPositions.get(19).y - 75));
					}
					else if (j == 21) {
						pixelPositions.put(j, new Point(pixelPositions.get(20).x + 75, pixelPositions.get(20).y));
					}
					else {
						pixelPositions.put(j, new Point(pixelPositions.get(21).x + (49 * ((j%20) - 1)), pixelPositions.get(20).y));
					}
				}
			}
			else if (i == 3) {
				for (int j = 30; j < 40; j++) {
					if (j == 30) {
						pixelPositions.put(j, new Point(FIRST_POS_X, pixelPositions.get(20).y));
					}
					else if (j == 31) {
						pixelPositions.put(j, new Point(FIRST_POS_X, pixelPositions.get(30).y + 75));
					}
					else {
						pixelPositions.put(j, new Point(FIRST_POS_X, pixelPositions.get(31).y + (49 * ((j%30) - 1))));
					}
				}
			}
		}
	}

}
