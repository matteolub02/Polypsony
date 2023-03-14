package view;

import java.awt.Image;

import javax.swing.*;

public class GameWindow extends JFrame {
	
	private static final long serialVersionUID = 1L;
	private static final int WIDTH = 1000, HEIGHT = 620;

	/*
	 * TODO inizializzare user interface nell'event dispatch thread (libro per controllo)
	 */
	
	public GameWindow () {
		this.setDefaultCloseOperation(EXIT_ON_CLOSE); //stoppa il programma quando si chiude TODO comportamento da tenere sotto controllo per gestione client
		this.setVisible(true); 
		this.setLocation(200, 100); 
		this.setIconImage(new ImageIcon("src/view/duck_img.jpg").getImage());
		this.setTitle("POLIPSONY");
		this.setSize(WIDTH, HEIGHT);
		this.setResizable(false);
		
	}
	
}