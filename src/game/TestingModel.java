package game;

import java.util.ArrayList;
import java.util.Scanner;

import javax.xml.stream.XMLStreamException;

import cards.*;

public class TestingModel {
	
	/*
	 * Questa classe serve per testare il model, tutto il gioco può essere testato direttamente qua
	 * Senza dover toccare controller o view
	 * Idea per come dovrebbe funzionare, non vero e proprio schema, da rivedere
	 */
	
	
	@SuppressWarnings("resource")
	public static void main (String args[]) throws XMLStreamException {
		
		ArrayList<Player> players = new ArrayList<>();
		Scanner scnr = new Scanner(System.in);
		System.out.println("Quanti giocatori?");
		//Selezione numero giocatori
		int playerNumber = 0;
		try {
			playerNumber = scnr.nextInt();
		}
		catch (Exception e) {
			System.out.println("errore");
		}
		finally {
			if (playerNumber == 0) playerNumber = 2;
		}
		
		//Creazione giocatori
		for (int i = 0; i < playerNumber; i++) {
			players.add(new Player(new String("Player" + i)));
		}
		//Creazione game
		Game game = new Game(players);
		
		do {
			Player playerPlaying = game.getPlayers().get(game.getTurn());
			if (playerPlaying.isInJail()) {
				if (game.checkHasGetOutOfJailFree()) {
					game.removePlayerFromJailForFree();
					game.nextTurn(); //perdi un turno stando in prigione
				}
				else {
					game.removePlayerFromJailNotForFree();
					if (!game.removePlayer()) game.nextTurn();
				}
			}
			System.out.println(playerPlaying.getName() + " - " + playerPlaying.getSavings() + " - case " 
			+ playerPlaying.getBuildedHouses() + " - pass" + playerPlaying.getGetOutOfJailFreeNumber()
			+ "- " + game.getPlayerPos().get(playerPlaying));
			System.out.println("Inserisci valore dadi");
			int dices = scnr.nextInt(); //per rendere meno casuale il testing
			if (dices == -1) game.forceRemovePlayer(game.getTurn()); //forza rimozione player (uscita del giocatore)
			else {
				System.out.println(playerPlaying.getName() + " lancia dadi, esce " + dices);
				//cambio pos
				game.changePlayerPos(dices);
				//nuova pos
				System.out.println("Pos: " + game.getPlayerPos().get(playerPlaying));
				int pos = game.getPlayerPos().get(playerPlaying);
				//controllo carta 
				String effect = game.cardEffect();
				System.out.println(game.getCards().get(game.getPlayerPos().get(playerPlaying))+ "\n" + effect);
				System.out.println("Posizione cambiata: " + game.hasPosChanged(pos)); //se la posizione è cambiata allora era su chance/imprevisti
				if (game.hasPosChanged(pos) && !playerPlaying.isInJail()) {
					effect = game.cardEffect();
					System.out.println(game.getCards().get(game.getPlayerPos().get(playerPlaying))+ "\n" + effect);
				}
				
				//una volta controllati gli effetti, si controlla dove si trova il player
				Card card = game.getCards().get(game.getPlayerPos().get(playerPlaying));

				switch (card.getType()) {
				case Card.STREET:
					Street street = (Street) card;
					if (game.getPossessions().get(street.getPos()).get(street) == Game.NO_ONE_HAS_THIS) {
						int ans = scnr.nextInt();
						if (ans == 1) {
							if (playerPlaying.getSavings() <= street.getCost()) {
								System.out.println("Siete poveri, investite in bitcoin o qualcosa non so");
							}
							else {
								game.getPlayerPlaying().removeMoneyFromSaving(street.getCost());
								game.getPossessions().get(game.getPlayerPlayingPos()).replace(street, game.getTurn());
								System.out.println(street.getName() + " - " + game.getPossessions().get(game.getPlayerPlayingPos()).get(street));
							}
						}
					}
					else if (game.getPossessions().get(street.getPos()).get(street) == game.getTurn() &&
							game.checkStreetRow(street.getPos(), game.getTurn()) &&
							street.getHousesNumber() < 5) {
						int ans_build = scnr.nextInt();
						if (ans_build == 1) {
							game.buildHouse(game.getPlayerPlayingPos());
							System.out.println(street.getHousesNumber());
						}
					}
					break;
				case Card.STATION:
				case Card.COMPANY:
					//come street, con meccanismo semplificato (o acquisto o solo mostra quanto si paga)
					break;
				}
				game.removePlayer();
				game.nextTurn();
			}
		} while (!game.isGameEnded());
		
		System.out.println("Winner: " + game.winner().getName());
	}
}
