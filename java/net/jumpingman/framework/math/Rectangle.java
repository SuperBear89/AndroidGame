package net.jumpingman.framework.math;

public class Rectangle {
	public Vector2 lowerLeft;
	public Vector2 lowerRight;
	public Vector2 upperLeft;
	public Vector2 upperRight;
	public float width, height;
	private float half_width, half_height;

	public Rectangle(float x, float y, float width, float height) {
		this.lowerLeft = new Vector2(x,y);
		this.lowerRight = new Vector2(x + width, y);
		this.upperLeft = new Vector2(x, y + height);
		this.upperRight = new Vector2(x + width, y + height);
		this.width = width;
		this.height = height;
		this.half_width = width/2;
		this.half_height = height/2;
	}
	
	public void updatePos(float x, float y)
	{
		this.lowerLeft.x = x - half_width;
		this.lowerLeft.y = y - half_height;
		this.lowerRight.x = x + half_width;
		this.lowerRight.y = y - half_height;
		this.upperLeft.x = x - half_width;
		this.upperLeft.y = y + half_height;
		this.upperRight.x = x + half_width;
		this.upperRight.y = y + half_height;
	}
}