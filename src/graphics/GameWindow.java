package graphics;


import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class GameWindow extends JFrame {
	
	private static final long serialVersionUID = 1L;
	private Map map;
	
	public GameWindow(int playerNumber) {
		map = new Map(playerNumber);
		this.setIconImage((new ImageIcon("src/graphics/appicon.png")).getImage());
		this.setLayout(null);
		this.setResizable(false);
		this.setTitle("MONOPOLY");
		this.setVisible(true);
		this.setSize(515, 540);
		this.add(map);
		map.removePlayer();
	}
}
