package net.jumpingman.game;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import javax.microedition.khronos.opengles.GL10;

import android.os.SystemClock;
import android.util.Log;

import net.jumpingman.framework.Game;
import net.jumpingman.framework.Input.TouchEvent;
import net.jumpingman.framework.gl.Camera2D;
import net.jumpingman.framework.gl.FPSCounter;
import net.jumpingman.framework.gl.SpriteBatcher;
import net.jumpingman.framework.gl.SpriteBatcher3D;
import net.jumpingman.framework.impl.GLScreen;
import net.jumpingman.framework.math.Vector2;
import net.jumpingman.game.World.WorldListener;

public class GameScreen extends GLScreen {
	static final int GAME_READY = 0;
	static final int GAME_RUNNING = 1;
	static final int GAME_PAUSED = 2;
	static final int GAME_OVER = 4;
	
	static final int CONTROLS_DRAG_TIME = 200;
	static final float CONTROLS_DRAG_LENGTH = 50;
	static final float CONTROLS_TOUCH_DELAY = 150;
	static final float CONTROLS_MELEE_ATTACK_DISTANCE = 230;
	
	int state;
	World world;
	WorldListener worldListener;

	Camera2D guiCam;
	Vector2 touchPoint;
	SpriteBatcher batcher;
	SpriteBatcher3D batcher3D;
	WorldRenderer renderer;
	FPSCounter fpsCounter;
	
	Stack<HoldEvent> playerMovementInput;
	LinkedList<DragEvent> playerDragInput;

	private class HoldEvent
	{
		public int pointer;
		public boolean direction;
		public long time;
	}

	private class DragEvent
	{
		public int x;
		public int y;
		public long time;
		
		public DragEvent(int x, int y, long time){
			this.x = x;
			this.y = y;
			this.time = time;
		}
	}
	
	public GameScreen(Game game, int width, int height){
		super(game, width, height);
		
		state = GAME_RUNNING;
		
		world = new World(worldListener);

		batcher = new SpriteBatcher(glGraphics, 3000);
		batcher3D = new SpriteBatcher3D(glGraphics, 3000);
		renderer = new WorldRenderer(glGraphics, batcher3D, world);
		guiCam = new Camera2D(glGraphics, 800, 480);
		fpsCounter = new FPSCounter();

		touchPoint = new Vector2();
		playerMovementInput = new Stack<HoldEvent>();
		playerDragInput = new LinkedList<DragEvent>();
	}
	
	@Override
	public void update(float deltaTime){
		if(deltaTime > 0.1f)
			deltaTime = 0.1f;
		switch(state){
		case GAME_READY:
			break;
		case GAME_RUNNING:
			updateRunning(deltaTime);
			break;
		case GAME_PAUSED:
			break;
		default:
			break;
		}
		fpsCounter.logFrame();
	}
	
	private void updateRunning(float deltaTime){
		//Player touch events
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
		int len = touchEvents.size();
		for(int i = 0; i < len; i++){
			TouchEvent event = touchEvents.get(i);
			switch(event.type){
				case TouchEvent.TOUCH_DOWN:
					HoldEvent newEvent = new HoldEvent(); //TODO: remove those nasty new while the game runs
					newEvent.pointer = event.pointer;
					newEvent.direction = (event.x > width/2);
					newEvent.time = event.time;
					playerMovementInput.push(newEvent);
					world.player.startWalking(newEvent.direction);
					Log.d("Controls", "HoldStart : " + event.pointer + ":(" + event.x + "," + event.y + ")");
					break;
					
				case TouchEvent.TOUCH_UP:
					int nbFinger = playerMovementInput.size();
					for (int a = 0; a < nbFinger; a++){
						if(playerMovementInput.get(a).pointer == event.pointer){
							if(playerMovementInput.get(a).time > event.time - CONTROLS_TOUCH_DELAY){
								if(200 < event.x && event.x < 600 &&
								   120 < event.y && event.y < 360 * 2){
									world.player.attackMelee();
								}
							}
							playerMovementInput.remove(a);
							Log.d("Controls", "HoldRelease : " + event.pointer + ":(" + event.x + "," + event.y + ")");
							break;
						}
					}
					if(playerMovementInput.isEmpty()){
						world.player.stopWalking();
					}
					else{
						world.player.startWalking(playerMovementInput.peek().direction);
					}
					break;
					
				case TouchEvent.TOUCH_DRAGGED:
					DragEvent dragEvent = new DragEvent(event.x, event.y, event.time); //TODO: Il y a surement moyen d'eviter cette alocation de memoire redondante
					playerDragInput.add(dragEvent);
					break;
			}
		}

		//flush drag inputs
		while(!playerDragInput.isEmpty() && playerDragInput.getFirst().time < (SystemClock.uptimeMillis() - CONTROLS_DRAG_TIME)){
			playerDragInput.removeFirst();
		}

		//compute swipe
		if(!playerDragInput.isEmpty()){
			Vector2 distance = new Vector2(playerDragInput.getLast().x - playerDragInput.getFirst().x, 
					playerDragInput.getFirst().y - playerDragInput.getLast().y);
			if(distance.len() >= CONTROLS_DRAG_LENGTH){
				distance.nor();
				if(distance.y > Math.abs(distance.x)){
					world.player.jump();
				}else{
					world.player.attackSpecial();
				}
				Log.d("Controls", "Jump!");
				playerDragInput.clear();
			}
		}
		world.update(deltaTime);
	}

	@Override
	public void present(float deltaTime) {
		GL10 gl = glGraphics.getGL();
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		gl.glEnable(GL10.GL_TEXTURE_2D);

		renderer.render();

		guiCam.setViewportAndMatrices();
		batcher.beginBatch(Assets.fonts);
		Assets.fontFps.drawText(batcher, "fps: " + fpsCounter.fps, 800 - 116, 480 - 26);
		Assets.fontFps.drawText(batcher, "items: " + renderer.items, 800 - 148, 480 - 58);
		Assets.fontFps.drawText(batcher, "pos: x(" + Math.round(world.player.bounds.lowerLeft.x) + ")", 800 - 148, 480 - 90);
		Assets.fontFps.drawText(batcher, "     y(" + Math.round(world.player.bounds.lowerLeft.y -0.5f) + ")", 800 - 148, 480 - 122);
		
		gl.glDisable(GL10.GL_BLEND);
		batcher.endBatch();
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub		
	}
}
