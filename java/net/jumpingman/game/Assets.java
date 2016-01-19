package net.jumpingman.game;

import net.jumpingman.framework.gl.Animation;
import net.jumpingman.framework.Sound;
import net.jumpingman.game.Settings;

import net.jumpingman.framework.gl.Font;
import net.jumpingman.framework.gl.Texture;
import net.jumpingman.framework.gl.TextureRegion;
import net.jumpingman.framework.impl.GLGame;

//This class contains all the game's resources.
public class Assets {

	//Game textures
	public static Texture fonts;
	public static Texture testBlocks;
	public static Texture jmPlaceHolder;
	public static Texture bunny;
	public static Texture buttons;
	public static Texture jmAttack;
	public static TextureRegion testBlock1;
	public static TextureRegion testBlock2;
	public static TextureRegion testBlock3;
	public static TextureRegion testBlock4;
	public static TextureRegion bunnyKicked;
	public static TextureRegion jmFall;
	public static TextureRegion buttonNone;
	public static TextureRegion buttonKnight;
	public static Animation jmWalk;
	public static Animation jmJump;
	public static Animation jmStand;
	public static Animation jmAttackSpecial;
	public static Animation jmAttackMelee;
	public static Animation jmHurt;
	public static Animation bunnyIdle;
	public static Animation bunnyWalk;
	public static Animation bunnyCharge;
	//Font for displaying the debug UI
	public static Font fontFps;
	
	//Cette methode est appelle au chargement du jeu. Elle permet de charger les ressources du jeu
	public static void loadMenu(GLGame game){
		buttons = new Texture(game, "menuIcons.png");
		
		buttonNone = new TextureRegion(buttons, 128, 128, 0);
		buttonKnight = new TextureRegion(buttons, 128, 128, 1);	
	}
	
