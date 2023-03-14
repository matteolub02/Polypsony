package game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import javax.xml.stream.XMLStreamException;
import cards.*;
import server.Player;

public class Game implements Serializable {

	private static final long serialVersionUID = 1;
	private static final int STATION_RENT_INITIAL = 25, COMPANY_FIRST_MULTIPLIER = 4,
			COMPANY_SECOND_MULTIPLIER = 10;
	public static final int NO_ONE_HAS_THIS = -1;
	private HashMap<Player, Integer> playerPos = new HashMap<>();
	private ArrayList<Player> players = null;
	private HashMap<Integer, Card> cards = null;
	private HashMap<Integer, HashMap<Card, Integer>> possessions = new HashMap<>();
	private int turn = 0;
	private int dicesValue = 0;
	
	public Game (ArrayList<Player> players) throws XMLStreamException {
		this.players = players;
		cards = InitializeCards.initialize();
		initializePossessions();
		for (int i = 0; i < players.size(); i++) {
			playerPos.put(players.get(i), 0); //posizione dei giocatori
		}
	}
	
	/**
	 * @param value (valore dadi)
	 * Sposta il giocatore in avanti di value caselle.
	 */
	public void changePlayerPos (int value) {
		Player playerPlaying = players.get(turn);
		dicesValue = value; //fondamentale per compagnie
		if ((playerPos.get(playerPlaying) + value) <= 39) 
			playerPos.replace(playerPlaying, playerPos.get(playerPlaying) + value);
		else playerPos.replace(playerPlaying, (playerPos.get(playerPlaying) + value) - 40);
	}
	
	/**
	 * @param value (posizione in cui spostare)
	 * Sposta il giocatore in una determinata posizione.
	 */
	public void changePlayerPosToSpecifiedPos (int value) {
		playerPos.replace(players.get(turn), value);
	}
	
