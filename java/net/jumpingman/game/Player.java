package net.jumpingman.game;

import android.view.VelocityTracker;
import net.jumpingman.framework.DynamicGameObject;

public class Player extends DynamicGameObject{
	public static final float WIDTH = 1.4f;
	public static final float HEIGHT = 2.8f;
	public static final float WALKING_ACCELERATION = 40;
	public static final float MAX_WALKING_SPEED = 14;
	public static final float JUMP_VELOCITY = 20;
	public static final float KICK_VELOCITY = 20;
	
	public static final int STATE_STANDING = 0;
	public static final int STATE_WALKING = 1;
	public static final int STATE_SLOWING_DOWN = 2;
	public static final int STATE_JUMPING = 3;
	public static final int STATE_JUMPING_GLIDE = 4;
	public static final int STATE_ATTACKING_MELEE = 5;
	public static final int STATE_ATTACKING_SPECIAL = 6;
	public static final int STATE_FALLING = 7;
	public static final int STATE_FALLING_GLIDE = 8;
	public static final int STATE_HURT = 9;
	
	public int state;
	public float stateTime;
	public boolean direction;//Left false, right true
	private boolean isWalking;
	private boolean walkingDirection;
	
	public Player(float x, float y){
		super(x, y, WIDTH, HEIGHT);
		direction = true;
		isWalking = false;
		switchState(STATE_FALLING);
	}
	
	public void update(float deltaTime){
		updatePos(deltaTime);

		switch(state){
		case STATE_WALKING:
			if(direction)
			{
				if(velocity.x < MAX_WALKING_SPEED)
					velocity.x += WALKING_ACCELERATION*deltaTime;
			}
			else
			{
				if(velocity.x > -MAX_WALKING_SPEED)
					velocity.x -= WALKING_ACCELERATION*deltaTime;
			}
			break;
			
		case STATE_SLOWING_DOWN:
			if(direction)
			{
				if(velocity.x > 0.1f)
					velocity.x -= WALKING_ACCELERATION*deltaTime;
				else{
					velocity.x = 0;
					switchState(STATE_STANDING);
				}
			}
			else
			{
				if(velocity.x < -0.1f)
					velocity.x += WALKING_ACCELERATION*deltaTime;
				else{
					velocity.x = 0;
					switchState(STATE_STANDING);
				}
			}
			break;

		case STATE_JUMPING_GLIDE:
			if(direction)
			{
				if(velocity.x < MAX_WALKING_SPEED)
					velocity.x += WALKING_ACCELERATION*deltaTime;
			}
			else
			{
				if(velocity.x > -MAX_WALKING_SPEED)
					velocity.x -= WALKING_ACCELERATION*deltaTime;
			}
			velocity.add(World.GRAVITY.x * deltaTime, World.GRAVITY.y * deltaTime);
			break;
			
		case STATE_FALLING_GLIDE:
			velocity.add(World.GRAVITY.x * deltaTime, World.GRAVITY.y * deltaTime);
			if(direction)
			{
				if(velocity.x < MAX_WALKING_SPEED)
					velocity.x += WALKING_ACCELERATION*deltaTime;
			}
			else
			{
				if(velocity.x > -MAX_WALKING_SPEED)
					velocity.x -= WALKING_ACCELERATION*deltaTime;
			}
			break;
			
		case STATE_HURT:
			velocity.add(World.GRAVITY.x * deltaTime, World.GRAVITY.y * deltaTime);
			break;
			
		default:
			velocity.add(World.GRAVITY.x * deltaTime, World.GRAVITY.y * deltaTime);
			break;
		}
		stateTime += deltaTime;
	}

	private void switchState(int state){
		this.state = state;
		stateTime = 0;
	}
	
