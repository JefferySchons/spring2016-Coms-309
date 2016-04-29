package com.git.cs309.mmoclient.gui.game;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ComponentListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.git.cs309.mmoclient.Client;
import com.git.cs309.mmoclient.entity.Entity;
import com.git.cs309.mmoclient.entity.character.Character;
import com.git.cs309.mmoclient.graphics.Sprite;
import com.git.cs309.mmoclient.graphics.SpriteDatabase;
import com.git.cs309.mmoclient.gui.interfaces.ChatBox;
import com.git.cs309.mmoclient.gui.interfaces.GameInterface;
import com.git.cs309.mmoclient.gui.interfaces.RightClickOptionsInterface;
import com.git.cs309.mmoclient.entity.EntityType; 

import com.git.cs309.mmoserver.packets.EntityClickPacket;

import com.git.cs309.mmoclient.gui.interfaces.ClientShopGUIT2;
import com.git.cs309.mmoclient.gui.interfaces.DungeonGUI;
import com.git.cs309.mmoclient.gui.interfaces.PlayerInventoryGUI;

import com.git.cs309.mmoserver.packets.MovePacket;

public class ViewPanel extends JPanel {
	public static final Color WATER_COLOR = new Color(40, 40, 240);
	
	private static final Thread PAINT_THREAD = new Thread(new Runnable() {

		@Override
		public void run() {
			while (true) {
				synchronized (INSTANCE) {
					INSTANCE.repaint();
				}
				try {
					Thread.sleep(16);
				} catch (InterruptedException e) {
					// Can just ignore this
				}
			}
		}
		
	});
	
	private static final ViewPanel INSTANCE = new ViewPanel();
	
	public static final ViewPanel getInstance() {
		return INSTANCE;
	}
	
	private final HashMap<String, GameInterface> gameInterfaces = new HashMap<String, GameInterface>();
	
	private Image offscreenImage = null;
	
	private ViewPanel() {
		this.setLayout(null);
		this.add(ChatBox.getInstance());

		this.setBackground(new Color(0, 0, 0, 0.0f));

		this.add(DungeonGUI.getInstance());
		DungeonGUI.getInstance().hide();
		this.add(ClientShopGUIT2.getInstance());
		ClientShopGUIT2.getInstance().hide();
		this.add(PlayerInventoryGUI.getInstance());
		PlayerInventoryGUI.getInstance().hide();
		this.setBackground(new Color(0, 0, 0, 0.0f));
		this.add(DungeonGUI.getInstance());
		DungeonGUI.getInstance().hide();
		this.add(ClientShopGUIT2.getInstance());
		ClientShopGUIT2.getInstance().hide();
		this.add(PlayerInventoryGUI.getInstance());
		PlayerInventoryGUI.getInstance().hide();

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
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				removeInterface("rightclickoptions");
				int gameX = Client.mouseXToGameX(e.getX());
				int gameY = Client.mouseYToGameY(e.getY());
				if (!Client.getMap().containsPoint(gameX, gameY)) {
					return;
				}
				if (SwingUtilities.isRightMouseButton(e)) {
					//TODO Create popup menu with names which can be clicked and such
					List<String> options = new ArrayList<>();
					for (Entity entity : Client.getMap().getEntities(gameX, gameY)) {
						if (entity.getUniqueID() == Client.getSelfId())
							continue;
						if (entity.getEntityType() == EntityType.NPC || entity.getEntityType() == EntityType.PLAYER) {
							Character c = (Character) entity;
							options.add(c.getName()+" ("+c.getLevel()+")");
						} else {
							options.add(entity.getName());
						}

						if(entity.getStaticID()== 2 && entity.getEntityType() ==EntityType.OBJECT)
						{
							DungeonGUI.getInstance().show();
							//System.out.println("dungion gui");
						}
						else if((entity.getStaticID()== 2 || entity.getStaticID()== 3) && entity.getEntityType() ==EntityType.NPC)
						{
							
							ClientShopGUIT2.getInstance().show();
							//System.out.println("shop gui");
						}
						else if (entity.getEntityType() == EntityType.PLAYER)
						{
							PlayerInventoryGUI.getInstance().show();
						}
						else 
						{
							DungeonGUI.getInstance().hide();
							ClientShopGUIT2.getInstance().hide();
							PlayerInventoryGUI.getInstance().hide();
						}

					}
					options.add("Walk here");
					options.add("Cancel");
					addInterface(new RightClickOptionsInterface(e.getX(), e.getY(), options.toArray(new String[options.size()])));
				} else {
					Entity[] entities = Client.getMap().getEntities(gameX, gameY);
					if (entities.length > 0) {
						Client.getConnection().addOutgoingPacket(new EntityClickPacket(null, entities[0].getStaticID(), entities[0].getUniqueID(), gameX, gameY, 0));

					} else {
						Client.getConnection().addOutgoingPacket(new MovePacket(null, gameX, gameY));
					
					}
					Client.getConnection().addOutgoingPacket(new MovePacket(null, gameX, gameY));
					DungeonGUI.getInstance().hide();
					ClientShopGUIT2.getInstance().hide();
					PlayerInventoryGUI.getInstance().hide();

				}
				ViewPanel.this.repaint();
			}
		});
		Component chatBox = ChatBox.getInstance();
		ChatBox.getInstance().setLocation(0, getHeight() - chatBox.getHeight());

		PAINT_THREAD.start();
	}
	
	public void addInterface(GameInterface newInterface) {
		gameInterfaces.put(newInterface.getName(), newInterface);
		add(newInterface);
		this.repaint();
	}
	
	public void removeInterface(String interfaceName) {
		GameInterface gameInterface = gameInterfaces.get(interfaceName);
		gameInterfaces.remove(interfaceName, gameInterface);
		if (gameInterface == null)
			return;
		remove(gameInterface);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 7651841779875266840L;
	
	@Override
	public void paint(Graphics g) {
		if (offscreenImage == null) {
			offscreenImage = createImage(getWidth(), getHeight());
		}
	    Graphics offscreenGraphics = offscreenImage.getGraphics();
		if (Client.getSelf() == null || Client.getMap() == null)
			return;
		Graphics2D g2d = (Graphics2D) offscreenGraphics;
		AffineTransform trans = new AffineTransform();
		AffineTransform oldTrans = g2d.getTransform();
		trans.setToIdentity();
		g2d.setTransform(trans);
		Sprite water = SpriteDatabase.getInstance().getSprite("waterbg");
		g2d.drawImage(water.getImage(), 0, 0, getWidth(), getHeight(), null);
		Client.getMap().paint(g2d);
		//Client.getSelf().paint(offscreenGraphics);
		offscreenGraphics.setColor(Color.RED);
		g2d.setTransform(oldTrans);

		super.paint(offscreenGraphics);
		offscreenGraphics.dispose();
		g.drawImage(offscreenImage, 0, 0, null);
	}

}
