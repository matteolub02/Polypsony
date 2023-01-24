package cards;

public class Street extends Card{
	
	private String color;
	private int cost, rent, house;
	
	
	public Street(int pos, String color, String name, int cost, int rent, int house) {
		super(name, pos, Card.STREET);
		this.color = color; 
		this.cost = cost;
		this.rent = rent;
		this.house = house; 
	}
	
	@Override
	public String toString() {
		return new String(super.toString() + " Color: " + color + " Rent: " + rent + " House Cost: " + house);
		
	}
	
	public int getCost() {
		return cost;
	}

	public int getHouse() {
		return house;
	}

	public int getRent() {
		return rent;
	}

	public String getColor() {
		return color;
	}

}
