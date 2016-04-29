package com.git.cs309.mmoclient.gui.interfaces;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import com.git.cs309.mmoclient.Client;
import com.git.cs309.mmoclient.items.ItemContainer;
import com.git.cs309.mmoclient.gui.game.ViewPanel;

public class PlayerInventoryGUI extends JPanel{
	private static final PlayerInventoryGUI INSTANCE = new PlayerInventoryGUI();
	
	public static final PlayerInventoryGUI getInstance() {
		return INSTANCE;
	}
	
	
	public PlayerInventoryGUI()
	{
		this.setLocation(100, 100);
		//Color background = new Color(153, 102, 51, 0x7F);
		//this.setBackground(background);
		this.setSize(400, 400);
		
		setLayout(new BorderLayout());
		
		JTextField title= new JTextField("Player invnetory");
		//title.setBackground(background);
		//title.setForeground(Color.YELLOW);
		this.add(title);
		
		/*
		ItemContainer inventoryStack=Client.getSelf().getInventory();
		int invSize=inventoryStack.getFirstEmptyIndex();
		*/
		String [] data;
		data= new String[5];
		
		for(int i=0; i< 5; i++)
		{
			//data[i]=inventoryStack.getItemStack(i).getItemName();
			data[i]="item"+i;
		}
		
		//Create a JList 
		JList <String>myList = new JList<String>(data);
		JScrollPane scrollPane = new JScrollPane(myList);
		//scrollPane.setBackground(background);
		//scrollPane.setForeground(Color.YELLOW);
		this.add(scrollPane);
		
		
		//this.setLayout(null);
		//this.setOpaque(false);
		//this.setLayout(null);
		this.setVisible(true);
	}
	
	
	public void show(){
		this.setSize(new Dimension(500, 500));
	}
	
	public void hide(){
		this.setSize(new Dimension(0, 0));
	}
}
