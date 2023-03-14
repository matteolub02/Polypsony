package view;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class GameWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	private static final int HEIGHT = 660;
	private static final int WIDTH = 1050;
	protected static final int HEIGHT_MAP = 606, WIDTH_MAP = 603;
	private JPanel contentPane;
	private ChoiceButtons choiceButtons = new ChoiceButtons();;
	private GameChat gameChat = new GameChat();
	private GameMap gameMap = new GameMap(HEIGHT_MAP, WIDTH_MAP);

	public GameWindow() {
		setTitle("Polypsony");
		setIconImage((new ImageIcon("src/view/duck_img.jpg")).getImage());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, WIDTH, HEIGHT);
		setResizable(false);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		gameChat.setBounds(626, 10, 400, 562);
		contentPane.add(gameChat);
		
		choiceButtons.setBounds(626, 582, 400, 31);
		contentPane.add(choiceButtons);
	
		gameMap.setBounds(10, 10, HEIGHT_MAP, WIDTH_MAP);
		contentPane.add(gameMap);

	}
}
