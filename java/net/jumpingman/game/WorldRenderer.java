package net.jumpingman.game;

import net.jumpingman.framework.gl.Animation;
import net.jumpingman.framework.gl.EulerCamera;
import net.jumpingman.framework.gl.SpriteBatcher3D;
import net.jumpingman.framework.gl.TextureRegion;
import net.jumpingman.framework.impl.GLGraphics;
import net.jumpingman.framework.math.Vector2;

//Classe qui dessine le jeu
public class WorldRenderer {
	static final float FRUSTUM_WIDTH = 25;
	static final float FRUSTUM_HEIGHT = 15;
	GLGraphics glGraphics;
	World world;
	EulerCamera cam;
	SpriteBatcher3D batcher;
	
	public int items;
	
	public WorldRenderer(GLGraphics glGraphics, SpriteBatcher3D batcher, World world) {
		super();
		this.glGraphics = glGraphics;
		this.world = world;
		this.cam = new EulerCamera(67, glGraphics.getWidth() / (float)glGraphics.getHeight(), 1, 100);//new Camera2D(glGraphics, FRUSTUM_WIDTH, FRUSTUM_HEIGHT);
		this.batcher = batcher;
	}
	
	public void render(){
		cam.getPosition().x = world.player.position.x;
		cam.getPosition().y = world.player.position.y;
		cam.getPosition().z = 10 ;//10 + Math.abs(world.player.velocity.x)/2;
		
		items = 0;
		
		glGraphics.getGL().glClearColor(0.5f, 0.5f, 0.5f, 0f);
		
		cam.setMatrices(glGraphics.getGL());
		renderSolidBlocks();
		renderPlayer();
		renderEnemys();
	}	
	
	private void renderSolidBlocks(){
		batcher.beginBatch(Assets.testBlocks);
		for (int j = 0; j < world.lines.length; j++){ 
			for (int i = 0; i < world.lines[j].size(); i++){
				Vector2 pointA = world.lines[j].get(i).pointA;
				Vector2 pointB = world.lines[j].get(i).pointB;

				batcher.drawSprite(pointA.x, pointA.y, -1f, 0.2f, 0.2f, Assets.testBlock3);
				items++;
				batcher.drawSprite(pointB.x, pointB.y, -1f, 0.2f, 0.2f, Assets.testBlock3);
				items++;
			}
		}
		for (int i = 0; i < world.blocks.size(); i++){
			batcher.drawSprite(world.blocks.get(i).position.x, world.blocks.get(i).position.y, 0, Block.WIDTH, Block.HEIGHT, Assets.testBlock2);
			items++;
		}
		for (int j = 0; j < world.lines.length; j++){ 
			for (int i = 0; i < world.lines[j].size(); i++){
				Vector2 pointA = world.lines[j].get(i).pointA;
				Vector2 pointB = world.lines[j].get(i).pointB;

				batcher.drawSprite(pointA.x, pointA.y, 0, 0.2f, 0.2f, Assets.testBlock3);
				items++;
				batcher.drawSprite(pointB.x, pointB.y, 0, 0.2f, 0.2f, Assets.testBlock3);
				items++;
			}
		}
		batcher.endBatch();
	}

