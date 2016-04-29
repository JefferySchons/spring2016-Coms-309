package com.git.cs309.mmoclient.map;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.git.cs309.mmoclient.Client;
import com.git.cs309.mmoclient.gui.game.ViewPanel;

public final class MapDefinition implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3174752973855574603L;
	private final String mapName;
	private final int width;
	private final int height;
	private final int xOrigin;
	private final int yOrigin;
	private final List<Tile> backgroundTiles;

	public MapDefinition(final String mapName, final int xOrigin, final int yOrigin, final int width,
			final int height, final Set<Tile> backgroundTiles) {
		this.mapName = mapName;
		this.xOrigin = xOrigin;
		this.yOrigin = yOrigin;
		this.width = width;
		this.height = height;
		this.backgroundTiles = Arrays.asList(backgroundTiles.toArray(new Tile[backgroundTiles.size()]));
	}
	
	public void paint(Graphics g) {
		backgroundTiles.sort(new Comparator<Tile>() {

			@Override
			public int compare(Tile arg0, Tile arg1) {
				return arg1.compareTo(arg0);
			}
			
		}); 
		Graphics2D g2d = (Graphics2D) g;
		AffineTransform trans = g2d.getTransform();
		AffineTransform oldTrans = g2d.getTransform();
		trans.setToIdentity();
		trans.rotate(Client.getRotation(), ViewPanel.getInstance().getWidth() / 2, ViewPanel.getInstance().getHeight() / 2);
		g2d.setTransform(trans);
		for (Tile tile : backgroundTiles) {
			tile.paint(g2d);
		}
		g2d.setTransform(oldTrans);
	}

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof MapDefinition)) {
			return false;
		}
		MapDefinition map = (MapDefinition) other;
		return map.mapName.equalsIgnoreCase(mapName) && map.xOrigin == xOrigin && map.yOrigin == yOrigin && map.width == width
				&& map.height == height;
	}

	public int getHeight() {
		return height;
	}

	public final String getMapName() {
		return mapName;
	}

	public final int getWidth() {
		return width;
	}

	public int getXOrigin() {
		return xOrigin;
	}

	public int getYOrigin() {
		return yOrigin;
	}
}
