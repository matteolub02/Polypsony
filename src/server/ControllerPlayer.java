package server;

import java.awt.EventQueue;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import game.Player;
import view.GameWindow;

public class ControllerPlayer {

	private Player player;
	private Client c = null;
	private GameWindow g;
	
	public ControllerPlayer (String name, String ip) {
		player = new Player(name);
		g = new GameWindow(this);
		startGameWindow();
		c = new Client(ip, ControllerServer.SERVER_PORT);
		Thread t = new Thread(() -> c.run());
		t.start();
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
	
	public void sendMsg(String msg) throws IOException {
		c.setObjectForInputHandler(player.getName() + ": " + msg);
	}
	
	class Client implements Runnable {
		
		private InputHandler inHandler = null;
		private ObjectOutputStream out;
		private ObjectInputStream in;
		private String ip;
		private int port;
		
		public Client (String ip, int port) {
			this.ip = ip;
			this.port = port;
			System.out.println("Creato client");
		}
		
		public void printMsgOnChat (String msg) {
			g.printMsgInChat(msg);
		}
		
		public void setObjectForInputHandler (Object o) throws IOException {
			inHandler.sendData(o);
		}
		
		@Override
		public void run() {
			System.out.println("In run client");
			try {
				Socket clientSocket = new Socket(ip, port);
				System.out.println("Creato client socket.");
				out = new ObjectOutputStream(clientSocket.getOutputStream());
				in = new ObjectInputStream(clientSocket.getInputStream());
				inHandler = new InputHandler();
				Thread t = new Thread(inHandler);
				t.start();
				
				Object o;
				
				while ((o = in.readObject()) != null) {
					switch (o.getClass().getSimpleName()) {
					case "String":
						System.out.println((String)o);
						printMsgOnChat((String)o);
						break;
					case "Update":
						break;
					}
				}
				
			}
			catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
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
					e.printStackTrace();
				}
				
			}
			
			public void sendData (Object o) throws IOException {
				out.writeObject(o);
				out.flush();
			}
			
		}

	}
	
}
