package com.mygdx.game;

import java.util.Iterator;
import java.util.Random;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

public class CatchTheCream implements Screen {

	final BPArty game;

	private Texture dropImage1;
	private Texture dropImage2;
	private Texture dropImage3;
	private Texture coneImage;
	private Texture background;
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private Rectangle cone;
	private Array<Rectangle> onePointDrop;
	private Array<Rectangle> twoPointDrop;
	private Array<Rectangle> threePointDrop;

	public static Boolean roundOneEnded = false;
	public int points = 0;
	public static int playerOnePoints = 0;
	public static int playerTwoPoints = 0;
	private CharSequence playerPointDisplay = "Score: " + points;
	BitmapFont score;

	private float deltaTime;
	private float timer;
	private float timer2;
	private float timer3;
	private float roundTimer;
	private int roundTimerTracker = 60;
	private CharSequence roundTimerDisplay = "Time Remaining: 00:" + roundTimerTracker;
	private BitmapFont timerDisplay;
	
	private Random rNumber = new Random();
	
	AudioManager audioManagerCream = new AudioManager();
	
	public CatchTheCream(final BPArty game) {
		this.game = game;
		dropImage1 = new Texture(Gdx.files.internal("VanillaIceCream.png"));
		dropImage2 = new Texture(Gdx.files.internal("BlueIceCream.png"));
		dropImage3 = new Texture(Gdx.files.internal("GoldenIceCream.png"));
		coneImage = new Texture(Gdx.files.internal("Cone.png"));
		background = new Texture(Gdx.files.internal("CreamDropBackground.jpg"));
		// Background texture is a free for commerical use background from vecteezy.com; Attribution: https://www.vecteezy.com/vector-art/370189-scene-with-icecream-and-lolipops
		// All other textures in this game were made by the BPArty team
		score = new BitmapFont();
		timerDisplay = new BitmapFont();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 400);
		batch = new SpriteBatch();
		cone = new Rectangle();
		cone.x = 800 / 2 - 64 / 2;
		cone.y = 20;
		cone.width = 64;
		cone.height = 64;

