package net.jumpingman.game;

import java.util.ArrayList;

import android.util.Log;

import net.jumpingman.framework.DynamicGameObject;
import net.jumpingman.framework.math.Line;
import net.jumpingman.framework.math.OverlapTester;
import net.jumpingman.framework.math.Vector2;

public class World {
	public interface WorldListener{
		public void jump();
	}
	
	public final WorldListener listener;
	
	public final Player player;
	public ArrayList<Enemy> enemies;
	public final ArrayList<Block> blocks;
	public final ArrayList<Line>[] lines;
	public final int[][] grid;
	
	public static final int UP = 0;
	public static final int RIGHT = 1;
	public static final int DOWN = 2;
	public static final int LEFT = 3;
	public static final float COLLISION_DISTANCE = 0.012f;
	public static final Vector2 GRAVITY = new Vector2(0, -24);

	public Vector2 testStartPos;
	private int x1, x2, y;
	
	public World(WorldListener listener){
		this.listener = listener;
		player = new Player(0, 0);
		enemies = new ArrayList<Enemy>();
		
		//Temporary world grid
		grid = new int[][]
				{{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
				 {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
				 {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
				 {0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
				 {0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
				 {0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
				 {0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
				 {0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
				 {0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
				 {0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
				 {0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0},
				 {0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0},
				 {0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0},
				 {0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 2, 0, 0, 0, 0, 0},
				 {0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0},
				 {0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0},
				 {0, 0, 0, 1, 0, 0, 0, 3, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0},
				 {0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0},
				 {0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0},
				 {0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0},
				 {0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0},
				 {0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
				 {0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
				 {0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
				 {0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
				 {0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
				 {0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0},
				 {0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0},
				 {0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0},
				 {0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0},
				 {0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0},
				 {0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0},
				 {0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0},
				 {0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 1, 0, 0, 3, 0, 0, 0, 0, 0, 0},
				 {0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0},
				 {0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0},
				 {0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0},
				 {0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0},
				 {0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0},
				 {0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0},
				 {0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0},
				 {0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0},
				 {0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
				 {0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
				 {0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
				 {0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
				 {0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
				 {0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
				 {0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
				 {0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
				 {0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
				 {0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
				 {0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
				 {0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
				 {0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0},
				 {0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
				 {0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
				 {0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
				 {0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
				 {0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
				 {0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
				 {0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
				 {0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
				 {0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
				 {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
				 {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
				 {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
				 {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
				 {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
				 {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}};
		
		blocks = new ArrayList<Block>();
		//Lignes de collision
		lines = new ArrayList[4];
		lines[UP] = new ArrayList<Line>();
		lines[RIGHT] = new ArrayList<Line>();
		lines[DOWN] = new ArrayList<Line>();
		lines[LEFT] = new ArrayList<Line>();

		for(int i = 0; i < grid.length; i++){
			for(int j = 0; j < grid[i].length; j++){
				if (grid[i][j] == 1){
					Block addedBlock = new Block(i * Block.WIDTH, j * Block.HEIGHT);
					blocks.add(addedBlock);
					float x = addedBlock.bounds.lowerLeft.x;
					float y = addedBlock.bounds.lowerLeft.y + Block.HEIGHT;
					
					if(j < grid[i].length - 1 && grid[i][j+1] != 1){
						lines[UP].add(new Line(x, y, x+Block.WIDTH, y));
					}
					
					if(i < grid.length - 1 && grid[i+1][j] != 1){
						lines[RIGHT].add(new Line(x+Block.WIDTH, y, x+Block.WIDTH, y-Block.HEIGHT));
					}
					
					if(j > 0 && grid[i][j-1] != 1){
						lines[DOWN].add(new Line(x+Block.WIDTH, y-Block.HEIGHT, x, y-Block.HEIGHT));
					}
					
					if(i > 0 && grid[i-1][j] != 1){
						lines[LEFT].add(new Line(x, y-Block.HEIGHT, x, y));
					}
				}
				else if (grid[i][j] == 2){
					player.position.x = i * Block.WIDTH;
					player.position.y = j * Block.HEIGHT;
					testStartPos = new Vector2(player.position);
				}
				else if (grid[i][j] == 3){
					enemies.add(new Enemy(i * Block.WIDTH, j * Block.HEIGHT));
				}
			}
		}
		
		//Merge continuous collision lines
		for(int a = 0; a < lines.length; a++){			
			for(int i = 0; i < lines[a].size(); i++){
				Line currentLine = lines[a].get(i);
				for(int j = 0; j < lines[a].size(); j++){
					if (i != j){
						Line testedLine = lines[a].get(j);
						if(currentLine.pointB.x == testedLine.pointA.x && 
						   currentLine.pointB.y == testedLine.pointA.y){
							currentLine.pointB.x = testedLine.pointB.x;
							currentLine.pointB.y = testedLine.pointB.y;
							lines[a].remove(j);
							j--;
							Log.d("Physics", "merged line: (" + currentLine.pointA.x + ", " + currentLine.pointA.y + ") -> (" + currentLine.pointB.x + ", " + currentLine.pointB.y + ")");
						}
						else if(currentLine.pointA.x == testedLine.pointB.x && 
								currentLine.pointA.y == testedLine.pointB.y){
							currentLine.pointA.x = testedLine.pointA.x;
							currentLine.pointA.y = testedLine.pointA.y;
							lines[a].remove(j);
							j--;
							Log.d("Physics", "merged line: (" + currentLine.pointA.x + ", " + currentLine.pointA.y + ") -> (" + currentLine.pointB.x + ", " + currentLine.pointB.y + ")");
						}
					}
				}
			}
		}
		
		Log.d("Physics", "Number of blocks : " + blocks.size());
		Log.d("Physics", "Number of lines : " + (lines[UP].size() + lines[RIGHT].size() + lines[DOWN].size() + lines[LEFT].size()));
	}
	
	public void update(float deltaTime){
		updatePlayer(deltaTime);
		updateEnemys(deltaTime);
		interactionPlayerEnemies();
	}
	
	private void updateEnemys(float deltaTime){
		Enemy currentEnnemy;

		for(int i = 0; i < enemies.size(); i++){
			currentEnnemy = enemies.get(i);
			enemies.get(i).velocity.add(GRAVITY.x * deltaTime, GRAVITY.y * deltaTime);

			x1 = Math.round(currentEnnemy.bounds.lowerLeft.x);;
			x2 = Math.round(currentEnnemy.bounds.lowerRight.x);
			y = Math.round(currentEnnemy.bounds.lowerLeft.y - 0.5f);

			if(x2 > 68 || x1 < 1 || y > 18 || y < 1){
				currentEnnemy.position.x = 10;
				currentEnnemy.position.y = 10;
				currentEnnemy.velocity.x = 0;
				currentEnnemy.velocity.y = 0;
			}

			if((grid[x1][y] != 1 && grid[x2][y] != 1)){
				currentEnnemy.velocity.x += GRAVITY.x * deltaTime;
				currentEnnemy.velocity.y += GRAVITY.y * deltaTime;
				//currentEnnemy.fall();
			}			
			
			checkCollisionObjToWorld(currentEnnemy, deltaTime);
			currentEnnemy.update(deltaTime);
		}
	}
	
	private void updatePlayer(float deltaTime){
		// TESTS
		x1 = Math.round(player.bounds.lowerLeft.x);
		x2 = Math.round(player.bounds.lowerRight.x);
		y = Math.round(player.bounds.lowerLeft.y - 0.5f);

		//TODO: get grid width & height
		if(x2 > 68 || x1 < 1 || y > 18 || y < 1){
			player.position.x = testStartPos.x;
			player.position.y = testStartPos.y;
			player.velocity.x = 0;
			player.velocity.y = 0;
			player.bounds.updatePos(player.position.x, player.position.y);
		}
		
		if((player.state == Player.STATE_WALKING || player.state == Player.STATE_SLOWING_DOWN) && (grid[x1][y] != 1 && grid[x2][y] != 1)){
			player.fall();
		}
		
		checkCollisionObjToWorld(player, deltaTime);
		player.update(deltaTime);
	}
	
	private void checkCollisionObjToWorld(DynamicGameObject movingObj, float deltaTime){
		Line solidLine;
		
		movingObj.updatePaths(deltaTime);
		
		float minR = 1.1f;
		float r = 2;
		
		if(movingObj.velocity.y > 0){
			for(int i = 0; i < lines[DOWN].size(); i++){
				solidLine = lines[DOWN].get(i);
				
				r = OverlapTester.overlapLineSegment(movingObj.paths[DynamicGameObject.UPPER_RIGHT], solidLine);
				if (r < minR){
					minR = r;
					movingObj.velocity.y = 0;
					movingObj.position.y = solidLine.pointA.y - movingObj.height/2 - COLLISION_DISTANCE;
				}
				r = OverlapTester.overlapLineSegment(movingObj.paths[DynamicGameObject.UPPER_LEFT], solidLine);
				if (r < minR){
					minR = r;
					movingObj.velocity.y = 0;
					movingObj.position.y = solidLine.pointA.y - movingObj.height/2 - COLLISION_DISTANCE;
				}
			}		
		}else if(movingObj.velocity.y < 0){
			for(int i = 0; i < lines[UP].size(); i++){
				solidLine = lines[UP].get(i);
				
				r = OverlapTester.overlapLineSegment(movingObj.paths[DynamicGameObject.LOWER_LEFT], solidLine);
				if (r < minR){
					minR = r;
					movingObj.velocity.y = 0;
					movingObj.position.y = solidLine.pointA.y + movingObj.height/2 + COLLISION_DISTANCE;
					movingObj.hitGround();
				}
				r = OverlapTester.overlapLineSegment(movingObj.paths[DynamicGameObject.LOWER_RIGHT], solidLine);
				if (r < minR){
					minR = r;
					movingObj.velocity.y = 0;
					movingObj.position.y = solidLine.pointA.y + movingObj.height/2 + COLLISION_DISTANCE;
					movingObj.hitGround();
				}
			}
		}
		
		minR = 1.1f;
		r = 2;
		
		if(movingObj.velocity.x > 0){
			for(int i = 0; i < lines[LEFT].size(); i++){
				solidLine = lines[LEFT].get(i);
				
				r = OverlapTester.overlapLineSegment(movingObj.paths[DynamicGameObject.LOWER_RIGHT], solidLine);
				if (r < minR){
					minR = r;
					movingObj.velocity.x = 0;
					movingObj.position.x = solidLine.pointA.x - movingObj.width/2 - COLLISION_DISTANCE;
				}
				r = OverlapTester.overlapLineSegment(movingObj.paths[DynamicGameObject.UPPER_RIGHT], solidLine);
				if (r < minR){
					minR = r;
					movingObj.velocity.x = 0;
					movingObj.position.x = solidLine.pointA.x - movingObj.width/2 - COLLISION_DISTANCE;
				}
			}
		}else if(movingObj.velocity.x < 0){
			for(int i = 0; i < lines[RIGHT].size(); i++){
				solidLine = lines[RIGHT].get(i);
				
				r = OverlapTester.overlapLineSegment(movingObj.paths[DynamicGameObject.LOWER_LEFT], solidLine);
				if (r < minR){
					minR = r;
					movingObj.velocity.x = 0;
					movingObj.position.x = solidLine.pointA.x + movingObj.width/2 + COLLISION_DISTANCE;
				}
				r = OverlapTester.overlapLineSegment(movingObj.paths[DynamicGameObject.UPPER_LEFT], solidLine);
				if (r < minR){
					minR = r;
					movingObj.velocity.x = 0;
					movingObj.position.x = solidLine.pointA.x + movingObj.width/2 + COLLISION_DISTANCE;
				}
			}
		}
	}
	
	private void interactionPlayerEnemies(){
		Enemy currentEnnemy;
		
		//Player attacks
		if(player.state == Player.STATE_ATTACKING_SPECIAL || player.state == Player.STATE_ATTACKING_MELEE){
			for(int i = 0; i < enemies.size(); i++){
				currentEnnemy = enemies.get(i);
				if (OverlapTester.overlapRectangles(player.bounds, currentEnnemy.bounds)){
					currentEnnemy.kicked(player.direction);
				}
			}
		}
		
		//EnnemySpot
		for(int i = 0; i < enemies.size(); i++){
			currentEnnemy = enemies.get(i);
			if(currentEnnemy.state == Enemy.STATE_STAND || currentEnnemy.state == Enemy.STATE_WALKING){
				if(player.position.dist(currentEnnemy.position) < Enemy.DISTANCE_DETECTION){
					currentEnnemy.charge(player.position.x > currentEnnemy.position.x);
				}
			}
			
			if(currentEnnemy.state == Enemy.STATE_CHARGING){
				if (OverlapTester.pointInRectangle(currentEnnemy.bounds, player.position)){
					player.kick(currentEnnemy.direction);
					currentEnnemy.stopCharging();
				}
				if(player.position.dist(currentEnnemy.position) > Enemy.DISTANCE_DETECTION * 1.5){
					currentEnnemy.stopCharging();
				}
			}
		}
		
		
	}
}
