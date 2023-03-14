package view;

import javax.swing.JPanel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JTextArea;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class GameChat extends JPanel {

	private static final long serialVersionUID = 1L;
	private JTextField textField;

	public GameChat() {
		
		JTextArea textArea = new JTextArea();

		textField = new JTextField();
		textField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					//textArea.append(textField.getText() + "\n");
					/*
					 * TODO: invia stringa al server per poter inviare messaggio
					 */
				}
			}
		});
		textField.setColumns(10);
		JButton sendMsg = new JButton("Invia msg");
		JScrollPane scrollPane = new JScrollPane();
		GroupLayout groupLayout = new GroupLayout(this);
		textArea.setLineWrap(true); //per far "fittare" il testo nella text area
		textArea.setWrapStyleWord(true);
		scrollPane.setViewportView(textArea);
		setLayout(groupLayout);
		textArea.setEditable(false);
		sendMsg.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//textArea.append(textField.getText() + "\n");
				/*
				 * TODO: stesso meccanismo dell'enter di prima
				 */
			}
		});
		
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 430, Short.MAX_VALUE)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(textField, GroupLayout.PREFERRED_SIZE, 280, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(sendMsg, GroupLayout.DEFAULT_SIZE, 144, Short.MAX_VALUE)))
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 253, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(sendMsg))
					.addContainerGap())
		);	
	}
}
