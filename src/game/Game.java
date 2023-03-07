package game;

import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.stream.XMLStreamException;

import cards.*;

public class Game {
	
	private static final int NO_ONE_HAS_THIS = -1;
	
	private ArrayList<Player> players = null;
	private HashMap<Integer, Card> cards = null;
	private HashMap<Integer, HashMap<Card, Integer>> possessions = new HashMap<>();
	private int turn = 0;
	
	
	public Game (ArrayList<Player> players, HashMap<Integer, Card> cards) throws XMLStreamException {
		this.players = players;
		this.cards = InitializeCards.initialize();
		initializePossessions();
	}

	public ArrayList<Player> getPlayers() {
		return players;
	}


	public HashMap<Integer, Card> getCards() {
		return cards;
	}

	public int getTurn() {
		return turn;
	}

	public void changePlayerPos (int value) {
		players.get(turn).changePos(value);
		if ((turn + 1) < players.size()) turn += 1;
		else turn = 0;
	}
	
	private void initializePossessions () {
		/*
		 * TODO: girare per tutte le carte e riempire HashMap
		 */
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
	
}
