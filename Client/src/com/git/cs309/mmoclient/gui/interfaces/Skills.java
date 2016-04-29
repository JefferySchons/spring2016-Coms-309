package com.git.cs309.mmoclient.gui.interfaces;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.git.cs309.mmoclient.Client;
import com.git.cs309.mmoclient.gui.characterselection.SelectionCharacter;
import com.git.cs309.mmoclient.gui.game.ViewPanel;
import com.git.cs309.mmoserver.packets.CharacterSelectionDataPacket;
import com.git.cs309.mmoserver.packets.InterfaceClickPacket;

public final class Skills extends Component {
	private static final long serialVersionUID = 3409845224182388202L;
	private final SkillBox skill;
	private final int index;
	
	public Skills(final int index){
		this.index = index;
		skill = null;//new SkillBox(index);
		
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
	
	public void paint(Graphics g) {
		ImageIcon skill = null;
		switch (index) {
		case 0:
			skill = new ImageIcon("data/sprites/skills/1 (1).png");
			break;
		case 1:
			skill = new ImageIcon("data/sprites/skills/1 (10).png");
			break;
		case 2:
			skill = new ImageIcon("data/sprites/skills/1 (2).png");
			break;
		case 3:
			skill = new ImageIcon("data/sprites/skills/1 (3).png");
			break;
		case 4:
			skill = new ImageIcon("data/sprites/skills/1 (4).png");
			break;
		}
			g.drawImage(skill.getImage(),0,0,getWidth(), getHeight(), null);
	}

	/**
	 * @return the index
	 */
	public int getIndex() {
		return index;
	}

	/*public void updateSkills(SkillsDataPacket packet) {
		skill.setCharacter(packet);
	}*/
	
}