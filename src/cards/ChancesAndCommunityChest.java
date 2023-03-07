package cards;

import java.util.ArrayList;
import java.util.Random;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

public class ChancesAndCommunityChest extends Card{
	public static final String PAY_TO_ALL = "payToAll"; 
	public static final String GO_BACK = "goBack";
	public static final String PAY_BY_HOUSES = "payByHouses"; 
	public static final String COLLECT_FROM_ALL = "collectFromAll";
	public static final String PAY = "pay";
	public static final String COLLECT = "collect";
	public static final String ADVANCE_TO = "advanceTo";
	public static final String ADVANCE_TO_NEXT_STATION = "advanceToNextStation";
	public static final String GO_TO_JAIL = "goToJail";
	public static final String GET_OUT_OF_JAIL_FREE = "getOutOfJailFree";
	private static final String COMM = "communitychest", CHANCE = "chances";
	
	public static class DescriptionAndAction {
		String description;
		String action;
		int amount = 0;
		
		public String toString () {
			return new String(description + "\n" + action + "\t" + amount);
		}
		
		public String getDescription() {
			return description;
		}
		
		public String getAction() {
			return action;
		}
		
		public int getAmount () {
			return amount;
		}
	}
	
	private static ArrayList<DescriptionAndAction> chances;
	private static ArrayList<DescriptionAndAction> communitychest;
	
	public ChancesAndCommunityChest(String name, int pos, int type) {
		super(name, pos, type);
	}
	
	public DescriptionAndAction returnRandomCardEffect () {
		Random rnd = new Random();
		switch (getTypeName()) {
		case COMM:
			return communitychest.get(rnd.nextInt(communitychest.size()));
		case CHANCE:
			return chances.get(rnd.nextInt(chances.size()));
		}
		return null;
	}
	
	public static void initializeChancesAndCommunityChestCards () {
		XMLStreamReader xmlr = InitializeCards.xmlrCreator("src/cards/Chances_CommunityChest.xml");
		chances = new ArrayList<>();
		communitychest = new ArrayList<>();
		try {
			while (xmlr.hasNext()) {
				xmlr.next();
				if (xmlr.getEventType() == XMLStreamConstants.START_ELEMENT && xmlr.getLocalName().equals(COMM)) {
					communitychest = readCardsData(xmlr, COMM);
				}
				else if (xmlr.getEventType() == XMLStreamConstants.START_ELEMENT && xmlr.getLocalName().equals(CHANCE)) {
					chances = readCardsData(xmlr, CHANCE);
				}
			}
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}
		
	}
	
	public static ArrayList<DescriptionAndAction> readCardsData (XMLStreamReader xmlr, String type) throws XMLStreamException {
		ArrayList<DescriptionAndAction> list = new ArrayList<>();
		DescriptionAndAction d = new DescriptionAndAction();
		while (xmlr.getEventType() != XMLStreamConstants.END_ELEMENT || !xmlr.getLocalName().equals(type)) {
			if (xmlr.next() == XMLStreamConstants.START_ELEMENT) {
				switch (xmlr.getLocalName()) {
				case "card":
					d = new DescriptionAndAction();
					list.add(d);
					break;
				case "name":
					xmlr.next();
					d.description = xmlr.getText();
					xmlr.next();
					break;
				case "action":
					xmlr.next();
					switch (xmlr.getText()) {
					case GET_OUT_OF_JAIL_FREE: case GO_TO_JAIL: case ADVANCE_TO_NEXT_STATION:
						d.action = xmlr.getText();
						xmlr.next();
						break;
					case ADVANCE_TO: case COLLECT: case PAY: case COLLECT_FROM_ALL:
					case PAY_BY_HOUSES: case GO_BACK: case PAY_TO_ALL:
						d.action = xmlr.getText(); 
						while (xmlr.next() != XMLStreamConstants.START_ELEMENT);
						xmlr.next();
						d.amount = Integer.parseInt(xmlr.getText());
						xmlr.next();
						break;	
					}
					xmlr.next();
					break;
				}
			}
		}
		return list;
	}
}
