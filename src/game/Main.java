package game;

import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.stream.XMLStreamException;

import cards.Card;

public class Main {
	
	private static final int pos = 30;
	
	public static void main (String args[]) throws XMLStreamException {
		ArrayList<Player> players = new ArrayList<>();
		players.add(new Player("Marco"));
		players.add(new Player("Paolo"));
		players.add(new Player("Giovanni"));
		Game game = new Game(players, new HashMap<Integer, Card>());
		game.changePlayerPos(pos);
		System.out.println(game.cardEffect());
		System.out.println(game.checkJailStatusPlayerPlaying());
		game.removePlayerFromJailNotForFree();
		System.out.println(players.get(0).getSavings() + " " + game.cardEffect());
		

	}
}