		onePointDrop = new Array<Rectangle>();
		twoPointDrop = new Array<Rectangle>();
		threePointDrop = new Array<Rectangle>();
		spawnIceCream();
		spawnIceCreamTwo();
		spawnIceCreamThree();

	}

	public void switchRounds() {
		playerOnePoints = points;
		System.out.println(playerOnePoints);
		roundTimerTracker = 60;
		points = 0;
		roundOneEnded = true;

		game.setScreen(new RoundSwitchScreen(game));
	}
	/**
	 * Switches the round when player one runs out of time to catch ice cream
	 */

	private void gameEnd() { 
		playerTwoPoints = points;
		if (playerOnePoints > playerTwoPoints) {
			game.setScreen(new IceCreamWin(game));
		} else if (playerTwoPoints > playerOnePoints) {
			game.setScreen(new PopsicleWin(game));
		} else if (playerTwoPoints == playerOnePoints) {
			int randomNumber = 1 + rNumber.nextInt(1);
			if (randomNumber == 1) {
				game.setScreen(new IceCreamWin(game));
			} else if (randomNumber == 2) {
				game.setScreen(new PopsicleWin(game));
			}
		}
	}
	/**
	 * Method compares player points at end of both rounds. Whichever player has more points wins the game and 
	 * the screen then switches to the appropriate win screen
	 */

	private void spawnIceCream() {
		Rectangle iceCreamDrop = new Rectangle();
		iceCreamDrop.x = MathUtils.random(0, 800 - 64);
		iceCreamDrop.y = 480;
		iceCreamDrop.width = 64;
		iceCreamDrop.height = 64;
		onePointDrop.add(iceCreamDrop);

	}
	/**
	 * Creates rectangle to represent 1 point ice cream and adds it to the iterator to drop onto screen in a random location
	 */

	private void spawnIceCreamTwo() {
		Rectangle iceCreamDrop2 = new Rectangle();
		iceCreamDrop2.x = MathUtils.random(0, 800 - 64);
		iceCreamDrop2.y = 480;
		iceCreamDrop2.width = 64;
		iceCreamDrop2.height = 64;
		twoPointDrop.add(iceCreamDrop2);

	}
	/**
	 * Creates rectangle to represent 2 point ice cream and adds it to the iterator to drop onto screen in a random location
	 */

	private void spawnIceCreamThree() {
		Rectangle iceCreamDrop3 = new Rectangle();
		iceCreamDrop3.x = MathUtils.random(0, 800 - 64);
		iceCreamDrop3.y = 480;
		iceCreamDrop3.width = 64;
		iceCreamDrop3.height = 64;
		threePointDrop.add(iceCreamDrop3);
	}
	/**
	 * Creates rectangle to represent 3 point ice cream and adds it to the iterator to drop onto screen in a random location
	 */
	@Override
	public void render(float delta) {
		audioManagerCream.playChillGame();
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		// Tells camera to update matrices
		camera.update();
		batch.setProjectionMatrix(camera.combined);

		batch.begin();
		
		batch.draw(background, 0, 0);
		batch.draw(coneImage, cone.x, cone.y);
		
		for (Rectangle iceCreamDrop : onePointDrop) {
			batch.draw(dropImage1, iceCreamDrop.x, iceCreamDrop.y);
		}
		for (Rectangle iceCreamDrop2 : twoPointDrop) {
			batch.draw(dropImage2, iceCreamDrop2.x, iceCreamDrop2.y);
		}
		for (Rectangle iceCreamDrop3 : threePointDrop) {
			batch.draw(dropImage3, iceCreamDrop3.x, iceCreamDrop3.y);
		}
		score.setColor(1.0f, 1.0f, 1.0f, 1.0f);
		score.draw(batch, playerPointDisplay, 25, 390);
		timerDisplay.setColor(1.0f, 1.0f, 1.0f, 1.0f);
		timerDisplay.draw(batch, roundTimerDisplay, 630, 390);
		
		batch.end();

		if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
			cone.x -= 300 * Gdx.graphics.getDeltaTime();
		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
			cone.x += 300 * Gdx.graphics.getDeltaTime();

		if (cone.x < 0)
			cone.x = 0;
		if (cone.x > 800 - 64)
			cone.x = 800 - 64;

		activateTimers();
		dropOnePointCream();
		dropTwoPointCream();
		dropThreePointCream();
		
	}
	
	private void dropOnePointCream() {
		for (Iterator<Rectangle> iter = onePointDrop.iterator(); iter.hasNext();) {
			Rectangle iceCreamDrop = iter.next();
			iceCreamDrop.y -= 200 * Gdx.graphics.getDeltaTime();
			if (iceCreamDrop.y + 64 < 0)
				iter.remove();
			if (iceCreamDrop.overlaps(cone)) {
				audioManagerCream.playPointCollect();
				points++;
				playerPointDisplay = "Score: " + points;
				iter.remove();
			}
		}
	}
	/**
	 * Iterator for 1 point ice cream, spawns ice cream at top of screen and handles drop physics
	 * If the ice cream overlaps the cone, method will play a sound, add points, and display points
	 */
	
	private void dropTwoPointCream() {
		for (Iterator<Rectangle> iter = twoPointDrop.iterator(); iter.hasNext();) {
			Rectangle iceCreamDrop2 = iter.next();
			iceCreamDrop2.y -= 300 * Gdx.graphics.getDeltaTime();
			if (iceCreamDrop2.y + 64 < 0)
				iter.remove();
			if (iceCreamDrop2.overlaps(cone)) {
				audioManagerCream.playPointCollect();
				points = points + 2;
				playerPointDisplay = "Score: " + points;
				iter.remove();
			}
		}
	}
	/**
	 * Iterator for 2 point ice cream, spawns ice cream at top of screen and handles drop physics
	 * If the ice cream overlaps the cone, method will play a sound, add points, and display points
	 */
	
	private void dropThreePointCream() {
		for (Iterator<Rectangle> iter = threePointDrop.iterator(); iter.hasNext();) {
			Rectangle iceCreamDrop3 = iter.next();
			iceCreamDrop3.y -= 400 * Gdx.graphics.getDeltaTime();
			if (iceCreamDrop3.y + 64 < 0)
				iter.remove();
			if (iceCreamDrop3.overlaps(cone)) {
				audioManagerCream.playPointCollect();
				points = points + 3;
				playerPointDisplay = "Score: " + points;
				iter.remove();
			}
		}
	}
	/**
	 * Iterator for 3 point ice cream, spawns ice cream at top of screen and handles drop physics
	 * If the ice cream overlaps the cone, method will play a sound, add points, and display points
	 */
	
	private void activateTimers() {
		deltaTime = Gdx.graphics.getDeltaTime();
		timer += 1 * deltaTime;
		timer2 += 1 * deltaTime;
		timer3 += 1 * deltaTime;
		roundTimer += 1 * deltaTime;

		while (roundTimer >= 1f) {
			if (roundTimerTracker == 0) {
				break;
			}
			roundTimerTracker -= 1;
			roundTimer -= 1f;
			roundTimerDisplay = "Time Remaining: 00:" + roundTimerTracker;
		}

		while (timer >= 1f) {
			spawnIceCream();
			timer -= 1f;
		}

		while (timer2 >= 3f) {
			spawnIceCreamTwo();
			timer2 -= 3f;
		}

		while (timer3 >= 7f) {
			spawnIceCreamThree();
			timer3 -= 7f;
		}
		
		if (roundTimerTracker == 0 && roundOneEnded.equals(true)) {
			gameEnd();
		} else if (roundTimerTracker == 0 && roundOneEnded.equals(false)) {
			switchRounds();
		}
		/**
		 * Activates all timers for the game, the round timer keeps track of the round time
		 * There are three separate timers keeping track of each ice cream to spawn them at varying time intervals based on rarity
		 * The method also keeps track of what round the game is one and when to end the game
		 */

	}
	
	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {
	}

	@Override
	public void hide() {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
		dropImage1.dispose();
		dropImage2.dispose();
		dropImage3.dispose();
		coneImage.dispose();
		background.dispose();
	}

}
