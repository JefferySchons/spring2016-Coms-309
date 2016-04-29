package com.git.cs309.mmoclient.graphics;

import java.awt.Image;
import java.awt.image.BufferedImage;

public final class Sprite {
	private final BufferedImage spriteImage;
	private final String spriteName;
	
	public Sprite(String spriteName, BufferedImage spriteImage) {
		this.spriteImage = spriteImage;
		this.spriteName = spriteName;
	}
	
	public BufferedImage getImage(int x, int y) {
		int width = this.spriteImage.getWidth() / 4;
		int height = this.spriteImage.getHeight() / 4;
		return spriteImage.getSubimage(x * width, y * width, width, height);
	}

	public final Image getImage() {
		return spriteImage;
	}
	
	public final String getName() {
		return spriteName;
	}
}
