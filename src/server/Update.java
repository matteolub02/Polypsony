package server;

import java.io.Serializable;

public class Update implements Serializable{
	
	/*
	 * 5 tipi:
	 * 1 costruisci casa
	 * 2 compra proprietà
	 * 3 vendi proprietà
	 * 4 vendi case
	 * 5 finisci turno
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
