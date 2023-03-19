package view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;
import server.ControllerPlayer;

/**
 * @author 736418
 * @summary It's the game's frame, in this frame we can interact with the game by pressing buttons or by sending msg through chat.
 *
 */
public class GameWindow extends JFrame {
	
	private static final long serialVersionUID = 1L;
	private static final int HEIGHT = 660;
	private static final int WIDTH = 1050;
	protected static final int HEIGHT_MAP = 606, WIDTH_MAP = 603;
	private JPanel contentPane;
	private ChoiceButtons choiceButtons = new ChoiceButtons();;
	private GameChat gameChat = new GameChat();
	private ControllerPlayer c;
	private GameMap gameMap = new GameMap(HEIGHT_MAP, WIDTH_MAP);
	public void setChoiseButtonsNotVisible() {
		choiceButtons.setEverythingNotVisible();
	}
	
	/**
	 * @param one - buy property
	 * @param two - buy house
	 * @param three - sell property
	 * @param four - sell houses
	 * @summary It sets necessary buttons visible, so that the player can make his choice.
	 */
	public void setChoiceButtons(boolean one, boolean two, boolean three, boolean four) {
		if (one) choiceButtons.setBuyPropertyVisible();
		if (two) choiceButtons.setBuyHouseVisible();;
		if (three) choiceButtons.setSellPropertyVisible();;
		if (four) choiceButtons.setSellHousesVisible();;
		choiceButtons.setEndTurnVisible();
		choiceButtons.repaint();
	}
	
	//repaints the gameMap
	public void repaintMap(ArrayList<Integer> pos) {
		gameMap.setPos(pos);
		gameMap.repaint();
		
	}
	
	public void printMsgInChat (String msg) {
		gameChat.printTextOnTextArea(msg);
	}
	
	public GameWindow(ControllerPlayer c) {
		this.c = c;
	}
	