	private void renderPlayer(){
		TextureRegion keyFrame;
		batcher.beginBatch(Assets.jmPlaceHolder);
		int dir = (world.player.direction) ? 4 : -4;
		if(world.player.state != Player.STATE_ATTACKING_SPECIAL && world.player.state != Player.STATE_ATTACKING_MELEE){			
			switch(world.player.state){
			case Player.STATE_STANDING:
				keyFrame = Assets.jmStand.getKeyFrame(world.player.stateTime, Animation.ANIMATION_LOOPING);
				batcher.drawSprite(world.player.position.x, world.player.position.y, -0.5f, dir, 4, keyFrame);
				break;
				
			case Player.STATE_WALKING:
				keyFrame = Assets.jmWalk.getKeyFrame(world.player.stateTime, Animation.ANIMATION_LOOPING);
				batcher.drawSprite(world.player.position.x, world.player.position.y, -0.5f, dir, 4, keyFrame);
				break;
				
			case Player.STATE_SLOWING_DOWN:
				keyFrame = Assets.jmWalk.getKeyFrame(world.player.stateTime, Animation.ANIMATION_LOOPING);
				batcher.drawSprite(world.player.position.x, world.player.position.y, -0.5f, dir, 4, keyFrame);
				break;
				
			case Player.STATE_JUMPING:
				keyFrame = Assets.jmJump.getKeyFrame(world.player.stateTime, Animation.ANIMATION_LOOPING);
				batcher.drawSprite(world.player.position.x, world.player.position.y, -0.5f, dir, 4, keyFrame);
				break;
				
			case Player.STATE_JUMPING_GLIDE:
				keyFrame = Assets.jmJump.getKeyFrame(world.player.stateTime, Animation.ANIMATION_LOOPING);
				batcher.drawSprite(world.player.position.x, world.player.position.y, -0.5f, dir, 4, keyFrame);
				break;
				
			case Player.STATE_FALLING:
				batcher.drawSprite(world.player.position.x, world.player.position.y, -0.5f, dir, 4, Assets.jmFall);
				break;
				
			case Player.STATE_FALLING_GLIDE:
				batcher.drawSprite(world.player.position.x, world.player.position.y, -0.5f, dir, 4, Assets.jmFall);
				break;
				
			case Player.STATE_HURT:
				keyFrame = Assets.jmHurt.getKeyFrame(world.player.stateTime, Animation.ANIMATION_LOOPING);
				batcher.drawSprite(world.player.position.x, world.player.position.y, -0.5f, -dir, 4, keyFrame);
				break;
				
			}
			items++;
			batcher.endBatch();
		}else{
			if(Settings.hat == Settings.HAT_NONE){
				batcher.beginBatch(Assets.jmPlaceHolder);
				keyFrame = Assets.jmAttackSpecial.getKeyFrame(world.player.stateTime, Animation.ANIMATION_LOOPING);
				batcher.drawSprite(world.player.position.x, world.player.position.y, -0.5f, dir, 4, keyFrame);
				batcher.endBatch();
			}else if(Settings.hat == Settings.HAT_KNIGHT){
				batcher.beginBatch(Assets.jmAttack);
				if(world.player.state == Player.STATE_ATTACKING_SPECIAL){
					keyFrame = Assets.jmAttackSpecial.getKeyFrame(world.player.stateTime, Animation.ANIMATION_NONLOOPING);
				}else{
					keyFrame = Assets.jmAttackMelee.getKeyFrame(world.player.stateTime, Animation.ANIMATION_NONLOOPING);
				}
				batcher.drawSprite(world.player.position.x, world.player.position.y, -0.5f, dir*2, 8, keyFrame);
				batcher.endBatch();
			}
		}
	}
	
	private void renderEnemys(){
		if (!world.enemies.isEmpty()){
			Enemy currentEnemy;
			TextureRegion keyFrame;
			batcher.beginBatch(Assets.bunny);
			for(int i = 0; i < world.enemies.size(); i++){
				currentEnemy = world.enemies.get(i);
				int dir = (currentEnemy.direction) ? -4 : 4;
				if(currentEnemy.state == Enemy.STATE_WALKING){
					keyFrame = Assets.bunnyWalk.getKeyFrame(currentEnemy.stateTime, Animation.ANIMATION_LOOPING);
					batcher.drawSprite(currentEnemy.position.x, currentEnemy.position.y, 0, dir, 4, keyFrame);
				}else if (currentEnemy.state == Enemy.STATE_STAND){
					keyFrame = Assets.bunnyIdle.getKeyFrame(currentEnemy.stateTime, Animation.ANIMATION_LOOPING);
					batcher.drawSprite(currentEnemy.position.x, currentEnemy.position.y, 0, dir, 4, keyFrame);
				}else if (currentEnemy.state == Enemy.STATE_KICKED){
					batcher.drawSprite(currentEnemy.position.x, currentEnemy.position.y, 0, -dir, 4, Assets.bunnyKicked);
				}else if (currentEnemy.state == Enemy.STATE_CHARGING){
					keyFrame = Assets.bunnyCharge.getKeyFrame(currentEnemy.stateTime, Animation.ANIMATION_LOOPING);
					batcher.drawSprite(currentEnemy.position.x, currentEnemy.position.y, 0, dir, 4, keyFrame);
				}
				items++;
			}
			batcher.endBatch();
		}
	}
}
