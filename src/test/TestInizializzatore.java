package test;

import game.Card;
import game.CompanyAndStation;
import game.Inizializzatore;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;

import javax.xml.stream.XMLStreamException;

import org.junit.jupiter.api.Test;

class TestInizializzatore {

	@Test
	public void testInizializzaDati() throws XMLStreamException {
		HashMap<Integer, Card> mappa = Inizializzatore.inizializza();
		assertEquals("Community Chest", mappa.get(2).getType());
		assertEquals("Castenedolo", mappa.get(39).getName());
		assertEquals(200, ((CompanyAndStation) mappa.get(15)).getCost());
	}

}
