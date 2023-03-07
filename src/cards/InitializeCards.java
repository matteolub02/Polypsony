package cards;

import java.io.FileInputStream;
import java.util.HashMap;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

public class InitializeCards {
	
	public static HashMap<Integer, Card> initialize () throws XMLStreamException {
		HashMap<Integer, Card> cards = new HashMap<>(); //
		XMLStreamReader xmlr = xmlrCreator ("src/cards/Cards.xml");
		int pos = 0;
		while (xmlr.hasNext()) {
			if (xmlr.next() == XMLStreamConstants.START_ELEMENT)switch (xmlr.getLocalName()) {
			case "start":
				pos = Integer.parseInt(xmlr.getAttributeValue(0));
				cards.put(pos, new Card("Partenza", pos, Card.NO_EFF));
				break;
				case "street":
					pos = Integer.parseInt(xmlr.getAttributeValue(0));
					cards.put(pos, streetFromXml(xmlr, pos));
					break;
				case "prison":
					pos = Integer.parseInt(xmlr.getAttributeValue(0));
					cards.put(pos, new Card("Prigione", pos, Card.NO_EFF));
					break;
				case "gotoprison":
					pos = Integer.parseInt(xmlr.getAttributeValue(0));
					cards.put(pos, new Card("Vai in prigione!", pos, Card.NO_EFF));
					break;
				case "station":
					pos = Integer.parseInt(xmlr.getAttributeValue(0));
					cards.put(pos, companyOrStationFromXml(xmlr, pos, Card.STATION));
					break;
				case "company":
					pos = Integer.parseInt(xmlr.getAttributeValue(0));
					cards.put(pos, companyOrStationFromXml(xmlr, pos, Card.COMPANY));	
					break;
				case "chances": 
					pos = Integer.parseInt(xmlr.getAttributeValue(0));
					cards.put(pos, new ChancesAndCommunityChest("Imprevisti", pos, Card.CHANCE));
					break;
				case "communitychest":
					pos = Integer.parseInt(xmlr.getAttributeValue(0));
					cards.put(pos, new ChancesAndCommunityChest("Probabilita'", pos, Card.COMMUNITY_CHEST));
					break;
				case "tax":
					pos = Integer.parseInt(xmlr.getAttributeValue(0));
					cards.put(pos, taxXml(xmlr, pos));
					break;
				case "parcheggio":
					pos = Integer.parseInt(xmlr.getAttributeValue(0));
					cards.put(pos, new Card("Parcheggio libero", pos, Card.NO_EFF));
					break;
			}
		}
		ChancesAndCommunityChest.initializeChancesAndCommunityChestCards();
		return cards;
	}
	
	private static Street streetFromXml(XMLStreamReader xmlr, int pos) throws XMLStreamException {
		int cost = 0, rent = 0, house = 0;
		String color = "", name = "";
		while (xmlr.getEventType() != XMLStreamConstants.END_ELEMENT || !xmlr.getLocalName().equals("street")) {
			if (xmlr.next() == XMLStreamConstants.START_ELEMENT) {
				switch (xmlr.getLocalName()) {
				case "color":
					xmlr.next();
					color = xmlr.getText();
					xmlr.next();
					break;
				case "name":
					xmlr.next();
					name = xmlr.getText();
					xmlr.next();
					break;
				case "cost":
					xmlr.next();
					cost = Integer.parseInt(xmlr.getText());
					xmlr.next();
					break;
				case "rent":
					xmlr.next();
					rent = Integer.parseInt(xmlr.getText());
					xmlr.next();
					break;
				case "house":
					xmlr.next();
					house = Integer.parseInt(xmlr.getText());
					xmlr.next();
					break;
				}
			}
			else continue;
		}
		return new Street(pos, color, name, cost, rent, house); 
	}
	
	private static CompanyAndStation companyOrStationFromXml (XMLStreamReader xmlr, int pos, int type) throws XMLStreamException {
		String name = "";
		while (xmlr.getEventType() != XMLStreamConstants.END_ELEMENT || 
				(!xmlr.getLocalName().equals("station") && !xmlr.getLocalName().equals("company"))) {
			if (xmlr.next() == XMLStreamConstants.START_ELEMENT) {
				if (xmlr.getLocalName().equals("name")) {
					xmlr.next();
					name = xmlr.getText();
					xmlr.next();
				}
			}
			else continue;
		}
		return new CompanyAndStation(name, pos, type);
	}
	
	private static Tax taxXml (XMLStreamReader xmlr, int pos) throws XMLStreamException {
		String name = "";
		int cost = 0;
		
		while (xmlr.getEventType() != XMLStreamConstants.END_ELEMENT ||
				!xmlr.getLocalName().equals("tax")) {
			if (xmlr.next() == XMLStreamConstants.START_ELEMENT) {
				switch (xmlr.getLocalName()) {
				case "name":
					xmlr.next();
					name = xmlr.getText();
					xmlr.next();
					break;
				case "cost":
					xmlr.next();
					cost = Integer.parseInt(xmlr.getText());
					xmlr.next();
					break;
				}
			}
		}
		
		return new Tax(name, pos, Card.TAX, cost);
	}
	
	public static XMLStreamReader xmlrCreator (String fileXmlPos) {
		XMLInputFactory xmlif = null;
		XMLStreamReader xmlr = null;
		try {
		 xmlif = XMLInputFactory.newInstance();
		 xmlr = xmlif.createXMLStreamReader(fileXmlPos, new FileInputStream(fileXmlPos));
		} catch (Exception e) {
		 System.out.println("Errore nell'inizializzazione del reader:");
		 System.out.println(e.getMessage());
		}
		return xmlr;
	}
	
}