	public void setWindow() {	
		setTitle("Polypsony"); //TITLE
		setIconImage((new ImageIcon("src/view/duck_img.jpg")).getImage()); //ICON
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
	
	
	/**
	 * @author 736418
	 * @summary A simple game chat.
	 */
	class GameChat extends JPanel {

		private static final long serialVersionUID = 1L;
		private JTextField textField;
		JTextArea textArea;
		
		public GameChat() {
			
			textArea = new JTextArea();

			textField = new JTextField();
			textField.addKeyListener(new KeyAdapter() {
				@Override
				public void keyPressed(KeyEvent e) {
					if (e.getKeyCode() == KeyEvent.VK_ENTER) {
						String msg = textField.getText();
						textField.setText("");
						try {
							c.sendMsg(msg);
						} catch (IOException e1) {
		
							e1.printStackTrace();
						}
					}
				}
			});
			textField.setColumns(10);
			JButton sendMsg = new JButton("Invia msg");
			JScrollPane scrollPane = new JScrollPane();
			GroupLayout groupLayout = new GroupLayout(this);
			textArea.setLineWrap(true); //per far "fittare" il testo nella text area
			textArea.setWrapStyleWord(true);
			scrollPane.setViewportView(textArea);
			setLayout(groupLayout);
			textArea.setEditable(false);
			sendMsg.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String msg = textField.getText();
					textField.setText(""); 
					try {
						c.sendMsg(msg);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			});
			
			groupLayout.setHorizontalGroup(
				groupLayout.createParallelGroup(Alignment.LEADING)
					.addGroup(groupLayout.createSequentialGroup()
						.addContainerGap()
						.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
							.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 430, Short.MAX_VALUE)
							.addGroup(groupLayout.createSequentialGroup()
								.addComponent(textField, GroupLayout.PREFERRED_SIZE, 280, GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(sendMsg, GroupLayout.DEFAULT_SIZE, 144, Short.MAX_VALUE)))
						.addContainerGap())
			);
			groupLayout.setVerticalGroup(
				groupLayout.createParallelGroup(Alignment.TRAILING)
					.addGroup(groupLayout.createSequentialGroup()
						.addContainerGap()
						.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 253, Short.MAX_VALUE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
							.addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addComponent(sendMsg))
						.addContainerGap())
			);	
		}
		
		public void printTextOnTextArea (String string) {
			textArea.append(string + "\n"); 
		}
	}
	
	/**
	 * @author 736418
	 * @summary The game map. TODO: upgrade with an SVG file so that it doesn't look like it came out from 1993.
	 *
	 */
	class GameMap extends JPanel {
		
		private HashMap<Integer, Point> pixelPositions = new HashMap<Integer, Point>();
		private JLabel label = new JLabel();
		private static final long serialVersionUID = 1L;
		private static final ImageIcon mapImg = new ImageIcon("src/view/map.png");
		private static final int FIRST_POS_X = 555, FIRST_POS_Y = 555;
		private ArrayList<Integer> positions = new ArrayList<>();
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
		
		public void setPos (ArrayList<Integer> positions) {
			this.positions = positions;
		}
		
		//aggiungere valore boolean che al primo repaint diventa true (game inizia, pedine sulla mappa)
		protected void paintChildren(Graphics g) {
			  super.paintChildren(g);
			  
			  for (int i = 0; i < positions.size(); i++) {
				  g.setColor(Color.BLUE);
				  Graphics2D g2 = (Graphics2D) g;
				  g2.setStroke(new BasicStroke(3));
				  switch (i) {
				  case 0:
					  g.fillOval(pixelPositions.get(positions.get(i)).x, pixelPositions.get(positions.get(i)).y, 10, 10);
					  break;
				  case 1:
					  g.fillOval(pixelPositions.get(positions.get(i)).x, pixelPositions.get(positions.get(i)).y + 10, 10, 10);
					  break;
				  case 2:
					  g.fillOval(pixelPositions.get(positions.get(i)).x + 10, pixelPositions.get(positions.get(i)).y, 10, 10);
					  break;
				  case 3:
					  g.fillOval(pixelPositions.get(positions.get(i)).x + 10, pixelPositions.get(positions.get(i)).y + 10, 10, 10);
					  break;
				  }
			  }
			  
			  /*
			   * TODO: questo ciclo deve prendere in considerazione solo gli id dei giocatori presenti (da 0 a 4)
			   * Ogni volta che viene chiamato repaint, si prendono i valori e si ristampano con relative posizioni
			   * Giocatore 2 ha 10 pixel in più verso il basso (y + 10)
			   * Giocatore 3 10 pixel in più verso sinistra (x  + 10)
			   * Giocatore 4 10 sinistra e basso (y+10 e x+10)
			   * Bastano coordinate di default di x
			   */
			  

		}
		
		//metodo che setta posizioni delle pedine
		private void setPixelLocations () {
			for (int i = 0; i < 4; i ++) {
				if (i == 0) {
					for (int j = 0; j < 10; j++) {
						if (j == 0) {
							pixelPositions.put(j, new Point(FIRST_POS_X, FIRST_POS_Y));
						}
						else if (j == 1) {
							pixelPositions.put(j, new Point(FIRST_POS_X - 75, FIRST_POS_Y));
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
	
	
	/**
	 * @author 736418
	 * @summary ChoiceButtons for player, so that he can make his choice.
	 *
	 */
	class ChoiceButtons extends JPanel {


		private static final long serialVersionUID = 1L;
		private JButton sellProperty, buyProperty, buildHouse, sellHouses, endTurn;
		
		public ChoiceButtons() {
			setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
			
			sellProperty = new JButton("Vendi"); //propriet� o casa
			add(sellProperty);
			sellProperty.setVisible(false);
			
			sellProperty.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					c.sellPropertyOfPlayerPlaying();
					setEverythingNotVisible();
				}
				
			});
			
			buyProperty = new JButton("Compra");
			add(buyProperty);
			buyProperty.setVisible(false);
			
			buyProperty.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					c.buyPropertyForPlayerPlaying();
					setEverythingNotVisible();
				}
				
			});
			
			buildHouse = new JButton("Costruisci");
			add(buildHouse);
			buildHouse.setVisible(false);
			buildHouse.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					c.buildHouse();
					setEverythingNotVisible();
				}
				
			});
			
			sellHouses = new JButton("Vendi tutte le case");
			add(sellHouses);
			sellHouses.setVisible(false);
			
			sellHouses.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					c.sellHouses();
					setEverythingNotVisible();
				}
				
			});
			
			endTurn = new JButton("Finisci il turno.");
			add(endTurn);
			endTurn.setVisible(false);
			
			endTurn.addActionListener( new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					c.endTurn();
					setEverythingNotVisible();
				}
			});
		}
		
		public void setSellPropertyVisible() {
			sellProperty.setVisible(true);
		}
		
		public void setBuyPropertyVisible() {
			buyProperty.setVisible(true);
		}
		
		public void setBuyHouseVisible() {
			buildHouse.setVisible(true);
		}
		
		public void setSellHousesVisible() {
			sellHouses.setVisible(true);
		}
		
		public void setEndTurnVisible() {
			endTurn.setVisible(true);
		}
		
		public void setEverythingNotVisible() {
			sellProperty.setVisible(false);
			sellHouses.setVisible(false);
			buildHouse.setVisible(false);
			buyProperty.setVisible(false);
			endTurn.setVisible(false);
			this.repaint();
		}
	}

}
