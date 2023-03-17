package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ControllerServer {
	
	final static int SERVER_PORT = 3019;
	Server s;
	
	public ControllerServer () {
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
		
	}
	
	
	class Server implements Runnable {
		
		private ServerSocket serverSocket;
		private ArrayList<ConnectionHandler> connections;
		private ExecutorService pool;
		
		public Server () {
			connections = new ArrayList<>();
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
		
		
		public void broadcastMsg(String msg) throws IOException {
			for (ConnectionHandler c : connections) {
				c.sendMsg(msg);
			}
		}
		
		public void shutdown() throws IOException {
			if (!serverSocket.isClosed()) serverSocket.close();
		}
		
		class ConnectionHandler implements Runnable {
			
			private Socket clientSocket;
			private ObjectOutputStream outData;
			private ObjectInputStream inData;
			
			public ConnectionHandler (Socket s) {
				clientSocket = s;
			}
			
			@Override
			public void run() {
				try {
					Object o = null;
					outData = new ObjectOutputStream(clientSocket.getOutputStream());
					inData = new ObjectInputStream(clientSocket.getInputStream());
					sendMsg("Benvenuto!");
					while ((o = inData.readObject()) != null) {
						switch (o.getClass().getSimpleName()) {
						case "String":
							System.out.println((String)o);
							broadcastMsg((String)o);
							break;
						case "Update":
							break;
						}
					}
				}
				catch (IOException | ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
			
			public void sendMsg(String msg) throws IOException {
				outData.writeObject(msg);
				outData.flush();
			}
			
			
			public void shutdown() throws IOException {
				if (!clientSocket.isClosed()) {
					inData.close();
					outData.close();
					clientSocket.close();
				}
			}
			
		}	
		
	}
	
}
