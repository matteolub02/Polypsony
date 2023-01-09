package cards;

public class Tax extends Card{
	
	private int cost;
	
	public Tax(String name, int pos, int type, int cost) {
		super(name, pos, type);
		this.cost = cost;
	}
	
	@Override
	public String toString () {
		return new String (super.toString() + " - Cost:" + cost);
	}
	
	public int getCost() {
		return cost;
	}

}
