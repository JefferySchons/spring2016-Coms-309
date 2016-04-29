package com.git.cs309.mmoserver.packets;

import com.git.cs309.mmoserver.connection.Connection;
import com.git.cs309.mmoserver.entity.characters.user.PlayerCharacter;

public final class EntityClickHandler {
	public static final void handlePacket(EntityClickPacket packet) {
		PlayerCharacter playerCharacter = ((Connection) packet.getConnection()).getUser().getCurrentCharacter();
		System.out.println("Character "+playerCharacter.getName()+" wants to walk to "+packet.getEntityX()+", "+packet.getEntityY());
		playerCharacter.walkTo(packet.getEntityX(), packet.getEntityY());
	}
}
