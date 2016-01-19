package net.jumpingman.framework.math;

public class Line {
	public Vector2 pointA;
	public Vector2 pointB;

	public Line(){
		this(0, 0, 0, 0);
	}
	
	public Line(Vector2 pointA, Vector2 pointB){
		this.pointA = pointA;
		this.pointB = pointB;		
	}

	public Line(float x1, float y1, float x2, float y2){
		this.pointA = new Vector2(x1, y1);
		this.pointB = new Vector2(x2, y2);
	}
	
	public void set(Vector2 pointA, Vector2 vector){
		this.pointA = pointA;
		this.pointB.x = pointA.x + vector.x;
		this.pointB.y = pointA.y + vector.y;
	}
}
