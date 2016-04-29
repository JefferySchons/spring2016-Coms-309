package com.git.cs309.mmoclient.gui.interfaces;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.git.cs309.mmoclient.Client;
import com.git.cs309.mmoserver.packets.InterfaceClickPacket;

public class DungeonGUI extends JPanel{
	private static final DungeonGUI INSTANCE = new DungeonGUI();
	//will be given list of dungion names 
	
	public static final DungeonGUI getInstance() {
		return INSTANCE;
	}
	
	public DungeonGUI() {
		//JFrame frame = new JFrame("Simple List Example");
		//Color background = new Color(153, 102, 51, 0x7F);
		//this.setBackground(background);
		this.setSize(300, 300);
		this.setLocation(200, 200);
		//this.setLayout(null);
		
		JTextArea messageArea = new JTextArea("dungeon");
		messageArea.setForeground(Color.BLACK);
		this.add(messageArea);
		
		//Create a JList 
		//String [] data = {Island,"def","ghi"}; //get list of dungion names
		//JList <String>myList = new JList<String>(data);
		//JScrollPane scrollPane = new JScrollPane(myList);
		
		JButton mainIsland=new JButton ("Main Island");
		mainIsland.setForeground(Color.BLACK);
		mainIsland.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//confurm what place to go to
				Client.getConnection().addOutgoingPacket(new InterfaceClickPacket(null, null));
				//TODO
			}
		});
		this.add(mainIsland);
		//TODO add other maps as buttons here
		
		//this.setLayout(null);
		//this.setOpaque(false);
		//this.setLayout(null);
		this.setVisible(true);
	}
	
	
	
	public void show(){
		this.setSize(new Dimension(200, 200));
	}
	
	public void hide(){
		this.setSize(new Dimension(0, 0));
	}
	
	

}
