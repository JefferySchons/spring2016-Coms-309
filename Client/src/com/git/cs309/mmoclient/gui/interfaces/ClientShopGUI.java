package com.git.cs309.mmoclient.gui.interfaces;

import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.git.cs309.mmoclient.Client;
import com.git.cs309.mmoclient.gui.interfaces.ClientShop;
import com.git.cs309.mmoclient.items.ItemStack;
import com.git.cs309.mmoserver.packets.InterfaceClickPacket;
import com.git.cs309.mmoserver.packets.LoginPacket;

public class ClientShopGUI extends JPanel{
	
	//do not use this one
	int shopID;
	ClientShop guiShop;
	
	public ClientShopGUI(String shopName, int shopID)
	{
		this.setSize(400, 400);
		ClientShop guiShop=new ClientShop(shopName, shopID);
		
		JTextArea nameOfShop = new JTextArea(guiShop.getName());
		this.add(nameOfShop);
		
		int inventorySize=guiShop.getNumberOfItems();
		JTextField shopInventory = new JTextField();
		for(int i=0; i<inventorySize; i++)
		{
			shopInventory.setText(shopInventory.getText()+guiShop.getItemName(i));
		}
		this.add(shopInventory);
		
		JTextField whatItemToBuy =new JTextField("whatItem");
		this.add(whatItemToBuy);
		
		JTextField whatItemToSell =new JTextField("whatItem");
		this.add(whatItemToSell);
		
		JButton buyButton = new JButton("Trade");
		this.add(buyButton);
		buyButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//send info to server
				//such as whatItemToBuy and whatItemToSell
				//TODO
				Client.getConnection().addOutgoingPacket(new InterfaceClickPacket(null, null));
			}
		});
		
		this.setLayout(null);
		this.setVisible(true);
	}
	
	
	public void addtems(ItemStack items) {
		
		guiShop.addItem(items);
		//chatArea.append(message + "\n");
		//chatArea.setCaretPosition(chatArea.getText().length());
	}
	
	public void show(){
		this.setPreferredSize(new Dimension(500, 500));
	}
	
	public void hide(){
		this.setPreferredSize(new Dimension(0, 0));
	}
	

}
