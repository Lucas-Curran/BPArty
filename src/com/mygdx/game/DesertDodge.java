package com.mygdx.game;

import java.util.Iterator;

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
import com.badlogic.gdx.utils.Array;

public class DesertDodge implements Screen {

	final BPArty game;
	private Texture iceCreamCharacter;
	private Texture popsicleCharacter;
	private Texture pyramidTexture;
	private Texture eyeOfRaTexture;
	private Texture cactusTexture;
	private Texture camelTexture;
	private Texture background;
	
	private Rectangle playerOne;
	private Rectangle playerTwo;
	private Array<Rectangle> cactusDrop;
	private Array<Rectangle> camelDrop;
	private Array<Rectangle> pyramidDrop;

	private OrthographicCamera camera;
	private SpriteBatch batch;

	private float deltaTime;
	private float cactusTimer;
	private float camelTimer;
	private float pyramidTimer;
	private float roundTimer;
	private int cactusDropSpeed = 300;
	private int camelDropSpeed = 200;

	private int playerOneLives = 3;
	private int playerTwoLives = 3;
	BitmapFont playerOneLivesDisplay;
	BitmapFont playerTwoLivesDisplay;
	private String livesTextOne = "Player One Lives: " + playerOneLives;
	private String livesTextTwo = "Player Two Lives: " + playerTwoLives;

	private Boolean phaseTwoStart = false;
	AudioManager audioManagerDesertDodge = new AudioManager();
	
	public DesertDodge(final BPArty game) {
		this.game = game;

		iceCreamCharacter = new Texture(Gdx.files.internal("IceCreamCharacter.png"));
		popsicleCharacter = new Texture(Gdx.files.internal("PopsicleCharacter.png"));
		pyramidTexture = new Texture(Gdx.files.internal("Pyramid.png"));
		eyeOfRaTexture = new Texture(Gdx.files.internal("Eye.png"));
		camelTexture = new Texture(Gdx.files.internal("Camel.png"));
		cactusTexture = new Texture(Gdx.files.internal("Cactus.png"));
		background = new Texture(Gdx.files.internal("DesertDodgeBackground.jpg"));
		// Background texture is a free for commerical use background from vecteezy.com; Attribution: https://www.vecteezy.com/vector-art/192736-africa-desert-landscape-vector
		// All other textures in this game were made by the BPArty team
		
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 400);
		batch = new SpriteBatch();

		playerOneLivesDisplay = new BitmapFont();
		playerTwoLivesDisplay = new BitmapFont();

		playerOne = new Rectangle();
		playerOne.x = 800 / 2 - 64 / 2;
		playerOne.y = 20;
		playerOne.width = 64;
		playerOne.height = 64;

		playerTwo = new Rectangle();
		playerTwo.x = 800 / 2 - 64 / 2;
		playerTwo.y = 20;
		playerTwo.width = 64;
		playerTwo.height = 64;

		cactusDrop = new Array<Rectangle>();
		camelDrop = new Array<Rectangle>();
		pyramidDrop = new Array<Rectangle>();

