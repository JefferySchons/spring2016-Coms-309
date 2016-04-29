package com.git.cs309.mmoserver.entity.characters;

import java.awt.EventQueue;
import java.util.Queue;

import com.git.cs309.mmoserver.Config;
import com.git.cs309.mmoserver.Server;
import com.git.cs309.mmoserver.combat.CombatManager;
import com.git.cs309.mmoserver.combat.CombatStyle;
import com.git.cs309.mmoserver.entity.Entity;
import com.git.cs309.mmoserver.lang.module.ModuleManager;
import com.git.cs309.mmoserver.map.Map;
import com.git.cs309.mmoserver.map.MapManager;
import com.git.cs309.mmoserver.map.PathFinder;
import com.git.cs309.mmoserver.map.PathFinder.Tile;
import com.git.cs309.mmoserver.packets.CharacterStatusPacket;
import com.git.cs309.mmoserver.util.ClosedIDSystem.IDTag;
import com.git.cs309.mmoserver.util.CycleQueue;

/**
 * 
 * @author Group 21
 *
 *         An abstract class representing the basis for all in-game characters,
 *         such as player, npcs, bosses, etc.
 */
public abstract class Character extends Entity {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4707730413469521334L;

	public static final int NO_OPPONENT = -1;
	
	//Current health.
	protected volatile int health;
	protected transient volatile boolean isDead = false; //true is dead
	protected transient volatile Queue<Tile> walkingQueue = new CycleQueue<>(0);
	protected transient volatile long walkingTick = 0;
	protected transient volatile boolean walking = false;
	protected transient volatile boolean inCombat = false;
	protected transient volatile int opponentId = -1;
	protected transient volatile Runnable onFinishedWalking = null;
	protected transient volatile long attackTimer = System.currentTimeMillis();

	public Character() {
		super();
	}

	public Character(final int x, final int y, final int z, final IDTag idTag, final int entityID, final String name) {
		super(x, y, z, idTag, entityID, name);
		EventQueue.invokeLater(new Runnable() {

			@Override
			public void run() {
				ModuleManager.getModule(CharacterManager.class).addCharacter(Character.this);
			}

		});
	}
	
	public boolean canAttack() {
		return System.currentTimeMillis() - Config.ATTACK_DELAY > attackTimer;
	}
	
	public void resetAttackTimer() {
		attackTimer = System.currentTimeMillis();
	}
	
	public void resetCombat() {
		System.out.println(this+" is resetting combat.");
		inCombat = false;
		opponentId = NO_OPPONENT;
	}

	public void applyDamage(int damageAmount) {
		health -= damageAmount;
		if (health <= 0) {
			isDead = true;
			onDeath();
		}
	}
	
	public abstract CharacterStatusPacket getCharacterStatusPacket();

	public void applyRegen(int regenAmount) {
		if (isDead) {
			return;
		}
		if (health + regenAmount <= getMaxHealth()) {
			health += regenAmount;
		} else {
			health = getMaxHealth();
		}
		//TODO handle regen
	}

	@Override
	public boolean canWalkThrough() {
		return true;
	}

	public int getHealth() {
		return health;
	}
	
	public boolean isWalking() {
		return walking;
	}

	public abstract int getLevel();

	public abstract int getMaxHealth();

	public boolean isDead() {
		return isDead || health <= 0;
	}
	
	public abstract CombatStyle getCombatStyle();
	
	public boolean inCombat() {
		return inCombat;
	}

	public void kill() {
		isDead = true;
		onDeath();
	}
	
	public final void walkTo(int x, int y) {
		Map map = ModuleManager.getModule(MapManager.class).getMapContainingPosition(instanceNumber, getX(), getY(), getZ());
		if (map.containsPoint(x, y))
			walkingQueue = PathFinder.getPathToPoint(map, getX(), getY(), x, y);
	}
	
	protected abstract void onDeath();
	
	protected abstract boolean canWalk();
	
	protected void handleWalking() {
		if (!canWalk() || walkingQueue == null) {
			return;
		}
		if (!walking && !walkingQueue.isEmpty()) {
			walking = true;
		}
		if (walking && !walkingQueue.isEmpty() && ModuleManager.getModule(Server.class).getTickCount() - walkingTick >= Config.TICKS_PER_WALK) {
			walkingTick = ModuleManager.getModule(Server.class).getTickCount();
			Tile t = walkingQueue.remove();
			Character opponent = inCombat ? ModuleManager.getModule(CharacterManager.class).getCharacter(this.getOpponentId()) : null;
			if (!inCombat || !(opponent.getX() == t.getX() && opponent.getY() == t.getY()))
				setPosition(t.getX(), t.getY(), getZ());
		}
		if (walking && walkingQueue.isEmpty()) {
			walking = false;
			if (onFinishedWalking != null) {
				onFinishedWalking.run();
				onFinishedWalking = null;
			}
		}
	}
	
	public void setOnFinishedWalking(final Runnable onFinishedWalking) {
		this.onFinishedWalking = onFinishedWalking;
	}
	
	public void attack(final Character character) {
		this.opponentId = character.getUniqueID();
		character.inCombat = true;
		character.opponentId = this.getUniqueID();
		inCombat = true;
		System.out.println(this+" is attacking "+character);
	}
	
	public int getOpponentId() {
		return opponentId;
	}
	
	protected abstract void characterProcess();

	public final void process() {
		if (isDead()) {
			return;
		}
		if (inCombat) {
			CombatManager.handleCombat(this);
		}
		handleWalking();
		characterProcess();
	}

}
