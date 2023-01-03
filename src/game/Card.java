package game;

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

	public String getType() {
		switch (type) {
		case STREET:
			return "Street";
		case TAX:
			return "Tax";
		case CHANCE:
			return "Chance";
		case COMMUNITY_CHEST:
			return "Community Chest";
		case NO_EFF:
			return "No Eff";
		case STATION:
			return "Station";
		case COMPANY:
			return "Company";
		}
		return "err";
	}
	

}
