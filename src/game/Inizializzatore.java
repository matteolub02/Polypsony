package game;

import java.io.FileInputStream;
import java.util.HashMap;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

public class Inizializzatore {
	
	public static HashMap<Integer, Card> inizializza () throws XMLStreamException {
		HashMap<Integer, Card> cards = new HashMap<>(); //
		XMLInputFactory xmlif = null;
		XMLStreamReader xmlr = null;
		try {
		 xmlif = XMLInputFactory.newInstance();
		 xmlr = xmlif.createXMLStreamReader("src/game/Cards.xml", new FileInputStream("src/game/Cards.xml"));
		} catch (Exception e) {
		 System.out.println("Errore nell'inizializzazione del reader:");
		 System.out.println(e.getMessage());
		}
		int pos = 0;
		while (xmlr.hasNext()) {
			if (xmlr.next() == XMLStreamConstants.START_ELEMENT)switch (xmlr.getLocalName()) {
			case "start":
				pos = Integer.parseInt(xmlr.getAttributeValue(0));
				break;
				case "street":
					pos = Integer.parseInt(xmlr.getAttributeValue(0));
					cards.put(pos, streetFromXml(xmlr, pos));
					break;	
				case "station":
					pos = Integer.parseInt(xmlr.getAttributeValue(0));
					cards.put(pos, companyOrStationFromXml(xmlr, pos, Card.STATION));
					break;
				case "company":
					pos = Integer.parseInt(xmlr.getAttributeValue(0));
					cards.put(pos, companyOrStationFromXml(xmlr, pos, Card.COMPANY));	
					break;
				/*
				case "imprevisto": 
					break;
				case "probabilita":
					
					break;
				case "tax":
					
					break;
				*/
			}
		}
		for (int i = 0; i < 40; i++) {
			System.out.println(cards.get(i));
		}
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
	
	

}
