package cards;

public class CompanyAndStation extends Card{
	private static final long serialVersionUID = 1L;

	private int cost = 0;
	
	public CompanyAndStation(String name, int pos, int type) {
		super(name, pos, type);
		if (type == Card.STATION) cost = 200;
		else cost = 150;
	}
	
	@Override
	public String toString() {
		return new String(super.toString() + " Cost:" + cost);
	}
	
	public int getCost() {
		return cost;
	}
	
}
