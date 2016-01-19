package net.jumpingman.framework.math;

public class OverlapTester {
	public static float overlapLineSegment(Line line1, Line line2){
		float ud, ua, ub;
		ud = (line2.pointB.y - line2.pointA.y) * (line1.pointB.x - line1.pointA.x) - 
			 (line2.pointB.x - line2.pointA.x) * (line1.pointB.y - line1.pointA.y);
		if (ud != 0){
			ua = ((line1.pointB.x - line1.pointA.x) * (line1.pointA.y - line2.pointA.y) - 
				  (line1.pointB.y - line1.pointA.y) * (line1.pointA.x - line2.pointA.x)) / ud;
			if (ua > 0 && ua < 1){
				ub = ((line2.pointB.x - line2.pointA.x) * (line1.pointA.y - line2.pointA.y) -
					  (line2.pointB.y - line2.pointA.y) * (line1.pointA.x - line2.pointA.x)) / ud;
				if (ub > 0 && ub < 1){
					return ub;
				}
			}
		}
		return 2;
	}

	public static boolean overlapCircles(Circle c1, Circle c2) {
		float distance = c1.center.distSquared(c2.center);
		float radiusSum = c1.radius + c2.radius;
		return distance <= radiusSum * radiusSum;
	}
	
	public static boolean overlapRectangles(Rectangle r1, Rectangle r2) {
		if(r1.lowerLeft.x < r2.lowerLeft.x + r2.width &&
				r1.lowerLeft.x + r1.width > r2.lowerLeft.x &&
				r1.lowerLeft.y < r2.lowerLeft.y + r2.height &&
				r1.lowerLeft.y + r1.height > r2.lowerLeft.y)
			return true;
		else
			return false;
	}
	
	public static boolean overlapCircleRectangle(Circle c, Rectangle r) {
		float closestX = c.center.x;
		float closestY = c.center.y;
		if(c.center.x < r.lowerLeft.x) {
			closestX = r.lowerLeft.x;
		}
		else if(c.center.x > r.lowerLeft.x + r.width) {
			closestX = r.lowerLeft.x + r.width;
		}
		if(c.center.y < r.lowerLeft.y) {
			closestY = r.lowerLeft.y;
		}
		else if(c.center.y > r.lowerLeft.y + r.height) {
			closestY = r.lowerLeft.y + r.height;
		}
		
		return c.center.distSquared(closestX, closestY) < c.radius * c.radius;
	}
	
	public static boolean pointInCircle(Circle c, Vector2 p) {
		return c.center.distSquared(p) < c.radius * c.radius;
	}
	
	public static boolean pointInCircle(Circle c, float x, float y) {
		return c.center.distSquared(x, y) < c.radius * c.radius;
	}
	
	public static boolean pointInRectangle(Rectangle r, Vector2 p) {
		return r.lowerLeft.x <= p.x && r.lowerLeft.x + r.width >= p.x &&
		       r.lowerLeft.y <= p.y && r.lowerLeft.y + r.height >= p.y;
	}
	
	public static boolean pointInRectangle(Rectangle r, float x, float y) {
		return r.lowerLeft.x <= x && r.lowerLeft.x + r.width >= x &&
			   r.lowerLeft.y <= y && r.lowerLeft.y + r.height >= y;
	}
}
