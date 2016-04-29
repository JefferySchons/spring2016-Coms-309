package com.git.cs309.mmoserver.util;

public class GameCoordinate {
	protected final int x;
	protected final int y;
	protected final int z;
	protected final int instanceNumber;
	
	public GameCoordinate(final int x, final int y, final int z, final int instanceNumber) {
		this.x = x;
		this.y = x;
		this.z = z;
		this.instanceNumber = instanceNumber;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public int getZ() {
		return z;
	}
	
	public int getInstanceNumber() {
		return instanceNumber;
	}
}