	/**
	 * @return stringa descrittiva carta
	 * In base alla posizione:
	 * 1. Tassa: messaggio e quanto viene pagato
	 * 2. Probabilit� o imprevisti: effetto carta
	 * 3. Street: 
	 * 		- Se di un giocatore, si paga
	 * 		- Se libero, si pu� acquistare
	 * 		- Se in row, si pu� costruire
	 * 4. Company/Station: come prima, solo con controlli specifici della station e company
	 * 5. NO_EFF (start, jail, free parking, go to jail)
	 */
	public String cardEffect () {
		
		int position = playerPos.get(players.get(turn));
		Player playerPlaying = players.get(turn);
		
		switch(cards.get(position).getType()) {
		
		
		case Card.TAX:
			Tax tax = (Tax)cards.get(position);
			playerPlaying.removeMoneyFromSaving(tax.getCost()); //rimuove soldi
			return new String("Oh no! Dovete pagare la " + tax.getName() + "!"
					+ "\nPagate " + tax.getCost() + "M!");
			
			
		case Card.CHANCE:
		case Card.COMMUNITY_CHEST:
			ChancesAndCommunityChest chances_or_comm = (ChancesAndCommunityChest) cards.get(position);
			ChancesAndCommunityChest.DescriptionAndAction c = chances_or_comm.returnRandomCardEffect();
			switch (c.getAction()) {
			case ChancesAndCommunityChest.ADVANCE_TO:
				if (position > c.getAmount()) {
					playerPlaying.putMoneyInSaving(200); //passa dal VIA
					playerPos.replace(playerPlaying, c.getAmount());
					return new String(c.getDescription() + "\nRitirate 200M passando dal via!");

				}
				else {
					playerPos.replace(playerPlaying, c.getAmount());
					return c.getDescription();
				}
			case ChancesAndCommunityChest.ADVANCE_TO_NEXT_STATION:
				if (position < 5) {
					playerPos.replace(playerPlaying, 5);
					return c.getDescription();
				}
				else if (position < 15) {
					playerPos.replace(playerPlaying, 15);
					return c.getDescription();
				}
				else if (position < 25) {
					playerPos.replace(playerPlaying, 25);
					return c.getDescription();
				}
				else if (position < 35) {
					playerPos.replace(playerPlaying, 35);
					return c.getDescription();
				}
				else {
					playerPos.replace(playerPlaying, 5);
					return new String (c.getDescription() + "\nRitirate 200M passando dal via!");
				}
			case ChancesAndCommunityChest.COLLECT:
				playerPlaying.putMoneyInSaving(c.getAmount());
				return c.getDescription();
			case ChancesAndCommunityChest.COLLECT_FROM_ALL:
				for (int i = 0; i < players.size(); i++) {
					if (i == turn) players.get(i).putMoneyInSaving(c.getAmount() * (players.size() - 1));
					else players.get(i).removeMoneyFromSaving(c.getAmount());
				}
				return c.getDescription();
			case ChancesAndCommunityChest.GET_OUT_OF_JAIL_FREE:
				playerPlaying.pickedGetOutOfJailFree();
				return c.getDescription();
			case ChancesAndCommunityChest.GO_BACK:
				playerPos.replace(playerPlaying, position - 3);
				return c.getDescription();
			case ChancesAndCommunityChest.GO_TO_JAIL:
				putPlayerInJail(turn);
				return c.getDescription();
			case ChancesAndCommunityChest.PAY:
				playerPlaying.removeMoneyFromSaving(c.getAmount());
				return c.getDescription();
			case ChancesAndCommunityChest.PAY_BY_HOUSES:
				playerPlaying.removeMoneyFromSaving(c.getAmount() * playerPlaying.getBuildedHouses());
				return new String (c.getDescription() + "\nPagate " + c.getAmount() * players.get(turn).getBuildedHouses() + "M!");
			case ChancesAndCommunityChest.PAY_TO_ALL:
				for (int j = 0; j < players.size(); j++) {
					if (j == turn) players.get(j).removeMoneyFromSaving(c.getAmount() * (players.size() - 1));
					else players.get(j).putMoneyInSaving(c.getAmount());
				}
				return c.getDescription();			
				
			}
			
			return "Errore lettura carta di chance o possibility test";

			
		case Card.STREET: 
			int streetPossession = possessions.get(position).get(cards.get(position));
			Street street = (Street)cards.get(position);
			if (streetPossession != turn && streetPossession != NO_ONE_HAS_THIS) {
				if (street.getHousesNumber() == 0) {
					if (checkStreetRow(position, streetPossession)) {
						playerPaysAnotherPlayer(streetPossession, 2 * street.getRent());
						int rent = 2 * street.getRent();
						return new String(playerPlaying.getName() + " paga " + rent + "M al giocatore "
								+ players.get(streetPossession).getName() + ".");
					}
					else {
						playerPaysAnotherPlayer(streetPossession, street.getRent());
						return new String(playerPlaying.getName() + " paga " + street.getRent() + "M al giocatore "
								+ players.get(streetPossession).getName() + ".");
					}
				}
				else {
					playerPaysAnotherPlayer(streetPossession, street.getRent());
					return new String(playerPlaying.getName() + " paga " + street.getRent() + "M al giocatore "
							+ players.get(streetPossession).getName() + ".");
				}
			}
			else if (streetPossession == NO_ONE_HAS_THIS) {
				return "Volete acquistare?";
			}
			else if (streetPossession == turn) {
				if (checkStreetRow(position, turn) && street.getHousesNumber() < 5) {
					return "Volete costruire?";
				}
				else {
					return "Siete finiti su un vostro possedimento.";	
				}
			}
			
			
		case Card.COMPANY:
			int companyPossession = possessions.get(position).get(cards.get(position));
			if (companyPossession != turn && companyPossession != NO_ONE_HAS_THIS) {
				if (countCompaniesOfAPlayer(companyPossession, position) == 2) {
					playerPaysAnotherPlayer(companyPossession,COMPANY_SECOND_MULTIPLIER * dicesValue);
					return new String(playerPlaying.getName() + " paga " + (COMPANY_SECOND_MULTIPLIER * dicesValue) 
							+ "M al giocatore " + players.get(companyPossession));
				}
				else {
					playerPaysAnotherPlayer(companyPossession,COMPANY_FIRST_MULTIPLIER * dicesValue);
					return new String(playerPlaying.getName() + " paga " + (COMPANY_FIRST_MULTIPLIER * dicesValue) 
							+ "M al giocatore " + players.get(companyPossession));
				}
			}
			else if (companyPossession == NO_ONE_HAS_THIS) {
				return new String("Volete acquistare?");
			}
			else return new String("");
			
			
		case Card.STATION:
			int stationPossession = possessions.get(position).get(cards.get(position));
			if (stationPossession != turn && stationPossession != NO_ONE_HAS_THIS) {
				playerPaysAnotherPlayer(stationPossession, STATION_RENT_INITIAL * countStationsOfAPlayer(stationPossession, position));
				return new String(playerPlaying.getName() + " paga " + (STATION_RENT_INITIAL * countStationsOfAPlayer(stationPossession, position)
						+ "M al giocatore " + players.get(stationPossession).getName()));
			}
			else if (stationPossession == NO_ONE_HAS_THIS) {
				return new String("Volete acquistare?"); 
			}
			
			
		case Card.NO_EFF:
			switch(position) {
			case 0:
				playerPlaying.putMoneyInSaving(200);
				return new String("Ritirate 200M passando dal via!");
			case 10:
				return new String("Tranquilli! Siete solo di passaggio.");
			case 20:
				return new String("Parcheggio libero.");
			case 30:
				putPlayerInJail(turn);
				return new String("Andate dritto in prigione, senza passare dal via!");
			}
		}
		
		return "Errore di lettura di carta";
	}
	