		spawnCactus();
		spawnCamel();
	}

	private void spawnCactus() {
		Rectangle cactus = new Rectangle();
		cactus.x = MathUtils.random(0, 800 - 64);
		cactus.y = 480;
		cactus.width = 64;
		cactus.height = 64;
		cactusDrop.add(cactus);
	}
	/**
	 * Method creates rectangle to represent cactus and sets its spawn position to a random value
	 */

	private void spawnCamel() {
		Rectangle camel = new Rectangle();
		camel.x = MathUtils.random(0, 800 - 64);
		camel.y = 480;
		camel.width = 150;
		camel.height = 64;
		camelDrop.add(camel);
	}
	/**
	 * Method creates rectangle to represent camel and sets its spawn position to a random value
	 */

	private void spawnPyramid() {
		Rectangle pyramid = new Rectangle();
		pyramid.x = MathUtils.random(0, 800 - 64);
		pyramid.y = 480;
		pyramid.width = 350;
		pyramid.height = 350;
		if (phaseTwoStart) {
			pyramidDrop.add(pyramid);
		}
	}
	/**
	 * Method creates pyramid to represent cactus and sets its spawn position to a random value
	 */

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(135 / 255f, 206 / 255f, 235 / 255f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		camera.update();
		batch.setProjectionMatrix(camera.combined);

		audioManagerDesertDodge.playIntenseGame();
		
		batch.begin();
		batch.draw(background, 0, 0);
		if (phaseTwoStart) {
			Gdx.gl.glClearColor(0.4f, 0, 0, 1);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
			batch.draw(eyeOfRaTexture, 380, 300);
		}
		batch.draw(iceCreamCharacter, playerOne.x, playerOne.y);
		batch.draw(popsicleCharacter, playerTwo.x, playerTwo.y);
		for (Rectangle cactus : cactusDrop) {
			batch.draw(cactusTexture, cactus.x, cactus.y);
		}
		for (Rectangle camel : camelDrop) {
			batch.draw(camelTexture, camel.x, camel.y);
		}
		for (Rectangle pyramid : pyramidDrop) {
			batch.draw(pyramidTexture, pyramid.x, pyramid.y);
		}

		playerOneLivesDisplay.setColor(1.0f, 1.0f, 1.0f, 1.0f);
		playerOneLivesDisplay.draw(batch, livesTextOne, 50, 390);
		playerTwoLivesDisplay.setColor(1.0f, 1.0f, 1.0f, 1.0f);
		playerTwoLivesDisplay.draw(batch, livesTextTwo, 630, 390);

		batch.end();

		if (Gdx.input.isKeyPressed(Input.Keys.A))
			playerOne.x -= 300 * Gdx.graphics.getDeltaTime();
		if (Gdx.input.isKeyPressed(Input.Keys.D))
			playerOne.x += 300 * Gdx.graphics.getDeltaTime();
		if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
			playerTwo.x -= 300 * Gdx.graphics.getDeltaTime();
		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
			playerTwo.x += 300 * Gdx.graphics.getDeltaTime();

		if (playerOne.x < 0)
			playerOne.x = 0;
		if (playerOne.x > 800 - 64)
			playerOne.x = 800 - 64;
		if (playerTwo.x < 0)
			playerTwo.x = 0;
		if (playerTwo.x > 800 - 64)
			playerTwo.x = 800 - 64;

		activateTimers();
		
		dropCactus();
		dropCamel();
		dropPyramid();
		
		checkWin();
	}
	
	private void dropCactus() {
		for (Iterator<Rectangle> iter = cactusDrop.iterator(); iter.hasNext();) {
			Rectangle cactus = iter.next();
			cactus.y -= cactusDropSpeed * Gdx.graphics.getDeltaTime();
			if (cactus.y + 64 < 0)
				iter.remove();
			if (cactus.overlaps(playerOne) && cactus.overlaps(playerTwo)) {
				audioManagerDesertDodge.playLoseLife();
				playerOneLives -= 1;
				playerTwoLives -= 1;
				livesTextOne = "Player One Lives: " + playerOneLives;
				livesTextTwo = "Player Two Lives: " + playerTwoLives;
				iter.remove();
			} else if (cactus.overlaps(playerOne)) {
				audioManagerDesertDodge.playLoseLife();
				playerOneLives -= 1;
				livesTextOne = "Player One Lives: " + playerOneLives;
				iter.remove();
			} else if (cactus.overlaps(playerTwo)) {
				audioManagerDesertDodge.playLoseLife();
				playerTwoLives -= 1;
				livesTextTwo = "Player Two Lives: " + playerTwoLives;
				iter.remove();
			}
		}
	}
	/**
	 * Iterator for spawning cacti. Also keeps track of collisions, if cactus overlaps a player
	 * the method will update lives text display and detract lives from players.
	 */
	
	private void dropCamel() {
		for (Iterator<Rectangle> iter = camelDrop.iterator(); iter.hasNext();) {
			Rectangle camel = iter.next();
			camel.y -= camelDropSpeed * Gdx.graphics.getDeltaTime();
			if (camel.y + 64 < 0)
				iter.remove();
			if (camel.overlaps(playerOne) && camel.overlaps(playerTwo)) {
				audioManagerDesertDodge.playLoseLife();
				playerOneLives -= 1;
				playerTwoLives -= 1;
				livesTextOne = "Player One Lives: " + playerOneLives;
				livesTextTwo = "Player Two Lives: " + playerTwoLives;
				iter.remove();
			} else if (camel.overlaps(playerOne)) {
				audioManagerDesertDodge.playLoseLife();
				playerOneLives -= 1;
				livesTextOne = "Player One Lives: " + playerOneLives;
				iter.remove();
			} else if (camel.overlaps(playerTwo)) {
				audioManagerDesertDodge.playLoseLife();
				playerTwoLives -= 1;
				livesTextTwo = "Player Two Lives: " + playerTwoLives;
				iter.remove();
			}
		}
	}
	/**
	 * Iterator for spawning camel. Also keeps track of collisions, if cactus overlaps a player
	 * the method will update lives text display and detract lives from players.
	 */
	
	private void dropPyramid() {
		for (Iterator<Rectangle> iter = pyramidDrop.iterator(); iter.hasNext();) {
			Rectangle pyramid = iter.next();
			pyramid.y -= 200 * Gdx.graphics.getDeltaTime();
			if (pyramid.y + 64 < 0)
				iter.remove();
			if (pyramid.overlaps(playerOne) && pyramid.overlaps(playerTwo)) {
				audioManagerDesertDodge.playLoseLife();
				playerOneLives -= 1;
				playerTwoLives -= 1;
				livesTextOne = "Player One Lives: " + playerOneLives;
				livesTextTwo = "Player Two Lives: " + playerTwoLives;
				// Add sound effect
				iter.remove();
			} else if (pyramid.overlaps(playerOne)) {
				audioManagerDesertDodge.playLoseLife();
				playerOneLives -= 1;
				livesTextOne = "Player One Lives: " + playerOneLives;
				iter.remove();
			} else if (pyramid.overlaps(playerTwo)) {
				audioManagerDesertDodge.playLoseLife();
				playerTwoLives -= 1;
				livesTextTwo = "Player Two Lives: " + playerTwoLives;
				iter.remove();
			}
		}
	}
	/**
	 * Iterator for spawning pyramid. Also keeps track of collisions, if cactus overlaps a player
	 * the method will update lives text display and detract lives from players.
	 */
	
	private void activateTimers() {
		deltaTime = Gdx.graphics.getDeltaTime();
		cactusTimer += 1 * deltaTime;
		camelTimer += 1 * deltaTime;
		if (phaseTwoStart) {
			pyramidTimer += 1 * deltaTime;
		}

		while (cactusTimer >= 1f) {
			roundTimer += 1;
			spawnCactus();
			cactusTimer -= 1f;
		}
		while (camelTimer >= 3f) {
			spawnCamel();
			camelTimer -= 3f;
		}
		while (pyramidTimer >= 5f && phaseTwoStart.equals(true)) {
			spawnPyramid();
			pyramidTimer -= 5f;
		}
		
		if (roundTimer == 60) {
			phaseTwoStart = true;
			cactusDropSpeed = 400;
			camelDropSpeed = 300;
		}
	}
	/**
	 *Method activates all timers for the code. Uses three separate timers to spawn cactus, camel and pyramid
	 *at increasing intervals. Round timers keep track of phase 1 and 2 of game, when 60 seconds has elapsed
	 *phase two starts and the speeds of objects increase
	 */
	
	private void checkWin() {
		if(playerOneLives == 0 && playerTwoLives == 0) {
			playerOneLives = 1;
			playerTwoLives = 1;
			livesTextTwo = "Player Two Lives: " + playerTwoLives;
			livesTextOne = "Player One Lives: " + playerOneLives;
		} else if (playerOneLives == 0) {
			audioManagerDesertDodge.stopAll();
			game.setScreen(new PopsicleWin(game));
		} else if (playerTwoLives == 0) {
			audioManagerDesertDodge.stopAll();
			game.setScreen(new IceCreamWin(game));
		}
	}
	/**
	 * Method checks player lives left, if a player reaches zero lives the method will display
	 * the appropriate win screen.
	 */
	
	@Override
	public void show() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

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
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		iceCreamCharacter.dispose();
		popsicleCharacter.dispose();
		cactusTexture.dispose();
		pyramidTexture.dispose();
		camelTexture.dispose();
		eyeOfRaTexture.dispose();
		background.dispose();
	}

}
