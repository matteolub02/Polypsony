package test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;

import javax.xml.stream.XMLStreamException;

import org.junit.jupiter.api.Test;

import cards.Card;
import cards.ChancesAndCommunityChest;
import cards.CompanyAndStation;
import cards.InitializeCards;

class TestInizializzatore {

	@Test
	public void testInitializeCards() throws XMLStreamException {
		HashMap<Integer, Card> mappa = InitializeCards.initialize();
		assertEquals("communitychest", mappa.get(2).getType());
		assertEquals("Castenedolo", mappa.get(39).getName());
		assertEquals(200, ((CompanyAndStation) mappa.get(15)).getCost());
	}
	
	@Test
	public void testInitializeCommunityAndChances () throws XMLStreamException {
		HashMap<Integer, Card> mappa = InitializeCards.initialize();
		ChancesAndCommunityChest comm = (ChancesAndCommunityChest)mappa.get(2);
		for (int i = 0; i < 50; i++) {
			ChancesAndCommunityChest.DescriptionAndAction d = comm.returnRandomCardEffect();
			if (!(d.getAction().equals(ChancesAndCommunityChest.ADVANCE_TO_NEXT_STATION) ||
					d.getAction().equals(ChancesAndCommunityChest.GO_TO_JAIL) ||
					d.getAction().equals(ChancesAndCommunityChest.GET_OUT_OF_JAIL_FREE))) {
				assertNotEquals(d.getAmount(), 0);
			}
			else assertEquals(d.getAmount(), 0);
		}
	}

}
