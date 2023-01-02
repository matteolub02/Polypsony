package game;

import java.io.FileInputStream;
import java.util.HashMap;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

public class Inizializzatore {
	
	public static HashMap<Integer, String> inizializza () throws XMLStreamException {
		HashMap<Integer, String> cards = new HashMap<>(); //
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
			xmlr.next();
			if (xmlr.getEventType() == XMLStreamConstants.START_ELEMENT)switch (xmlr.getAttributeCount()) {
			case 1:
				pos = Integer.parseInt(xmlr.getAttributeValue(0));
				break;
			case 0:
				if (xmlr.getLocalName().equals("name")) {
					xmlr.next();
					cards.put(pos, xmlr.getText());
					pos = 0;
				}
				break;
				
			/*
				case "street":
					
					break;
				case "station":
					
					break;
				case "company":
					
					break;
				case "imprevisto": 
					break;
				case "probabilita":
					
					break;
				case "tax":
					
					break;
			*/
			}
		}
		
		for (int i = 0; i < cards.size(); i++) {
			System.out.println(i + " - " + cards.get(i));
		}
		
		return cards;
	}

}
