package view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class GameMap extends JPanel {
	
	private JLabel label = new JLabel();
	private static final long serialVersionUID = 1L;
	private static final ImageIcon mapImg = new ImageIcon("src/view/map.png");
	
	//606 603 TODO: cambiare con height e width
	public GameMap(int height, int width) {
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
		   */
		  
		  for (int i = 0; i < 3; i++) {
			  switch (i) {
			  case 0:
				  g.setColor(Color.PINK);
				  g.fillOval(545, 545, 10, 10);
				  break;
			  case 1:
				  g.setColor(Color.RED);
				  g.fillOval(545, 565, 10, 10);
				  break;
			  case 2:
				  g.setColor(Color.GREEN);
				  g.fillOval(565, 545, 10, 10);
				  break;
			  case 3:
				  g.setColor(Color.YELLOW);
				  g.fillOval(565, 565, 10, 10);
				  break;
			  }
		  }

	}

	

}
