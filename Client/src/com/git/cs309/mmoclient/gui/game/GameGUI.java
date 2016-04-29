package com.git.cs309.mmoclient.gui.game;

import java.awt.Component;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;

import com.git.cs309.mmoclient.gui.interfaces.ChatBox;

import com.git.cs309.mmoclient.gui.interfaces.PlayerInventoryGUI;


public class GameGUI extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6023633314141474861L;
	
	private static final GameGUI SINGLETON = new GameGUI();
	
	private GameGUI() {
		addComponentListener(new ComponentAdapter() {

			@Override
			public void componentResized(ComponentEvent arg0) {
				Component chatBox = ChatBox.getInstance();
				Component viewPanel = ViewPanel.getInstance();
				ChatBox.getInstance().setLocation(0, viewPanel.getHeight() - chatBox.getHeight());
			}
			
		});
		addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent arg0) {
				System.out.println("Pressed");
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
				System.out.println("Released");
			}

			@Override
			public void keyTyped(KeyEvent arg0) {
				System.out.println("Typed");
			}
			
		});
		setSize(Toolkit.getDefaultToolkit().getScreenSize());
		add(ViewPanel.getInstance());
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	public static GameGUI getSingleton() {
		return SINGLETON;
	}
	
	@Override
	public void setVisible(boolean state) {
		super.setVisible(state);
	}
}
