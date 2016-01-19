package net.jumpingman.framework;

import net.jumpingman.framework.math.Line;
import net.jumpingman.framework.math.Vector2;

public class DynamicGameObject extends GameObject {
	public final float height;
	public final float width;
	public final Vector2 velocity;
	private final Vector2 deltaVelocity;
	public final Vector2 accel;
	public Line[] paths;
	
	public static final int LOWER_LEFT = 0;
	public static final int LOWER_RIGHT = 1;
	public static final int UPPER_RIGHT = 2;
	public static final int UPPER_LEFT = 3;

	public DynamicGameObject(float x, float y, float width, float height) {
		super(x, y, width, height);
		this.height = height;
		this.width = width;
		velocity = new Vector2();
		deltaVelocity = new Vector2();
		accel = new Vector2();
		
		paths = new Line[4];
		paths[LOWER_LEFT] = new Line();
		paths[LOWER_RIGHT] = new Line();
		paths[UPPER_RIGHT] = new Line();
		paths[UPPER_LEFT] = new Line();
	}
	
	public void updatePos(float deltaTime){
		deltaVelocity.x = velocity.x * deltaTime;
		deltaVelocity.y = velocity.y * deltaTime;	
		position.x += deltaVelocity.x;
		position.y += deltaVelocity.y;
		bounds.updatePos(position.x, position.y);
	}
	
	public void updatePaths(float deltaTime){
		deltaVelocity.x = velocity.x * deltaTime;
		deltaVelocity.y = velocity.y * deltaTime;	
		paths[LOWER_LEFT].set(bounds.lowerLeft, deltaVelocity);
		paths[LOWER_RIGHT].set(bounds.lowerRight, deltaVelocity);
		paths[UPPER_RIGHT].set(bounds.upperRight, deltaVelocity);
		paths[UPPER_LEFT].set(bounds.upperLeft, deltaVelocity);
	}
	
	public void hitGround()
	{
		
	}
}