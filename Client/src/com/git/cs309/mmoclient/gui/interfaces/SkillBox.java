package com.git.cs309.mmoclient.gui.interfaces;

import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Toolkit;

import javax.swing.JFrame;

import com.git.cs309.mmoclient.gui.characterselection.CharacterSelectionGUI;
import com.git.cs309.mmoclient.gui.characterselection.SelectionCharacterComponent;
import com.git.cs309.mmoserver.packets.CharacterSelectionDataPacket;

public class SkillBox  extends JFrame{
private static final long serialVersionUID = -7062272668164270613L;
	
	private static final SkillBox SINGLETON = new SkillBox();
	
	public static final SkillBox getSingleton() {
		return SINGLETON;
	}
	
	private final Skills[] components = new Skills[5];
	
	private SkillBox() {
		this.setSize(300, 60);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(new GridLayout(0, 5));
		for (int i = 0; i < 5; i++) {
			components[i] = new Skills(i);
			add(components[i]);
		}
	}
	
	@Override
	public void paint(Graphics g) {
		//Do paint background
		super.paint(g);
	}
	
	/*public void updateComponents(CharacterSelectionDataPacket packet) {
		components[packet.getIndex()].updateSkillBox(packet);
		this.repaint();
	}*/
	
	
}
