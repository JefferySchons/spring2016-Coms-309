package com.git.cs309.mmoclient.entity;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import com.git.cs309.mmoclient.Client;
import com.git.cs309.mmoclient.Config;
import com.git.cs309.mmoclient.graphics.Sprite;
import com.git.cs309.mmoclient.graphics.SpriteDatabase;
import com.git.cs309.mmoclient.gui.game.ViewPanel;
import com.git.cs309.mmoclient.entity.character.Character;

public abstract class Entity extends Component {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4656689876001467423L;
	protected volatile int x = 0, y = 0; // Coordinates
	protected int entityID = -1;
	protected final int uniqueId;
	protected String name = "Null";
	protected int previousX = 0;
	protected int previousY = 0;
	protected int direction = 3;
	protected int stop = 1;

	public Entity(final int x, final int y, final int uniqueId, final int entityID, final String name) {
		this.x = x;
		this.y = y;
		this.entityID = entityID;
		this.uniqueId = uniqueId;
		this.name = name;
		ViewPanel.getInstance().repaint();
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				System.out.println("Clicked: "+name);
			}
		});
	}
	
	public Sprite getSprite() {
		return SpriteDatabase.getInstance().getSprite(getName());
	}
	
	@Override
	public void paint(Graphics g) {
		Sprite sprite = getSprite();
		if (sprite != null) {
				//Handling animations here
				/*if (System.currentTimeMilliseconds() - lastTime >= 100 && lastTime != -1) {
					a + 1
					lastTime = System.currentTimeMillis();
				}
				if (a == end of walk anim) {
					lastTime == -1;
				}*/
				if(getEntityType() == EntityType.PLAYER){
					long a =System.currentTimeMillis();
					Character player = (Character) this;
					if(player.isInCombat()==true){
						if (getEntityType() == EntityType.NPC || getEntityType() == EntityType.PLAYER) {
							Character character = (Character) this;
							if (character.isInCombat() && character.getOpponentId() != Character.NO_OPPONENT) {
								Entity opponent = Client.getMap().getEntity(character.getOpponentId());
								if(x-opponent.x>0){
									if(y-opponent.y>0){
										if((x-opponent.x)>=(y-opponent.y)){
											direction = 2;
										}else{
											direction = 1;
										}
									}else if(y-opponent.y<0){
										if((x-opponent.x)>=(opponent.y-y)){
											direction = 2;
										}else{
											direction = 3;
										}
									}else{
										direction = 2;
									}
								}else if(x-opponent.x<0){
									if(y-opponent.y>0){
										if((opponent.x-x)>=(y-opponent.y)){
											direction = 4;
										}else{
											direction = 1;
										}
									}else if(y-opponent.y<0){
										if((opponent.x-x)>=(opponent.y-y)){
											direction = 4;
										}else{
											direction = 3;
										}
									}else{
										direction = 4;
									}
								}else{
									if(y-opponent.y>0){
										direction = 1;
									}else if(y-opponent.y<0){
										direction = 3;
									}
								}
								g.drawString("In Combat", 0, 0);
							}
						}
					}
					if(direction == 3){
						if(player.isWalking()==false){
							g.drawImage(sprite.getImage(0,3), getPaintX(), getPaintY(), Config.DEFAULT_SPRITE_WIDTH, Config.DEFAULT_SPRITE_HEIGHT, null);
						}else{
							if((a/400%2)==0){
								g.drawImage(sprite.getImage(1,3), getPaintX(), getPaintY(), Config.DEFAULT_SPRITE_WIDTH, Config.DEFAULT_SPRITE_HEIGHT, null);
							}else{
								g.drawImage(sprite.getImage(3,3), getPaintX(), getPaintY(), Config.DEFAULT_SPRITE_WIDTH, Config.DEFAULT_SPRITE_HEIGHT, null);
							}
						}
					}else if(direction == 2){
						if(player.isWalking()==false){
							g.drawImage(sprite.getImage(0,2), getPaintX(), getPaintY(), Config.DEFAULT_SPRITE_WIDTH, Config.DEFAULT_SPRITE_HEIGHT, null);
						}else{
							if((a/400%2)==0){
								g.drawImage(sprite.getImage(1,2), getPaintX(), getPaintY(), Config.DEFAULT_SPRITE_WIDTH, Config.DEFAULT_SPRITE_HEIGHT, null);
							}else{
								g.drawImage(sprite.getImage(3,2), getPaintX(), getPaintY(), Config.DEFAULT_SPRITE_WIDTH, Config.DEFAULT_SPRITE_HEIGHT, null);
							}
						}
						
					}else if(direction == 1){
						if(player.isWalking()==false){
							g.drawImage(sprite.getImage(0,0), getPaintX(), getPaintY(), Config.DEFAULT_SPRITE_WIDTH, Config.DEFAULT_SPRITE_HEIGHT, null);
						}else{
							if((a/400%2)==0){
								g.drawImage(sprite.getImage(1,0), getPaintX(), getPaintY(), Config.DEFAULT_SPRITE_WIDTH, Config.DEFAULT_SPRITE_HEIGHT, null);
							}else{
								g.drawImage(sprite.getImage(3,0), getPaintX(), getPaintY(), Config.DEFAULT_SPRITE_WIDTH, Config.DEFAULT_SPRITE_HEIGHT, null);
							}
						}
					}if(direction == 4){
						if(player.isWalking()==false){
							g.drawImage(sprite.getImage(0,1), getPaintX(), getPaintY(), Config.DEFAULT_SPRITE_WIDTH, Config.DEFAULT_SPRITE_HEIGHT, null);
						}else{
							if((a/400%2)==0){
								g.drawImage(sprite.getImage(1,1), getPaintX(), getPaintY(), Config.DEFAULT_SPRITE_WIDTH, Config.DEFAULT_SPRITE_HEIGHT, null);
							}else{
								g.drawImage(sprite.getImage(3,1), getPaintX(), getPaintY(), Config.DEFAULT_SPRITE_WIDTH, Config.DEFAULT_SPRITE_HEIGHT, null);
							}
						}
					}	
				}else{
					g.drawImage(sprite.getImage(), getPaintX(), getPaintY(), Config.DEFAULT_SPRITE_WIDTH, Config.DEFAULT_SPRITE_HEIGHT, null);
				}
				if (getEntityType() == EntityType.NPC || getEntityType() == EntityType.PLAYER) {
					Character character = (Character) this;
					if (character.isInCombat() && character.getOpponentId() != Character.NO_OPPONENT) {
						Entity opponent = Client.getMap().getEntity(character.getOpponentId());
					}
				}
		} else {
			g.setColor(Color.BLACK);
			g.drawRect(getPaintX(), getPaintY(), Config.DEFAULT_SPRITE_WIDTH, Config.DEFAULT_SPRITE_HEIGHT);
			g.setFont(g.getFont().deriveFont(Font.BOLD, 18.0f));
			g.drawString(""+getName().charAt(0), getPaintX() + (Config.DEFAULT_SPRITE_WIDTH / 2) - 5, getPaintY() + (Config.DEFAULT_SPRITE_HEIGHT / 2) + 5);
		}
	}

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof Entity)) {
			return false;
		}
		Entity entity = (Entity) other;
		return entity.x == x && entity.y == y && entity.uniqueId == uniqueId && name.equals(entity.name)
				&& getEntityType() == entity.getEntityType();
	}

	public abstract EntityType getEntityType();

	public final String getName() {
		return name;
	}

	public final int getStaticID() {
		return entityID;
	}

	public final int getUniqueID() {
		return uniqueId;
	}
	
	public int getPaintX() {
		double rotX = ((Math.cos(Client.getRotation()) * (x - Client.getSelf().getX())) - (Math.sin(Client.getRotation()) * (y - Client.getSelf().getY())));// + Client.getSelf().getX();
		return (int) (((rotX) * Config.DEFAULT_SPRITE_WIDTH) + (ViewPanel.getInstance().getWidth() / 2) - (Config.DEFAULT_SPRITE_WIDTH / 2));
	}
	
	public int getPaintY() {
		double rotY = ((Math.sin(Client.getRotation()) * (x - Client.getSelf().getX())) + (Math.cos(Client.getRotation()) * (y - Client.getSelf().getY())));// + Client.getSelf().getY();
		return (int) (((rotY) * Config.DEFAULT_SPRITE_HEIGHT) + (ViewPanel.getInstance().getHeight() / 2) - (Config.DEFAULT_SPRITE_HEIGHT / 2));
	}

	public final int getX() {
		return x;
	}

	public final int getY() {
		return y;
	}

	public void setPosition(final int x, final int y) {
		//Animation walk = new Animation(uniqueId, this.x, this.y, x, y);
		//TODO handle walking
		if(this.x == x&&this.y == y){
			stop =1;
		}else{
			stop =0;
		}
		previousX = this.x;
		previousY = this.y;
		this.x = x;
		this.y = y;
		if(x-previousX>0){
			if(y-previousY>0){
				if((x-previousX)>=(y-previousY)){
					direction = 2;
				}else{
					direction = 1;
				}
			}else if(y-previousY<0){
				if((x-previousX)>=(previousY-y)){
					direction = 2;
				}else{
					direction = 3;
				}
			}else{
				direction = 2;
			}
		}else if(x-previousX<0){
			if(y-previousY>0){
				if((previousX-x)>=(y-previousY)){
					direction = 4;
				}else{
					direction = 1;
				}
			}else if(y-previousY<0){
				if((previousX-x)>=(previousY-y)){
					direction = 4;
				}else{
					direction = 3;
				}
			}else{
				direction = 4;
			}
		}else{
			if(y-previousY>0){
				direction = 1;
			}else if(y-previousY<0){
				direction = 3;
			}
		}
		
		//this.lastTime = System.currentTimeMillis();
		//ViewPanel.getInstance().repaint();
	}
}
