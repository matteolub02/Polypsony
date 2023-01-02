package game;

public class Street extends Card{
	
	private int cost, house;
	
	public Street(String name, int pos, int cost, int house) {
		super(name, pos, Card.STREET);
		this.cost = cost;
		this.house = house;
	}

	public int getCost() {
		return cost;
	}

	public int getHouse() {
		return house;
	}

}
