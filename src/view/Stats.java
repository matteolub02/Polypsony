package view;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import javax.swing.JTextArea;

/*
 * @author 736418
 * @summary it shows game's stats.
 */

public class Stats extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel contentPane;

	private JTextArea textArea = new JTextArea();
	public Stats(String s) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 350);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		setResizable(false);
		textArea.setText(s);
		textArea.setEditable(false);
		contentPane.add(textArea, BorderLayout.CENTER);
	}
	
	public void changeTextArea(String s) {
		textArea.setText("");
		textArea.append(s);
	}

}
