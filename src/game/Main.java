package game;

import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.stream.XMLStreamException;

import cards.Card;

public class Main {
	public static void main (String args[]) throws XMLStreamException {
		
		Game game = new Game(new ArrayList<Player>(), new HashMap<Integer, Card>());

		
	}
}
