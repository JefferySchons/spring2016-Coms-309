package com.git.cs309.mmoclient.gui.characterselection;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import com.git.cs309.mmoclient.Client;
import com.git.cs309.mmoclient.graphics.SpriteDatabase;
import com.git.cs309.mmoserver.packets.CharacterSelectionDataPacket;
import com.git.cs309.mmoserver.packets.InterfaceClickPacket;

public class SelectionCharacterComponent extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3409845224182388202L;
	private final SelectionCharacter character;
	private final int index;
	
	public SelectionCharacterComponent(final int index) {
		this.index = index;
		character = new SelectionCharacter(index);
		JButton loginButton = new JButton("Log in");
		this.add(loginButton);
		
		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				switch (index) {
				case 0:
					Client.getConnection().addOutgoingPacket(new InterfaceClickPacket(null, InterfaceClickPacket.CHARACTER_1_SLOT, 0, 0, 0));
					break;
				case 1:
					Client.getConnection().addOutgoingPacket(new InterfaceClickPacket(null, InterfaceClickPacket.CHARACTER_2_SLOT, 0, 0, 0));
					break;
				case 2:
					Client.getConnection().addOutgoingPacket(new InterfaceClickPacket(null, InterfaceClickPacket.CHARACTER_3_SLOT, 0, 0, 0));
					break;
				case 3:
					Client.getConnection().addOutgoingPacket(new InterfaceClickPacket(null, InterfaceClickPacket.CHARACTER_4_SLOT, 0, 0, 0));
					break;
				case 4:
					Client.getConnection().addOutgoingPacket(new InterfaceClickPacket(null, InterfaceClickPacket.CHARACTER_5_SLOT, 0, 0, 0));
					break;
				}
			}
		});
	}
	
	public void updateSelectionCharacter(CharacterSelectionDataPacket packet) {
		character.setCharacter(packet);
	}
	
	@Override
	public void paint(Graphics g) {
		ImageIcon icon0 = new ImageIcon("data/sprites/gui/f.pvr.ccz.png"); 
		//ImageIcon button = new ImageIcon("data/sprites/gui/f.pvr.ccz.png");
		//JButton rectangle=new JButton(button);
		g.drawImage(icon0.getImage(), 0, 0, getWidth(), getHeight(), 2, 10, 570, 590, null);
		if (character.hasCharacter()) {
			//SpriteDatabase.getInstance().getSprite("playerStop");
			ImageIcon icon1 = new ImageIcon("data/sprites/playerStop.png");
			g.drawImage(icon1.getImage(), getWidth()/2-50, getHeight()/2-50, getWidth()/2+50, getHeight()/2+50, 0, 0, 96, 96, null);
			g.drawString(character.getName(), getWidth() / 4, getHeight() / 4 );
		}
	}

	/**
	 * @return the index
	 */
	public int getIndex() {
		return index;
	}
}
