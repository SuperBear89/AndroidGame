package net.jumpingman.game;

import net.jumpingman.framework.GameObject;

public class Block extends GameObject{
	public static final float WIDTH = 1f;
	public static final float HEIGHT = 1f;
	
	public Block(float x, float y){
		super(x, y, WIDTH, HEIGHT);
	}
}
