package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.xml.stream.XMLStreamException;

import cards.*;
import game.*;


public class ControllerServer {
	
	final static int SERVER_PORT = 3019;
	Server s;
	Scanner scanner = new Scanner(System.in);
	private Game game;
	private ArrayList<Player> players = new ArrayList<>();
	private static final String SERVER = "Server: ";
	private boolean done = false;

	public ControllerServer () throws IOException {
		System.out.println("Creato controller server.");
		
		s = new Server();
		Thread t = new Thread(() -> {
			try {
				s.run();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		t.start();
		
		while (!(new String(scanner.nextLine())).equals("/start")) {

		}
		System.out.println("Numero giocatori: " + players.size() + "\nAvvio del gioco...");
		try {
			startGame();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (XMLStreamException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void startGame() throws IOException, XMLStreamException, InterruptedException {
		s.broadcastObj(SERVER + "Inizia il gioco!");
		game = new Game(players);
		s.broadcastObj(game);
		if (!game.isGameEnded()) {
		do {
			/*
			 * Gioco a turni, inizia il giocatore 0 e lancia i dadi
			 * 
			 */
			
			Player playerPlaying = game.getPlayerPlaying(); //player inizia a giocare
			
			Thread.sleep(2000);
			if (!game.checkJailStatusPlayerPlaying()) {
				int firstPos = game.getPlayerPlayingPos();
				game.changePlayerPos(game.dices()); //viene impostato dicesValue, si lanciano i dadi
				s.broadcastObj(SERVER + playerPlaying.getName() + " lancia i dadi. Esce " + game.getDicesValue() + ".");
				s.broadcastObj(SERVER + playerPlaying.getName() + " arriva su " + game.getCards().get(game.getPlayerPlayingPos()).getName() + ".");
				int prevPos = game.getPlayerPlayingPos(); //utile se il giocatore viene spostato
				if (firstPos > prevPos) { //controllo fatto prima dell'effetto carta
					s.broadcastObj(SERVER + playerPlaying.getName() + " ritira 200M passando dal via.");
					game.getPlayerPlaying().putMoneyInSaving(200);
				}
				s.broadcastObj(game); //viene spostato il player, updatato il gioco
				
				/*
				 * Si applica l'effetto della carta
				 */
				
				
				Thread.sleep(2000);
				String effect = game.cardEffect(); //si applica effetto carta
				s.broadcastObj(game); //viene comunicato effetto carta (può essere spostamento)
				System.out.println(effect); 
				s.broadcastObj(SERVER + effect); //stampato in chat effetto carta
				/*
				 * Effetto fatto, se viene cambiata la posizione del giocatore si controlla cosa è successo:
				 * 	-Prigione
				 * 	-Cambiato solo posizione
				 */
				Thread.sleep(2000);
				if (game.hasPosChanged(prevPos) && game.getPlayerPlayingPos() != 0) { //se il player viene spostato
					if (!game.checkJailStatusPlayerPlaying()) {
						s.broadcastObj(SERVER + playerPlaying.getName() + " arriva su " + game.getCards().get(game.getPlayerPlayingPos()).getName() + ".");
						effect = game.cardEffect(); //si applica nuovo effetto
						s.broadcastObj(game);  //inviato status di gioco, se turno del giocatore attuale può decidere cosa fare
						System.out.println(effect);
						s.broadcastObj(SERVER + effect); 
						/*
						 * Effetto fatto, ora si vede cosa fare:
						 * -Finire il turno
						 * -Chiedere l'acquisto/costruzione
						 * -Chiedere la vendita della proprietà se possibile
						 */
						int type = game.getCards().get(game.getPlayerPlayingPos()).getType(); //se uno di questi tre tipi, in controllerplayer compare decision making
						switch (type) {
						case Card.STREET:
							if (game.isStreetBuildable() || game.isStreetCompanyStationBuyable() || game.isStreetSellable()) {
								game.usedCardEffect(); //ora il player può eseguire il suo turno (se può)
								s.broadcastObj(game);
								while (!done) {
									if (done) break; //arriva update, finisce
								}
							}
							done = false;
							break;
						case Card.COMPANY:
						case Card.STATION:
							if (game.isStationOrCompanySellable() || game.isStreetCompanyStationBuyable())	{
								game.usedCardEffect(); //ora il player può eseguire il suo turno (se può)
								s.broadcastObj(game);
								while (!done) {
									if (done) break; //arriva update, finisce
								}
							}
							done = false;
							break;
						}

					}
				}
				else {
					/*
					 * Effetto fatto, ora si vede cosa fare:
					 * -Finire il turno
					 * -Chiedere l'acquisto/costruzione
					 * -Chiedere la vendita della proprietà se possibile
					 */
					int type = game.getCards().get(game.getPlayerPlayingPos()).getType();
					switch (type) { //se uno di questi tre tipi, in controllerplayer compare decision making
					case Card.STREET:
						if (game.isStreetBuildable() || game.isStreetCompanyStationBuyable() || game.isStreetSellable()){
							game.usedCardEffect(); //ora il player può eseguire il suo turno (se può)
							s.broadcastObj(game);
							while (!done) {
								if (done) break; //arriva update, finisce
							}
						}
						done = false;
						break;
					case Card.COMPANY:
					case Card.STATION:
						if (game.isStationOrCompanySellable() || game.isStreetCompanyStationBuyable()) {
							game.usedCardEffect(); //ora il player può eseguire il suo turno (se può)
							s.broadcastObj(game);
							while (!done) {
								if (done) break; //arriva update, finisce
							}
						}
						done = false;
						break;
					}
					
				}
			}
			else { //Se è in prigione, deve uscirne, usa il pass o paga (se possibile)
				if (game.checkHasGetOutOfJailFree()) {
					game.removePlayerFromJailForFree();
					System.out.println("Player usa pass per uscire di prigione.");
					s.broadcastObj(SERVER + playerPlaying.getName() + " ha usato il pass per uscire di prigione.");
				}
				else {
					if (game.removePlayerFromJailNotForFree()) {
						System.out.println("Giocatore perde mentre in galera.");
						s.setIdPlayerToMinus1(game.getTurn());
						players.remove(playerPlaying);
						
						s.broadcastObj(SERVER + playerPlaying.getName() + " non ha abbastanza soldi per uscire di galera. Rimosso dal gioco."); 
					}
					else {
						System.out.println("PLayer usa 50M per uscire di prigione.");
						s.broadcastObj(SERVER + playerPlaying.getName() + " paga 50M per uscire di prigione.");
					}
				}
			}
			
			System.out.println("Finita lettura update.");
			
			game.resetCardEffectUsed();
			s.broadcastObj(game); //ERRORE DI BROADCAST
			System.out.println("Inviato status di gioco dopo lettura update.");
			Thread.sleep(500); //chiunque abbia ideato java è la prova fatta persona della malvagità umana
			if (game.removePlayer()) {
				s.setIdPlayerToMinus1(game.getTurn());
				players.remove(playerPlaying);
				s.broadcastObj(SERVER + playerPlaying.getName() + " non ha più soldi, viene rimosso dal gioco.");
				game.resetCardEffectUsed();
				s.broadcastObj(game);
			}
			else {
				s.broadcastObj(SERVER + playerPlaying.getName() +  " finisce il turno. Inizia il prossimo.");
				game.resetCardEffectUsed();
				game.nextTurn();
				System.out.println(game.getTurn());
				s.broadcastObj(game); //il gioco viene updatato dal singolo //commento al commento: l'ho scritto in preda a delirio alle 3 di mattina e si vede
			}
			
			done = false;
		} while (!game.isGameEnded());
		}
		s.broadcastObj(SERVER + game.getPlayers().get(0).getName() + " ha vinto il gioco!"); 
		s.shutdownServer();
		Thread.currentThread().interrupt();
		System.exit(0);
	}
	
	public void handleUpdate (Update up) throws IOException {
		System.out.println("Update arrivato.");
		switch (up.getType()) {
		/*
		 * 5 tipi:
		 * 1 costruisci casa
		 * 2 compra proprietà
		 * 3 vendi proprietà
		 * 4 vendi case
		 * 5 finisci turno
		 */
		case 1: 
			Street street = (Street) game.getCards().get(game.getPlayerPlayingPos());
			game.buildHouse(game.getPlayerPlayingPos());
			game.getPlayerPlaying().removeMoneyFromSaving(street.getHouse());
			s.broadcastObj(SERVER + game.getPlayerPlaying().getName() + " ha costruito una casa.");
			break;
		case 2:
			Card prop = game.getCards().get(game.getPlayerPlayingPos());
			switch (prop.getType()) {
			case Card.STREET:
				Street street2 = (Street) prop;
				game.buyStreetStationOrCompany();
				game.getPlayerPlaying().removeMoneyFromSaving(street2.getCost());
				s.broadcastObj(SERVER + game.getPlayerPlaying().getName() + " ha acquistato la proprietà.");
				break;
			case Card.COMPANY:
			case Card.STATION:
				CompanyAndStation companyOrStation = (CompanyAndStation) prop;
				game.buyStreetStationOrCompany();
				game.getPlayerPlaying().removeMoneyFromSaving(companyOrStation.getCost());
				s.broadcastObj(SERVER + game.getPlayerPlaying().getName() + " ha acquistato la proprietà.");
				break;
			}
			break;
		case 3:
			game.sellStreetStationCompany(game.getPlayerPlayingPos());
			s.broadcastObj(SERVER + game.getPlayerPlaying().getName() + " ha venduto la proprietà.");
			break;
		case 4:
			game.soldHouses(game.getPlayerPlayingPos());
			s.broadcastObj(SERVER + game.getPlayerPlaying().getName() + " ha venduto le case sulla proprietà.");
			break;
		case 5:
			break;
		}
	}
	
	
	public void removePlayer(Player player) {
		if (game != null)game.forceRemovePlayer(game.getPlayers().indexOf(player));
	}
	
	class Server implements Runnable {
		
		private ServerSocket serverSocket;
		private ArrayList<ConnectionHandler> connections;
		private ExecutorService pool;
		
		public Server () {
			connections = new ArrayList<>();
		}
		
		public void setIdPlayerToMinus1(int player) {
			ConnectionHandler ch =  connections.get(player);
			Integer value = -1;
			try {
				ch.outData.writeObject(value);
				ch.outData.flush();
				ch.outData.reset();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		@Override
		public void run() {
			try {
				serverSocket = new ServerSocket(SERVER_PORT);
				pool = Executors.newCachedThreadPool(); //Solo perché pochi dati, si potrebbe ottimizzare
				System.out.println("Accettando client...");
				while (connections.size() < 4) {
					Socket client = serverSocket.accept();
					ConnectionHandler handler = new ConnectionHandler(client);
					connections.add(handler);
					System.out.println("Nuovo client connesso!");
					pool.execute(handler); 
				}
				System.out.println("Non si accettano più client.");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		public void broadcastObj(Object o) throws IOException {
			for (ConnectionHandler c : connections) {
				System.out.println("Ciclo broadcastObj");
				c.sendObj(o);
			}
		}
		
		public void removeChFromConnections (ConnectionHandler ch) {
			connections.remove(ch);
		}
		
		public void shutdownServer() throws IOException {
			if (!serverSocket.isClosed()) serverSocket.close();
			for (ConnectionHandler ch : connections) ch.shutdown();
		}
		
		class ConnectionHandler implements Runnable {
			
			private Socket clientSocket;
			private ObjectOutputStream outData;
			private ObjectInputStream inData;
			private Player player;
			
			public ConnectionHandler (Socket s) {
				clientSocket = s;
			}
			
			@Override
			public void run() {
				try {
					Object o = null;
					outData = new ObjectOutputStream(clientSocket.getOutputStream());
					inData = new ObjectInputStream(clientSocket.getInputStream());
					sendObj("Benvenuto!");
					while ((o = inData.readObject()) != null) {
						switch (o.getClass().getSimpleName()) {
						case "String":
							System.out.println((String)o);
							broadcastObj((String)o);
							break;
						case "Player": 
							player = (Player)o;
							players.add(player);
							broadcastObj(player.getName() + " si è connesso.");
							Integer id = players.size() - 1;
							outData.writeObject(id);
							break;
						case "Update": //update di gioco dal client, inviato solo dopo richiesta!
							handleUpdate((Update)o); 
							done = true;
							System.out.println("Ricevuto aggiornamento di gioco."); 
							break;
						}
					}
				}
				catch (IOException | ClassNotFoundException e) {
					connections.remove(this); //viene rimosso connectionhandler
					try {
						broadcastObj(player.getName() + " si è disconnesso."); //viene comunicato uscita del client
						players.remove(player);
						removePlayer(player); //TODO: aggiornare status di gioco (rimuovere tutte le proprietà del giocatore)
						shutdown();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					System.out.println("Rimosso connectionhandler.");
				}
			}
			
			public void sendObj(Object o) throws IOException {
				outData.writeObject(o);
				outData.flush();
				outData.reset();
			}

			
			public void shutdown() throws IOException {
				outData.close();
				inData.close();
				if (!clientSocket.isClosed()) {
					System.out.println("Client disconnesso.");
					clientSocket.close();
				}
			}
			
		}	
		
	}
	
}