	public static void loadInGame(GLGame game){
		testBlocks = new Texture(game, "testBlocks.png");
		fonts = new Texture(game, "font.png");
		bunny = new Texture(game, "bunny.png");
		
		switch(Settings.hat){
		case Settings.HAT_NONE:
			jmPlaceHolder = new Texture(game, "jmPlaceHolder.png");
			jmAttackSpecial = new Animation(12,
					new TextureRegion(jmPlaceHolder, 128, 128, 19),
					new TextureRegion(jmPlaceHolder, 128, 128, 20),
					new TextureRegion(jmPlaceHolder, 128, 128, 21),
					new TextureRegion(jmPlaceHolder, 128, 128, 22),
					new TextureRegion(jmPlaceHolder, 128, 128, 23),
					new TextureRegion(jmPlaceHolder, 128, 128, 24),
					new TextureRegion(jmPlaceHolder, 128, 128, 25));
			jmAttackMelee = new Animation(12,
					new TextureRegion(jmPlaceHolder, 128, 128, 19),
					new TextureRegion(jmPlaceHolder, 128, 128, 20),
					new TextureRegion(jmPlaceHolder, 128, 128, 21),
					new TextureRegion(jmPlaceHolder, 128, 128, 22),
					new TextureRegion(jmPlaceHolder, 128, 128, 23),
					new TextureRegion(jmPlaceHolder, 128, 128, 24),
					new TextureRegion(jmPlaceHolder, 128, 128, 25));
			break;
		case Settings.HAT_KNIGHT:
			jmPlaceHolder = new Texture(game, "jmPlaceHolderKnight.png");
			jmAttack = new Texture(game, "jmPlaceHolderAttack.png");
			jmAttackSpecial = new Animation(24,
					new TextureRegion(jmAttack, 256, 256, 8),
					new TextureRegion(jmAttack, 256, 256, 9),
					new TextureRegion(jmAttack, 256, 256, 10),
					new TextureRegion(jmAttack, 256, 256, 11),
					new TextureRegion(jmAttack, 256, 256, 12),
					new TextureRegion(jmAttack, 256, 256, 13),
					new TextureRegion(jmAttack, 256, 256, 14),
					new TextureRegion(jmAttack, 256, 256, 15),
					new TextureRegion(jmAttack, 256, 256, 16));
			jmAttackMelee = new Animation(24,
					new TextureRegion(jmAttack, 256, 256, 0),
					new TextureRegion(jmAttack, 256, 256, 1),
					new TextureRegion(jmAttack, 256, 256, 2),
					new TextureRegion(jmAttack, 256, 256, 3),
					new TextureRegion(jmAttack, 256, 256, 4),
					new TextureRegion(jmAttack, 256, 256, 5),
					new TextureRegion(jmAttack, 256, 256, 6),
					new TextureRegion(jmAttack, 256, 256, 7));
			break;
		}
		
		testBlock1 = new TextureRegion(testBlocks, 32, 32, 0);
		testBlock2 = new TextureRegion(testBlocks, 32, 32, 1);
		testBlock3 = new TextureRegion(testBlocks, 32, 32, 2);
		testBlock4 = new TextureRegion(testBlocks, 32, 32, 3);		
		bunnyKicked = new TextureRegion(bunny, 128, 128, 0);
		jmFall = new TextureRegion(jmPlaceHolder, 128, 128, 9);
		
		jmWalk = new Animation(12,
				new TextureRegion(jmPlaceHolder, 128, 128, 0),
				new TextureRegion(jmPlaceHolder, 128, 128, 1),
				new TextureRegion(jmPlaceHolder, 128, 128, 2),
				new TextureRegion(jmPlaceHolder, 128, 128, 3),
				new TextureRegion(jmPlaceHolder, 128, 128, 4),
				new TextureRegion(jmPlaceHolder, 128, 128, 5),
				new TextureRegion(jmPlaceHolder, 128, 128, 6),
				new TextureRegion(jmPlaceHolder, 128, 128, 7),
				new TextureRegion(jmPlaceHolder, 128, 128, 8));
		jmHurt = new Animation(8,
				new TextureRegion(jmPlaceHolder, 128, 128, 10),
				new TextureRegion(jmPlaceHolder, 128, 128, 11),
				new TextureRegion(jmPlaceHolder, 128, 128, 10),
				new TextureRegion(jmPlaceHolder, 128, 128, 12));
		jmJump = new Animation(12,
				new TextureRegion(jmPlaceHolder, 128, 128, 13),
				new TextureRegion(jmPlaceHolder, 128, 128, 14),
				new TextureRegion(jmPlaceHolder, 128, 128, 15),
				new TextureRegion(jmPlaceHolder, 128, 128, 16),
				new TextureRegion(jmPlaceHolder, 128, 128, 17),
				new TextureRegion(jmPlaceHolder, 128, 128, 18));
		jmStand = new Animation(12,
				new TextureRegion(jmPlaceHolder, 128, 128, 26),
				new TextureRegion(jmPlaceHolder, 128, 128, 27),
				new TextureRegion(jmPlaceHolder, 128, 128, 28),
				new TextureRegion(jmPlaceHolder, 128, 128, 29),
				new TextureRegion(jmPlaceHolder, 128, 128, 30),
				new TextureRegion(jmPlaceHolder, 128, 128, 31),
				new TextureRegion(jmPlaceHolder, 128, 128, 32));
		bunnyIdle = new Animation(18,
				new TextureRegion(bunny, 128, 128, 1),
				new TextureRegion(bunny, 128, 128, 2),
				new TextureRegion(bunny, 128, 128, 3),
				new TextureRegion(bunny, 128, 128, 4));
		bunnyWalk = new Animation(16,
				new TextureRegion(bunny, 128, 128, 5),
				new TextureRegion(bunny, 128, 128, 6),
				new TextureRegion(bunny, 128, 128, 7),
				new TextureRegion(bunny, 128, 128, 8),
				new TextureRegion(bunny, 128, 128, 9));
		bunnyCharge = new Animation(60,
				new TextureRegion(bunny, 128, 128, 5),
				new TextureRegion(bunny, 128, 128, 6),
				new TextureRegion(bunny, 128, 128, 7),
				new TextureRegion(bunny, 128, 128, 8),
				new TextureRegion(bunny, 128, 128, 9));
		
		fontFps = new Font(fonts, 0, 0, 16, 16, 20);
		
	}

	public static void reloadMenu(){
		buttons.reload();
	}
	
	public static void reloadInGame(){
		testBlocks.reload();
		fonts.reload();
		bunny.reload();
		jmPlaceHolder.reload();
		if(Settings.hat == Settings.HAT_KNIGHT){
			jmAttack.reload();
		}
	}

	public static void playSound(Sound sound) {
		if(Settings.soundEnabled)
			sound.play(1);
	}
}
