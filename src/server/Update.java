package server;

import java.io.Serializable;

/**
 * @author 736418
 * @summary Update from Client to Server, it tells which choice the player has made.
 *
 */
public class Update implements Serializable{
	
	/*
	 * 5 types:
	 * 1 build house
	 * 2 buy property
	 * 3 sell property
	 * 4 sell houses
	 * 5 end turn
	 */
	
	private static final long serialVersionUID = 1L;
	private int type = -1;
	
	public Update (int type) {
		this.type = type;
	}
	
	public int getType() {
		return type;
	}
	
}
