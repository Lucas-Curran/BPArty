package com.mygdx.game;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;

public class BalloonPop implements Screen {
	final BPArty game;
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private Texture balloonMachine;
	private Texture iceCreamTexture;
	private Texture popsicleTexture;
	private Texture background;
	private int popsicleX = 100;
	private int iceCreamX = 190;
	
	private BitmapFont balloonPumpsDisplay;
	private int balloonPumpsLeft = 10;
	private int balloonPumpCounter = 0;
	private String balloonPumpsText = "Balloon Pumps Left: " + balloonPumpsLeft;
	private int playerOnePumps = 10;
	private int playerTwoPumps = 10;
	private boolean playerOneTurn = true;
	
	private ShapeRenderer shapeRenderer;
	private int balloonRadius = 15;
	private int balloonY = 120;
	private Random rNumber = new Random();
	private int popsRequired = 40 + rNumber.nextInt(40);
	
	AudioManager audioManagerBalloonPop = new AudioManager();
	
	public BalloonPop(final BPArty game) {
		this.game = game;
		
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 400);
		batch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();
	
		balloonMachine = new Texture(Gdx.files.internal("BalloonMachine.png"));
		iceCreamTexture = new Texture(Gdx.files.internal("IceCreamCharacter.png"));
		popsicleTexture = new Texture(Gdx.files.internal("PopsicleCharacter.png"));
		background = new Texture(Gdx.files.internal("SkyBackground.jpg"));
		// Background texture is a free for commerical use background from vecteezy.com; Attribution: 
		// All other textures in this game were made by the BPArty team
		balloonPumpsDisplay = new BitmapFont();
	}
	
	private void createBalloon() {
		shapeRenderer.setColor(Color.WHITE);
	    shapeRenderer.begin(ShapeType.Filled);
	    shapeRenderer.circle(398, balloonY, balloonRadius);
	    shapeRenderer.end();
	}
	/**
	 * Creates circle shape to represent balloon
	 */
	
	private void blowBalloon() {
		if (balloonPumpCounter == popsRequired) {
			audioManagerBalloonPop.playBalloonPop();
			if (playerOneTurn = true) {
				audioManagerBalloonPop.stopAll();
				game.setScreen(new PopsicleWin(game));
			} else if (playerOneTurn = false) {
				audioManagerBalloonPop.stopAll();
				game.setScreen(new IceCreamWin(game));
			}
		}
		balloonPumpCounter++;
		balloonRadius += 2;
    	balloonY += 2;
    	balloonPumpsLeft -= 1;
    	balloonPumpsText = "Balloon Pumps Left: " + balloonPumpsLeft;
	}
	/**
	 * Method for increasing balloon size every time balloon is pumped.
	 * Method also keeps track of total balloon pumps, if balloon pump equals the random number
	 * required for the balloon to pop, it will pop the balloon and play a sound
	 */
	
	private void checkSwitch() {
		if (balloonPumpsLeft == 0 && playerOneTurn == true) {
			playerOneTurn = false;
	    	popsicleX = 190;
	    	iceCreamX = 100;
	    	balloonPumpsLeft = 10;
	    	playerOnePumps = 10;
	    	playerTwoPumps = 10;
	    	balloonPumpsText = "Balloon Pumps Left: " + balloonPumpsLeft;
		} else if (balloonPumpsLeft == 0 && playerOneTurn == false) {
			playerOneTurn = true;
    		popsicleX = 100;
    		iceCreamX = 190;
    		balloonPumpsLeft = 10;
    		playerOnePumps = 10;
	    	playerTwoPumps = 10;
        	balloonPumpsText = "Balloon Pumps Left: " + balloonPumpsLeft;
		}
	}
	/**
	 * Checks to see if a player switch needs to happen, if a player has no more pumps left
	 * the method will automatically switch the players and reset pumps and pumps display.
	 */

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(135 / 255f, 206 / 255f, 235 / 255f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		audioManagerBalloonPop.playIntenseGame();
		
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		
		batch.begin();
		
		batch.draw(background, 0, 0);
		batch.draw(balloonMachine, 100, -140);
		batch.draw(popsicleTexture, popsicleX, 48);
		batch.draw(iceCreamTexture, iceCreamX, 50);
		
		balloonPumpsDisplay.setColor(1.0f, 1.0f, 1.0f, 1.0f);
		balloonPumpsDisplay.draw(batch, balloonPumpsText, 20, 390);
		
		batch.end();
		
		createBalloon();
		
		if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && playerOneTurn == true) {
			if (playerOnePumps > 0) {
		    	blowBalloon();
		    	playerOnePumps -= 1;
		    	checkSwitch();
			}
		} else if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && playerOneTurn == false) {
			if (playerTwoPumps > 0) {
		    	blowBalloon();
		    	playerTwoPumps -= 1;
		    	checkSwitch();
			}
		}
		
		if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) && playerOneTurn == true && balloonPumpsLeft != 10) {
			playerOneTurn = false;
	    	popsicleX = 190;
	    	iceCreamX = 100;
	    	balloonPumpsLeft = 10;
	    	playerOnePumps = 10;
	    	playerTwoPumps = 10;
	    	balloonPumpsText = "Balloon Pumps Left: " + balloonPumpsLeft;
		} else if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) && playerOneTurn == false && balloonPumpsLeft != 10) {
			playerOneTurn = true;
	    	popsicleX = 100;
	    	iceCreamX = 190;
	    	balloonPumpsLeft = 10;
	    	playerOnePumps = 10;
	    	playerTwoPumps = 10;
	    	balloonPumpsText = "Balloon Pumps Left: " + balloonPumpsLeft;
		}
		
	}
	
	@Override
	public void show() {
		
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
		balloonMachine.dispose();
		iceCreamTexture.dispose();
		popsicleTexture.dispose();
		background.dispose();
	}
}
