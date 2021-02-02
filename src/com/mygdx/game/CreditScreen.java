package com.mygdx.game;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;

public class CreditScreen implements Screen {

	private static final Logger LOGGER = LogManager.getLogger(CreditScreen.class);
	
	final BPArty game;
	OrthographicCamera camera;
	Texture background;
	Stage stage;
	TextButton returnBtn;

	AudioManager audioManager = new AudioManager();
	
	/**
	 * Sets camera and game
	 * @param game
	 */
	public CreditScreen(final BPArty game) {
		this.game = game;
		camera = new OrthographicCamera();
		camera.setToOrtho(false, BPArty.WIDTH / 2, BPArty.HEIGHT / 2);
		background = new Texture("credits.png");

	}

	/**
	 * Creates stage and buttons
	 */
	@Override
	public void show() {
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);

		returnBtn = new TextButton("Return", game.ButtonStyles("MenuRectangle", "MenuRectangle"));
		returnBtn.setPosition(10, 10);
		returnBtn.getLabel().setAlignment(Align.left);
		returnBtn.getLabelCell().padLeft(20);
		returnBtn.getLabel().setFontScale(2, 2);

		stage.addActor(returnBtn);
	}

	/**
	 * Renders background and button inputs
	 */
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();

		try {
		stage.getBatch().begin();
		stage.getBatch().draw(background, 0, 0, BPArty.WIDTH, BPArty.HEIGHT);
		stage.getBatch().end();
		
		if (returnBtn.isPressed()) {
			audioManager.playClick();
			game.setScreen(new TransitionScreen(new SettingScreen(game), game));
		}

		stage.act(delta);
		stage.draw();
		
		} catch (Exception e) {
			LOGGER.error("Error while moving. Message: " +  e.getMessage());
			e.printStackTrace();
//			try {
//				LOGGER.fatal("A fatal error has occured. Game crashed.");
//				CrashInfo ci = new CrashInfo(e, game);
//				game.setScreen(ci);
//			} catch (IOException n) {
//				//Do nothing
//			}
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
