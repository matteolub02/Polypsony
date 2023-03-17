package server;

public class Update {
	
	
	private int type, player1, player2, newPos, dices;
	private boolean buyable, sellable;
	
	public Update (int type) {
		
	}
	
	/*
	 * Questa classe rappresenta i singoli update inviati tra client e server.
	 * Considerando la condizione di gioco, ci sono questi aggiornamenti:
	 * -	Cambio turno (solo tipo)
	 * -	Lancio dadi  (intero?)
	 * -	Cambio posizione giocatore dell'attuale turno
	 * -	Pagamento di un giocatore verso altro giocatore
	 * -	Pagamento di un giocatore
	 * -	Versamento nei confronti di un giocatore
	 * -	Vendita di un possedimento
	 * -	Acquisto di un possedimento
	 * -	Fine turno (se non si vuole acquistare possedimento o casa/vendere)
	 * -	Rimozione giocatore
	 * -	Fine partita
	 * -	Controllo se uscito di prigione
	 * -	Acquistabile
	 * - 	Vendibile
	 */
	
}
