package com.git.cs309.mmoserver.entity.characters.npc;

import com.git.cs309.mmoserver.Config;
import com.git.cs309.mmoserver.Server;
import com.git.cs309.mmoserver.combat.CombatStyle;
import com.git.cs309.mmoserver.cycle.CycleProcess;
import com.git.cs309.mmoserver.cycle.CycleProcessManager;
import com.git.cs309.mmoserver.entity.Entity;
import com.git.cs309.mmoserver.entity.EntityType;
import com.git.cs309.mmoserver.entity.characters.Character;
import com.git.cs309.mmoserver.entity.characters.npc.dropsystem.DropSystem;
import com.git.cs309.mmoserver.items.ItemStack;
import com.git.cs309.mmoserver.lang.module.ModuleManager;
import com.git.cs309.mmoserver.map.Map;
import com.git.cs309.mmoserver.map.MapManager;
import com.git.cs309.mmoserver.packets.CharacterStatusPacket;
import com.git.cs309.mmoserver.packets.ExtensiveCharacterPacket;
import com.git.cs309.mmoserver.packets.Packet;
import com.git.cs309.mmoserver.util.ClosedIDSystem;
import com.git.cs309.mmoserver.util.MathUtils;

/**
 * 
 * @author Group 21
 *
 *         <p>
 *         The NPC class represents the basic framework for all non player
 *         character characters, for example wolves or shopkeepers. Each NPC
 *         requires an NPCDefinition, which defines the properties of the NPC.
 *         </p>
 */
public class NPC extends Character {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7665018239684872294L;
	private transient final NPCDefinition definition; // The NPC definition of this NPC
	private transient final int spawnX;
	private transient final int spawnZ;
	private transient final int spawnY;
	private transient final boolean autoRespawn;
	protected transient volatile int walkDesperation = Config.NPC_WALKING_RATE;

	public NPC(int x, int y, int z, final NPCDefinition definition, int instanceNumber) {
		super(x, y, z, ClosedIDSystem.getTag(), definition.getID(), definition.getName());
		assert definition != null;
		this.spawnX = x;
		this.spawnY = y;
		this.spawnZ = z;
		this.definition = definition;
		this.instanceNumber = instanceNumber;
		this.autoRespawn = true;
	}

	public NPC(int x, int y, int z, final NPCDefinition definition, int instanceNumber, boolean autoRespawn) {
		super(x, y, z, ClosedIDSystem.getTag(), definition.getID(), definition.getName());
		assert definition != null;
		this.spawnX = x;
		this.spawnY = y;
		this.spawnZ = z;
		this.definition = definition;
		this.instanceNumber = instanceNumber;
		this.autoRespawn = autoRespawn;
	}

	@Override
	public EntityType getEntityType() {
		return EntityType.NPC;
	}

	@Override
	public Packet getExtensivePacket() {
		return new ExtensiveCharacterPacket(null, getUniqueID(), definition.getID(), x, y, getHealth(), getMaxHealth(),
				definition.getLevel(), definition.getName());
	}

	@Override
	public int getLevel() {
		return definition.getLevel();
	}

	@Override
	public int getMaxHealth() {
		return definition.getMaxHealth();
	}

	public int getRespawnTimer() {
		return definition.getRespawnTimer();
	}

	public int getSpawnX() {
		return spawnX;
	}

	public int getSpawnY() {
		return spawnY;
	}

	public int getSpawnZ() {
		return spawnZ;
	}

	public boolean isAutoRespawn() {
		return autoRespawn && definition.isAutoRespawn();
	}

	@Override
	public String toString() {
		return definition.getName() + ":" + getUniqueID();
	}

	@Override
	protected void characterProcess() {
		if(definition.aggressive() && !inCombat) {
			for (Entity entity : ModuleManager.getModule(MapManager.class).getMapContainingEntity(this).getEntitySet()) {
				if (entity.getEntityType() != EntityType.NPC || entity.equals(this)) {
					continue;
				}
				Character character = (Character) entity;
				if (character.inCombat()) {
					continue;
				}
				if (MathUtils.distance(entity.getX(), entity.getY(), getX(), getY()) < 5) {
					attack((Character) entity);
					break;
				}
			}
		}
	}
	
	@Override
	public void handleWalking() {
		super.handleWalking();
		if (!walking && walkingQueue.isEmpty() && !inCombat && (int) (Math.random() * walkDesperation--) == 0) {
			int newX = (int) (Config.MAX_WALKING_DISTANCE - (Math.random() * ((Config.MAX_WALKING_DISTANCE * 2) + 1)));
			int newY = (int) (Config.MAX_WALKING_DISTANCE - (Math.random() * ((Config.MAX_WALKING_DISTANCE * 2) + 1)));
			walkTo(newX, newY);
			if (walkingQueue.size() == 0) {
				walkDesperation /= 2;
			} else {
				walkDesperation = Config.NPC_WALKING_RATE;
			}
		}
		if (walkDesperation <= 0) {
			walkDesperation = 1;
		}
	}

	@Override
	protected boolean canWalk() {
		return definition.canWalk();
	}

	@Override
	protected void onDeath() {
		System.out.println(this+" has died.");
		Map map = ModuleManager.getModule(MapManager.class).getMapContainingEntity(this);
		for (ItemStack stack : DropSystem.getInstance().getDropsForNPC(getName())) {
			map.putItemStack(getX(), getY(), stack);
		}
		NPC.this.requestDisposal();
		if (isAutoRespawn()) {
			ModuleManager.getModule(CycleProcessManager.class).addProcess(new CycleProcess() {
				final Server server = ModuleManager.getModule("Server", Server.class);
				final long startTick = server.getTickCount();
				long currentTick = server.getTickCount();

				@Override
				public void end() {
					NPCFactory.getInstance().createNPC(NPC.this.getName(), NPC.this.getSpawnX(), NPC.this.getSpawnY(), NPC.this.getSpawnZ(),
							NPC.this.getInstanceNumber());
				}

				@Override
				public boolean finished() {
					return currentTick - startTick == (Config.TICKS_PER_MINUTE * NPC.this.getRespawnTimer());
				}

				@Override
				public void process() {
					currentTick = server.getTickCount();
				}

			});
		}
	}

	@Override
	public CharacterStatusPacket getCharacterStatusPacket() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CombatStyle getCombatStyle() {
		return definition.getCombatStyle();
	}

}
