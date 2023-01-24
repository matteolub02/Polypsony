package game;

import graphics.GameWindow;

public class Main {

	public static void main(String[] args) {
		/*
		 * Descrizione funzione main:
		 * La funzione main inizializza l'intero programma, ci sono due metodi di avvio:
		 * 1. Server
		 * 2. Client
		 * Risulta ovvio dunque che la funzione main avrà il compito di chiedere al giocatore
		 * se vuole hostare o se vuole joinare un server, quindi avviare un client che si connetterà.
		 * Quindi verrà avviata una scheda con due pulsanti: "HOST" e "CONNETTITI" (controllare)
		 * Se viene scelto host, verrà inizializzato il server, per poter iniziare devono esserci ALMENO
		 * due giocatori. 
		 * Se viene scelto connettiti, verrà richiesta una stringa indicante l'ip al quale connettersi
		 */
		
		GameWindow window = new GameWindow(4);
		
	}

}
