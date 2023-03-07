package game;

public class Player {
	
	private String name = null;
	private int savings, pos;
	
	public Player(String name) {
		this.name = name;
		savings = 1500;
		pos = 0;
	}
	
	
	public int getPos() {
		return pos;
	}
	
	public void changePos(int value) {
		if ((pos+value) <= 39) pos += value;
		else pos = ((pos+value) - 39);
	}
	
	public String getName() {
		return name;
	}

	public int putSavings() {
		return savings;
	}

	public void putMoneyInSaving(int value) {
		savings += value;
	}
	
	public void removeMoneyFromSaving(int value) {
		savings -= value;
	}
	
	
}	
