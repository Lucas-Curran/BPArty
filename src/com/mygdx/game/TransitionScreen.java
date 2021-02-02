package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class TransitionScreen implements Screen {

	private Screen nextScreen;

	private BPArty game;
	ShapeRenderer shapeRenderer;

	// Once this reaches 1.0f the next scene is shown
	private float alpha = 0;
	// true if fade in, false if fade out
	private boolean fadeDirection = true;


	/**
	 * Sets next screen
	 * @param next
	 * @param game
	 */
	public TransitionScreen(Screen next, BPArty game) {
		this.nextScreen = next;
		
		game.setScreen(next);
		
		this.game = game;
	}

	@Override
	public void show() {
		
	}


	/**
	 * Creates shape to simulate fade in
	 */
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		
		
		if (fadeDirection != true) {
			nextScreen.render(Gdx.graphics.getDeltaTime());
		}
		
		//Creates a shape that will gradually get transparent, simulating fade in
		shapeRenderer = new ShapeRenderer();
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		shapeRenderer.setColor(0, 0, 0, alpha);
		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		shapeRenderer.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		shapeRenderer.end();
		Gdx.gl.glDisable(GL20.GL_BLEND);

		if (alpha >= 1) {
			fadeDirection = false;
		} else if (alpha <= 0 && fadeDirection == false) {
			game.setScreen(nextScreen);
		}
		//speed of fade
		alpha += fadeDirection == true ? 0.1 : -0.1;
	}

	
	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void hide() {

	}

	@Override
	public void dispose() {

	}


}