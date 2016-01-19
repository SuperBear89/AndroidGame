package net.jumpingman.framework.gl;

import java.util.LinkedList;


public class FPSCounter {
	public int fps = 0;
	LinkedList<Long> frames = new LinkedList<Long>();
	
	public void logFrame(){
		long time = System.nanoTime();
		frames.addLast(time);
		fps++;
		while(!frames.isEmpty() && frames.getFirst() < time - 1000000000){
			frames.removeFirst();
			fps--;
		}
	}
}
