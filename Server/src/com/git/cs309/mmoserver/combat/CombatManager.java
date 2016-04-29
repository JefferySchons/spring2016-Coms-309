package com.git.cs309.mmoserver.combat;

import com.git.cs309.mmoserver.entity.characters.Character;
import com.git.cs309.mmoserver.entity.characters.CharacterManager;
import com.git.cs309.mmoserver.lang.module.ModuleManager;
import com.git.cs309.mmoserver.util.MathUtils;

public final class CombatManager {
	
	public static final void handleCombat(Character character) {
		Character opponent = ModuleManager.getModule(CharacterManager.class).getCharacter(character.getOpponentId());
		if (opponent == null) {
			character.resetCombat();
			return;
		}
		if (opponent.isDead()) {
			character.resetCombat();
		}
		if (character.isDead()) {
			opponent.resetCombat();
		}
		character.walkTo(opponent.getX(), opponent.getY());
		if (character.canAttack() && hasLineOfSight(character, opponent)) {
			opponent.applyDamage(calculateDamage(character, opponent));
			character.resetAttackTimer();
		}
	}
	
	private final static int calculateDamage(Character attacker, Character target) {
		return (int) (5 * Math.random());
	}
	
	private static final boolean hasLineOfSight(Character attacker, Character target) {
		if (attacker.getCombatStyle() == CombatStyle.MAGIC || attacker.getCombatStyle() == CombatStyle.RANGED) {
			return true;
		}
		return MathUtils.distance(attacker.getX(), attacker.getY(), target.getX(), target.getY()) < 2;
	}
}
