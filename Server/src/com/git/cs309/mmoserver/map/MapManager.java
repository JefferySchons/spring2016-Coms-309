package com.git.cs309.mmoserver.map;

import java.util.HashSet;
import java.util.Set;

import com.git.cs309.mmoserver.Config;
import com.git.cs309.mmoserver.entity.Entity;
import com.git.cs309.mmoserver.lang.module.Module;

public final class MapManager extends Module {

	private final Set<Map> maps = new HashSet<>();

	public MapManager() {
		
	}

	public final Entity getEntityAtPosition(final int instanceNumber, final int x, final int y, final int z) {
		Map map = getMapContainingPosition(instanceNumber, x, y, z);
		if (map == null) {
			return null;
		}
		return map.getEntity(x, y);
	}

	public final Map getMapContainingPosition(final int instanceNumber, final int x, final int y, final int z) {
		for (Map map : maps) {
			if (map.getZ() == z && map.containsPoint(x, y) && map.getInstanceNumber() == instanceNumber) {
				return map;
			}
		}
		throw new RuntimeException("No map for position.");
	}
	
	public final boolean mapExists(final int instanceNumber, final String mapName) {
		for (Map map : maps) {
			if (map.getInstanceNumber() == instanceNumber && map.getName().equals(mapName)) {
				return true;
			}
		}
		return false;
	}
	
	public final Map getMapContainingEntity(final Entity entity) {
		for (Map map : maps) {
			if (map.getZ() == entity.getZ() && map.containsPoint(entity.getX(), entity.getY()) && map.getInstanceNumber() == entity.getInstanceNumber()) {
				return map;
			}
		}
		throw new RuntimeException("Entity is not in a map.");
	}

	public final void loadMaps() {
		maps.clear();
		addMap(MapFactory.getInstance().createMap("island", Config.GLOBAL_INSTANCE));
	}
	
	public final Map createInstanceMap(int instanceNumber, String mapName) {
		Map map = MapFactory.getInstance().createMap(mapName, instanceNumber);
		if (map == null) {
			throw new RuntimeException("No map definition for map name \""+mapName+"\"");
		}
		return map;
	}

	public final boolean moveEntity(final int uniqueId, final int oInstanceNumber, final int oX, final int oY, final int oZ,
			final int dInstanceNumber, final int dX, final int dY, final int dZ) {
		Map map = getMapContainingPosition(oInstanceNumber, oX, oY, oZ);
		if (!map.equals(getMapContainingPosition(dInstanceNumber, dX, dY, dZ))) {
			Map newMap = getMapContainingPosition(dInstanceNumber, dX, dY, dZ);
			Entity e = map.getEntity(uniqueId, oX, oY);
			map.removeEntity(oX, oY, e);
			newMap.putEntity(dX, dY, e);
			return true;
		}
		if (map.walkable(dX, dY)) {
			map.moveEntity(uniqueId, oX, oY, dX, dY);
			return true;
		}
		return false;
	}

	public final void putEntityAtPosition(final int instanceNumber, final int x, final int y, final int z,
			final Entity entity) {
		Map map = getMapContainingPosition(instanceNumber, x, y, z);
		if (map == null) {
			return;
		}
		map.putEntity(x, y, entity);
	}

	public final void removeEntityAtPosition(final int instanceNumber, final int x, final int y, final int z, final Entity entity) {
		Map map = getMapContainingPosition(instanceNumber, x, y, z);
		if (map == null) {
			return;
		}
		assert (map.getEntity(x, y) != null);
		map.removeEntity(x, y, entity);
	}

	final void addMap(Map map) {
		if (maps.contains(map)) {
			return;
		}
		maps.add(map);
		map.loadSpawns();
	}

	final void removeMap(Map map) {
		if (maps.contains(map))
			maps.remove(map);
	}

	@Override
	public String getVariableName() {
		return "MapManager";
	}
}
