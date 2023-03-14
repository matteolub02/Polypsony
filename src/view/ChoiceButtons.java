package view;

import javax.swing.JPanel;
import java.awt.FlowLayout;
import javax.swing.JButton;

public class ChoiceButtons extends JPanel {


	private static final long serialVersionUID = 1L;
	private JButton throwDices, sellProperty, buyProperty, buildHouse;
	
	public ChoiceButtons() {
		setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		throwDices = new JButton("Lancia dadi");
		add(throwDices);
		
		sellProperty = new JButton("Vendi"); //propriet� o casa
		add(sellProperty);
		
		buyProperty = new JButton("Compra");
		add(buyProperty);
		
		buildHouse = new JButton("Costruisci");
		add(buildHouse);
		
		
	}

}
