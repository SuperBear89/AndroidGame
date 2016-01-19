package net.jumpingman.game;

import java.util.Random;

import net.jumpingman.framework.DynamicGameObject;

public class Enemy extends DynamicGameObject {

	public static final int STATE_STAND = 0;
	public static final int STATE_WALKING = 1;
	public static final int STATE_KICKED = 2;
	public static final int STATE_CHARGING = 3;
	public static final int STATE_FALLING = 4;
	private int MAX_STATE_TIME;
	
	public static final int DISTANCE_DETECTION = 6;
	public static final float WALKING_SPEED = 1;
	public static final float CHARGING_SPEED = 10;
	public static final float CHARGING_ACCELERATION = 10;
	
	public int state;
	public float stateTime;
	public boolean direction;//Left false, right true
	
	private Random rng;
	
	public Enemy(float x, float y) {
		super(x, y, 1, 2.9f);
		rng = new Random();
		state = STATE_STAND;
		stateTime = 0;
		MAX_STATE_TIME = rng.nextInt(2) + 1;
	}
	
	public void update(float deltaTime){
		updatePos(deltaTime);
		stateTime += deltaTime;
		if(state == STATE_STAND || state == STATE_WALKING){
			if(stateTime > MAX_STATE_TIME){
				switchState(rng.nextInt(2));
				if(state == STATE_WALKING)
					direction = rng.nextBoolean();
			}
		}
		
		switch(state){
			case STATE_STAND:
				velocity.x = 0;
				break;
				
			case STATE_WALKING:
				if(direction)
					velocity.x = WALKING_SPEED;
				else
					velocity.x = -WALKING_SPEED;
				break;
				
			case STATE_CHARGING:
				if(direction)
						velocity.x += CHARGING_ACCELERATION * deltaTime;
				else
						velocity.x -= CHARGING_ACCELERATION * deltaTime;
				break;
			
			case STATE_FALLING:
				velocity.add(World.GRAVITY);
			default:
				break;
		}
	}

	private void switchState(int state){
		this.state = state;
		stateTime = 0;
	}
	
	public void kicked(boolean direction){
		if(state != STATE_KICKED)
		{
			switchState(STATE_KICKED);
			this.direction = direction;
			velocity.y = 32;
			if(direction){
				velocity.x = 16;
			}else{
				velocity.x = -16;
			}
		}
	}

	public void hitGround(){
		if(state == STATE_KICKED){
			switchState(STATE_STAND);
		}
	}
	
	public void charge(boolean direction){
		switchState(STATE_CHARGING);
		this.direction = direction;
		velocity.y = 12;
		if(direction)
			velocity.x = -8;
		else
			velocity.x = 8;
	}
	
	public void stopCharging(){
		switchState(STATE_STAND);
		velocity.x = 0;
	}
	
	public void fall(){
		switchState(STATE_FALLING);
	}
}
