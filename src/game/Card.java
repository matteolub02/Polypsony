package game;

public abstract class Card {
	
	public static final int STREET = 0, TAX = 1, CHANCE = 2, COMMUNITY_CHEST = 3, NO_EFF = 4, STATION = 5, COMPANY = 6;

	private String name;
	private int pos, type;
	
	
	public Card(String name, int pos, int type) {
		this.name = name;
		this.pos = pos;
		this.type = type;
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
	

}
