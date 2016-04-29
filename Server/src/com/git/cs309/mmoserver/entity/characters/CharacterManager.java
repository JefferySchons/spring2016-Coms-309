package com.git.cs309.mmoserver.entity.characters;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.git.cs309.mmoserver.Config;
import com.git.cs309.mmoserver.lang.module.ModuleManager;
import com.git.cs309.mmoserver.map.MapManager;
import com.git.cs309.mmoserver.util.TickProcess;

/**
 * 
 * @author Group 21
 *
 *         TickProcess implementation that manages all Character objects.
 */
public final class CharacterManager extends TickProcess {

	private final Set<Character> characterSet = new HashSet<>(); // All registered characters

	public CharacterManager() {
		
	}

	/**
	 * Adds a character object to the character set. In theory, this should only
	 * need to be called from the Character constructor.
	 * 
	 * @param character
	 *            character to add to set.
	 */
	public void addCharacter(final Character character) { // Add new character to characterSet
		synchronized (characterSet) {
			characterSet.add(character);
			ModuleManager.getModule(MapManager.class).putEntityAtPosition(character.getInstanceNumber(), character.getX(),
					character.getY(), character.getZ(), character);
		}
	}

	@Override
	public void ensureSafeClose() {
		//Not required
	}

	@Override
	public void printStatus() {
		println("Total characters: " + characterSet.size());
	}

	public void removeCharacter(final Character character) {
		synchronized (characterSet) {
			characterSet.remove(character);
			ModuleManager.getModule(MapManager.class).removeEntityAtPosition(character.getInstanceNumber(), character.getX(),
					character.getY(), character.getZ(), character);
			character.cleanUp();
		}
	}

	/**
	 * Processes characters. Should be called each tick, from the tickTask
	 * method.
	 * 
	 * @param regenTick
	 *            determines whether or not to apply regeneration this tick.
	 */
	private void processCharacters(final boolean regenTick) {
		List<Character> toRemove = new ArrayList<>(characterSet.size());
		synchronized (characterSet) {
			for (Character character : characterSet) {
				if (character.needsDisposal()) {
					toRemove.add(character);
					continue;
				}
				if (regenTick) {
					character.applyRegen(Config.REGEN_AMOUNT);
				}
				character.process();
			}
		}
		for (Character character : toRemove) {
			removeCharacter(character);
		}
	}
	
	public Character getCharacter(final int uniqueId) {
		synchronized (characterSet) {
			for (Character character : characterSet) {
				if (character.getUniqueID() != uniqueId) {
					continue;
				}
				return character;
			}
		}
		return null;
	}

	@Override
	protected void tickTask() {
		processCharacters(server.getTickCount() % Config.TICKS_PER_REGEN == 0); // Process characters.
	}

	@Override
	public String getVariableName() {
		return "CharacterManager";
	}

}
