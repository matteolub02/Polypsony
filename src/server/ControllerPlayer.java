package server;

import java.awt.EventQueue;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import cards.*;
import game.*;
import view.GameWindow;
import view.Stats;

/**
 * @author 736418
 * @summary It controls the Client and the User view.
 *
 */
public class ControllerPlayer {

	private Player player;
	private Client c = null;
	private GameWindow g;
	private Game game;
	private Stats stats;
	private Boolean isMyTurn = false;
	
	public ControllerPlayer (String name, String ip) {
		player = new Player(name);
		g = new GameWindow(this);
		startGameWindow();
		startStatsWindow();
		c = new Client(ip, ControllerServer.SERVER_PORT);
		Thread t = new Thread(() -> c.run());
		t.start();
	}
	
	public void startStatsWindow() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					stats = new Stats(getStatsText());
					stats.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public void startGameWindow() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					g.setWindow();
					g.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	//check if it can show buttons
	public void checkIsMyTurn() {
		
		if (isMyTurn && game.didCardEffect()) { //TODO check
			System.out.println("è il mio turno!");
			Card actualCard = game.getCards().get(game.getPlayerPlayingPos());
			boolean one = false, two = false, three = false, four = false;
			switch (actualCard.getType()) {
			case Card.STREET:
				if (game.isStreetCompanyStationBuyable()) one = true; //street buyable
				if (game.isStreetBuildable()) two = true; //street buildable
				if (game.isStreetSellable()) three = true; //street sellable
				if (game.areThereHouses()) four = true; //house sellable
				g.setChoiceButtons(one, two, three, four);
				g.repaint();
				break;
			case Card.STATION:
			case Card.COMPANY:
				if (game.isStreetCompanyStationBuyable()) one = true;
				if (game.isStationOrCompanySellable()) three = true;
				g.setChoiceButtons(one, false, three, false);
				g.repaint();
				break;
			}
		}
		else g.setChoiseButtonsNotVisible();
	}
	
	public String getStatsText() {
		String s = new String("Stats:\n");
		if (game != null) for (Player p : game.getPlayers()) {
			s += (p.getName() + ": Soldi: " + p.getSavings() + "M" 
					+ "\tCase costruite: " + p.getBuildedHouses() + "\tPos: " + game.getPlayerPos().get(p)
					+ "\nPass Jail: " + p.getGetOutOfJailFreeNumber() + "\n");
		}
		return s;
	}

	public void sellPropertyOfPlayerPlaying() {
		try {
			c.setObjectForInputHandler(new Update(3));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<Integer> getPlayersPos() {
		ArrayList<Integer> pos = new ArrayList<>();
		if (game != null) for (Player p : game.getPlayers()) {
			pos.add(game.getPlayerPos().get(p));
		}
		return pos;
	}

	
	public void buyPropertyForPlayerPlaying() {
		Card property = game.getCards().get(game.getPlayerPlayingPos());
		switch (property.getType()) {
		case Card.STREET:
			Street street = (Street) property;
			if (game.getPlayerPlaying().getSavings() > street.getCost()) {
				//acquisto della proprietà
				try {
					c.setObjectForInputHandler(new Update(2));

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			else {
				g.printMsgInChat("Non hai abbastanza soldi!");
				try {
					c.setObjectForInputHandler(new Update(5));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			break;
		case Card.COMPANY:
		case Card.STATION:
			CompanyAndStation company = (CompanyAndStation)property;

			if (game.getPlayerPlaying().getSavings() > company.getCost()) {
				try {
					c.setObjectForInputHandler(new Update(2));

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			else {
				g.printMsgInChat("Non hai abbastanza soldi!");
				try {
					c.setObjectForInputHandler(new Update(5));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			break;
		}
	}
	
	public void buildHouse () {
		Street property = (Street)game.getCards().get(game.getPlayerPlayingPos());
		if (game.getPlayerPlaying().getSavings() > property.getHouse()) {
			try {
				c.setObjectForInputHandler(new Update(1));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else {
			g.printMsgInChat("Non hai abbastanza soldi!");
			try {
				c.setObjectForInputHandler(new Update(5));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void sellHouses () {
		Street street = (Street)game.getCards().get(game.getPlayerPlayingPos());
		try {
			c.setObjectForInputHandler(game.getPlayerPlaying().getName() + " ha venduto " + street.getHousesNumber() + " case.");
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		try {
			c.setObjectForInputHandler(new Update(4));
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setGame(Game g) {
		game = g;
	}
	
	public void endTurn () {
		try {
			c.setObjectForInputHandler(new Update(5));;
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void sendMsg(String msg) throws IOException {
		c.setObjectForInputHandler(player.getName() + ": " + msg);;
	}
	
	class Client implements Runnable {
		
		private InputHandler inHandler = null;
		private ObjectOutputStream out;
		private ObjectInputStream in;
		private String ip;
		private int port;
		private Socket clientSocket;
		
		public Client (String ip, int port) {
			this.ip = ip;
			this.port = port;
			System.out.println("Creato client");
		}
		
		public void printMsgOnChat (String msg) {
			g.printMsgInChat(msg);
		}
		
		public void setObjectForInputHandler (Object o) throws IOException{
			inHandler.sendData(o);
		}

		
		public void shutdown() throws IOException, InterruptedException {
			if (!clientSocket.isClosed()) {
				clientSocket.close();
				in.close();
				out.close();
				Thread.sleep(10000);
				g.dispose();
				stats.dispose();
				System.exit(0);
			}
		}
		
		@Override
		public void run() {
			System.out.println("In run client");
			try {
				clientSocket = new Socket(ip, port);
				System.out.println("Creato client socket.");
				out = new ObjectOutputStream(clientSocket.getOutputStream());
				in = new ObjectInputStream(clientSocket.getInputStream());
				inHandler = new InputHandler();
				Thread t = new Thread(inHandler);
				t.start();
				
				inHandler.sendData(player);

				Object o;
				
				while ((o = in.readObject()) != null) { //reading objects...
					switch (o.getClass().getSimpleName()) {
					case "String":
						System.out.println((String)o);
						printMsgOnChat((String)o);
						break;
					case "Game": //aggiornato status di gioco
						setGame((Game)o); //TODO: refresh view2
						g.repaintMap(getPlayersPos());
						checkIsMyTurn();
						stats.changeTextArea(getStatsText());
						break;
					case "Boolean":
						isMyTurn = (Boolean)o; 
						break;
					}
				}
				
			}
			catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
				try {
					shutdown();
				} catch (IOException | InterruptedException e1) {
					e1.printStackTrace();
				} 
			} 
			

			
		}
		
		
		class InputHandler implements Runnable {

			private boolean finished = false;

			@Override
			public void run() {	
				try {
					while (!finished) {
						
					} 
				} catch (Exception e) {
					try {
						shutdown();
					} catch (IOException | InterruptedException e1) {
						e1.printStackTrace();
					}
					e.printStackTrace();
				}
				
			}
			
			public void sendData (Object s) throws IOException {
				out.writeObject(s);
				out.flush();
				out.reset();
			}

			
		}

	}
	
}
