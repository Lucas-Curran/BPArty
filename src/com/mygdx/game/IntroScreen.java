package com.mygdx.game;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;



public class IntroScreen implements Screen {

	final BPArty game;
	OrthographicCamera camera;
	Texture texture;
	Sprite sprite;
	private float time = 0f;
	private float fade = 0f;
	SpriteBatch batch = new SpriteBatch();
	AudioManager audioManager = new AudioManager();

	/**
	 * Sets camera and game
	 * @param game
	 */
	public IntroScreen(final BPArty game) {
		this.game = game;
		camera = new OrthographicCamera();
		camera.setToOrtho(false, BPArty.WIDTH, BPArty.HEIGHT);

	}

	/**
	 * Creates sprite with logo
	 */
	@Override
	public void show() {
		texture = new Texture("CognitiveLogo.png");
		sprite = new Sprite(texture);
		sprite.scale(3f);
		sprite.setPosition(BPArty.WIDTH/2 - 150, BPArty.HEIGHT/2 - 50);
	}

	/**
	 * Renders logo and fades to next screen
	 */
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();
		audioManager.playIntro();
		batch.begin();
		sprite.draw(batch);
		if (time > 5) {
			sprite.setAlpha(fade);
		}
		
		fade -= delta * .25f;
		time += delta;
		
		if (time > 7) {

			audioManager.stopAll();
			game.setScreen(new MainMenuScreen(game));
			
		}
		batch.end();
		
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
