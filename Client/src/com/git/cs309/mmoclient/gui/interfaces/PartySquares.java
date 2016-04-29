package com.git.cs309.mmoclient.gui.interfaces;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.JTextField;


public class PartySquares extends JPanel{
	
	private static final PartySquares INSTANCE = new PartySquares();
	
	private final JTextField partyArea = new JTextField();
	
	
	public static final PartySquares getInstance() {
		return INSTANCE;
	}
	
	public PartySquares()
	{
		this.setSize(400, 400);
		
		Color background = new Color(153, 102, 51, 0x7F);
		this.setBackground(background);
		this.setSize(500, 200);
		setLayout(new BorderLayout());
		partyArea.setEditable(false);
		partyArea.setBackground(background);
		partyArea.setForeground(Color.YELLOW);
		partyArea.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
			}
	});
		
	}
	
	public void addplayer()
	{
		
	}
	
	public void show(){
		this.setPreferredSize(new Dimension(500, 500));
	}
	
	public void hide(){
		this.setPreferredSize(new Dimension(0, 0));
	}
}