	/**
	 * @return se viene eliminato giocatore (se ha perso)
	 */
	public boolean removePlayer () {
		if (players.get(turn).getSavings() <= 0) {
			playerPos.remove(players.get(turn));
			players.remove(turn);
			resetPlayerPossessions(turn);
			changeOrderPossessions (turn);
			return true;
		}
		else return false;
	}
	
	/**
	 * @return status prigione del giocatore
	 */
	public boolean checkJailStatusPlayerPlaying () {
		return players.get(turn).isInJail();
	}
	
	/**
	 * @return se ha dei pass per uscire di prigione
	 */
	public boolean checkHasGetOutOfJailFree () {
		return (players.get(turn).getGetOutOfJailFreeNumber() > 0);
	}
	
	/**
	 * Rimuove giocatore dalla prigione usando pass
	 */
	public void removePlayerFromJailForFree () {
		players.get(turn).usedGetOutOfJailFree();
	}
	
	/**
	 * @return se giocatore ha perso dopo essere uscito di prigione
	 * Rimuove giocatore dalla prigione pagando 50 M
	 */
	public boolean removePlayerFromJailNotForFree () {
		players.get(turn).removeMoneyFromSaving(50);
		if (removePlayer()) {
			return true;
		}
		else {
			players.get(turn).freeFromJail();
			return false;
		}
	}
	
	//DADI 
	public int dices () {
		Random rnd = new Random();
		int dice_one = rnd.nextInt(1, 7), dice_two = rnd.nextInt(1, 7);
		return (dice_one + dice_two);
	}
	
	/*
	 * Forza la rimozione di un giocatore (ad esempio se esce dalla partita)
	 * Bisogna tenere controllato questo metodo in quanto una volta rimosso il giocatore
	 * bisogna controllare il comportamento di turn
	 * Se rimuovo un giocatore, il turn va avanti di uno nell'arraylist quindi tocca al giocatore dopo 
	 * e non al giocatore di diritto, questo vuol dire che bisogna aumentare il turno SOLTANTO se il giocatore
	 * non viene eliminato!
	 */
	public void forceRemovePlayer (int player) {
		playerPos.remove(players.get(player));
		players.remove(player);
		resetPlayerPossessions(player);
		changeOrderPossessions (player);
	}
	
