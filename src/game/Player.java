package game;

public class Player {
	
	private String name = null;
	private int savings;
	private boolean isInJail = false;
	private int getOutOfJailFree = 0;
	private int buildedHouses = 0;
	
	
	public Player(String name) {
		this.name = name;
		savings = 1500;
	}
	
	public String getName() {
		return name;
	}

	public int getSavings() {
		return savings;
	}

	public void putMoneyInSaving(int value) {
		savings += value;
	}
	
	public void removeMoneyFromSaving(int value) {
		savings -= value;
	}
	
	public void putInJail () {
		isInJail = true;
	}
	
	public void freeFromJail() {
		isInJail = false;
	}

	public boolean isInJail() {
		return isInJail;
	}
	
	public void pickedGetOutOfJailFree () {
		getOutOfJailFree++;
	}
	
	public void usedGetOutOfJailFree () {
		getOutOfJailFree--;
	}
	
	public int getGetOutOfJailFreeNumber () {
		return getOutOfJailFree;
	}
	
	public void setGetOutOfJailFree (int value) {
		getOutOfJailFree = value;
	}

	public int getBuildedHouses() {
		return buildedHouses;
	}

	public void buildedHouse () {
		buildedHouses++;
	}


}	
