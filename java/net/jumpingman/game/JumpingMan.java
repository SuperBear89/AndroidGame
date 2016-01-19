package net.jumpingman.game;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.graphics.Point;
import android.view.Display;

import net.jumpingman.framework.Screen;
import net.jumpingman.framework.impl.GLGame;

public class JumpingMan extends GLGame {
	boolean firstTimeCreate = true;
	public static int currentScreen = 0;
	
	@Override
	public Screen getStartScreen(){
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		return new MainMenuScreen(this, size.x, size.y);
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config){
		super.onSurfaceCreated(gl, config);
		if(firstTimeCreate) {
			Assets.loadMenu(this);
			firstTimeCreate = false;
		} else {
			if(currentScreen == 0)
				Assets.reloadMenu();
			else if(currentScreen == 1)
				Assets.reloadInGame();
		}
	}
	
	@Override
	public void onBackPressed() {
		if(currentScreen == 1){
			currentScreen = 0;
			Display display = getWindowManager().getDefaultDisplay();
			Point size = new Point();
			display.getSize(size);
			setScreen(new MainMenuScreen(this, size.x, size.y));
		}else{
			super.onBackPressed();
		}
	}
}