	/*
	 * Imposta correttamente possessions dopo che esce player
	 */
	public void changeOrderPossessions (int player) {
		for (HashMap<Card, Integer> poss : possessions.values()) {
			for (Card value : poss.keySet()) {
				if (poss.get(value) > player) {
					poss.replace(value, poss.get(value) - 1);
				}
			}
		}
		//if (turn > player) turn -= 1; TODO: check
	}
	
	/*
	 * Resetta possessions di player uscito
	 */
	public void resetPlayerPossessions (int player) {
		for (HashMap<Card, Integer> poss : possessions.values()) {
			for (Card value : poss.keySet()) {
				if (poss.get(value) == player) {
					poss.replace(value, NO_ONE_HAS_THIS);
					if (value.getType() == Card.STREET) {
						Street street = (Street) value;
						street.removeHouse();
					}
				}
			}
		}
	}
	
	public void nextTurn() {
		if ((turn + 1) < players.size()) turn += 1;
		else turn = 0;
	}
	
	private void initializePossessions () {

		HashMap<Card, Integer> brownStreets = new HashMap<>();
		HashMap<Card, Integer> azureStreets = new HashMap<>();
		HashMap<Card, Integer> purpleStreets = new HashMap<>();
		HashMap<Card, Integer> orangeStreets = new HashMap<>();
		HashMap<Card, Integer> redStreets = new HashMap<>();
		HashMap<Card, Integer> yellowStreets = new HashMap<>();
		HashMap<Card, Integer> greenStreets = new HashMap<>();
		HashMap<Card, Integer> blueStreets = new HashMap<>();
		HashMap<Card, Integer> stations = new HashMap<>();
		HashMap<Card, Integer> companies = new HashMap<>();
		
		for (int i = 0; i < cards.size(); i++) {
			switch(cards.get(i).getType()) {
			case Card.STREET:
				Street street = (Street) cards.get(i);
				switch(street.getColor()) {
				case "BROWN":
					possessions.put(i, brownStreets); //i pos della casella, streets colore
					brownStreets.put(street, NO_ONE_HAS_THIS);
					break;
				case "AZURE":
					possessions.put(i, azureStreets);
					azureStreets.put(street, NO_ONE_HAS_THIS);
					break;
				case "PURPLE":
					possessions.put(i, purpleStreets);
					purpleStreets.put(street, NO_ONE_HAS_THIS);
					break;
				case "ORANGE":
					possessions.put(i, orangeStreets);
					orangeStreets.put(street, NO_ONE_HAS_THIS);
					break;
				case "RED":
					possessions.put(i, redStreets);
					redStreets.put(street, NO_ONE_HAS_THIS);
					break;
				case "YELLOW":
					possessions.put(i, yellowStreets);
					yellowStreets.put(street, NO_ONE_HAS_THIS);
					break;
				case "GREEN":
					possessions.put(i, greenStreets);
					greenStreets.put(street, NO_ONE_HAS_THIS);
					break;
				case "BLUE":
					possessions.put(i, blueStreets);
					blueStreets.put(street, NO_ONE_HAS_THIS);
					break;
				}
				break;
			case Card.COMPANY:
				CompanyAndStation company = (CompanyAndStation)cards.get(i);
				possessions.put(i, companies);
				companies.put(company, NO_ONE_HAS_THIS);
				break;
			case Card.STATION:
				CompanyAndStation station = (CompanyAndStation)cards.get(i);
				possessions.put(i, stations);
				stations.put(station, NO_ONE_HAS_THIS);
				break;
			}
				
		}
		
	}	
	
	public int countStationsOfAPlayer (int player, int pos) {
		int count = 0;
		HashMap<Card, Integer> stations = possessions.get(pos);
		for (int i = 5; i < 39; i += 10) {
			if (stations.get(cards.get(i)) == player) count++;
		}
		return count;
	}
	
