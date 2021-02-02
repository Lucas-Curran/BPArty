package com.mygdx.game;

import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Sets the screen for a random minigame tutorial, and after the user clicks, plays that minigame
 * 
 *
 */
public class TutorialMaster implements Screen, InputProcessor {

	public static final Logger LOGGER = LogManager.getLogger(Board.class);
	
	final BPArty game;
	Texture desertDodge, snakeTron, creamCatch, rps, hotPotato, memoryMatch, balloonPop, pong;
	
	private int width = 1000;
	private int height = 1000; 
	
	Random rNumb = new Random();
	
	int randGame = 1+rNumb.nextInt(8);
	
	private OrthographicCamera camera = new OrthographicCamera(width, height); 
	
	public TutorialMaster(final BPArty game) {
		this.game = game;
		
		camera.setToOrtho(false, width, height);
		
		snakeTron = new Texture("SnakeMenu.png");
		desertDodge = new Texture("DesertDodgeMenu.png");
		creamCatch = new Texture("IceCreamDropMenu.png");
		balloonPop = new Texture("BalloonPop.png");
		pong = new Texture("PongMenu.png");
		hotPotato = new Texture("HotPotatoMenu.png");
		memoryMatch = new Texture("MemoryMatchMenu.png");
		rps = new Texture("RPSMenu.png");
		
	}
	
	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(float amountX, float amountY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void show() {

	}


	/**
	 * Draws the game tutorial to the screen
	 * @param tutorial specifies which game tutorial to draw
	 */
	public void setGame(Texture tutorial) {
		game.batch.begin();
		game.batch.draw(tutorial, 0, 0, width, height);
		game.batch.end();
	}
	
	@Override
	public void render(float delta) {
		
		Gdx.gl.glClearColor(1, 1, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		camera.update();
		game.batch.setProjectionMatrix(camera.combined);
		
		if (randGame == 1) {	
			
			LOGGER.info("Snake tron tutorial.");
			
			setGame(snakeTron);
			
			if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
				game.setScreen(new TransitionScreen(new SnakeTron(game), game));
			} 
		}
		else if (randGame == 2) {
			
			LOGGER.info("Desert Dodge tutorial.");
			
			setGame(desertDodge);

			if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
				game.setScreen(new TransitionScreen(new DesertDodge(game), game));
			} 
		}
		else if (randGame == 3) {
			
			LOGGER.info("Cream Catch tutorial.");
			
			setGame(creamCatch);
			
			if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
				game.setScreen(new TransitionScreen(new CatchTheCream(game), game));
			} 
		}
		
		else if (randGame == 4) {
			
			LOGGER.info("Balloon Pop tutorial.");
			
			setGame(balloonPop);
			
			if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
				game.setScreen(new TransitionScreen(new BalloonPop(game), game));
			} 
		}
		
		else if (randGame == 5) {
			
			LOGGER.info("Plong tutorial.");
			
			setGame(pong);
			
			if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
				game.setScreen(new TransitionScreen(new PlingPong(game), game));
			} 
		}
		
		else if (randGame == 6) {
			
			LOGGER.info("Memory Match tutorial.");
			
			setGame(memoryMatch);
			
			if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
				game.setScreen(new HauntedMemoryMatch(game));
			} 
		}
		
		else if (randGame == 7) {
			
			LOGGER.info("RPS tutorial.");
			
			setGame(rps);
			
			if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
				game.setScreen(new TransitionScreen(new RockPaperScissors(game), game));
			} 
		}
		
		else if (randGame == 8) {
			
			LOGGER.info("Plong tutorial.");
			
			setGame(hotPotato);
			
			if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
				game.setScreen(new TransitionScreen(new HotPotato(game), game));
			} 
		}
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
		// TODO Auto-generated method stub
		
	}
	
}
