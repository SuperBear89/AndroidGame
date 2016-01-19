package net.jumpingman.game;

import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import android.view.Display;

import net.jumpingman.framework.Game;
import net.jumpingman.framework.GameObject;
import net.jumpingman.framework.Input.TouchEvent;
import net.jumpingman.framework.gl.Camera2D;
import net.jumpingman.framework.gl.SpriteBatcher;
import net.jumpingman.framework.impl.GLScreen;
import net.jumpingman.framework.math.OverlapTester;
import net.jumpingman.framework.math.Vector2;

public class MainMenuScreen extends GLScreen {

	Camera2D guiCam;
	Vector2 touchPoint;
	SpriteBatcher batcher;
	
	GameObject buttonNone;
	GameObject buttonKnight;
	
	public MainMenuScreen(Game game, int width, int height) {
		super(game, width, height);
		
		// TODO Auto-generated constructor stub
		batcher = new SpriteBatcher(glGraphics, 3000);
		guiCam = new Camera2D(glGraphics, width, height);
		buttonNone = new GameObject(width*0.25f, height/2, 128, 128);
		buttonKnight = new GameObject(width*0.75f, height/2, 128, 128);
	}

	@Override
	public void update(float deltaTime) {
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
		int len = touchEvents.size();
		for(int i = 0; i < len; i++){
			TouchEvent event = touchEvents.get(i);
			if(event.type == TouchEvent.TOUCH_UP){
				//todo: add a loading screen?
				if(OverlapTester.pointInRectangle(buttonNone.bounds, event.x, event.y)){
					Settings.hat = Settings.HAT_NONE;
					Assets.loadInGame(glGame);
					JumpingMan.currentScreen = 1;
					game.setScreen(new GameScreen(game, width, height));
				}else if(OverlapTester.pointInRectangle(buttonKnight.bounds, event.x, event.y)){
					Settings.hat = Settings.HAT_KNIGHT;
					Assets.loadInGame(glGame);
					JumpingMan.currentScreen = 1;
					game.setScreen(new GameScreen(game, width, height));
				}
			}
		}
	}

	@Override
	public void present(float deltaTime) {
		GL10 gl = glGraphics.getGL();
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		gl.glEnable(GL10.GL_TEXTURE_2D);
		
		gl.glClearColor(0f, 0f, 0f, 0f);
		
		guiCam.setViewportAndMatrices();

		batcher.beginBatch(Assets.buttons);
		batcher.drawSprite(buttonNone.position.x, buttonNone.position.y, buttonNone.bounds.width, buttonNone.bounds.height, Assets.buttonNone);
		batcher.drawSprite(buttonKnight.position.x, buttonKnight.position.y, buttonKnight.bounds.width, buttonKnight.bounds.height, Assets.buttonKnight);
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
