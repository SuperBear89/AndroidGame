package net.jumpingman.framework.impl;

import net.jumpingman.framework.Game;
import net.jumpingman.framework.Screen;

public abstract class GLScreen extends Screen {
	protected final GLGraphics glGraphics;
	protected final GLGame glGame;
	protected final int width;
	protected final int height;
	
	
	public GLScreen(Game game, int width, int height) {
		super(game);
		glGame = (GLGame)game;
		glGraphics = ((GLGame)game).getGLGraphics();
		this.width = width;
		this.height = height;
	}
}