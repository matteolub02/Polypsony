package server;

import java.awt.EventQueue;

import view.HostOrConnectWindow;


/*----------------------------------------/
 * @author 736418
 * @summary Polypsony - Progetto di PAJC
/*---------------------------------------*/


/**
 *	Avvia il programma
 */
public class Main {
	
	public static void main (String args[]) {
		
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					HostOrConnectWindow frame = new HostOrConnectWindow();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		
		
	}
	
}
