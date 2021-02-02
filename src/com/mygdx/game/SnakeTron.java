package com.mygdx.game;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class SnakeTron implements Screen {
	
	private static final Logger LOGGER = LogManager.getLogger(Board.class);	
	
	public int width = 1000;
	public int height = 1000;
	
	private OrthographicCamera camera = new OrthographicCamera(width, height); 
	private Viewport viewport;
	
	
	final BPArty game;
	
	private SnakeTronState snakeTronState = new SnakeTronState();
	
	AudioManager audioManager = new AudioManager();
	
	public SnakeTron(final BPArty game) {
		this.game = game;
		camera.setToOrtho(false, width, height);
		viewport = new FitViewport(width, height, camera);
		viewport.apply();
		
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(float delta) {
		
		try {
		
		camera.update();
		viewport.apply();
		Gdx.gl.glClearColor(1, 1, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		audioManager.playIntenseGame();
		
		snakeTronState.update(delta);
		snakeTronState.draw(width, height, camera);
		
		if (snakeTronState.win == true) {
			audioManager.stopAll();
			game.setScreen(new IceCreamWin(game));
			dispose();
		} else if (snakeTronState.win2 == true) {
			audioManager.stopAll();
			game.setScreen(new PopsicleWin(game));
			dispose();
		}
		
		} catch (Exception e) {
			LOGGER.error("Error while moving. Message: " +  e.getMessage());
			e.printStackTrace();
			try {
				LOGGER.fatal("A fatal error has occured. Game crashed.");
				CrashInfo ci = new CrashInfo(e, game);
				game.setScreen(ci);
			} catch (IOException n) {
				//Do nothing
			}
		}
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height);
		
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
