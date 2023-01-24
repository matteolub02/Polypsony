package game;

public class Player {
	
	private String name;
	private int balance;
	private int pos;

	public Player(String name) {
		this.name = name;
		balance = 1500;
		pos = 0;
	}
	
	public String getName() {
		return name;
	}

	public int getBalance() {
		return balance;
	}

	public int getPos() {
		return pos;
	}

	public void setPos(int pos) {
		this.pos = pos;
	}
	
	public void setBalance(int balance) {
		this.balance = balance;
	}
	
}
