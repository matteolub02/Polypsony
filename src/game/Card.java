package game;

public abstract class Card {
	
	/*
	 * The cards are divided in purchasable and not for sale.
	 * If a card is not purchasable, it gives the player an effect:
	 * -Draw a card
	 * -Make him pay for:
	 * 	.TAXES
	 * 	.
	 */
	
	private String name;
	private int pos;
	
	
	public Card(String name, int pos) {
		this.name = name;
		this.pos = pos;
	}
	
	public String getName() {
		return name;
	}

	public int getPos() {
		return pos;
	}	

}
