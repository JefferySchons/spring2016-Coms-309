package com.git.cs309.mmoclient.gui.login;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import com.git.cs309.mmoclient.Client;
import com.git.cs309.mmoserver.packets.LoginPacket;

public final class LoginGUI extends JFrame implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4537727222546132067L;
	
	private static final LoginGUI SINGLETON = new LoginGUI();
	
	private final JLabel JLUserName = new JLabel("Username:");
	private final JLabel JLPaw = new JLabel("Password");
	private final JTextField usernameField = new JTextField("Username");
	private final JPasswordField passwordField = new JPasswordField("Password");
	private final JButton loginButton = new JButton("Log in");
	private final JButton cancelButton = new JButton("Cancel");
	
	private LoginGUI() {
		this.setTitle("Island Depths Login in Menu");
		this.setLayout(null);
		JLUserName.setBounds(100, 40, 100, 20);
		this.add(JLUserName);
		usernameField.setBounds(200,40,80,20);
		this.add(usernameField);
		
		JLPaw.setBounds(100, 100, 60, 20);
		this.add(JLPaw);
		passwordField.setBounds(200, 100, 80, 20);
		this.add(passwordField);
		
		loginButton.setBounds(100,200,60,20);
		this.add(loginButton);
		loginButton.addActionListener(this);

		cancelButton.setBounds(200, 200, 60, 20);
		this.add(cancelButton);
		cancelButton.addActionListener(this);
		this.setVisible(true);
		this.setBounds(10, 10, 400, 250);
		/*addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
			System.exit(0);
			}
		});*/
		//pack();
	}
	
	public static LoginGUI getSingleton() {
		return SINGLETON;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==loginButton){
			Client.getConnection().addOutgoingPacket(new LoginPacket(null, usernameField.getText(), String.valueOf(passwordField.getPassword())));
		}
	}
}