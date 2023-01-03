package game;

public class CompanyAndStation extends Card{
	
	int cost = 0;
	
	public CompanyAndStation(String name, int pos, int type) {
		super(name, pos, type);
		if (type == Card.STATION) cost = 200;
		else cost = 150;
	}

}
