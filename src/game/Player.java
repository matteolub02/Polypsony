package game;

import java.io.Serializable;

/**
 * @author 736418
 * @summary This is the Player class, it represents the single player.
 */
public class Player implements Serializable{
	

	private static final long serialVersionUID = 1L;
	
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
		freeFromJail();
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
	
	public boolean soldHouses (int soldHouses) {
		buildedHouses -= soldHouses;
		return (buildedHouses >= 0);
	}	
	
}	

