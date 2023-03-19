package cards;

public class Street extends Card{
	private static final long serialVersionUID = 1L;

	private String color;
	private int cost, rent, house;
	private int houseNumber = 0;
	
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
		/*
		 * SINGOLO AFFITTO = y
			ROW = 2 * y
			PRIMA CASA = 2 * (ROW) + y (houseNumber = 1)
			SECONDA CASA = 3 * (PRIMA CASA)
			TERZA CASA = 2 * (SECONDA CASA) + PRIMA CASA 
			QUARTA CASA = TERZA CASA + PRIMA CASA = 2*SECONDA + 2*PRIMA
			ALBERGO = TERZA CASA + SECONDA CASA (houseNumber = 5) = 3 seconda + prima
		 */
		
		switch (houseNumber) {
		case 0:
			return rent;
		case 1:
			return (2*(2*rent) + rent);
		case 2:
			return (3*(2*(2*rent) + rent));
		case 3:
			return (2*(3*(2*(2*rent) + rent)) + (2*(2*rent) + rent));
		case 4:
			return (2*(3*(2*(2*rent) + rent)) + 2*(2*(2*rent) + rent));
		case 5:
			return (3*(3*(2*(2*rent) + rent)) + (2*(2*rent) + rent));
		}
		
		return -1;
	}
	
	public String getColor() {
		return color;
	}
	
	public void buildHouse () {
		houseNumber++;
	}
	
	public void removeHouse () {
		houseNumber = 0;
	}
	
	public int getHousesNumber() {
		return houseNumber;
	}
	
}
