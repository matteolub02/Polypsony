package cards;

public class Card {
	
	public static final int STREET = 0, TAX = 1, CHANCE = 2, COMMUNITY_CHEST = 3, NO_EFF = 4, STATION = 5, COMPANY = 6;

	private String name;
	private int pos, type;
	
	public Card(String name, int pos, int type) {
		this.name = name;
		this.pos = pos;
		this.type = type;
	}
	
	@Override
	public String toString () {
		return new String(name + " - Type: " + getType());
	}
	
	public String getName() {
		return name;
	}

	public int getPos() {
		return pos;
	}
	
	public int getType() {
		return type;
	}
	
	public String getTypeName() {
		switch (type) {
		case STREET:
			return "street";
		case TAX:
			return "tax";
		case CHANCE:
			return "chances";
		case COMMUNITY_CHEST:
			return "communitychest";
		case NO_EFF:
			return "noeff";
		case STATION:
			return "station";
		case COMPANY:
			return "company";
		}
		return "err"; //tipo carta, utile per controllo azioni nel model
	}
	

}
