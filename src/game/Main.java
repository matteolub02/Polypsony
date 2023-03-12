package game;

import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.stream.XMLStreamException;

import cards.Card;

public class Main {
	
	private static final int pos = 37;
	
	public static void main (String args[]) throws XMLStreamException {
		ArrayList<Player> players = new ArrayList<>();
		players.add(new Player("Marco"));
		players.add(new Player("Paolo"));
		players.add(new Player("Giovanni"));
		Game game = new Game(players, new HashMap<Integer, Card>());
		game.nextTurn();
		game.nextTurn();
		System.out.println(game.getTurn());
		game.changePlayerPos(pos);
		game.buyStreetStationOrCompany();
		game.forceRemovePlayer(0);
		System.out.println(game.getPlayerPos() + "" + game.getTurn());
		System.out.println(game.isStreetCompanyStationBuyable());
		

	}
}
