package com.git.cs309.mmoclient.gui.interfaces;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.git.cs309.mmoclient.items.ItemContainer;

public class EquipmentGUI  extends JPanel{
	private static final EquipmentGUI INSTANCE = new EquipmentGUI();
	//will be given list of dungion names 
	
	public static final EquipmentGUI getInstance() {
		return INSTANCE;
	}
	
	public EquipmentGUI()
	{
		Color background = new Color(153, 102, 51, 0x7F);
		this.setBackground(background);
		this.setSize(400, 400);
		
		this.add(createPanel());
		
		final ItemContainer inventoryStack=new ItemContainer();
		//inventoryStack=Self.getEquipment();
		int invSize=inventoryStack.getFirstEmptyIndex();
		
		String [] data;
		data= new String[invSize];
		
		for(int i=0; i< invSize; i++)
		{
			data[i]=inventoryStack.getItemStack(i).getItemName();;
		}
		
		//Create a JList 
		JList <String>myList = new JList<String>(data);
		JScrollPane scrollPane = new JScrollPane(myList);
		this.add(scrollPane);
		
		
		//this.setLayout(null);
		//this.setOpaque(false);
		this.setLayout(null);
		this.setVisible(true);
	}
	
	public JPanel createPanel(){
		
		JPanel panel = new JPanel();
		final ItemContainer inventoryStack=new ItemContainer();
		//inventoryStack=Self.getEquipment();
		int invSize=inventoryStack.getFirstEmptyIndex();
		
		String [] data;
		data= new String[invSize];
		
		for(int i=0; i< invSize; i++)
		{
			data[i]=inventoryStack.getItemStack(i).getItemName();;
		}
		
		//Create a JList 
		JList <String>myList = new JList<String>(data);
		JScrollPane scrollPane = new JScrollPane(myList);
		panel.add(scrollPane);
		
		return panel;
	}
	
	public void show(){
		this.setPreferredSize(new Dimension(500, 500));
	}
	
	public void hide(){
		this.setPreferredSize(new Dimension(0, 0));
	}
}