	public int countCompaniesOfAPlayer (int player, int pos) {
		int count = 0; //Finisce sulla compagnia, se � sua non paga e non viene neanche invocato, se non � sua viene inviato numero player per conteggio
		HashMap<Card, Integer> companies = possessions.get(pos);	
		if (companies.get(cards.get(12)) == player) count++;
		if (companies.get(cards.get(28)) == player) count++;
		return count;
	}
	
	public void playerPaysAnotherPlayer (int playerWhoGetsPayed, int value) {
		players.get(turn).removeMoneyFromSaving(value);
		players.get(playerWhoGetsPayed).putMoneyInSaving(value);			
	}
	
	public void soldHouses(int position) {
		Street street = (Street)cards.get(position);
		int houseValue = street.getHouse() / 2;
		players.get(turn).putMoneyInSaving(houseValue * street.getHousesNumber());
		players.get(turn).soldHouses(street.getHousesNumber());
		street.removeHouse();
	}
	
	//solo se ha 0 case sopra, necessario metodo che controlli che non ci siano case su altre propriet�
	public void sellStreetStationCompany (int position) {
		switch (cards.get(position).getType()) {
		case Card.STREET:
			Street street = (Street)cards.get(position);
			possessions.get(position).replace(street, NO_ONE_HAS_THIS);
			players.get(turn).putMoneyInSaving(street.getCost() / 2);
			break;
		case Card.STATION:
		case Card.COMPANY:
			CompanyAndStation c = (CompanyAndStation)cards.get(position);
			possessions.get(position).replace(c, NO_ONE_HAS_THIS);
			players.get(turn).putMoneyInSaving(c.getCost() / 2);
			break;
		}
	}
	
	public void buildHouse (int position) {
			Street street = (Street) cards.get(position);
			street.buildHouse();
			players.get(turn).buildedHouse();
	}
	
	public void buyStreetStationOrCompany () {	
		int position = playerPos.get(players.get(turn));
		possessions.get(position).replace(cards.get(position), turn);
	}
	
	/*
	 * @return se street sta in row
	 */
	public boolean checkStreetRow(int position, int player) {
		HashMap<Card, Integer> street = possessions.get(position); //ottengo la via di 2/3 case
		for (Integer value : street.values()) { //itera sui valori dello street
			if (value != player) return false;
			else continue;
		}
		return true;
	}	

	public boolean hasPosChanged (int prevPos) {
		return (playerPos.get(players.get(turn)) != prevPos);
	}
	
	public void putPlayerInJail (int player) {
		players.get(player).putInJail();
		playerPos.replace(players.get(player), 10);
	}
	
	public boolean isStreetBuildable () {
		if (checkStreetRow(playerPos.get(players.get(turn)), turn)) return true;
		else return false;
	}
	
	/*
	 * Se ci sono delle case costruite, non si pu� vendere la via. 
	 */
	public boolean isStreetSellable() {
		if (!checkStreetRow(playerPos.get(players.get(turn)), turn)) return true;
		else {
			HashMap<Card, Integer> street = possessions.get(playerPos.get(players.get(turn)));
			for (Card card : street.keySet()) {
				Street s = (Street) card;
				if (s.getHousesNumber() > 0) return false;
			}
			
			return true;
		}
	}
	
	public boolean isStreetCompanyStationBuyable () {
		int position = playerPos.get(players.get(turn));
		Card card = cards.get(position);
		if (possessions.get(position).get(card) == NO_ONE_HAS_THIS) return true;
		else return false;
	}

	public boolean isGameEnded () {
		return (players.size() == 1);
	}
	
	public Player winner () {
		return players.get(0);
	}
	
	public HashMap<Integer, Card> getCards() {
		return cards;
	}
	
	public HashMap<Player, Integer> getPlayerPos() {
		return playerPos;
	}
		
	public ArrayList<Player> getPlayers() {
		return players;
	}
	
	public Player getPlayerPlaying () {
		return players.get(turn);
	}
	
	public int getPlayerPlayingPos () {
		return playerPos.get(players.get(turn));
	}
	
	public int getTurn() {
		return turn;
	}

	public HashMap<Integer, HashMap<Card, Integer>> getPossessions() {
		return possessions;
	}
}
