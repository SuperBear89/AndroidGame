package net.jumpingman.framework.impl;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.view.Window;
import android.view.WindowManager;

import net.jumpingman.framework.Audio;
import net.jumpingman.framework.FileIO;
import net.jumpingman.framework.Game;
import net.jumpingman.framework.Graphics;
import net.jumpingman.framework.Input;
import net.jumpingman.framework.Screen;

public class GLGame extends Activity implements Game, Renderer{
	enum GLGameState {
		Initialized,
		Running,
		Paused,
		Finished,
		Idle
	}

	GLSurfaceView glView;
	GLGraphics glGraphics;
	//Audio audio;
	Input input;
	FileIO fileIO;
	Screen screen;
	GLGameState state = GLGameState.Initialized;
	Object stateChanged = new Object();
	long startTime = System.nanoTime();
	WakeLock wakeLock;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		glView = new GLSurfaceView(this);
		glView.setRenderer(this);
		setContentView(glView);

		glGraphics = new GLGraphics(glView);
		fileIO = new AndroidFileIO(getAssets());
		//audio = new AndroidAudio(this);
		input = new AndroidInput(this, glView, 1, 1);
		PowerManager powerManager = (PowerManager)
		getSystemService(Context.POWER_SERVICE);
		wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "GLGame");
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
	    super.onConfigurationChanged(newConfig);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
	}
	public void onResume() {
		super.onResume();
		glView.onResume();
		wakeLock.acquire();
	}

	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		glGraphics.setGL(gl);
		synchronized(stateChanged) {
			if(state == GLGameState.Initialized)
				screen = getStartScreen();
			state = GLGameState.Running;
			screen.resume();
			startTime = System.nanoTime();
		}
	}

	public void onSurfaceChanged(GL10 gl, int width, int height) {
	}

	public void onDrawFrame(GL10 gl) {
		GLGameState state = null;
		synchronized(stateChanged) {
			state = this.state;
		}
		if(state == GLGameState.Running) {
			float deltaTime = (System.nanoTime()-startTime) / 1000000000.0f;
			startTime = System.nanoTime();
			screen.update(deltaTime);
			screen.present(deltaTime);
		}
		if(state == GLGameState.Paused) {
			screen.pause();
			synchronized(stateChanged) {
				this.state = GLGameState.Idle;
				stateChanged.notifyAll();
			}
		}
		if(state == GLGameState.Finished) {
			screen.pause();
			screen.dispose();
			synchronized(stateChanged) {
				this.state = GLGameState.Idle;
				stateChanged.notifyAll();
			}
		}
	}

	@Override
	public void onPause() {
		synchronized(stateChanged) {
			if(isFinishing())
				state = GLGameState.Finished;
			else
				state = GLGameState.Paused;
		}
		wakeLock.release();
		glView.onPause();
		super.onPause();
	}

	public Input getInput() {
		return input;
	}

	public FileIO getFileIO() {
		return fileIO;
	}

	public Graphics getGraphics() {
		throw new IllegalStateException("We are using OpenGL!");
	}


	public Audio getAudio() {
		return null;
	}

	public void setScreen(Screen screen) {
		if (screen == null)
			throw new IllegalArgumentException("Screen must not be null");
		
		this.screen.pause();
		this.screen.dispose();
		screen.resume();
		screen.update(0);
		this.screen = screen;
	}
	
	public Screen getCurrentScreen() {
		return screen;
	}

	public Screen getStartScreen() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public GLGraphics getGLGraphics(){
		return glGraphics;
	}
}