	public void startWalking(boolean direction){
		isWalking = true;
		walkingDirection = direction;
		if(state == STATE_JUMPING || state == STATE_JUMPING_GLIDE){
			this.direction = direction;
			switchState(STATE_JUMPING_GLIDE);
		}else if(state == STATE_FALLING || state == STATE_FALLING_GLIDE){
			this.direction = direction;
			switchState(STATE_FALLING_GLIDE);
		}else if(state == STATE_WALKING || state == STATE_STANDING || state == STATE_SLOWING_DOWN){
			this.direction = direction;
			switchState(STATE_WALKING);
		}
	}
	
	public void stopWalking(){
		isWalking = false;
		if(state == STATE_WALKING){
			switchState(STATE_SLOWING_DOWN);
		}
		else if(state == STATE_JUMPING_GLIDE){
			switchState(STATE_JUMPING);
		}
		else if(state == STATE_FALLING_GLIDE){
			switchState(STATE_FALLING);
		}
	}
	
	public void jump(){
		if(state == STATE_STANDING || state == STATE_WALKING || state == STATE_SLOWING_DOWN){
			velocity.y += JUMP_VELOCITY;
			if(state == STATE_WALKING)
				switchState(STATE_JUMPING_GLIDE);
			else
				switchState(STATE_JUMPING);
		}
	}
	
	public void attackSpecial(){
		if(state == STATE_STANDING || state == STATE_WALKING || state == STATE_SLOWING_DOWN){
			switchState(STATE_ATTACKING_SPECIAL);
			switch(Settings.hat){
			case Settings.HAT_NONE:
				velocity.y += JUMP_VELOCITY/2;
				if(direction)
					velocity.x = KICK_VELOCITY;
				else
					velocity.x = -KICK_VELOCITY;
				break;
				
			case Settings.HAT_KNIGHT:
				velocity.y += 5;
				if(direction)
					velocity.x = 26;
				else
					velocity.x = -26;
				break;
			}
		}
	}
	
	public void attackMelee(){
		if(state == STATE_STANDING || state == STATE_WALKING || state == STATE_SLOWING_DOWN){
			switchState(STATE_ATTACKING_MELEE);
			velocity.y += 4;
		}
	}
	
	@Override
	public void hitGround()
	{
		switch(state){
		case STATE_JUMPING:
			switchState(STATE_SLOWING_DOWN);
			break;
			
		case STATE_JUMPING_GLIDE:
			switchState(STATE_WALKING);
			break;
			
		case STATE_ATTACKING_SPECIAL:
			if(velocity.x > MAX_WALKING_SPEED){
				velocity.x = MAX_WALKING_SPEED;
			}else if(velocity.x < -MAX_WALKING_SPEED){
				velocity.x = -MAX_WALKING_SPEED;
			}
			if(isWalking){
				switchState(STATE_WALKING);
				direction = walkingDirection;
			}else{
				switchState(STATE_SLOWING_DOWN);
			}
			break;
			
		case STATE_FALLING:
			switchState(STATE_SLOWING_DOWN);
			break;
		
		case STATE_FALLING_GLIDE:
			switchState(STATE_WALKING);
			break;
			
		case STATE_HURT:
			if(isWalking){
				switchState(STATE_WALKING);
				direction = walkingDirection;
			}else{
				switchState(STATE_SLOWING_DOWN);
			}
			break;
			
		case STATE_ATTACKING_MELEE:
			if(isWalking){
				switchState(STATE_WALKING);
				direction = walkingDirection;
			}else{
				switchState(STATE_SLOWING_DOWN);
			}
			break;
		}
	}
	
	public void fall(){
		if(state == STATE_WALKING)
			switchState(STATE_FALLING_GLIDE);
		else
			switchState(STATE_FALLING);
	}
	

	public void kick(boolean direction){
		if(state != STATE_HURT){
			switchState(STATE_HURT);
	
			this.direction = direction;
			velocity.y = 12;
			if(direction)
				velocity.x = 8;
			else
				velocity.x = -8;
		}
	}
}