package view;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import server.ControllerPlayer;
import server.ControllerServer;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

public class HostOrConnectWindow extends JFrame {
	
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtNome;
	private JTextField txtIndirizzoIp;
	private ControllerServer cServer;
	private ControllerPlayer cPlayer;
	
	public HostOrConnectWindow() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 80);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JButton btnNewButton = new JButton("Host");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)  {
				dispose();
				try {
					cServer = new ControllerServer();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		contentPane.add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("Connect");
		contentPane.add(btnNewButton_1);
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String name = txtNome.getText();
				String ip = txtIndirizzoIp.getText();
				dispose();
				cPlayer = new ControllerPlayer(name, ip);

			}
		});
		
		txtNome = new JTextField();
		txtNome.setText("Nome");

		txtNome.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				txtNome.setText("");
			}
		});
		contentPane.add(txtNome);
		txtNome.setColumns(10);
		
		txtIndirizzoIp = new JTextField();
		txtIndirizzoIp.setText("Indirizzo IP");
		txtIndirizzoIp.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				txtIndirizzoIp.setText("");
			}
		});
		contentPane.add(txtIndirizzoIp);
		txtIndirizzoIp.setColumns(10);
	}

	public ControllerServer getcServer() {
		return cServer;
	}

	public ControllerPlayer getcPlayer() {
		return cPlayer;
	}

}
