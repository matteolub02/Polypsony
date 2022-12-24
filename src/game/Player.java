package game;

public class Player {
	
	private String name;
	private int balance;
	/*
	 * TODO: add player's properties (houses, contracts)
	 * TODO: 
	 */
	public Player(String name) {
		this.name = name;
		balance = 1500;
	}
	
	public String getName() {
		return name;
	}

	public int getBalance() {
		return balance;
	}

}
