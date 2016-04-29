package com.git.cs309.mmoclient.gui.interfaces;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.git.cs309.mmoclient.Client;
import com.git.cs309.mmoclient.items.ItemContainer;
import com.git.cs309.mmoserver.packets.InterfaceClickPacket;

public class PlayerTradedGUI extends JPanel{
	
	private static final PlayerTradedGUI INSTANCE = new PlayerTradedGUI();
	
	public static final PlayerTradedGUI getInstance() {
		return INSTANCE;
	}
	
	JButton yesButton = new JButton("yes");
	JButton noButton = new JButton("no");
	//player 1
	//player2
	
	
	public PlayerTradedGUI()
	{		
		Color background = new Color(153, 102, 51, 0x7F);
		this.setBackground(background);
		this.setSize(400, 400);
		
		
		JTextArea nameOfShop = new JTextArea("trading with player");
		this.add(nameOfShop);
		
		ItemContainer inventoryStack1=Client.getSelf().getInventory();
		int playerinvSize1=inventoryStack1.getFirstEmptyIndex();
		

		//JScrollPane otherSidescrollPane = new JScrollPane(otherPlayerArea);
		//otherSidescrollPane.setBounds(0,0,150,400); //left side of screen
		//this.add(otherSidescrollPane);
		
		JTextArea ClientArea= new JTextArea();//PLAYER 1 inventory (CLIENT)
		for(int i=0; i<playerinvSize1; i++)
		{
			Client.getSelf().getInventory().getItemStack(i).getItemName();
		} 
		JScrollPane ClientplayerSidescrollPane = new JScrollPane(ClientArea);//player inventory 
		ClientplayerSidescrollPane.setBounds(250,0,400,150); //right side of screen
		this.add(ClientplayerSidescrollPane);
		
		JTextField whatItemToBuy =new JTextField("whatItem");
		this.add(whatItemToBuy);
		
		JTextField whatItemToSell =new JTextField("whatItem");
		this.add(whatItemToSell);
		
		JButton acceptButton = new JButton("accept");
		acceptButton.setBounds(150,0,100,100);
		this.add(acceptButton);
		acceptButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//send info to server
				//such as whatItemToBuy and whatItemToSell
				Client.getConnection().addOutgoingPacket(new InterfaceClickPacket(null, null));
				//TODO
			}
		});
		
		JButton declineButton = new JButton("decline");
		acceptButton.setBounds(150,100,100,100);
		this.add(declineButton);
		declineButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				hide();
			}
		});
		
		
		JTextArea amountArea = new JTextArea("amount of transaction");
		acceptButton.setBounds(150,200,200,100);
		this.add(amountArea);
		
		
		this.setLayout(null);
		this.setVisible(true);
	}
	
	public void show(){
		this.setPreferredSize(new Dimension(500, 500));
	}
	
	public void hide(){
		this.setPreferredSize(new Dimension(0, 0));
	}

}
